import time #just for testing
import pkg_resources
from symspellpy import SymSpell, Verbosity

from bs4 import BeautifulSoup
import re
import string
import sys, getopt
from spellchecker import SpellChecker
from googletrans import Translator
import unidecode



#Remove tags html of the text
def remove_html(text):
	soup = BeautifulSoup(text,'lxml')
	ntext = soup.get_text()
	return ntext  


#Converts the string to the automata alphabet
def convert_automata_alphabet(c):
    ax = ''
    if(c.isalpha() or c.isnumeric()):
        if(c in string.ascii_lowercase):
            ax = 'x'
        else: 
            ax = 'X'
    else:
        ax = '.'
    return ax

def word_breaker(transitions,initial,accepting,s):
    state = initial
    state_b = initial
    ns = ''
    s = unidecode.unidecode(s)
    for c in s:
        ax = convert_automata_alphabet(c)
        state_b = state
        state = transitions[state][ax]
        #print(state)
        if( (state_b == 3 and state == 2) or (state_b == 3 and state == 1)
         or (state_b == 2 and state == 1) or (state_b == 4 and state == 1) or
         (state_b == 4 and state == 2)):
            c = ' '+c
        ns = ns+c

    #return ns, state in accepting
    return ns

def phrase_breaker(t):
	#automata definition
	dfa = {0:{'X':1, 'x':2, '.':4},
       	   1:{'X':1,'x':2, '.':3},
           2:{'x':2, 'X':1, '.':3},
       	   3:{'X':1, 'x':2, '.':5},
       	   4:{'X':1, 'x':2, '.':5},
           5:{'.':5, 'x':5, 'X': 5}}
    #split the phrase in words 
	l = t.split()
	li =[]
	s = " "
	#apply the word breaker 
	for i in range(len(l)):
		li.append(word_breaker(dfa,0,{1,2,3,4},l[i]))
		s=" ".join(li)
	return s

def convert_lower_case(s):
	return s.lower()

def only_punctuation(word):
	for i in word:
		if not (i in string.punctuation):
			return False
	return True

def check_word_has_punctuation(word):
    flag = False
    w=''
    for p in string.punctuation:
        #print(p,word)
        if(word.find(p)!= -1 and only_punctuation(word) == False):
            flag = True
            w = re.sub('['+string.punctuation+']', '', word).split()
            break
    if(flag):
        return flag, w[0], p
    else: 
        return flag, p

def opinion_spell_checker(opinion):
	#words = re.sub('['+string.punctuation+']', '', opinion).split()
	words = opinion.split()
	li =[]
	s = " "
	spell = SpellChecker(language='pt')
	# find those words that may be misspelled
	#misspelled = spell.unknown(['Olá mund.'])
	for word in words:
		cwhp = check_word_has_punctuation(word)
		if(cwhp[0]):
			li.append(spell.correction(cwhp[1]))
			li.append(cwhp[2])		
		else: 
			li.append(spell.correction(word))
		s=" ".join(li)
	return s 

def opinion_translate(opinion):
	translator = Translator()
	#origin = translator.translate(opinion, src='pt').origin
	translate = translator.translate(opinion, dest='en').text
	return translate

def translateTokenized(opinion):
	translator = Translator()
	translatedTokens = []

	for word in opinion:
		translatedTokens.append(translator.translate(word, dest='en').text)
		
	return translatedTokens	

##pre_processor para input do leonardo 
def pre_processing_leo(opinion_text):

	opinion_text = remove_html(opinion_text)
	#Converte tudo para lower case
	opinion_text = convert_lower_case(opinion_text)
	#Separar palavras coladas/ frases coladas.
	opinion_text = phrase_breaker(opinion_text)
	#Verifica erros ortográficos.
	opinion_text = opinion_spell_checker(opinion_text)
	#Traduz o texto 
	opinion_text = opinion_translate(opinion_text)
	#Tokenization do texto e remoção stop words.
	#opinion_text = t.filtered_comment(opinion_text)

	return opinion_text

#--------------------

sym_spell = SymSpell(max_dictionary_edit_distance=2, prefix_length=7)

#Load da biblioteca com os dicionários.
def load_SymSpell():
	global sym_spell
	dictionary_path = pkg_resources.resource_filename("symspellpy", "frequency_dictionary_en_82_765.txt")
	bigram_path = pkg_resources.resource_filename( "symspellpy", "frequency_bigramdictionary_en_243_342.txt")
	
	# term_index is the column of the term and count_index is the column of the term frequency
	sym_spell.load_dictionary(dictionary_path, term_index=0, count_index=1)
	sym_spell.load_bigram_dictionary(bigram_path, term_index=0, count_index=2)


#correção da opinião 
def opinion_spell_corrections(opinion):
	suggestions = sym_spell.lookup_compound(opinion, max_edit_distance=2 ) #Para permitir numeros, colocar :ignore_non_words=True
	op=''.join(str(w.term) for w in suggestions)
	#print(op)
	words = op.split()
	s = ''
	for w in words:
		sugge = sym_spell.lookup(w, Verbosity.CLOSEST,max_edit_distance=2)# ignore_token="[0-9]*")
		if sugge: # se existir no dicionário 
			s+=''.join([w,' ']) 
	s=s[:-1]#remove o ultimo carater
	return s


#pre_processor para input dos modelos(ou seja,para treinar os modelos). 
def pre_processing_for_train(opinion_text):
	#start = time.time()
	opinion_text = remove_html(opinion_text)
	opinion_text = opinion_spell_corrections(opinion_text)
	opinion_text = phrase_breaker(opinion_text)
	#end = time.time()
	#print("Resultado Final:"+ opinion_text)
	#print("Demorou: "+ str(round((end - start),4)))
	return opinion_text


#TESTE do symspell
#load_SymSpell()
#a =opinion_spell_corrections("This ubject wa s quitte asdfaasdfjnjoawsdf confusinng, whic h Slows downthe learning.")
#print(a)









