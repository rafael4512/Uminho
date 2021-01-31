#!/bin/bash
#mudar  as preferencias do terminal;
#Preferences->Perfil->Shell-> When the shell exits -> change to "Close the window" if it was closed properly

#------------------------
source ./progress_bar.sh 
#resize da janela do terminal.
printf '\e[8;40;130t'

#--------------
re='^[0-9]+$'
printf "Insira o numero de projetos a executa(1 a 99)- "
read num_proj 

if ! [[ $num_proj =~ $re ]] ; then #verifica se é um numero.
   	echo "error:Escreva um número entre [1..99]" >&2; exit 1
elif (( num_proj > 0 )) && (( num_proj <= 99 )); then 
	echo "Ira ser executado $num_proj projeto(s)!"
else
    echo "Entre 1 e 99!"; exit 1
fi
#---------
#Executar o SONARQUBE
if [ "$(uname)" == "Darwin" ]; then
	osascript -e 'tell application "Terminal" to do script "/opt/sonarqube-8.5.0.37579/bin/macosx-universal-64/sonar.sh console;exit"'
	printf "A executar o server do sonarqube.\n"
elif [ "$(uname)" == "Linux" ]; then
    gnome-terminal -- bash -c "cd; ./opt/sonarqube-8.5.0.37579/bin/linux-x86-64/sonar.sh console; exit; exec bash"
	printf "A executar o server do sonarqube.\n"
fi
#espera que o sonarqube execute.
sleep 70

#elimina o token criado na ultima execução .
curl -X POST  -u admin:admin 'http://localhost:9000/api/user_tokens/revoke?name=ATS'
#token para adicionar projetos ao sonar
token=$(curl -X POST  -u admin:admin 'http://localhost:9000/api/user_tokens/generate?name=ATS' | sed  's/^.*"token":"\([^"]*\)".*$/\1/' ) 

printf "Inicio da execução de projetos no sonarqube\n"

#contador para controlo de projetos em execução
ativo=0 
if [ "$(uname)" == "Darwin" ]; then
	for (( counter=0; counter<num_proj ; counter++ ))
	do
		if [ $counter != 38 ] && [ $counter != 62 ] && [ $counter != 75 ] && [ $counter != 80 ]; then
			DIR=$(pwd)
			osascript -e 'tell application "Terminal" to do script  "cd '${DIR}/../Proj_sonar/${counter}'; mvn compile; mvn package;mvn sonar:sonar -Dsonar.projectName=p'${counter}' -Dsonar.projectKey=p'${counter}' -Dsonar.host.url=http://localhost:9000 -Dsonar.login='${token}';exit"'
			((ativo++))
			printf "$counter \n"
		fi
		if [ $ativo -gt 2 ]; then
			sleep 70
			ativo=0
		fi
	done
	printf "Foram colocados $num_proj projetos no sonarqube!\n"
elif [ "$(uname)" == "Linux" ]; then
	#23 segundos +/- que demora a correr 1 mvn compile+mvn package
	for (( counter=0; counter<num_proj; counter++ ))
	do
		if [ $counter != 38 ] && [ $counter != 62 ] && [ $counter != 75 ] && [ $counter != 80 ]; then
			#Este é o oficial
			gnome-terminal -- bash -c "cd ../Proj_sonar/$counter; mvn compile; mvn package; mvn sonar:sonar -Dsonar.projectName=p'${counter}' -Dsonar.projectKey=p'${counter}' -Dsonar.host.url=http://localhost:9000 -Dsonar.login='${token}'; exit; exec bash"
			((ativo++))
			printf "$counter \n"
		fi
		if [ $ativo -gt 2 ]; then
			sleep 75
			ativo=0
		fi
	done
	printf "Foram colocados $num_proj projetos no sonarqube!\n"
fi



sleep 80




