#!/bin/bash
source ./progress_bar.sh 
#resize da janela do terminal.
printf '\e[8;40;130t'
 
DIR=$(pwd)
 
FILES=$(grep -rl 'System.out.print.*' ${DIR}/../Proj_sonar/ |  awk '/.*\.java/{ print $0 }')
#x=$(grep -R -l 'System.out.print.*' /Users/ril/Desktop/ATS/project/Proj_sonar) ;while read line; do echo "LINE: '${line}'"; done < <(echo "$x")
re1='Proj_sonar/68/'
re2='Proj_sonar/0/'
re3='Proj_sonar/16/'
re4='Proj_sonar/4/'
re5='Proj_sonar/22/'
re6='Proj_sonar/85/'
re7='Proj_sonar/47/'
re0='\n'
 
contador=1;
resolve_prints_mac()
{
	echo "$FILES" | while read line ; 
	do
		if [[ $line != *${re1}* ]] &&  [[ $line != *${re2}* ]] &&[[ $line != *${re3}* ]] &&[[ $line != *${re4}* ]]&&[[ $line != *${re5}* ]] &&[[ $line != *${re6}* ]] &&[[ $line != *${re7}* ]]; then
			prog "$(((( ${contador} * 100)) / 383))" 
			n=$(grep -n  "public class.*" ${line} | sed 's/:.*//');
			n2=$(( ${n} + 2 )) 
			find ${line} -exec sed -i '' -e "${n2}s/^/private transient Logger logger = Logger.getLogger(this.getClass().getName()); /" {} \;			
			LC_CTYPE=C sed -i "" 's/System.out.println(/logger.log(Level.WARNING,/g' ${line} 
			LC_CTYPE=C sed -i "" 's/System.out.print(/logger.log(Level.WARNING,/g' ${line}
			((contador++))
		fi
	done
	echo "\nFIM\n";
}
 
 
resolve_prints_linux()
{
	echo "$FILES" | while read line ; 
	do
		if [[ $line != *${re1}* ]] &&  [[ $line != *${re2}* ]] &&[[ $line != *${re3}* ]] &&[[ $line != *${re4}* ]]&&[[ $line != *${re5}* ]] &&[[ $line != *${re6}* ]] &&[[ $line != *${re7}* ]]; then
			prog "$(((( ${contador} * 100)) / 383))" 
			n=$(grep -n  "public class.*" ${line} | sed 's/:.*//');
			n2=$(( ${n} + 2 )) 
			find ${line} -exec sed -i  -e "${n2}s/^/Logger logger = Logger.getLogger(this.getClass().getName()); /" {} \;			
			LC_CTYPE=C sed -i  's/System.out.println(/logger.log(Level.WARNING,/g' ${line} 
			LC_CTYPE=C sed -i  's/System.out.print(/logger.log(Level.WARNING,/g' ${line}
			((contador++))
		fi
	done
	echo "\nFIM\n";
}
 
if [ "$(uname)" == "Darwin" ]; then
resolve_prints_mac
elif [ "$(uname)" == "Linux" ]; then
resolve_prints_linux
fi
