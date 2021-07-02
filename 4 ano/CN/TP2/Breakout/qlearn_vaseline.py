# noinspection PyUnresolvedReferences
from baselines.common.atari_wrappers import make_atari, wrap_deepmind
import numpy as np
import tensorflow as tf
from tensorflow import keras
from tensorflow.keras import layers
import time
import os
import argparse
import gc

# Configuration paramaters for the whole setup
SEED = 42
GAMMA = 0.99  # Discount factor for past rewards
BATCH_SIZE = 64  # Size of batch taken from replay buffer
BS_DIV = 4  # Number of division of batch for training
MAX_STEPS_PER_EPISODE = 10000
EPSILON_DECAY = 0.9995  # value of decay
NUM_ACTIONS = 4
# Maximum replay length - Note: The Deepmind paper suggests 1000000 however this causes memory issues
MAX_MEMORY_LENGTH = 100000
# How often to update the target network
UPDATE_TARGET_NETWORK = 10000
# How often to update information files
UPDATE_INFO_FILES = 20


def create_q_model():
    # Network defined by the Deepmind paper
    inputs = layers.Input(shape=(84, 84, 4,))

    # Convolutions on the frames on the screen
    layer1 = layers.Conv2D(32, 8, strides=4, activation="relu")(inputs)
    layer2 = layers.Conv2D(64, 4, strides=2, activation="relu")(layer1)
    layer3 = layers.Conv2D(64, 3, strides=1, activation="relu")(layer2)

    layer4 = layers.Flatten()(layer3)

    layer5 = layers.Dense(512, activation="relu")(layer4)
    action = layers.Dense(NUM_ACTIONS, activation="linear")(layer5)

    return keras.Model(inputs=inputs, outputs=action)


class Breakout:
    def __init__(self, arguments):
        # Use the Baseline Atari environment because of Deepmind helper functions
        # env = make_atari("BreakoutNoFrameskip-v4")
        env = make_atari("BreakoutDeterministic-v4")
        # Warp the frames, grey scale, stake four frame and scale to smaller ratio
        self.env = wrap_deepmind(env, frame_stack=True, scale=True)
        self.env.seed(SEED)

        # The first model makes the predictions for Q-values which are used to make a action.
        self.model = create_q_model()
        # Build a target model for the prediction of future rewards. The weights of a target model get updated every
        # 10000 steps thus when the loss between the Q-values is calculated the target Q-value is stable.
        self.model_target = create_q_model()
        # In the Deepmind paper they use RMSProp however then Adam optimizer improves training time
        # self.optimizer = keras.optimizers.RMSprop(learning_rate=0.00025, rho=0.95, epsilon=0.01)
        self.optimizer = keras.optimizers.Adam(learning_rate=0.00025, clipnorm=1.0)
        # Using huber loss for stability
        self.loss_function = keras.losses.Huber()

        self.episode_count = 0
        self.frame_count = 0
        self.true_episode_count = 0
        self.true_episode_reward = 0
        self.epsilon = 1.0  # Epsilon greedy parameter
        self.epsilon_random_frames = 50000  # Number of frames to take random action and observe output

        # Experience replay buffers
        self.action_history = []
        self.state_history = []
        self.state_next_history = []
        self.rewards_history = []
        self.done_history = []
        self.losses = []
        self.q_values_action = []
        self.rewards = []
        self.times = []

        self.termination_condition = lambda: True
        self.train_condition = lambda: True

        self.EPSILON_MIN = 0.05  # Minimum epsilon greedy parameter

        if arguments['mode'] is None:
            pass

        elif arguments['mode'].lower() == 'ctrain':  # Continue previous train
            self.epsilon = 0.1
            self.epsilon_random_frames = BATCH_SIZE * 4
            self.model.load_weights("model.h5")
            self.model_target.load_weights("model.h5")

        elif arguments['mode'].lower() == 'ctrain0':  # Continue previous train
            self.epsilon = 0.0
            self.epsilon_random_frames = BATCH_SIZE * 4
            self.model.load_weights("model.h5")
            self.model_target.load_weights("model.h5")

            self.EPSILON_MIN = 0

        elif arguments['mode'].lower() == 'run':  # Continue previous train
            self.termination_condition = lambda: self.true_episode_count < 1
            self.train_condition = lambda: False
            self.epsilon = 0.0
            self.model.load_weights("model.h5")
            self.model_target.load_weights("model.h5")

            self.EPSILON_MIN = 0

    def write_loss_q(self):
        directory = "infos"
        if not os.path.exists(directory):
            os.makedirs(directory)

        loss_file = os.path.join(directory, 'loss_info.csv')
        q_file = os.path.join(directory, 'q_info.csv')

        if (not os.path.exists(loss_file)) or self.true_episode_count == UPDATE_INFO_FILES:
            with open(loss_file, "w") as outfile:
                outfile.write("loss\n")
        with open(loss_file, "a") as outfile:
            for l_ in self.losses:
                outfile.write('{0}\n'.format(l_))
        self.losses = []

        if (not os.path.exists(q_file)) or self.true_episode_count == UPDATE_INFO_FILES:
            with open(q_file, "w") as outfile:
                outfile.write("q_values\n")
        with open(q_file, "a") as outfile:
            for q in self.q_values_action:
                outfile.write('"{0}"\n'.format(q))
        self.q_values_action = []

    def write_episode_info(self):
        directory = "infos"
        if not os.path.exists(directory):
            os.makedirs(directory)

        episode_file = os.path.join(directory, 'episodes_info.csv')

        if (not os.path.exists(episode_file)) or self.true_episode_count == UPDATE_INFO_FILES:
            with open(episode_file, "w") as outfile:
                outfile.write("reward,time\n")
        with open(episode_file, "a") as outfile:
            for r, t in zip(self.rewards, self.times):
                outfile.write('{0},{1}\n'.format(r, t))

        self.rewards = []
        self.times = []

    def print_episode_info(self, starting_time, true_episode_start_time):
        current_time = time.time()
        time_running = current_time - starting_time
        true_episode_time = current_time - true_episode_start_time
        print(f'Episode {self.true_episode_count} ended with reward {self.true_episode_reward}, timestep '
              f'{self.frame_count} and epsilon {self.epsilon:.6f}! Time {true_episode_time:.2f}s and total time'
              f' {time_running:.2f}s')

        self.rewards.append(self.true_episode_reward)
        self.times.append(true_episode_time)

        return current_time, true_episode_time, time_running

    @tf.function
    def train_model(self, state_sample, masks, updated_q_values):
        with tf.GradientTape() as tape:
            # Train the model on the states and updated Q-values
            q_values = self.model(state_sample)

            # Apply the masks to the Q-values to get the Q-value for action taken
            q_action = tf.reduce_sum(tf.multiply(q_values, masks), axis=1)
            # Calculate loss between new Q-value and old Q-value
            loss = self.loss_function(updated_q_values, q_action)

        # Backpropagation
        grads = tape.gradient(loss, self.model.trainable_variables)
        self.optimizer.apply_gradients(zip(grads, self.model.trainable_variables))

        return loss, q_action

    @tf.function
    def predict_action(self, state):
        state_tensor = tf.convert_to_tensor(state)
        state_tensor = tf.expand_dims(state_tensor, 0)
        action_probs = self.model(state_tensor, training=False)

        return tf.argmax(action_probs[0]), action_probs[0]

    def delete_first(self):
        del self.rewards_history[:1]
        del self.state_history[:1]
        del self.state_next_history[:1]
        del self.action_history[:1]
        del self.done_history[:1]

    def play(self):
        starting_time = time.time()
        true_episode_start_time = time.time()
        time_running = 0

        while self.termination_condition():  # Run until solved
            gc.collect()

            state = np.array(self.env.reset())
            state = state[10:-4, :, :]
            state = tf.image.resize(state, [84, 84], method='nearest')

            episode_reward = 0

            for timestep in range(1, MAX_STEPS_PER_EPISODE):
                # env.render(); Adding this line would show the attempts of the agent in a pop up window.
                self.frame_count += 1

                # Use epsilon-greedy for exploration
                if self.frame_count < self.epsilon_random_frames or self.epsilon > np.random.rand(1)[0]:
                    # Take random action
                    action = np.random.choice(NUM_ACTIONS)
                    self.q_values_action.append([0] * action + [1] + [0] * (NUM_ACTIONS - action - 1))

                else:
                    # Take best action
                    arged_max_action, probs = self.predict_action(state)
                    action = arged_max_action.numpy()
                    self.q_values_action.append(probs.numpy().tolist())

                # Apply the sampled action in our environment
                state_next, reward, done, _ = self.env.step(action)

                state_next = state_next[10:-4, :, :]
                state_next = tf.image.resize(state_next, [84, 84], method='nearest')
                state_next = np.array(state_next)

                episode_reward += reward
                self.true_episode_reward += reward

                if done:
                    reward += -10.0

                # Save actions and states in replay buffer
                self.action_history.append(action)
                self.state_history.append(state)
                self.state_next_history.append(state_next)
                self.done_history.append(done)
                self.rewards_history.append(reward)
                state = state_next

                if self.frame_count % UPDATE_TARGET_NETWORK == 0:
                    # update the the target network with new weights
                    self.model_target.set_weights(self.model.get_weights())
                    self.model.save_weights("model.h5", overwrite=True)
                    # Log details
                    print(f"Saving Model at episode {self.true_episode_count}, frame count {self.frame_count}!!!")

                # Limit the state and reward history
                if len(self.rewards_history) > MAX_MEMORY_LENGTH:
                    self.delete_first()

                if done:
                    break

            if self.episode_count % 5 == 4:

                # Save model if good reward
                if self.true_episode_reward > 200:
                    directory = "gud_models"
                    if not os.path.exists(directory):
                        os.makedirs(directory)

                    model_name = f'best_model_{self.true_episode_count:05d}_{int(self.true_episode_reward)}.h5'
                    path = os.path.join(directory, model_name)
                    self.model.save_weights(path, overwrite=True)

                # Train model
                if self.train_condition() and len(self.done_history) > (BATCH_SIZE * BS_DIV):
                    # Get indices of samples for replay buffers
                    indices1 = np.random.choice(range(len(self.done_history) - BATCH_SIZE),
                                                size=(BS_DIV - 1) * BATCH_SIZE // BS_DIV)
                    indices2 = np.random.choice(range(len(self.done_history) - BATCH_SIZE, len(self.done_history)),
                                                size=BATCH_SIZE // BS_DIV)
                    indices = np.concatenate((indices1, indices2))

                    # Using list comprehension to sample from replay buffer
                    state_sample = np.array([self.state_history[i] for i in indices])
                    state_next_sample = np.array([self.state_next_history[i] for i in indices])
                    rewards_sample = [self.rewards_history[i] for i in indices]
                    action_sample = [self.action_history[i] for i in indices]
                    done_sample = tf.convert_to_tensor([float(self.done_history[i]) for i in indices])

                    # Build the updated Q-values for the sampled future states
                    # Use the target model for stability
                    future_rewards = self.model_target.predict(state_next_sample)
                    # Q value = reward + discount factor * expected future reward
                    updated_q_values = rewards_sample + GAMMA * tf.reduce_max(future_rewards, axis=1)

                    # If final frame set the last value to -1
                    updated_q_values = updated_q_values * (1 - done_sample) - done_sample

                    # Create a mask so we only calculate loss on the updated Q-values
                    masks = tf.one_hot(action_sample, NUM_ACTIONS)

                    loss, q_action = self.train_model(state_sample, masks, updated_q_values)

                    self.losses.append(np.float32(loss.numpy()).item())

                # Check times
                current_time, true_episode_time, time_running = self.print_episode_info(starting_time, true_episode_start_time)

                if self.true_episode_count % UPDATE_INFO_FILES == 0:
                    self.write_loss_q()
                    self.write_episode_info()

                true_episode_start_time = current_time
                self.true_episode_reward = 0
                self.true_episode_count += 1

                # Reduce the epsilon gradually
                self.epsilon *= EPSILON_DECAY
                self.epsilon = max(self.EPSILON_MIN, self.epsilon)

            self.episode_count += 1

            if episode_reward > 431:  # Condition to consider the task solved
                print("Solved at episode {}! Got a reward of {}!".format(self.episode_count, episode_reward))
                self.model.save_weights("model.h5", overwrite=True)
                break


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Description of your program')
    parser.add_argument('-m', '--mode', help='Train / CTrain / CTrain0 / Run', required=False)
    args = vars(parser.parse_args())

    breakout = Breakout(args)

    breakout.play()
