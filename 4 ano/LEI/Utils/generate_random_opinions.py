import sys, getopt
import argparse
import pandas as pd
import utils as ut 

from read_properties_from_inquiry import get_properties
from save_data_mongodb import save_dic_mongodb

#from utils import generate_number, get_vector_polarity


#Read opinion file model
def read_data_opinion(file_name):
    data_opinion = pd.read_csv(file_name)
    return data_opinion

#Input: List of properties (opinions). Output: List of opinion values for each propertie
def generate_random_opinions_values(properties):
    random_opinions_values = []
    for i in range(len(properties)):
        random_opinions_values.append(ut.generate_number(1,6))    
    return random_opinions_values


def create_dic_opinions(module, properties,random_opinions_values,comment, sentiment):
    opinions_dict={}
    opinions_dict["module"] = module
    for i in range(len(properties)):
        opinions_dict[properties[i]] = random_opinions_values[i]
    opinions_dict["comment"] = comment
    opinions_dict["sentiment"] = sentiment
    return opinions_dict
    


def generate_random_opinions(inquiry_name, opinion_file_model, number_of_samples):
    p = get_properties(inquiry_name)
    module = p[0]
    properties = p[1]
    df_data_opinion = read_data_opinion(opinion_file_model)
    lst_opinions =[]
    
    cp = 0 # count number of positive comments
    cn = 0 # count number of negative comments
    for index, row in df_data_opinion.iterrows():
        if cp > number_of_samples and cn > number_of_samples:
            break
        elif row['sentiment'] =='positive':
            flag = False
            while flag == False:
                random_opinions_values = generate_random_opinions_values(properties)
                if ut.get_vector_polarity(random_opinions_values) =='positive':
                    flag = True
                    opinion_dict = create_dic_opinions(module, properties,
                                                       random_opinions_values,
                                                       row['review'], "positive") 
                    lst_opinions.append(opinion_dict)
                    cp = cp+1

        elif row['sentiment'] =='negative':
            flag = False
            while flag == False:
                random_opinions_values = generate_random_opinions_values(properties)
                if ut.get_vector_polarity(random_opinions_values) =='negative':
                    flag = True
                    opinion_dict = create_dic_opinions(module, properties,
                                                       random_opinions_values,
                                                       row['review'], "negative") 
                    lst_opinions.append(opinion_dict)
                    cn = cn+1
    return lst_opinions
                    
    
    

def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("-i", "--i", required=True,
	help="input inquiry file")
    ap.add_argument("-t", "--t", required=True,
	help="opinion file model")
    ap.add_argument("-n", "--n", required=True,
	help="number of samples")
    args = vars(ap.parse_args())
    input_inquiry = args["i"]
    opinion_file_model = args["t"]
    number_of_samples = int(args["n"])
    lst_opinions = generate_random_opinions(input_inquiry, opinion_file_model, number_of_samples)
    save_dic_mongodb(lst_opinions)
    
    
if __name__ == "__main__":
   #main(sys.argv[1:])
   main() 
