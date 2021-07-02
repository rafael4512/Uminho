from pymodm import connect, fields, MongoModel
from pymongo import MongoClient


def save_dic_mongodb(lst_dic):
    #connection establish using pymodm
    connect('mongodb://localhost:27017/OpinionMiningDB')
    #connection establish using pymongo
    client = MongoClient('mongodb://localhost:27017')
    db = client.OpinionMiningDB
    myclient = MongoClient("mongodb://localhost:27017/")
    mydb = myclient["OpinionMiningDB"]
    mycol = mydb["opinions"]
    for x in lst_dic:
        mycol.insert_one(x)
