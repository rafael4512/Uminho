from save_results import opinion_analysis
import pymongo
import json

client = pymongo.MongoClient("mongodb://localhost:27017/")
db = client["OpinionMiningDB"]
collection = db['opinions']

positive_comments = collection.find({'sentiment':'positive'}).limit(100)
negative_comments = collection.find({'sentiment':'negative'}).limit(100)

pos_correct = 0
pos_count = 0

for item in positive_comments:
    score = opinion_analysis(item['comment'])
    if score[1] >= 4.0:
        pos_correct += 1
    pos_count += 1

neg_correct = 0
neg_count = 0

for item in negative_comments:
    score = opinion_analysis(item['comment'])
    if score[1] <= 2.0:
        neg_correct += 1
    neg_count += 1

print("Positive accuracy = {}% via {} samples".format(pos_correct/pos_count*100.0,pos_count))
print("Negative accuracy = {}% via {} samples".format(neg_correct/neg_count*100.0,neg_count))