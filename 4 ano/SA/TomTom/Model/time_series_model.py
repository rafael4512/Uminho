"""
@author: Grupo 1
"""
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import time
import gc
import random
import operator

from sklearn.model_selection import TimeSeriesSplit

import tensorflow as tf
from tensorflow.keras.callbacks import EarlyStopping
from tensorflow.keras.layers import LSTM, Dropout, Dense, Conv1D, GRU, Flatten, BatchNormalization
from tensorflow.keras.layers import AveragePooling1D, MaxPool1D, Concatenate, Input
from tensorflow.keras.callbacks import ReduceLROnPlateau
from tensorflow.keras.models import Sequential, Model

tf.random.set_seed(23)
np.random.seed(23)
tf.keras.backend.clear_session()

KERNEL_SIZE = [3, 5]
POOL_SIZE = [2, 3]
NEURONS_FILTERS = [8, 16, 32, 64, 128, 512, 1024]
DROPOUT_RATE =  [0.15, 0.2, 0.25, 0.3, 0.35, 0.4] 


# split data into training and validation sets
def split_data(training, percentage = 20):
    train_idx = np.arange(0, int(len(training) * (100 - percentage) / 100))
    val_idx = np.arange(int(len(training) * (100 - percentage) / 100 + 1), len(training))
    
    return train_idx, val_idx


# Plot time series data
def plot_number_incidents(data):
    plt.figure(figsize = (8, 6))
    plt.plot(range(len(data)), data)
    plt.ylabel('Number of incidents')
    plt.xlabel('Hour')
    plt.show()
 
    
# Preparing the dataset for the LSTM
def to_supervised(df, timesteps, features = 2):

    data = df.values
    x, y = [], []
    
    # iterate over the training set to create x and y
    dataset_size = len(data)
    
    for curr_pos in range(dataset_size):
        # end of the input sequence is the current position + the number 
        # of timesteps of the input sequence
        input_index = curr_pos + timesteps
        
        # end of the labels correspond to the end of the input sequence + 1
        label_index = input_index + 1
        
        # if we have enough data for this sequence 
        if label_index < dataset_size:
            x.append(data[curr_pos:input_index, :])
            
            if features > 1:
                y.append(data[input_index:label_index, :])
            else:
                y.append(data[input_index:label_index, 0])
        
    # using np.float32 for GPU performance
    return np.array(x).astype('float32'), np.array(y).astype('float32')


def rmse(y_true, y_pred):
    return tf.keras.backend.sqrt(
        tf.keras.backend.mean(tf.keras.backend.square(y_pred - y_true)))


def build_LSTM_model(timesteps, features, name):
    try:
        number_LSTMs = random.randint(0, 5)
        number_denses = random.randint(0, 5)
        
        model = Sequential(name = name)
        
        if number_LSTMs > 0:
            model.add(LSTM(random.choice(NEURONS_FILTERS), 
                           input_shape = (timesteps, features),
                           return_sequences = True,
                           activation = 'tanh'))
        else:
            model.add(LSTM(random.choice(NEURONS_FILTERS), 
                       input_shape = (timesteps, features),
                       activation = 'tanh'))
        
        model.add(BatchNormalization())

        for i in range(number_LSTMs):
            if i == number_LSTMs - 1:
                model.add(LSTM(random.choice(NEURONS_FILTERS), 
                          activation = 'tanh'))
            else:
                model.add(LSTM(random.choice(NEURONS_FILTERS), 
                          return_sequences = True, 
                          activation = 'tanh'))

            model.add(BatchNormalization())
                
            if random.uniform(0, 1) < 0.5:
                model.add(Dropout(random.choice(DROPOUT_RATE)))

        for i in range(number_denses):
            model.add(Dense(random.choice(NEURONS_FILTERS), 
                            activation = 'tanh'))
                
            model.add(BatchNormalization())

            if random.uniform(0, 1) < 1:
                model.add(Dropout(random.choice(DROPOUT_RATE)))

                
        model.add(Dense(features, activation = 'tanh'))
        
        model.build((None, timesteps, features))
        print(model.summary())
        
        
    except Exception as e:
        print(e)
        model = build_LSTM_model(timesteps, features, name)
    
    finally:
        return model


def build_LSTM_model_stateful(timesteps, features, batch, name):
    try:
        number_LSTMs = random.randint(0, 5)
        number_denses = random.randint(0, 5)
        
        model = Sequential(name = name)
        
        if number_LSTMs > 0:
            model.add(LSTM(random.choice(NEURONS_FILTERS), 
                           batch_input_shape = (batch, timesteps, features), 
                           return_sequences = True,
                           activation = 'tanh', stateful = True))
        else:
            model.add(LSTM(random.choice(NEURONS_FILTERS), 
                           batch_input_shape = (batch, timesteps, features), 
                           activation = 'tanh', stateful = True))
        
        
        for i in range(number_LSTMs):
            if i == number_LSTMs - 1:
                model.add(LSTM(random.choice(NEURONS_FILTERS), 
                          activation = 'tanh', stateful = True))
            else:
                model.add(LSTM(random.choice(NEURONS_FILTERS), 
                               return_sequences = True, 
                               activation = 'tanh', stateful = True))
                
            if random.uniform(0, 1) < 0.5:
                model.add(Dropout(random.choice(DROPOUT_RATE)))
            
        for i in range(number_denses):
            model.add(Dense(random.choice(NEURONS_FILTERS), 
                            activation = 'tanh'))
                
            if random.uniform(0, 1) < 1:
                model.add(Dropout(random.choice(DROPOUT_RATE)))
                
        model.add(Dense(features, activation = 'tanh'))
        
        model.build((None, timesteps, features))
        print(model.summary())
        
        
    except Exception as e:
        print(e)
        model = build_LSTM_model_stateful(timesteps, features, name)
    
    finally:
        return model


def build_GRU_model(timesteps, features, name):
    try:
        number_GRUs = random.randint(0, 5)
        number_denses = random.randint(0, 5)
        
        model = Sequential(name = name)
        
        if number_GRUs > 0:
            model.add(GRU(random.choice(NEURONS_FILTERS), 
                          input_shape = (timesteps, features), 
                          return_sequences = True,
                          activation = 'tanh'))
        else:
            model.add(GRU(random.choice(NEURONS_FILTERS), 
                          input_shape = (timesteps, features), 
                          activation = 'tanh'))
        
        model.add(BatchNormalization())

        for i in range(number_GRUs):
            if i == number_GRUs - 1:
                model.add(GRU(random.choice(NEURONS_FILTERS), 
                          activation = 'tanh'))
            else:
                model.add(GRU(random.choice(NEURONS_FILTERS), 
                          return_sequences = True, 
                          activation = 'tanh'))

            model.add(BatchNormalization())
                
            if random.uniform(0, 1) < 0.5:
                model.add(Dropout(random.choice(DROPOUT_RATE)))

        for i in range(number_denses):
            model.add(Dense(random.choice(NEURONS_FILTERS), 
                            activation = 'tanh'))

            model.add(BatchNormalization())
                
            if random.uniform(0, 1) < 1:
                model.add(Dropout(random.choice(DROPOUT_RATE)))
                
        model.add(Dense(features, activation = 'tanh'))
        
        model.build((None, timesteps, features))
        print(model.summary())
        
        
    except Exception as e:
        print(e)
        model = build_GRU_model(timesteps, features, name)
    
    finally:
        return model


def build_CNN_model(timesteps, features, name):
    try:
        model = Sequential(name = name)
        
        number_Conv1d = random.randint(0, 5)
        number_denses = random.randint(0, 5)
        
        ks = random.choice(KERNEL_SIZE)
        
        if (timesteps - ks) + 1 <= 0:
            raise Exception('Hiper-parameters dont match up... trying again')
        
        model.add(Conv1D(random.choice(NEURONS_FILTERS), 
                         kernel_size = ks, 
                         activation = 'tanh', 
                         data_format = 'channels_last', 
                         input_shape = (timesteps, features)))

        model.add(BatchNormalization())
        
        for i in range(number_Conv1d):
            ks = random.choice(KERNEL_SIZE)
            
            if (timesteps - ks) + 1 <= 0:
                raise Exception('Hiper-parameters dont match up... trying again')
            
            model.add(Conv1D(random.choice(NEURONS_FILTERS), 
                             kernel_size = ks, 
                             activation = 'tanh', 
                             data_format = 'channels_last'))
                
            if random.uniform(0, 1) < 0.5:
                if random.uniform(0, 1) < 0.5:
                    model.add(MaxPool1D(pool_size = random.choice(POOL_SIZE), 
                                        data_format = 'channels_first'))
                else:
                    model.add(AveragePooling1D(pool_size = random.choice(POOL_SIZE), 
                                               data_format = 'channels_first'))
            
            model.add(BatchNormalization())

            if random.uniform(0, 1) < 0.5:
                model.add(Dropout(random.choice(DROPOUT_RATE)))
            
        model.add(Flatten())
            
        for i in range(number_denses):
            model.add(Dense(random.choice(NEURONS_FILTERS), 
                            activation = 'tanh'))

            model.add(BatchNormalization())
                
            if random.uniform(0, 1) < 1:
                model.add(Dropout(random.choice(DROPOUT_RATE)))
                
        model.add(Dense(features, activation = 'tanh'))
        
        model.build((None, timesteps, features))
        print(model.summary())
        
    except Exception as e:
        print(e)
        model = build_CNN_model(timesteps, features, name)
    
    finally:
        return model


def build_CNN_LSTM_model(timesteps, features, batch, name):
    try:
        number_Conv1d = random.randint(0, 5)
        number_denses = random.randint(0, 5)
        number_LSTMs = random.randint(0, 5)

        inp = Input(batch_input_shape = (None, timesteps, features),
                    batch_size = batch)
        
        ks = random.choice(KERNEL_SIZE)
        
        if (timesteps - ks) + 1 <= 0:
            raise Exception('Hiper-parameters don\'t match up... trying again')
        
        model_conv = Conv1D(random.choice(NEURONS_FILTERS), 
                            kernel_size = ks,
                            activation = 'tanh', 
                            data_format = 'channels_last', 
                            input_shape = (timesteps, features))(inp)

        model_conv = BatchNormalization()(model_conv)
        
        for i in range(number_Conv1d):
            ks = random.choice(KERNEL_SIZE)
            
            if (timesteps - ks) + 1 <= 0:
                raise Exception('Hiper-parameters don\'t match up... trying again')
            
            model_conv = Conv1D(random.choice(NEURONS_FILTERS), 
                                kernel_size = ks, activation = 'tanh', 
                                data_format = 'channels_last')(model_conv)
                
            if random.uniform(0, 1) < 0.5:
                if random.uniform(0, 1) < 0.5:
                    model_conv = MaxPool1D(pool_size = random.choice(POOL_SIZE), 
                                           data_format = 'channels_first')(model_conv)
                else:
                    model_conv = AveragePooling1D(pool_size = random.choice(POOL_SIZE), 
                                                  data_format = 'channels_first')(model_conv)

            model_conv = BatchNormalization()(model_conv)
                
            if random.uniform(0, 1) < 0.5:
                model_conv = Dropout(random.choice(DROPOUT_RATE))(model_conv)


        model_conv = Flatten()(model_conv)

        if number_LSTMs > 0:
            model_lstm = LSTM(random.choice(NEURONS_FILTERS), 
                              batch_input_shape = (batch, timesteps, features),
                              return_sequences = True,
                              activation = 'tanh')(inp)
        else:
            model_lstm = LSTM(random.choice(NEURONS_FILTERS), 
                              batch_input_shape = (batch, timesteps, features),
                              activation = 'tanh')(inp)
        
        model_lstm = BatchNormalization()(model_lstm)
        
        for i in range(number_LSTMs):
            if i == number_LSTMs - 1:
                model_lstm = LSTM(random.choice(NEURONS_FILTERS), 
                                  activation = 'tanh')(model_lstm)
            else:
                model_lstm = LSTM(random.choice(NEURONS_FILTERS), 
                                  return_sequences = True, 
                                  activation = 'tanh')(model_lstm)

            model_lstm = BatchNormalization()(model_lstm)
                
            if random.uniform(0, 1) < 1:
                model_lstm = Dropout(random.choice(DROPOUT_RATE))(model_lstm)


        model_lstm = Flatten()(model_lstm)

        model = Concatenate()([model_conv, model_lstm])
        
        for i in range(number_denses):
            model = Dense(random.choice(NEURONS_FILTERS), 
                            activation = 'tanh')(model)

            model = BatchNormalization()(model)
                
            if random.uniform(0, 1) < 1:
                model = Dropout(random.choice(DROPOUT_RATE))(model)
                
        model = Dense(features, activation = 'tanh')(model)
        
        model_final = Model(inputs = inp, outputs = model, name = name)
        
        model_final.build((None, timesteps, features))
        model_final.summary()
    
    except Exception as e:
        print(e)
        model_final = build_CNN_LSTM_model(timesteps, features, batch, name)
    
    finally:
        tf.keras.utils.plot_model(model_final, './' + name + '.png', 
                                  show_shapes = True)
        return model_final
    


def build_MLP_model(timesteps, features, name):
    try:
        model = Sequential(name = name)
        
        number_denses = random.randint(0, 5)
            
        model.add(Dense(random.choice(NEURONS_FILTERS), 
                        activation = 'tanh', 
                        input_shape = (timesteps, features)))

        model.add(BatchNormalization())
        
        for i in range(number_denses):
            model.add(Dense(random.choice(NEURONS_FILTERS), 
                            activation = 'tanh'))

            model.add(BatchNormalization())
                
            if random.uniform(0, 1) < 1:
                model.add(Dropout(random.choice(DROPOUT_RATE)))
                
        model.add(Flatten())
        number_denses = random.randint(0, 5)

        for i in range(number_denses):
            model.add(Dense(random.choice(NEURONS_FILTERS), 
                            activation = 'tanh'))

            model.add(BatchNormalization())
                
            if random.uniform(0, 1) < 1:
                model.add(Dropout(random.choice(DROPOUT_RATE)))
        
        model.add(Dense(features, activation = 'tanh'))
        
        model.build((None, timesteps, features))
        print(model.summary())
        
    except Exception as e:
        print(e)
        model = build_MLP_model(timesteps, features, name)
    
    finally:
        return model


# Compiling and fit the model
def compile_and_fit(model, x, y, epochs = 25, batch_size = 72, cv_splits = 10):
    callback = []
    
    callback.append(EarlyStopping(monitor = 'val_loss', min_delta = 0, 
                                  patience = 200, verbose = 0, 
                                  mode = 'auto', restore_best_weights = True))
    
    callback.append(ReduceLROnPlateau(monitor = 'val_loss', factor = 0.1, 
                                      patience = 30, verbose = 0,
                                      mode = 'auto', cooldown = 0, 
                                      min_lr = 0.00001))
    
    # compile
    model.compile(loss = rmse, 
                  optimizer = 'adam', 
                  metrics = ['mae'])    
    # fit
    hist_list = {}
    cv = 0
    # time series cross validator
    tscv = TimeSeriesSplit(n_splits = cv_splits)
    
    print('\n---------- Cross validation training model', 
          model.get_config()['name'], '----------\n')
    
    for train_index, test_index in tscv.split(x):
        # further split into training and validation sets
        train_idx, val_idx = split_data(train_index, percentage = 20)
        
        x_train, y_train = x[train_idx], y[train_idx]
        x_val, y_val = x[val_idx], y[val_idx]
        
        if model.get_config()['name'].startswith('LSTM_Stateful_'):
            callback.append(CustomCallback_reset_state())
        
        history = model.fit(x_train, y_train, validation_data = (x_val, y_val), 
                            epochs = epochs, batch_size = batch_size, verbose = 2,
                            shuffle = False, callbacks = callback)
        
        hist_list['model_' + str(cv)] = history
        cv += 1
        
        plot_learning_curves(history, model_name = model.get_config()['name'])
    
    return model, hist_list


def fit_model(model, x, y, epochs, batch_size, cv_splits):
    start_time = time.time()    

    model, hist_list = compile_and_fit(model, x, y, epochs, 
                                       batch_size, cv_splits)
    
    execution_time = time.time() - start_time

    history = [i.history for i in hist_list.values()]

    results_model = {'val_loss': 0.0, 'val_mae': 0.0}
    
    for i in history:
        results_model['val_loss'] += float(min(i['val_loss']))
        index = i['val_loss'].index(min(i['val_loss']))
        results_model['val_mae'] += float(i['val_mae'][index])
        
    results_model['val_loss'] /= len(history)
    results_model['val_mae'] /= len(history)
    
    model1 = Model_Forecast(rmse = results_model['val_loss'],
                            mae = results_model['val_mae'], 
                            model = model, 
                            type_model = 'LSTM', 
                            execution_time = execution_time)
    
    
    tf.keras.backend.clear_session()
    gc.collect()
        
    return model1
    

def fit_best_model(model, x, y, epochs, batch_size):
    callback = []
    callback.append(EarlyStopping(monitor = 'loss', min_delta = 0, 
                                  patience = 200, verbose = 0, 
                                  mode = 'auto', restore_best_weights = True))

    op = tf.keras.optimizers.Adam(learning_rate=0.0001)
    # compile
    model.compile(loss = rmse, 
                  optimizer = op, 
                  metrics = ['mae']) 

    model.fit(x, y, epochs = epochs, batch_size = batch_size, 
              shuffle = False, callbacks = callback)

    tf.keras.utils.plot_model(model, './BestModel.png', 
                              show_shapes = True)

    return model


def benchmarking(timesteps, features, x, y, epochs, batch_size, cv_splits, 
                 number_models_LSTM_Stateful = 10, number_models_LSTM = 10, 
                 number_models_CNN = 10, number_models_GRU = 10, 
                 number_models_CNN_LSTM = 10, number_models_MLP = 10):
    models = []
    
    for i in range(number_models_LSTM_Stateful):
        models.append(build_LSTM_model_stateful(timesteps, features, 
                                                batch_size,
                                                'LSTM_Stateful_' + str(i)))
        print("Done building model...")
    #tf.keras.backend.clear_session()
    #gc.collect()
        
    for i in range(number_models_LSTM):
        models.append(build_LSTM_model(timesteps, features, 
                                       'LSTM_' + str(i)))
        print("Done building model...")
        
    #tf.keras.backend.clear_session()
    #gc.collect()
    
    for i in range(number_models_GRU):
        models.append(build_GRU_model(timesteps, features, 
                                       'GRU_' + str(i)))
        print("Done building model...")
    
    #tf.keras.backend.clear_session()
    #gc.collect()
    
    for i in range(number_models_CNN):
        models.append(build_CNN_model(timesteps, features, 
                                       'CNN_' + str(i)))
        print("Done building model...")
        
    
    for i in range(number_models_CNN_LSTM):
        models.append(build_CNN_LSTM_model(timesteps, features, batch_size,
                                       'CNN_LSTM_' + str(i)))
        print("Done building model...")
        
        
    for i in range(number_models_MLP):
        models.append(build_MLP_model(timesteps, features, 'MLP_' + str(i)))
        print("Done building model...")
        
    
        
    #tf.keras.backend.clear_session()
    #gc.collect()    
    
    models = [fit_model(m, x, y, epochs, batch_size, cv_splits) for m in models]
    
    models.sort(key = operator.attrgetter('fitness'),
                reverse = False)
    
    df = pd.DataFrame(columns = ['Model name', 'Number of LSTMs',
                                 'Number of GRUs', 'Number of Convs',
                                 'Number of Denses', 'Fitness', 
                                 'RMSE', 'MAE', 'Execution time'])
    
    for m in models:
        print(m)
        df = m.append_results(df)
        
    df.to_csv('./results_models.csv', sep = ',', index = False)
    
    #return fit_best_model(models[0].model, x, y, epochs, batch_size)
    return models[0].model, models[0].rmse


# Recursive Multi-step Forecast
def forecast(model, df, scaler_target, timesteps = 24, 
             multisteps = 6, features = 2, batch_size = 24, rmse = 0.0):
    # getting the last sequence of known value
    input_seq = df[-timesteps:].values
    inp = input_seq
    forecasts = []
    number_roads_forecast = []
    
    yhat_inf = []
    yhat_sup = []
    # multisteps tells us how many iterations we want to perform, i.e., how many days
    # we want to predict
    for step in range(1, multisteps + 1):

        inp = inp.reshape(1, timesteps, features)

        yhat = model.predict(inp)
        yhat_inf.append([i - rmse for i in yhat[0]])
        yhat_sup.append([i + rmse for i in yhat[0]])

        target = scaler_target.inverse_transform(yhat[0])

        forecasts.append(round(target[0][features - 1]))

        if features > 0:
            number_roads_forecast.append(round(target[0][0]))

        # prepare new input to forecast the next day
        inp = np.append(inp[0], yhat[0][0])

        inp = inp[-timesteps*features:]

    return forecasts, number_roads_forecast, yhat_inf, yhat_sup

# Recursive Multi-step Forecast
def forecast_evaluate(model, df, scaler_target, timesteps = 24, 
                      multisteps = 24, features = 2, batch_size = 24):
    # getting the last sequence of known value
    input_seq = df[-timesteps:].values
    inp = input_seq
    forecasts = []
    number_roads_forecast = []
    
    # multisteps tells us how many iterations we want to perform, i.e., how many days
    # we want to predict
    for step in range(1, multisteps + 1):
        inp = inp.reshape(1, timesteps, features)
        
        yhat = model.predict(inp)
        target = scaler_target.inverse_transform(yhat)
        
        forecasts.append(round(target[0][features - 1]))
        
        if features > 0:
            number_roads_forecast.append(round(target[0][0]))

        # prepare new input to forecast the next day
        inp = np.append(inp[0], yhat[0])
        
        inp = inp[-timesteps*features:]
        
    plot_prediction_evaluate(df, forecasts, scaler_target)
    
    if features > 0:
        plot_prediction_othervalue_evaluate(df, number_roads_forecast, 
                                            scaler_target)



def plot_prediction_evaluate(data, forecasts, scaler):
    d1 = data.values[-48:-24, -1]
    d2 = data.values[-24:, -1]
    
    d1 = scaler.inverse_transform([d1])[0]
    d2 = scaler.inverse_transform([d2])[0]
    last_value_day = d1[0]
    
    print('Real data (first 24h):', d1, '\n')
    print('Real data (second 24h):', d2)
    print('Forecasting values:', forecasts)
    
    f = []; f.append(round(last_value_day)); f.extend(forecasts[-23:])
    
    plt.figure(figsize = (8, 6))
    plt.plot(range(len(d1)), d1, label = 'Real', color = '#FF9999', 
             linewidth = 3)
    
    plt.plot(range(len(d1) - 1, len(d1) + len(d2) - 1), d2, label = 'Real to Predict', 
             color = '#FFCC99', linewidth = 3)
    
    plt.plot(range(len(d1) - 1, len(d1) + len(f) - 1), f, label = 'Forecasted', 
             color = 'red', linewidth = 3)
    
    plt.title('Number of Incidents per hour')
    plt.ylabel('Number of Incidents')
    plt.xlabel('Hours')
    plt.legend()
    plt.show()


def plot_prediction_othervalue_evaluate(data, forecasts, scaler):
    d1 = data.values[-48:-24, 0]
    d2 = data.values[-24:, 0]
    
    d1 = scaler.inverse_transform([d1])[0]
    d2 = scaler.inverse_transform([d2])[0]
    last_value_day = d1[0]
    
    print('Real data (first 24h):', d1, '\n')
    print('Real data (second 24h):', d2)
    print('Forecasting values:', forecasts)
    
    f = []; f.append(round(last_value_day)); f.extend(forecasts)
    
    plt.figure(figsize = (8, 6))
    plt.plot(range(len(d1)), d1, label = 'Real', color = '#FF9999', 
             linewidth = 3)
    
    plt.plot(range(len(d1) - 1, len(d1) + len(d2) - 1), d2, label = 'Real to Predict', 
             color = '#FFCC99', linewidth = 3)
    
    plt.plot(range(len(d1) - 1, len(d1) + len(f) - 1), f, label = 'Forecasted', 
             color = 'red', linewidth = 3)
    
    plt.title('Number of Roads affected per hour')
    plt.ylabel('Number of Principal Roads affected')
    plt.xlabel('Hours')
    plt.legend()
    plt.show()


def plot_prediction(data, forecasts, scaler):
    d = data.values[-24:, -1]
    d = scaler.inverse_transform([d])[0]
    
    print('Real data:', d)
    print('Forecasting values:', forecasts)
    
    f = []; f.append(round(d[-1])); f.extend(forecasts[-23:])
    
    plt.figure(figsize = (8, 6))
    plt.plot(range(len(d)), d, label = 'Real', color = '#FF9999', 
             linewidth = 3)
    plt.plot(range(len(d) - 1, len(d) + len(f) - 1), f, label = 'Forecasted', 
             color = '#FFCC99', linewidth = 3)
    plt.title('Number of Incidents per hour')
    plt.ylabel('Number of Incidents')
    plt.xlabel('Hours')
    plt.legend()
    plt.show()


def plot_othervalue_prediction(data, forecasts, scaler):
    d = data.values[-24:, 0]
    d = scaler.inverse_transform([d])[0]
    
    print('Real data:', d)
    print('Forecasting values:', forecasts)
    
    f = []; f.append(round(d[-1])); f.extend(forecasts)
    
    plt.figure(figsize = (8, 6))
    plt.plot(range(len(d)), d, label = 'Real', color = '#FF9999', 
             linewidth = 3)
    plt.plot(range(len(d) - 1, len(d) + len(f) - 1), f, label = 'Forecasted', 
             color = '#FFCC99', linewidth = 3)

    plt.title('Number of Roads affected per hour')
    plt.ylabel('Number of Principal Roads affected')
    plt.xlabel('Hours per day')
    plt.legend()
    plt.show()


def plot_learning_curves(history, model_name):
    print(history.history.keys())
    
    # summarize history for loss
    plt.plot(history.history['loss'], color = '#FF9999', linewidth = 3)
    plt.plot(history.history['val_loss'], color = '#FFCC99', linewidth = 3)
    plt.title('Model ({}) loss'.format(model_name))
    plt.ylabel('Loss')
    plt.xlabel('Epoch')
    plt.legend(['train', 'val'], bbox_to_anchor = (1.05, 1), loc = 'upper left',
               borderaxespad = 0.)

    plt.show()


class Model_Forecast:
    def __init__(self, mae, rmse, model, 
              type_model, execution_time):
        
        self.mae = round(mae, 5)
        self.rmse = round(rmse, 5)
        self.execution_time = round(execution_time, 5) 
        self.type_model = type_model
        self.model = model
        
        self.fitness = round(self.getFitness(), 5)
        
        
    def __str__(self):  
        return ('Model name: %s with executing time: %s seconds\n\tWith fitness: %s\n\tRMSE: %s\n\tMAE: %s' 
                % (self.model.get_config()['name'], self.execution_time, self.fitness, 
                   self.rmse, self.mae))
    
    
    def append_results(self, df_results):
        n_LSTMs = 0
        n_Denses = 0
        n_GRUs = 0
        n_Convs = 0
        
        layers = self.model.get_config()['layers']
        
        for i in range(len(layers)):
            if layers[i]['config']['name'].startswith('conv1d'):
                n_Convs += 1
            elif layers[i]['config']['name'].startswith('dense'):
                n_Denses += 1
            elif layers[i]['config']['name'].startswith('lstm'):
                n_LSTMs += 1
            elif layers[i]['config']['name'].startswith('gru'):
                n_GRUs += 1
        
        df = pd.DataFrame([[self.model.get_config()['name'], 
                            n_LSTMs, n_GRUs, n_Convs, n_Denses,
                            self.fitness, self.rmse, self.mae, 
                            self.execution_time]], 
                          columns = ['Model name', 'Number of LSTMs',
                                     'Number of GRUs', 'Number of Convs',
                                     'Number of Denses', 'Fitness', 
                                     'RMSE', 'MAE', 'Execution time'])
        
        return df_results.append(df)
    
    def getFitness(self):
        return self.rmse*3/4 + self.mae*1/4
    

class CustomCallback_reset_state(tf.keras.callbacks.Callback):
    
    def on_train_batch_end(self, batch, logs = None):
        print('\n[RESET STATE]\nTraining: end of batch {}. Reseting LSTM states...'.format(batch))
        self.model.reset_states()



# ax.fill_between(x, y1, y2, where=y2 >= y1, facecolor='green', interpolate=True)