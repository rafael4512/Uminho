# AIBM-Aplicações Informáticas na Biomedicina

Este projeto consiste na criação de um <i> datawarehouse</i>  para um serviço hospitalar. Para isso, foi usado o <i>MySql Workbench</i> para a construção do modelo lógico e criação dos <i>procedures</i>,<i>triggers</i> e <i>functions</i> para inserção de conhecimento novo. De seguida, o povoamento foi feito através do <i> Tallend</i>. Para tratar as datas do <i> datawarehouse</i>  foi feito um script em <i>python</i>, de forma a, agilizar o processo  sendo que, este criava um script sql, que posteriormente seria executado no <i>MySql Workbench</i>, povoando a tabela das datas mais facilmente. O input para o povoamento eram ficheiros <i> csv</i>, que tiveram de ser processado através de <i>regex</i> para eliminar as incongruências. Por último, foi feita uma análise detalhada dos dados fornecidos, através da aplicação <i>Tableau Desktop</i>.

:scroll: Para informação mais detalhada, ver o <a href="https://github.com/rafael4512/Uminho/blob/main/4%20ano/AIBM/report.pdf">relatório</a>
<p>

<h2>Como povoar a Base de dados:</h2>
<ol>
   <li>No MySqlWorkbench, executar o modelo lógico. </li>
   <li>executar o script querys_pov.sql.</li>
   <li>executar o script das datas e das hierarquias.</li>
   <li>Abrir o talend e escolher a opção de "import an existing project".</li>
   <li>escolher a pasta que está no git.</li>
   <li>Após o talend estar abeto, mudar os caminhos dos ficheiros csv, indo a METADATA->File Delimited ---> "Carregando 2 vezes em cada csv podem alterar essas configurações". </li>
   <li>Mudar as conecção da bases de dados em: METADATA->DB Connections ----->  "Carregando 2 vezes em mysql_connection, e alterem para as configurações do vosso MYSQL.</li>
	<li>Ir a todas as jobs verificar se o caminho do File_delimiter_input está correto. Para isso, basta fazer duplo click em cima do mesmo na job respetiva. </li>
   <li>Executar as jobs por ordem de nome, sendo a primeira a aibm_1_dim_rdcdc, de seguida a aibm_2_dim ...</li>
   <li> O povoamento foi concluído com sucesso.</li>
</ol>

