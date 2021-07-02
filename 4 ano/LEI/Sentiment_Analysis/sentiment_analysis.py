# sentence sentiment analysis
#from utils import convert_scale
from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer

# Global variables regarding scales
OLD_MAX = 1
OLD_MIN = -1
NEW_MAX = 5
NEW_MIN = 1
OLD_RANGE = 2
NEW_RANGE = 4
    
analyser = SentimentIntensityAnalyzer()

# Convert scale [-1,1] to scale [1,5]
def convert_scale(compound):
    new_value = (((compound - OLD_MIN) * NEW_RANGE) / OLD_RANGE) + NEW_MIN
    return new_value
    
def get_polarity_from_comment(comment):
    fc = ' '.join(comment)
    vs = analyser.polarity_scores(fc)
    scaled_compound = convert_scale(vs['compound'])
    return scaled_compound

def get_important_words(comment):
    important_words = []

    for f_word in comment:
        word_score = analyser.polarity_scores(f_word)
        if word_score['compound'] >= 0.05 or word_score['compound'] <= -0.05:
            important_words.append(f_word)

    return important_words