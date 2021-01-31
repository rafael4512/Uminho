

#progress Bar function.
prog() {
    local w=80 p=$1;  shift
    # coloca na varivel dots o numero de pontos passado no parametro
    printf -v dots "%*s" "$(( $p*$w/100 ))" ""; dots=${dots// /.};
    # substitui esses ponts  num tamanho fixo de espaços criado e atualiza a percentagem . 
    printf "\r\e[K|%-*s| %3d %% %s" "$w" "$dots" "$p" "$*"; 
}