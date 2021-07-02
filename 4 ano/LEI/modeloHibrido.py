import os
import sys

clear = lambda: os.system('cls')
filename_model="best_model.keras"
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3' #INFO, WARNING, and ERROR messages are not printed
#import nltk
#nltk.download('stopwords')
#nltk.download('punkt')

import numpy as np
import pandas as pd
import pickle
import tensorflow as tf
import Preprocessing.preprocessor as ppc
from Preprocessing.tokenizer import filtered_comment
from Sentiment_Analysis.sentiment_analysis import get_polarity_from_comment, get_important_words
from tensorflow.keras.layers.experimental.preprocessing import TextVectorization
from Utils.progress_bar import *

from tensorflow import keras
from simple_term_menu import TerminalMenu

### Função que vai obter a classificação do vader do comentário fornecido.
def opinion_analysis(comment):
    processed_text = ppc.pre_processing_leo(comment) #Texto pre_processado.
    tokenized_text = filtered_comment(processed_text) #Vai remover stop words e aplicar tokenization
    polarity = get_polarity_from_comment(tokenized_text) #Obtem o array com a polaridade das palavras do vader 
    important_words = get_important_words(tokenized_text) # Calcula a média da polaridade, e dá o resultado.
    return polarity, important_words

### Função que vai obter a classificação do modelo de machine_learning q se usar no fim.
def machine_learning(comment):
    
    from_disk = pickle.load(open("tv_layer.pkl", "rb"))
    vectorizer = TextVectorization.from_config(from_disk['config'])
    # You have to call `adapt` with some dummy data (BUG in Keras)
    vectorizer.adapt(tf.data.Dataset.from_tensor_slices(["xyz"]))
    vectorizer.set_weights(from_disk['weights'])
    
    
    ppc.load_SymSpell()
    english_text = ppc.opinion_translate(comment) #Texto traduzido.
    texto_preprocessado = ppc.pre_processing_for_train(english_text)
    
    '''
        Aqui fazer load do modelo, e substituir a polarity pelo resultado
    '''
    model = keras.models.load_model(filename_model) 

    x_test = vectorizer(np.array([texto_preprocessado])).numpy()
    
    print(model.predict(x_test))
    preds=np.round(model.predict(x_test),0)
    pred_labels = np.argmax(preds, axis=-1)
    
    return pred_labels

### Função que vai obter a polaridade  híbrida de uma frase input
def assess_polarity(comment):
    polarity, importante = opinion_analysis(comment)
    polarityML = machine_learning(comment)
    
    return_polarity = 3 #'not_calculated'
    
    if ((2.7 <= polarity <= 3.3) and polarityML==1):
        return_polarity = 1 #'neutro'
    
    elif ((polarity < 2.7) and polarityML==1):
        return_polarity = 0 #'negativo'
    
    elif ((polarity > 3.3) and polarityML==1):
        return_polarity = 2 #'positivo'
        
    elif ((polarity >= 3.15) and polarityML==2):
        return_polarity = 2 #'positivo'
    
    elif ((3 < polarity <= 3.15) and polarityML==2):
        return_polarity = 1 #'neutro'
        
    elif ((2.85 <= polarity < 3) and polarityML==0):
        return_polarity = 0 #'negativo'
    
    elif (polarityML==2):
        return_polarity = 2 #'positivo'
    
    elif (polarityML==1):
        return_polarity = 1 #'neutro'
        
    else: 
        return_polarity = 0 #'negativo'
    
    head, *tail = polarityML
    print(''.join(["(Modelo CRNN,VADER) = (",str(head),",", str(polarity),")\nResultado final: ",polarity_to_str(return_polarity)]))
    #print(polarity)
    #print(polarityML)
    #print(return_polarity)
    
    return return_polarity

def assess_polarity_2(polarity,polarityML):
    #polarity, importante = opinion_analysis(comment)
    #polarityML = machine_learning(comment)
    
    return_polarity = 3 #'not_calculated'
    
    if ((2.7 <= polarity <= 3.3) and polarityML==1):
        return_polarity = 1 #'neutro'
    
    elif ((polarity < 2.7) and polarityML==1):
        return_polarity = 0 #'negativo'
    
    elif ((polarity > 3.3) and polarityML==1):
        return_polarity = 2 #'positivo'
        
    elif ((polarity >= 3.15) and polarityML==2):
        return_polarity = 2 #'positivo'
    
    elif ((3 < polarity <= 3.15) and polarityML==2):
        return_polarity = 1 #'neutro'
        
    elif ((2.85 <= polarity < 3) and polarityML==0):
        return_polarity = 0 #'negativo'
    
    elif (polarityML==2):
        return_polarity = 2 #'positivo'
    
    elif (polarityML==1):
        return_polarity = 1 #'neutro'
        
    else: 
        return_polarity = 0 #'negativo'
    
    return return_polarity
    
### Função para correr o modelo hibrido no dataset de teste
def test_on_dataset():
    ############################################################################################
    from_disk = pickle.load(open("tv_layer.pkl", "rb"))
    vectorizer = TextVectorization.from_config(from_disk['config'])
    # You have to call `adapt` with some dummy data (BUG in Keras)
    vectorizer.adapt(tf.data.Dataset.from_tensor_slices(["xyz"]))
    vectorizer.set_weights(from_disk['weights'])
    model = keras.models.load_model(filename_model) 
    
    ppc.load_SymSpell()
    ############################################################################################
    
    data1 = pd.read_feather('dataset/deepl_df.ftr', columns=None, use_threads=True)
    data1.head()
    
    results = []
    size = len(data1.index)
    #print(size)
    i=0
    for index, row in data1.iterrows():
        showbar(index,1000)
        
        comment = row['comentario']
        
        ##################################################################################
        #english_text = ppc.opinion_translate(comment) #Texto traduzido.
        texto_preprocessado = ppc.pre_processing_for_train(comment)  
        '''
            Aqui fazer load do modelo, e substituir a polarity pelo resultado
        '''
        x_test = vectorizer(np.array([texto_preprocessado])).numpy()

        preds=np.round(model.predict(x_test),0)
        pred_labels = np.argmax(preds, axis=-1)
        ##################################################################################
        polarity, importante = opinion_analysis(comment)
        ##################################################################################
        
        poruPol = assess_polarity_2(polarity,pred_labels)
        results.append(poruPol)
        
        i+=1
        if(i>1000):
            break
    
    totalSize = 0
    correct = 0
    
    totalZero = 0
    totalOne = 0
    totalTwo = 0
    
    ZeroAsZero = 0
    ZeroAsOne = 0
    ZeroAsTwo = 0
    
    OneAsOne = 0
    OneAsZero = 0
    OneAsTwo = 0
    
    TwoAsTwo = 0
    TwoAsZero = 0
    TwoAsOne = 0
    
    negAsPos = []
    
    for index,row in data1.iterrows():
        showbar(index,1000)
        
        polpol = row['polarityClass']
        
        if results[totalSize]==0 and polpol==0 :
            ZeroAsZero += 1
            correct += 1
            totalZero += 1
            
        elif results[totalSize]==1 and polpol==0 :
            ZeroAsOne += 1
            totalZero += 1
            
        elif results[totalSize]==2 and polpol==0 :
            ZeroAsTwo += 1
            totalZero += 1
            negAsPos.append(row['comentario'])
            
        elif results[totalSize]==1 and polpol==1 :
            OneAsOne += 1
            correct += 1
            totalOne += 1
            
        elif results[totalSize]==0 and polpol==1 :
            OneAsZero += 1
            totalOne += 1
            
        elif results[totalSize]==2 and polpol==1 :
            OneAsTwo += 1
            totalOne += 1
            
        elif results[totalSize]==2 and polpol==2 :
            TwoAsTwo += 1
            correct += 1
            totalTwo += 1
            
        elif results[totalSize]==0 and polpol==2 :
            TwoAsZero += 1
            totalTwo += 1
            
        elif results[totalSize]==1 and polpol==2 :
            TwoAsOne += 1
            totalTwo += 1
        
        totalSize+=1
        
        if(totalSize>1000):
            break
    
    clear()
    print('##------------------------------------------------------------------------------------------------------------------------------------------------------------------------##')
    print('##------------------------------------------------------------------------------------------------------------------------------------------------------------------------##')
    print('##------------------------------------------------------------------------------------------------------------------------------------------------------------------------##')
    print('##------------------------------------------------------------------------------------------------------------------------------------------------------------------------##')
    print('##------------------------------------------------------------------------------------------------------------------------------------------------------------------------##')
    print('##------------------------------------------------------------------------------------------------------------------------------------------------------------------------##')
    
    print("Numero de reviews:")
    print(totalSize)
    
    print("ACC:")
    print(correct/totalSize)
    
    print("Ocurrencias Negativo:")
    print(totalZero)
    
    print("Ocurrencias Neutro:")
    print(totalOne)
    
    print("Ocurrencias Positivo:")
    print(totalTwo)
    
    print('##---------------------##')
    print("NegativoAsNegativo:")
    print(ZeroAsZero)
    print("NegativoAsNeutro:")
    print(ZeroAsOne)
    print("NegativoAsPositivo:")
    print(ZeroAsTwo)
    
    print('##---------------------##')
    print("NeutroAsNegativo:")
    print(OneAsZero)
    print("NeutroAsNeutro:")
    print(OneAsOne)
    print("NeutroAsPositivo:")
    print(OneAsTwo)
    
    
    print('##---------------------##')
    print("PositivoAsNegativo:")
    print(TwoAsZero)
    print("PositivoAsNeutro:")
    print(TwoAsOne)
    print("PositivoAsPositivo:")
    print(TwoAsTwo)
    
    with open('NegAsPos.txt', 'w') as f:
        for item in negAsPos:
            f.write("%s\n" % item)
        
###Chamar esta função para classificar uma mensagem individual -- Para futuras vitimas deste trabalho!! Desculpem a falta de comentários 
#assess_polarity("the tutorials is not for average students it takes a very high skill to understand them also the quiz and the assignments a too tough hope you can make them a bit easy the last one in with graphs and trees was a bouncer if the maths required is so high please take a week to first explain the match fresher required in course and then start the main course")
#test_on_dataset()

def polarity_to_str(p):
    if p==0:
        return "Negativo"
    elif p==1:
        return "Neutro"
    elif p==2:
        return "Positivo"
    else:
        return "Erro com o Modelo CRNN"



def menu():
    terminal_menu = TerminalMenu(["Inserir uma crítica e classificar", "Realizar um teste com o Dataset","Exit"])
    menu_entry_index = terminal_menu.show()
    return menu_entry_index

def main():
    op=menu()
    if op==0:
        op=input("Insira a opinião que pretende classificar:")
        assess_polarity(op)
    elif op==1:
        print("Demora perto de 50 min:")
        test_on_dataset()
    else:
        exit()  


if __name__ == '__main__': 
    main()



