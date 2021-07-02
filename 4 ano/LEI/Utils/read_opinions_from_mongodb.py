from pymodm import connect, fields, MongoModel
from pymongo import MongoClient
import json

#Return a list of opinions (dictionary)
def get_opinions_from_db():
	# Create connection to the mongodb database. 
	connect('mongodb://localhost:27017/OpinionMiningDB')
	#connection establish using pymodm
	#connection establish using pymong
	client = MongoClient('mongodb://localhost:27017')
	db = client.OpinionMiningDB
	op = db.opinions
	opinions = list(op.find())
	return opinions 

