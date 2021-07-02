import sys
import operator
import os
import json
import pandas as pd
import numpy as np
import feather
#import time
from collections import OrderedDict

from simple_term_menu import TerminalMenu

#caminho da diretoria do repositorio omm.
omm_path = ''.join([str(os.path.dirname(os.path.realpath(__file__))),'/../'])

# Dado que não é possivel dar import de diretorias anteriores, temos de adicionar essa dir atraves do cmd seguinte
sys.path.insert(1,''.join([omm_path]))

#local imports
from Utils.progress_bar import * 
import preprocessor  as p
import tokenizer  as t


#file_dir=''.join([omm_path,'coursera_review_dataset.json'])	#file with reviews
file_dir=''.join([omm_path,'xaf.json'])	#file with reviews
words_occur=OrderedDict() 				#number of occurences of a word.

number_comments=125						# just for print progress bar
comments=dict()							# processed comments

#lê o ficheiro json com as reviews sendo criado 2 dicionarios, um com as occorencias de cada palavra e outro com os commentários já processados.
def process_jsonFile():
	global number_comments
	f = open(file_dir,'r')
	data=json.load(f)
	number_comments=len(data)*25 
	i=0
	#percorre o json 
	for l1 in data:
		for l2 in l1:
			comm=p.pre_processing_for_train(l2['rev_text'])
			showbar(i,number_comments)
			l_words=t.filtered_comment(comm)
			#create value 
			dict_v= {}
			dict_v['rev_text']=comm
			dict_v['tokens']=l_words
			dict_v['rev_polarity']=l2['rev_polarity']
			comments[i+1]=dict_v
			i+=1
			for w in l_words:
				if (w in words_occur):
					if(words_occur[w]>4):	#move a chave para primeiro no map
						words_occur.move_to_end(w,last=False)
					words_occur[w]+=1
				else:
					words_occur[w]=1
	number_comments=i
	f.close()

#processa o um split do ficheiro amazon,  xaf.json.
def process_jsonFile2():
	global number_comments
	f = open(file_dir,'r')
	data=json.load(f)
	number_comments=len(data)
	i=0
	#print(data[str(0)]['rev_text'])
	#percorre o json 
	p.load_SymSpell()
	for index in range(518877,518877+number_comments):
		comm=p.pre_processing_for_train(data[str(index)]['rev_text'])
		showbar(i,number_comments)
		l_words=t.filtered_comment(comm)
		#create value 
		dict_v= {}
		dict_v['rev_text']=comm
		dict_v['tokens']=l_words
		dict_v['rev_polarity']=data[str(index)]['rev_polarity']
		comments[i+1]=dict_v
		#print(i)
		i+=1
		for w in l_words:
			if (w in words_occur):
				if(words_occur[w]>4):	#move a chave para primeiro no map
					words_occur.move_to_end(w,last=False)
				words_occur[w]+=1
			else:
				words_occur[w]=1
	number_comments=i
	#print("OLEEEE")
	f.close()



# -------------------
# DATAFRAMES
# -------------------

'''
Cria dataframe com os dicionários.
Dataframe -> Este dataframe coloca todas as palavras de todos commentarios processados nas colunas do df. 
			  Cada commentário seguiga para cada commenta corresponde a uma linha , onde é colocado o numero de ocurrencias de uma dada palavra. 
'''

def create_dataframe1_json():
	print("Creating DataFrame 1 from Json!")
	file_words = ''.join([omm_path, 'output/words_processed.json'])
	file_comms = ''.join([omm_path, 'output/comments_processed.json'])
	file_output = ''.join([omm_path, 'output/dataframe1.csv'])
	wordsDict = json.load(open(file_words,'r'))
	comms = json.load(open(file_comms,'r'))
	
	classes = [ k for k in wordsDict.keys() if wordsDict[k] >2 ]
	classes.append('polarityClass')
	allWords = []
	w_occur = OrderedDict()
	n_comms=len(comms)
	d_polarity = {'negative':0,'neutro':1, 'positive':2}
	with open(file_output, 'w') as df1:
		df1.write(','.join(str(v) for v in classes))
		df1.write('\n')
		for i in range(1,n_comms): #para cada commentário
			comm=comms[str(i)]
			showbar(i,n_comms)
			for word in classes:
				w_occur[word] = 0 #incializa tudo a 0

			polarity = comm['rev_polarity']
			for token in comm['tokens']:
				if wordsDict[token] >2:
					w_occur[token] += 1 # conta o numero de occurencias de uma palavra.

			df1.write(''.join([','.join(str(w_occur[v]) for v in classes),str(d_polarity[polarity]),'\n']))
			#allWords.append(list(w_occur.values())) #corresponde a adicionar uma linha ao dataframe.
	print("Dataframe1.csv saved into output!")


#Similar ao anterior no entanto em vez de colocar o numero de ocurrencias coloca um 1 se aparece , e um 0 caso contrário.
def create_dataframe2_json():
	print("Creating DataFrame 2 from Json!")
	file_words = ''.join([omm_path, 'output/words_processed.json'])
	file_comms = ''.join([omm_path, 'output/comments_processed.json'])
	file_output = ''.join([omm_path, 'output/dataframe2.csv'])
	wordsDict = json.load(open(file_words,'r'))
	comms = json.load(open(file_comms,'r'))
	
	classes = [ k for k in wordsDict.keys() if wordsDict[k] >2 ]
	classes.append('polarityClass')
	allWords = []
	w_occur = OrderedDict()
	n_comms=len(comms)
	d_polarity = {'negative':0,'neutro':1, 'positive':2}
	with open(file_output, 'w') as df2:
		df2.write(','.join(str(v) for v in classes))
		df2.write('\n')
		for i in range(1,n_comms): #para cada commentário
			comm=comms[str(i)]
			showbar(i,n_comms)
			for word in classes:
				w_occur[word] = 0 #incializa tudo a 0

			polarity = comm['rev_polarity']
			for token in comm['tokens']:
				if wordsDict[token] >2:
					w_occur[token] += 1 # conta o numero de occurencias de uma palavra.

			df2.write(''.join([','.join(str(w_occur[v]) for v in classes),str(d_polarity[polarity]),'\n']))
			#allWords.append(list(w_occur.values())) #corresponde a adicionar uma linha ao dataframe.
	print("Dataframe2.csv saved into output!")



# -------------------


#escreve um dicionario num ficheiro json.
def writeToJsonFile(filename,dictionary):
	with open(filename, "w") as outfile: 
		json.dump(dictionary, outfile)

#Simple term menu inicial.
def menu():
	terminal_menu = TerminalMenu(["Process comments", "Create DataFrame1","Create DataFrame2","Create Feather Datasets","Exit"])
	menu_entry_index = terminal_menu.show()
	return menu_entry_index

#Simple term menu para os feather datasets.
def menu2():
	terminal_menu = TerminalMenu(["Dataset 1 ", "Dataset 2 ","back"])
	menu_entry_index = terminal_menu.show()
	return menu_entry_index
 


def main():
	op=menu()
	if op==0:
		process_jsonFile2()
		#print(len(words_occur.keys()))
		writeToJsonFile(''.join([omm_path,'output/comments_processed.json']),comments)
		writeToJsonFile(''.join([omm_path,'output/words_processed.json']),dict( sorted(words_occur.items(), key=operator.itemgetter(1),reverse=True)))
	elif op == 1 :
		create_dataframe1_json()
	elif op == 2: 
		create_dataframe2_json()
	elif op == 3: 
		op = menu2() 
		create_Eficient_dataset(op)
	elif op == 4: 
		exit() 

#Cria um ficheiro com um dataframe serializado , onde o load é bastante mais rápido.
def create_Eficient_dataset(op):
	if op == 2:
		main()
	else:
		fn = '/Users/ril/github/omm/output/dataframe'
		extension=['.csv','.ftr']
		#start = time.time()
		print("Creating Dataset...")
		df = pd.read_csv(''.join([fn,str(op+1),extension[0]]) ,dtype='uint8')
		print("Converting to feather...")
		df= df.to_feather(''.join([fn,str(op+1),extension[1]]))
		#end = time.time()
		#print(round((end - start),4))


#executa main.
#Nota:Este if serve para a main não ser executado quando se dá import noutro ficheiro. 
if __name__ == '__main__': 
	if os.name == 'posix':
		main()
	elif os.name == 'nt':  
		print("- Need a unix shell, due to the directories being made with the bars to the right.\nYou can install by: https://www.microsoft.com/en-us/p/ubuntu-1804-lts/9n9tngvndl3q?activetab=pivot:overviewtab")
	else:	
		print('- Run on Ambient posix.')


