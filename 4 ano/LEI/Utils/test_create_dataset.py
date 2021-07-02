def create_dataset(propertiers,random_opinions_values,comment, sentiment):
    #header of the json
    st = '"opinion":{\n'
    # list of propertiers and values
    temp =""
    for i in range(len(properties)):
        temp = temp +'"'+str(properties[i])+ '": '+'"'+str(random_opinions_values[i]) +'",\n'
    st = st+temp+'"comment": '+'"'+comment+'",\n"sentiment": '+'"'+sentiment+'"'+"\n}"
    print(st)

def create_dic_opinions(module,propertiers,random_opinions_values,comment, sentiment):
    opinions_dict["module"] = module 
    
    for i in range(len(properties)):
        opinions_dict[properties[i]] = random_opinions_values[i]
    opinions_dict["comment"] = comment
    opinions_dict["sentiment"] = sentiment
    return opinions_dict
        
    
lst =[ ] 
properties = ["interface", "interactive", "gamification"]
values = [2, 1, 3]
comment = "Not good"
sentiment = "negative"
opinions_dic = create_dic_opinions(properties,values,comment,sentiment)
lst.append(opinions_dic)

properties= ["interface", "interactive", "gamification"]
values = [4, 5, 4]
comment = "Very good"
sentiment = "positive"
opinions_dic = create_dic_opinions(properties,values,comment,sentiment)
lst.append(opinions_dic)


print(lst)

    
