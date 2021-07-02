# sentence tokenizer and word tokenizer
from nltk.tokenize import sent_tokenize, word_tokenize
from nltk.corpus import stopwords
import re

stop_words = set(stopwords.words("english"))

def word_tokenizer(comment):
    words = word_tokenize(comment)
    return words

def word_tokenizer2(comment):
    words = []
    for word in comment:
        words = words + word_tokenize(word)
    
    return words

def remove_stop_words(tokenized_comment):
    filtered_comment = []

    for word in tokenized_comment:
        #Para Maiusculas - [a-zA-Z]
        if word not in stop_words and len(word)>1:
            if re.search('[a-z]', word) != None:
                filtered_comment.append(word)
            
    return filtered_comment

def filtered_comment(comment):
    tokenized_comment = word_tokenizer(comment)
    filtered_comment = remove_stop_words(tokenized_comment)
    return filtered_comment