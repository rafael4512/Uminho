# ATS - Análise e Teste de Software

Este projeto consiste na <b> análise/melhoria </b> de 99 projetos java, utilizando <b>shell script</b> de forma a automatizar o processo.

Dado isto, este pode ser  dividido em <b>4</b> fases distintas, sendo estas:
<ul>
 	<li> Análise da qualidade do código java, através do <i>sonarqube</i> (métricas de software);</li>
 	<li> </i>Refactor</i> do código, de forma a melhorar as falhas obtidas na fase anterior;</li>
 	<li> Realização de tetes unitários (<i>evosuite</i>), análise de cobertura dos mesmos e geração de ficheiros logs (<i>haskell</i>) para a realização de outros testes. </li>
 	<li> Foi feita uma análise energética do mesmo. Esta pode ser vista com mais detalhe no relatório.</li>
</ul>
<p>
:movie_camera: Pode ver uma demonstração do projeto <a href="https://youtu.be/Dfwf9XbKtC4">aqui</a>.<p>
:scroll: Para informação mais detalhada ver o <a href="https://github.com/rafael4512/Uminho/blob/main/4%20ano/ATS/Documenta%C3%A7%C3%A3o/Relat%C3%B3rio_grupo4.pdf">relatório</a>, caso queira uma overview do projeto veja a <a href="https://docs.google.com/presentation/d/1u_4lutkfyAkt25dak6jNt5voOtRI36tzPN6lHMd1ug4/edit?usp=sharing">apresentação</a>.

<h2>Guia de utilização </h2>

 <ol>
        <li>Na diretoria <b>/opt</b> é necessario ter a pasta do software sonarqube.</li>
        <li>Colocar os projetos que se quer testar na diretoria <b>projectsPOO_1920</b> em que esta se encontra na diretoria anterior a onde estão os scritps.</li>
        <li>Garantir que as permissões para correr qualquer script usando <b>./script.hs</b>.Para isso basta correr este comando: <b>chmod +x "script.sh"</b>. Caso não queira dar permissões pode simplesmente fazer <b>bash script.hs</b>.</li>
	<li>Para correr o trabalho, basta correr o script EXECUTABLE.sh </li>
 
</ol>
<h3>Notas</h3>
<ol>
	<li> <b>Projetos sem ficheiros</b> </li> 
	<ul>
	 	<li>38</li>
		<li>42</li>
		<li>62</li>
		<li>75</li>
		<li>80</li>
	</ul>
	
<li> <b>Projetos 23 e 83 para tarefa 3, mais debt e code smells</b></li>

</ol> 
