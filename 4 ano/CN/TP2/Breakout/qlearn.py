#!/usr/bin/env python
from __future__ import print_function

import argparse
import skimage as skimage
from skimage import transform, color, exposure
from skimage.transform import rotate
from skimage.viewer import ImageViewer
import sys

sys.path.append("game/")
# import wrapped_flappy_bird as game
import gym
import random
import numpy as np
from collections import deque
import time

import json
from tensorflow.keras.initializers import identity
from tensorflow.keras.models import model_from_json
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense, Dropout, Activation, Flatten, Conv2D, MaxPooling2D, BatchNormalization
from tensorflow.keras.optimizers import SGD, Adam, RMSprop
import tensorflow as tf

GAMMA = 0.9  # decay rate of past observations
OBSERVATION = 40000.  # timesteps to observe before training
EXPLORE = 1000000.  # frames over which to anneal epsilon
FINAL_EPSILON = 0.01  # final value of epsilon
INITIAL_EPSILON = 0.8  # starting value of epsilon
EPSILON_DECAY = 0.998  # value of decay
REPLAY_MEMORY = 40000  # number of previous transitions to remember
BATCH = 32  # size of minibatch
FRAME_PER_ACTION = 1
LEARNING_RATE = 0.00025

IMG_ROWS, IMG_COLS = 84, 84
# Convert image into Black and white
IMG_CHANNELS = 4  # Number of stack frames

NOOP = 0
FIRE = 1
RIGHT = 2
LEFT = 3
ACTIONS = [FIRE, RIGHT, LEFT]
ACTIONS_NAMES = ['FIRE', 'RIGHT', 'LEFT']


def build_model():
    model = Sequential()

    model.add(Conv2D(32, 8, strides=(4, 4), padding="valid", activation="relu",
                     input_shape=(IMG_ROWS, IMG_COLS, IMG_CHANNELS)))
    model.add(Conv2D(64, 4, strides=(2, 2), padding="valid", activation="relu"))
    model.add(Conv2D(64, 3, strides=(1, 1), padding="valid", activation="relu"))

    model.add(Flatten())
    model.add(Dense(512, activation="relu"))
    model.add(Dense(len(ACTIONS)))

    opt = RMSprop(learning_rate=LEARNING_RATE, rho=0.95, epsilon=0.01)
    model.compile(loss='huber_loss', optimizer=opt, metrics=["accuracy"])

    return model


def trainNetwork(model, args, env):
    # open up a game state to communicate with emulator

    # game_state = game.GameState()
    env.reset()

    starting_time = time.time()

    # store the previous observations in replay memory
    replay = deque()

    # get the first state by doing nothing and preprocess the image to IMG_ROWSxIMG_COLSx4
    # x_t, r_0, terminal = game_state.frame_step(start_game)
    observation, reward, done, info = env.step(FIRE)

    # Store initial lives
    last_step_lives = info["ale.lives"]

    observation = skimage.color.rgb2gray(observation)
    observation = skimage.transform.resize(observation, (IMG_ROWS, IMG_COLS))
    observation = skimage.exposure.rescale_intensity(observation, out_range=(0, 255))
    observation = observation / 255.0

    s_t = np.stack((observation,) * IMG_CHANNELS, axis=2)
    # print (s_t.shape)

    # In Keras, need to reshape
    s_t = s_t.reshape(1, s_t.shape[0], s_t.shape[1], s_t.shape[2])

    if args['mode'] == 'Run':
        OBSERVE = 999999999  # We keep observe, never train
        epsilon = FINAL_EPSILON
        print("Now we load weight")
        model.load_weights("model.h5")
        print("Weight load successfully")
        t = 0

    elif args['mode'] == 'CTrain':  # Continue previous train
        OBSERVE = 0
        epsilon = 0.15
        t = 0  # 360045
        print("Now we load weight")
        model.load_weights("model.h5")
        print("Weight load successfully")

    else:  # We go to training mode
        OBSERVE = OBSERVATION
        epsilon = INITIAL_EPSILON
        t = 0

    episode = 0
    episode_reward = 0
    while True:
        loss = 0
        q_values = 0
        action_index = 0
        is_random = False
        action = FIRE
        negative_reward = False

        if done:
            print(f'EPISODE {episode} ended with {episode_reward} total reward!')
            episode_reward = 0
            env.reset()

            action_index = 0
            action = ACTIONS[action_index]

            episode += 1

            # We reduce the epsilon gradually
            if epsilon > FINAL_EPSILON and t > OBSERVE:
                epsilon *= EPSILON_DECAY
                epsilon = max(FINAL_EPSILON, epsilon)

        elif negative_reward:
            action_index = 0
            action = ACTIONS[action_index]
            negative_reward = False

        else:
            # choose an action epsilon greedy
            # if t % FRAME_PER_ACTION == 0:
            if random.random() <= epsilon or t < OBSERVE:
                is_random = True
                action_index = random.randrange(0, len(ACTIONS))
                action = random.choice(ACTIONS)

            else:
                predict = model.predict(s_t)  # input a stack of 4 images, get the prediction
                action_index = np.argmax(predict)
                action = ACTIONS[action_index]

        last_step_lives = info["ale.lives"]

        # run the selected action and observed next state and reward
        observation, reward, done, info = env.step(action)
        episode_reward += reward

        observation = skimage.color.rgb2gray(observation)
        observation = observation[30:-8, :]
        observation = skimage.transform.resize(observation, (IMG_ROWS, IMG_COLS))
        observation = skimage.exposure.rescale_intensity(observation, out_range=(0, 255))
        observation = observation / 255.0

        final_done = done
        if last_step_lives > info["ale.lives"]:
            negative_reward = True
            reward += -2.0
            final_done = True

        if info["ale.lives"] > last_step_lives:
            s_t = np.stack((observation,) * IMG_CHANNELS, axis=2)
            s_t = s_t.reshape(1, s_t.shape[0], s_t.shape[1], s_t.shape[2])

        observation = observation.reshape(1, observation.shape[0], observation.shape[1], 1)
        s_t1 = np.append(observation, s_t[:, :, :, :IMG_CHANNELS - 1], axis=3)

        replay.append((s_t, action_index, reward, s_t1, final_done))

        if len(replay) > REPLAY_MEMORY:
            replay.popleft()

        if t > BATCH:
            # sample a minibatch to train on
            minibatch = random.sample(replay, BATCH)

            # Now we do the experience replay
            state_t, action_t, reward_t, state_t1, done_t = zip(*minibatch)
            state_t = np.concatenate(state_t)
            state_t1 = np.concatenate(state_t1)
            targets = model.predict(state_t)
            q_values = model.predict(state_t1)
            targets[range(BATCH), action_t] = reward_t + GAMMA * np.max(q_values, axis=1) * np.invert(done_t)
            # updated_q_values = reward_t + GAMMA * tf.reduce_max(targets, axis=1) * np.invert(done_t)
            # print(type(targets), updated_q_values)

            loss = model.train_on_batch(state_t, targets)

        s_t = s_t1
        t = t + 1

        # save progress every 1000 iterations
        if t % 1000 == 0 and t > OBSERVE:
            time_running = time.time() - starting_time
            print(f'Time since started {time_running:.5f}s')
            print("Now we save model")
            model.save_weights("model.h5", overwrite=True)
            with open("model.json", "w") as outfile:
                json.dump(model.to_json(), outfile)

        # print info
        state = ""
        if t <= OBSERVE:
            state = "observe"
        elif OBSERVE < t <= OBSERVE + EXPLORE:
            state = "explore"
            env.render()
        else:
            state = "train"
            env.render()

        print(
            f'TIMESTEP {t} / EPISODE {episode} / STATE {state} / EPSILON {epsilon:.4f} / '
            f'ACTION {ACTIONS_NAMES[action_index]:5s} / RANDOM_ACTION {str(is_random):5s} / REWARD {reward:2.0f} / '
            f'Q_MAX {np.max(q_values):2.7f} / Loss {loss}')


def playGame(args, env):
    model = build_model()
    trainNetwork(model, args, env)


def main():
    parser = argparse.ArgumentParser(description='Description of your program')
    parser.add_argument('-m', '--mode', help='Train / CTrain / Run', required=True)
    args = vars(parser.parse_args())
    env = gym.make('BreakoutDeterministic-v4')
    playGame(args, env)


if __name__ == "__main__":
    gpus = tf.config.experimental.list_physical_devices('GPU')
    if gpus:
        try:
            # Currently, memory growth needs to be the same across GPUs
            for gpu in gpus:
                tf.config.experimental.set_memory_growth(gpu, True)

            logical_gpus = tf.config.experimental.list_logical_devices('GPU')
            print(len(gpus), "Physical GPUs,", len(logical_gpus), "Logical GPUs")
        except RuntimeError as e:
            # Memory growth must be set before GPUs have been initialized\n
            print(e)

    main()
