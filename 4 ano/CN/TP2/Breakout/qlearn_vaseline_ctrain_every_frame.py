from baselines.common.atari_wrappers import make_atari, wrap_deepmind
import numpy as np
import tensorflow as tf
from tensorflow import keras
from tensorflow.keras import layers
import time
import json
import skimage as skimage
from skimage import transform

# Configuration paramaters for the whole setup
seed = 42
gamma = 0.99  # Discount factor for past rewards
epsilon = 0.1  # Epsilon greedy parameter
epsilon_min = 0.05  # Minimum epsilon greedy parameter
epsilon_max = 1.0  # Maximum epsilon greedy parameter
epsilon_interval = (
    epsilon_max - epsilon_min
)  # Rate at which to reduce chance of random action being taken
batch_size = 64  # Size of batch taken from replay buffer
max_steps_per_episode = 10000
EPSILON_DECAY = 0.998  # value of decay

# Use the Baseline Atari environment because of Deepmind helper functions
#env = make_atari("BreakoutNoFrameskip-v4")
env = make_atari("BreakoutDeterministic-v4")
# Warp the frames, grey scale, stake four frame and scale to smaller ratio
env = wrap_deepmind(env, frame_stack=True, scale=True)
env.seed(seed)

num_actions = 4


def create_q_model():
    # Network defined by the Deepmind paper
    inputs = layers.Input(shape=(84, 84, 4,))

    # Convolutions on the frames on the screen
    layer1 = layers.Conv2D(32, 8, strides=4, activation="relu")(inputs)
    layer2 = layers.Conv2D(64, 4, strides=2, activation="relu")(layer1)
    layer3 = layers.Conv2D(64, 3, strides=1, activation="relu")(layer2)

    layer4 = layers.Flatten()(layer3)

    layer5 = layers.Dense(512, activation="relu")(layer4)
    action = layers.Dense(num_actions, activation="linear")(layer5)

    return keras.Model(inputs=inputs, outputs=action)


# The first model makes the predictions for Q-values which are used to
# make a action.
model = create_q_model()
model.load_weights("model.h5")
# Build a target model for the prediction of future rewards.
# The weights of a target model get updated every 10000 steps thus when the
# loss between the Q-values is calculated the target Q-value is stable.
model_target = create_q_model()
model_target.load_weights("model.h5")



# In the Deepmind paper they use RMSProp however then Adam optimizer
# improves training time
optimizer = keras.optimizers.RMSprop(learning_rate=0.00025, rho=0.95, epsilon=0.01)

# Experience replay buffers
action_history = []
state_history = []
state_next_history = []
rewards_history = []
done_history = []
episodes_info = {'reward': [], 'time': []}
mean_reward = 0
episode_count = 0
frame_count = 0
true_episode_count = 0
true_episode_reward = 0
best_episode_reward = 0
# Number of frames to take random action and observe output
epsilon_random_frames = 100
# Number of frames for exploration
epsilon_greedy_frames = 1000000.0
# Maximum replay length
# Note: The Deepmind paper suggests 1000000 however this causes memory issues
max_memory_length = 100000
# Train the model after 4 actions
update_after_actions = 4
# How often to update the target network
update_target_network = 10000
# How often to update the episodes information files
update_episodes_info_files = 20
# Using huber loss for stability
loss_function = keras.losses.Huber()

starting_time = time.time()
true_episode_start_time = time.time()


@tf.function
def train_model(state_sample, masks, updated_q_values):
    with tf.GradientTape() as tape:
        # Train the model on the states and updated Q-values
        q_values = model(state_sample)

        # Apply the masks to the Q-values to get the Q-value for action taken
        q_action = tf.reduce_sum(tf.multiply(q_values, masks), axis=1)
        # Calculate loss between new Q-value and old Q-value
        loss = loss_function(updated_q_values, q_action)

    # Backpropagation
    grads = tape.gradient(loss, model.trainable_variables)
    optimizer.apply_gradients(zip(grads, model.trainable_variables))

    return loss, q_action


@tf.function
def predict_action(state):

    state_tensor = tf.convert_to_tensor(state)
    state_tensor = tf.expand_dims(state_tensor, 0)
    action_probs = model(state_tensor, training=False)

    return tf.argmax(action_probs[0])


while True:  # Run until solved
    state = np.array(env.reset())
    state = state[10:-4, :, :]
    state = tf.image.resize(state, [84, 84], method='nearest')

    episode_reward = 0

    for timestep in range(1, max_steps_per_episode):
        # of the agent in a pop up window.
        frame_count += 1

        # Use epsilon-greedy for exploration
        if frame_count < epsilon_random_frames or epsilon > np.random.rand(1)[0]:
            # Take random action
            action = np.random.choice(num_actions)
        else:
            # Take best action
            action = predict_action(state).numpy()

        # Apply the sampled action in our environment
        state_next, reward, done, _ = env.step(action)

        state_next = state_next[10:-4, :, :]
        state_next = tf.image.resize(state_next, [84, 84], method='nearest')
        state_next = np.array(state_next)

        episode_reward += reward
        true_episode_reward += reward

        if done:
            reward += -10.0

        # Save actions and states in replay buffer
        action_history.append(action)
        state_history.append(state)
        state_next_history.append(state_next)
        done_history.append(done)
        rewards_history.append(reward)
        state = state_next

        # Update every fourth frame and once batch size is over 32
        if frame_count % update_after_actions == 0 and len(done_history) > batch_size:

            # Get indices of samples for replay buffers
            indices = np.random.choice(range(len(done_history)), size=batch_size)

            # Using list comprehension to sample from replay buffer
            state_sample = np.array([state_history[i] for i in indices])
            state_next_sample = np.array([state_next_history[i] for i in indices])
            rewards_sample = [rewards_history[i] for i in indices]
            action_sample = [action_history[i] for i in indices]
            done_sample = tf.convert_to_tensor([float(done_history[i]) for i in indices])

            # Build the updated Q-values for the sampled future states
            # Use the target model for stability
            future_rewards = model_target.predict(state_next_sample)
            # Q value = reward + discount factor * expected future reward
            updated_q_values = rewards_sample + gamma * tf.reduce_max(future_rewards, axis=1)

            # If final frame set the last value to -1
            updated_q_values = updated_q_values * (1 - done_sample) - done_sample

            # Create a mask so we only calculate loss on the updated Q-values
            masks = tf.one_hot(action_sample, num_actions)

            loss, q_action = train_model(state_sample, masks, updated_q_values)

        if frame_count % update_target_network == 0:
            # update the the target network with new weights
            model_target.set_weights(model.get_weights())
            model.save_weights("model.h5", overwrite=True)
            # Log details
            template = "Mean replay reward: {:.2f} at episode {}, frame count {}! Saving Model!"
            print(template.format(mean_reward, true_episode_count, frame_count))

        # Limit the state and reward history
        if len(rewards_history) > max_memory_length:
            del rewards_history[:1]
            del state_history[:1]
            del state_next_history[:1]
            del action_history[:1]
            del done_history[:1]

        if done:
            break

    if episode_count % 5 == 4:
        # env.close()
        current_time = time.time()
        time_running = current_time - starting_time
        true_episode_time = current_time - true_episode_start_time

        print(f'Episode {true_episode_count} ended with reward {true_episode_reward}, timestep {frame_count}'
              f' and epsilon {epsilon:.6f}! Time {true_episode_time:.2f}s and total time {time_running:.2f}s')

        episodes_info['reward'].append(true_episode_reward)
        episodes_info['time'].append(true_episode_time)
        if true_episode_count % update_episodes_info_files == 0:
            with open('episodes_info.json', "w") as outfile:
                json.dump(episodes_info, outfile, indent=2)

        if true_episode_reward > 375:
            model_name = f'modelbest{true_episode_count}_{int(true_episode_reward)}.h5'
            model.save_weights("gud_models/"+model_name, overwrite=True)
            best_episode_reward = true_episode_reward

        true_episode_start_time = current_time
        true_episode_reward = 0
        true_episode_count += 1

        # Reduce the epsilon gradually
        epsilon *= EPSILON_DECAY
        epsilon = max(epsilon_min, epsilon)

    episode_count += 1

    # Update mean reward to check condition for solving
    mean_reward = np.mean(episodes_info['reward'][-100:])
    if mean_reward > 300:
        print("Episode {} got a mean replay reward of {}!".format(episode_count, mean_reward))

    if episode_reward > 900:  # Condition to consider the task solved
        print("Solved at episode {}! Got a reward of {}!".format(episode_count, episode_reward))
        model.save_weights("model.h5", overwrite=True)
        break
