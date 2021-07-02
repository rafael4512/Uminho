import requests
from bs4 import BeautifulSoup as bso
import json
import os
import sys
#caminho da diretoria do repositorio omm.
omm_path = ''.join([str(os.path.dirname(os.path.realpath(__file__))),'/../'])
# Dado que não é possivel dar import de diretorias anteriores, temos de adicionar essa dir atraves do cmd seguinte
sys.path.insert(1,''.join([omm_path]))
from Utils.progress_bar import * 

#polaridade dos comentários
dic_polarity = {'1': 'negative',
                '2': 'negative',
                '3': 'neutro',
                '4': 'positive',
                '5': 'positive'
}

#link_page - link da pagina dos comentários do corsera.
#n_star - procura comentários com "n_star" estrelas.
#page - nº da pagina a procurar.
def coursera_scraper(link_page, n_star, page):
    page = requests.get(link_page+'star='+str(n_star)+'&page='+str(page)) #faz o pedido get
    soup = bso(page.content, 'lxml')

    # Get the wrapper of all the the reviews from that page
    results = list(soup.find_all("div", attrs={"class": "review"}))
    # Iterate through results in one page and create list of review objects
    review_list = []
    for result in results:
        rev_wrap = result.contents[0]
        rev_text = rev_wrap.find_all("div", attrs={"class": "reviewText"})[0].text
        #guarda o comentario e a sua info num dicitonario
        dict_review= {}
        dict_review['rev_text']  = rev_text
        dict_review['rev_stars']  = n_star
        dict_review['rev_polarity'] = dic_polarity[str(n_star)]
        review_list.append(dict_review)
    return review_list



def main():
    link_page='https://www.coursera.org/learn/python/reviews?'
    print('review_text;stars;polarity')
    review_coursera = []
    i=0
    n_pages=5
    for star in range(1, 6):       #Ciclo for que vai buscar comentarios entre [1,5] estrelas.
        for page in range(1, n_pages):   #Ciclo for que vai buscar as paginas de 1 a 4 dos comentarios entre [1,5] estrelas.
            review_coursera.append(coursera_scraper(link_page,star,page))
            showbar(i,(n_pages-1)*5)    #print da progress_bar 
            i+=1
            #print('{};{};{}'.format(review['rev_text'], review['rev_stars'], review['rev_polarity']))
    print(type(review_coursera), len(review_coursera))
    with open('coursera_review_dataset.json', 'w+') as fp:
        json.dump(review_coursera, fp) 





main()
