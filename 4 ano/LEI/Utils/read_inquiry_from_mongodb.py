from pymodm import connect, fields, MongoModel
from pymongo import MongoClient
import json

# Create connection to the mongodb database. 

#connection establish using pymodm
connect('mongodb://localhost:27017/OpinionMiningDB')

#connection establish using pymongo
client = MongoClient('mongodb://localhost:27017')
db = client.OpinionMiningDB

#creating database models to save data in the database using pymodm

class inquiry(MongoModel):
    id_inquiry = fields.CharField(primary_key=True)
    language = fields.CharField()
    datetime = fields.DateTimeField()
    module = fields.CharField()
    questions = fields.DictField()



#get the inquerity documents by name
def get_inquiry(inquiry_name):
    inq = db.inquiry.find_one({"_id" : inquiry_name})
    return inq

#inquiry1 = inquiry(
 #   id_inquiry = "PTQEFS01",
  #  language =  "pt",
   # datetime =  "2019-11-12 11:10",
    #module =  "qf",
    #questions ={ 
     # "difficulty":  "A questão apresenta um nível de dificuldade adequado.",
      #"time": "O tempo disponível para resolver a questão foi suficiente.",
      #"clarity": "A questão está apresentada de forma clara.",
      #"content": "A questão está de acordo com o conteúdo proposto.",
      #"knowledge": "Seu conhecimento sobre o assunto permitiu resolver a questão com facilidade.",
      #"interactivity": "Os feedbacks interativos na resolução da questão são apropriados."
      #}
#).save()

