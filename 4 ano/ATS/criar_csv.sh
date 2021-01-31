#!/bin/bash
source ./progress_bar.sh 
#resize da janela do terminal.
printf '\e[8;40;130t'
#remove o ficheiro antigo, se existir! (option -i ->faz a pergunta se quer remover) 
rm -f statistics.csv
printf "\t\t\t\t\t A obter metricas \n"
echo "name,bugs,security_hotspots,code_smells,complexity,duplicated_lines,debt" >> statistics.csv 
for (( counter=0; counter<99 ; counter++ ))
do
	if [ $counter != 38 ] && [ $counter != 62 ] && [ $counter != 75 ] && [ $counter != 80 ]; then
		prog "$((counter + 2))" 
		metricas=$(curl -sX GET -u admin:admin  'http://localhost:9000/api/measures/component?metricKeys=complexity,duplicated_lines_density,sqale_index,code_smells,security_hotspots,bugs&component=p'$counter'')
		bugs=$(echo "$metricas" | sed  's/^.*"bugs","value":"\([^"]*\)".*$/\1/' )
		security_hotspots=$(echo "$metricas" | sed  's/^.*"security_hotspots","value":"\([^"]*\)".*$/\1/' )
		code_smells=$(echo "$metricas" | sed  's/^.*"code_smells","value":"\([^"]*\)".*$/\1/' )
		complexity=$(echo "$metricas" | sed  's/^.*"complexity","value":"\([^"]*\)".*$/\1/' )
		duplicated_lines_density=$(echo "$metricas" | sed  's/^.*"duplicated_lines_density","value":"\([^"]*\)".*$/\1/' )
		debt=$(echo "$metricas" | sed  's/^.*"sqale_index","value":"\([^"]*\)".*$/\1/')
		echo "p$counter,$bugs,$security_hotspots,$code_smells,$complexity,$duplicated_lines_density,$debt" >> statistics.csv 		
	fi
done
printf "\n \t\t\t\t\t CSV criado. \n"

