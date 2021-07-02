import numpy as np
#Generate random number betwen values a and b 
def generate_number(a, b):
    np.random.seed() 
    x = np.random.randint(a,b)
    return x
    

def get_vector_polarity(vector):
    polarity= ''
    if np.mean(vector) > 3:
        polarity = 'positive'
    elif np.mean(vector) < 3:
        polarity = 'negative'
    else:
        polarity = 'neutro'
    return polarity
    
#x = [4, 2, 3, 5, 1, 2]

#print(getVectorPolarity(x))
