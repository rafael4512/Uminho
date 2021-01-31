#!/bin/bash

printf "Insira o numero da tarefa a executar(0 a 4)- \n"
printf "0 - Limpar tudo feito\n"
printf "1 - Criar projetos + preparar sonarqube + descobrir as metrics dos projetos + criar CSV com informacao\n"
printf "2 - Auto Refactor\n"
printf "3 - Criacao de testes automaticos (Requisito: tarefa 1)\n"
printf "4 - nem ideia\n"

re='^[0-9]+$'
read num_tarefa

if ! [[ $num_tarefa =~ $re ]] ; then #verifica se é um numero.
        echo "error:Escreva um número entre [0..4]" >&2; exit 1
elif (( num_tarefa >= 0 )) && (( num_tarefa <= 4 )); then
        echo "Ira ser executado a tarefa $num_tarefa !!"
else
    echo "Entre 0 e 4!"; exit 1
fi


tarefa0(){
	printf "Executar tarefa 0!!!\n\n"
	bash limpar.sh
	printf "Tudo limpo\n"
}


tarefa1(){
	printf "Executar tarefa 1!!!\n\n"
	bash criar_pastas.sh
	bash preparar_sonarqube.sh
	bash criar_csv.sh
}

tarefa2(){
	printf "Executar tarefa 2!!!\n\n"
	bash refractoring.sh
}

tarefa3(){
	printf "Executar tarefa 3!!!\n\n"
	bash evosuite.sh

}

tarefa4(){
	printf "Esta tarefa terá de ser executada num pc com Intel !!!\n\n"
}

if [ $num_tarefa == 0 ]; then
	tarefa0
elif [ $num_tarefa == 1 ]; then
	tarefa1
elif [ $num_tarefa == 2 ]; then
	tarefa2
elif [ $num_tarefa == 3 ]; then
	tarefa3
elif [ $num_tarefa == 4 ]; then
	tarefa4
fi



