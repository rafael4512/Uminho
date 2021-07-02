# Dataset's

> Dataset original:
- :page_facing_up: **url_data_set** - url do dataset original do kaggle com 100k comentários

<br />
<hr />
<br />

> Dataset usados para os modelos de machine learning:
> &nbsp;&nbsp;&nbsp;Cada coluna dos seguintes datasets correspondem a uma palavra e cada linha corresponde a um comentário.

- :page_facing_up: **df1.ftr** - Dataset da frequência de palavras já com o pré processamento feito, pronto a ser utilizado nos modelos (ML).
- :page_facing_up: **df2.ftr** - Dataset da existência de palavras já com o pré processamento feito, pronto a ser utilizado nos modelos (ML).

<br />
<hr />
<br />

> Dataset usados para os modelos de deep learning:

- :page_facing_up: **deepl_df.ftr** - Dataset com o comentário já preprocessado, os tokens e a sua classificação.


**Nota:** O formato ftr permite é um objeto(dataframe) serializado e permite um load de dataframes muito mais rápido.

<br />
<hr />
<br />

> Python files:

- :page_facing_up: **web_scraper.py** - Script python para obter opinioes do site corsera. Foi usado inicialmente porém, com a descoberta dos dataset do kaggle nao foi usado na versão final apresentada.
