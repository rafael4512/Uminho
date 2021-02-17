# AIBM-TP



<h2>Como povoar a Base de dados:</h2>
<ol>
   <li>No MySqlWorkbench, executar o modelo logico. </li>
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

