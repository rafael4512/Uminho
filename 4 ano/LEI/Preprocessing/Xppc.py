# coding=utf-8

 #caminho da diretoria do repositorio omm.
omm_path = ''.join([str(os.path.dirname(os.path.realpath(__file__))),'/../'])

# Dado que não é possivel dar import de diretorias anteriores, temos de adicionar essa dir atraves do cmd seguinte
sys.path.insert(1,''.join([omm_path]))

import pandas as pd
import numpy as np
import langdetect as ld
import Preprocessing.preprocessor  as ppc
import feather
import Preprocessing.tokenizer  as t
from Utils.progress_bar import *

def is_en(txt):
    try:
        return ld.detect(txt)=='en'
    except:
        return False

def firstPPC():
    df = pd.read_csv("reviews.csv",sep=",",skipinitialspace=True)

    print(df.size)

    df = df[df['Review'].apply(is_en)]

    df['Label'] = df['Label'].map({1 : 'negativo', 2 : 'negativo', 3 : 'neutro', 4 : 'positivo', 5 : 'positivo'})

    df.to_csv("reviews_preProcess_1.csv", index = False)
    
def secondPPC():
    df = pd.read_csv("reviews_preProcess_1.csv",sep=",",skipinitialspace=True)
    
    df['Label'] = df['Label'].map({'negativo':0,'neutro':1, 'positivo':2})
    
    colunas = ['comentario','tokens','polarityClass']
    to_feather = pd.DataFrame(columns = colunas)
   
    
    ppc.load_SymSpell()
    
    for index, row in df.iterrows():
        showbar(index,105436)
        
        comentario = row['Review']
        
        preprocessed_c = ppc.pre_processing_for_train(comentario)
        
        tokenized = t.filtered_comment(preprocessed_c) 
        
        polaridade = row['Label']
        
        new_row = pd.DataFrame([[preprocessed_c,tokenized,polaridade]],columns = colunas)
        
        to_feather = to_feather.append(new_row, ignore_index=True) 
        
    
    
    to_feather.to_feather('deepl_df.ftr')

def thirdPPC():
    df = pd.read_feather('deepl_df.ftr')
    print(df.head())
    
firstPPC()
secondPPC()  
#thirdPPC()