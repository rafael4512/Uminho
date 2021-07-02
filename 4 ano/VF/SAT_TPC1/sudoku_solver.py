import sys
import subprocess
#global vars
N=2                   # Tamanho do sudoku
K=N*N                 # Valores possiveis para cada celula
var = dict()          # Map responsável por armazenar as variaveis.
var2 = dict()         # Map anterior reverso, ou seja, a key é um inteiro e a chave é uma variavel.
conditions = dict()   # Todas as condições do problema.
n_conds=0             # Numero total de condições geradas.
filename="sudoku.cnf" # Ficheiro Para o SAT SOLVER

#Inicializa 2 dicionarios com as variaveis para o Satsolver.
def init_vars():
    k=1
    for i in range (0,K):
        for j in range (0,K):
               for v in range (1,K+1):
                   s="x"+ str(i)+","+str(j)+"$"+str(v)
                   var[s]= k
                   var2[k]= s
                   #print(str(k)+"->"+str(s))
                   k +=1



#Gera as clausulas de existir pelo menos 1 valor em cada celula
#x001 V x002 V x003 Vx004
def clause1(f):
  global n_conds
  counter=0
  c=""
  for i in range (0,K):
    for j in range (0,K):
      for k in range(1,K+1):
        s1='x'+ str(i)+","+str(j)+"$"+str(k)
        #print(s1 + " V ", end='')
        #print(str(var[s1])+ " ", end='')
        c += str(var[s1])+" " 
      c +="0\n"
      f.write(c)
      counter+=1
      c=""
      #print("0\n", end='')
  n_conds+=counter
  print("Clause1 - 100%---"+str(counter))



#Gera as clausulas de existir no maximo 1 valor em cada celula
#x001 -> -x002 ^ -x003 ^ -x004 => são 96
#equiv  (-x001 V  -x002)  ^ (-x001  V -x003) ^ (-x001 V -x004 )
def clause2(f):
  global n_conds
  counter=0
  for i in range (0,K):
    for j in range (0,K):
      for k in range(1,K+1):
        s1='x'+ str(i)+","+str(j)+"$"+str(k)
        #print("XX\n", end='')
        for p in range(1,K+1):
          if(p!=k):
            s2='x'+ str(i)+","+str(j)+"$"+str(p)
            #print ("-"+s1 +" V -" +s2)
            c=('-'+ str(var[s1])+" -"+str(var[s2])+" 0\n")
            if(revCond(c) not in  conditions):
              conditions[c]=1
              f.write(c)
              counter+=1 
        #print("\n", end='')
        #print(s1 + " V ", end='')
        #print(var[s1], end='')
        #print(" " , end='')
  n_conds+=counter
  print("Clause2 - 100%---"+str(counter))



#Clausulas para linhas diferentes.
# N=2 => 96 clauses  
def clause3(f):
  global n_conds
  counter=0
  for k in range(1,K+1):
    for i in range (0,K):
      for j in range (0,K):
        s1='x'+ str(i)+","+str(j)+"$"+str(k)
        for p in range (j+1,K):
          s2='x'+ str(i)+","+str(p)+"$"+str(k)
          #print ("-"+s1 +" V -" +s2)
          c=('-'+ str(var[s1])+" -"+str(var[s2])+" 0\n")
          if(revCond(c) not in  conditions and  c not in  conditions ):
            conditions[c]=1
            f.write(c)
            counter+=1
  n_conds+=counter
  print("Clause3 - 100%---"+str(counter))



#Clausula para colunas diferentes.
# N=2 => 96 clauses  
def clause4(f):
  global n_conds
  counter=0
  for k in range(1,K+1):
    for j in range (0,K):
      for i in range (0,K):
        s1='x'+ str(i)+","+str(j)+"$"+str(k)
        for p in range (i+1,K):
          s2='x'+ str(p)+","+str(j)+"$"+str(k)
          #print ("-"+s1 +" V -" +s2)
          c=('-'+ str(var[s1])+" -"+str(var[s2])+" 0\n")
          if(revCond(c) not in  conditions and  c not in  conditions ):
            conditions[c]=1
            f.write(c)
            counter+=1
  n_conds+=counter
  print("Clause4 - 100%---"+str(counter))
 


#Clausula para subMatrizes diferentes
#Basta verificar as diagnoais pq a clausula das linhas e colunas já apanham o resto.
# N=2 => 48 clauses
def clause5(f):
  global n_conds
  counter=0  
  for k in range(1,K+1):
    for i in range (0,K):
      for j in range (0,K):
        for p in range ((i//N)*N,N+(i//N)*N):
          for l in range ((j//N)*N, N+(j//N)*N):
            if not (i==p and j==l ):
              s1='x'+ str(i)+","+str(j)+"$"+str(k)
              s2='x'+ str(p)+","+str(l)+"$"+str(k)
              c=('-'+ str(var[s1])+" -"+str(var[s2])+" 0\n")
              if(revCond(c) not in  conditions and  c not in  conditions ):
                conditions[c]=1
                f.write(c)
                counter+=1
  n_conds+=counter
  print("Clause5 - 100%---"+str(counter))



#Clausula gerar a matriz do enunciado.
def geraSudokuMatrix(f):
  global n_conds
  s1='x'+ str(0)+","+str(0)+"$"+str(4)
  s2='x'+ str(0)+","+str(2)+"$"+str(1)
  s3='x'+ str(1)+","+str(1)+"$"+str(2)
  s4='x'+ str(2)+","+str(2)+"$"+str(3)
  s5='x'+ str(3)+","+str(1)+"$"+str(4)
  s6='x'+ str(3)+","+str(3)+"$"+str(1)
  c= str(var[s1])+" 0\n"+str(var[s2])+" 0\n"+str(var[s3])+" 0\n"+ str(var[s4])+" 0\n"+str(var[s5])+" 0\n"+str(var[s6])+" 0\n"
  f.write(c)
  n_conds+=6
  print("Matriz inicial do Enunciado gerada!")

# --------- Funções Auxiliares ---------------
#reverse a condition with 2 vars.
def revCond(s1):
  #l = s1.split(' V ') 
  #res= l[1]+" V "+l[0]
  l = s1.split(' ') 
  res= str(l[1]+" "+l[0]+" "+ l[2])
  return res


#Corre o satsolver e dá print da solução no ecra!
#Nota: É possivel ver as variaveis que estáo a true 
#      se remover a linha 176 de comentário e colocar a 175 a comentario.
def run_SAT():
  out=subprocess.run(["cryptominisat5", "--verb", "0" ,filename],capture_output=True)
  print(".........Solução do exemplo.........")
  l=out.stdout.decode('ascii').split(' ')
  is_integer = lambda s: s.isdigit() or (s[0] == '-' and s[1:].isdigit()) # função para retornar todos os valores inteiros
  integers = filter(is_integer,l)
  ints = filter(lambda s: int(s) > 0 ,integers) # todos os maiores que 0
  printModeMatrix(ints)
  #printModeVars(ints) 
  #print(len(list(ints)))
  print(".....................................")


#print das variaveis finais
def printModeVars(ints):
  for i in ints: 
    print("\t\t"+var2[int(i)])


#print da matriz final do sudoku.
def printModeMatrix(ints):
  c=0
  for i in ints:
    c+=1 
    print(var2[int(i)].split("$")[1] + " ", end='')
    if c==K:
      c=0
      print("\n", end='')


#Prints de menus para input do utilizador
def menu1(): 
  while (True):
    print("----Solver Sudoku-----")
    print("1 - Executar o exemplo do enunciado N=2")
    print("2 - Caso geral")
    print("0 - Sair")
    e=input("Escolha uma das opções:")
    if (e.isdigit() and int(e)<3 and int(e)>=0):
        return int(e);      


def menu2(): 
  while (True):
    print("NOTA:\tInsira um N menor que 7!\n\tPois para N=6 já tem 3 milhoes de clausulas.\n\tDevido a ter um crescimento exponencial poderá demorar muito tempo.")
    e=input("\nInsira um valor para N: ")  
    if (e.isdigit() and int(e)>=0):
      return int(e);
    print("Têm de inserir um número positivo!")

 
# ------------------------------------------

#função que executa gera todas as clausulas e escreve no ficheiro respectivo.
def solver():
  # Open the file for Sat solver
  f = open(filename, "w+")
  s="p cnf "+str(K*K*K)
  pos_f=len(s) + 1   # posição para inserir o numero das clausulas no final da execução.
  s+="                    \n" # Tamanho do numero de clausulas, pois só no fim se sabe o numero de clausulas. 
  f.write(s)
  init_vars()
  clause1(f)
  clause2(f)
  clause3(f)
  clause4(f)
  clause5(f)
  if (N==2):
    geraSudokuMatrix(f)# Exemplo do enunciado
  print("Total: "+str(n_conds)+ " clauses")
  #escrever as clausulas no ficheiro.
  f.seek(pos_f,0)
  f.write(str(n_conds))
  f.close()
  #SE TIVER O "cryptominisat5" INSTALADO,Tire esta linha de comentário.
  #run_SAT()

def main():
  e=menu1()
  if (e==0):
    sys.exit()
  elif (e==1):
    print("\n", end='')
    solver()
  else:
    global N,K
    N=menu2()
    K=N*N
    print("\n", end='')
    solver()

if __name__ == "__main__":
    main()