import collections

from baselines.common.atari_wrappers import make_atari, wrap_deepmind
import numpy as np
import tensorflow as tf
from tensorflow import keras
from tensorflow.keras import layers
import time
import skimage as skimage
from skimage import transform, color, exposure
import matplotlib.pyplot as plt
from PIL import Image
import os

# Configuration paramaters for the whole setup
seed = 42
batch_size = 64  # Size of batch taken from replay buffer
max_steps_per_episode = 1000
num_runs = 1

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
model2 = create_q_model()
model3 = create_q_model()
model.load_weights('m1.h5')
model2.load_weights('m2.h5')
model3.load_weights('m3.h5')


# In the Deepmind paper they use RMSProp however then Adam optimizer
# improves training time
optimizer = keras.optimizers.Adam(learning_rate=0.00025, clipnorm=1.0)

# Experience replay buffers
episode_count = 0
frame_count = 0
true_episode_count = 0
true_episode_reward = 0
# Using huber loss for stability
loss_function = keras.losses.Huber()

starting_time = time.time()
true_episode_start_time = time.time()

if not os.path.exists('gif_images/'):
    os.makedirs('gif_images/')

models_dir = 'gud_models/'
files_list = [name for name in os.listdir(models_dir) if name.endswith(".h5")]  # ['best_model_00007_422.h5']  #
gif_model = 'best_model_00505_496.h5'
print(files_list)
rewards = []

# for file in files_list:
if True:

    true_episode_count = 0
    episode_count = 0
    frame_count = 0

    while True:  # Run until solved
        state = np.array(env.reset())

        # if file == gif_model:
        im = Image.fromarray(env.render(mode='rgb_array'))
        state_name = f'{frame_count:05d}.png'
        im.save('gif_images/' + state_name)

        state = state[10:-4, :, :]
        state = tf.image.resize(state, [84, 84], method='nearest')
        episode_reward = 0

        for timestep in range(1, max_steps_per_episode):
            #env.render()
            #time.sleep(1 / 30)

            # of the agent in a pop up window.
            frame_count += 1

            # Predict action Q-values
            # From environment state
            state_tensor = tf.convert_to_tensor(state)
            state_tensor = tf.expand_dims(state_tensor, 0)
            action_probs1 = model(state_tensor, training=False)
            action_probs2 = model2(state_tensor, training=False)
            action_probs3 = model3(state_tensor, training=False)
            # Take best action
            action1 = tf.argmax(action_probs1[0]).numpy()
            action2 = tf.argmax(action_probs2[0]).numpy()
            action3 = tf.argmax(action_probs3[0]).numpy()

            counter = collections.Counter([action1, action2, action3])
            action = counter.most_common(1)[0][0]

            # Apply the sampled action in our environment
            state_next, reward, done, _ = env.step(action)

            # print(f'Step reward {reward}. Total episode reward {true_episode_reward}.')

            # if file == gif_model:
            im = Image.fromarray(env.render(mode='rgb_array'))
            state_name = f'{frame_count:05d}.png'
            im.save('gif_images/' + state_name)

            state_next = state_next[10:-4, :, :]
            state_next = tf.image.resize(state_next, [84, 84], method='nearest')
            state_next = np.array(state_next)

            episode_reward += reward
            true_episode_reward += reward

            state = state_next

            if done:
                break

        if episode_count % 5 == 4:
            current_time = time.time()
            time_running = current_time - starting_time
            true_episode_time = current_time - true_episode_start_time

            print(f'Model {True} ended with reward {true_episode_reward} and time {true_episode_time:.2f}s')

            rewards.append(true_episode_reward)
            true_episode_start_time = current_time
            true_episode_reward = 0
            true_episode_count += 1

        episode_count += 1

        if true_episode_count >= num_runs:
            break

print('Rewards:', rewards)
print(f'Model {files_list[rewards.index(max(rewards))]} with {max(rewards)} reward!')
