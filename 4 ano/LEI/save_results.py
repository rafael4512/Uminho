from Preprocessing.preprocessor import pre_processing
from Preprocessing.tokenizer import filtered_comment
from Sentiment_Analysis.sentiment_analysis import get_polarity_from_comment, get_important_words
import pymongo
import json

def opinion_analysis(comment):
    processed_text = pre_processing(comment)
    tokenized_text = filtered_comment(processed_text)
    polarity = get_polarity_from_comment(tokenized_text)
    important_words = get_important_words(tokenized_text)
    return polarity, important_words

client = pymongo.MongoClient("mongodb://localhost:27017/")
db = client["OpinionMiningDB"]
processed_opinions = db['processed_opinions']
opinions = db['opinions']

def processed_opinions_1():
    for opinion in opinions.find().limit(10):
        score, important_words = opinion_analysis(opinion['comment'])
        processed_opinions.update_one({'_id': opinion['_id']},{'$set': {'_id': opinion['_id'], 'module': opinion['module'], 'profile': opinion['profile'], 'interface': opinion['interface'], 'interactivity': opinion['interactivity'], 'gamification': opinion['gamification'], 'dialogues': opinion['dialogues'], 'facility': opinion['facility'], 'performance': opinion['performance'], 'accessibility': opinion['accessibility'], 'availabilty': opinion['availabilty'], 'help': opinion['help'], 'comment': opinion['comment'], 'sentiment': opinion['sentiment'], 'score': score, 'important_words': important_words}}, True)

def processed_opinions_2():
    for opinion in opinions.find().limit(10):
        score, important_words = opinion_analysis(opinion['comment'])
        processed_opinions.update_one({'_id': opinion['_id']},{'$set': {'_id': opinion['_id'], 'module': opinion['module'], 'profile': opinion['profile'], 'opinion': [], 'comment': []}}, True)
        processed_opinions.update_one({'_id': opinion['_id'], 'opinion.interface': {'$ne': opinion['interface']}}, {'$addToSet': {'opinion': {'interface': opinion['interface'], 'interactivity': opinion['interactivity'], 'gamification': opinion['gamification'], 'dialogues': opinion['dialogues'], 'facility': opinion['facility'], 'performance': opinion['performance'], 'accessibility': opinion['accessibility'], 'availability': opinion['availability'], 'help': opinion['help']}}},True)
        processed_opinions.update_one({'_id': opinion['_id'], 'comment.comment': {'$ne': opinion['comment']}}, {'$addToSet': {'comment': {'comment': opinion['comment'], 'sentiment': opinion['sentiment'], 'score': score, 'important_words': important_words}}})

processed_opinions_2()