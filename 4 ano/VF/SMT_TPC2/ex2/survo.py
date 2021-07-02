from z3 import *
#Survo game 

linha={} #guarda o valor inteiro que a soma de uma dada linha tem de dar.
coluna={}#guarda o valor inteiro que a soma de uma dada coluna tem de dar.


in_filename='sample.txt'       #ficheiro in
out_filename='solution.txt'    #ficheiro out
n_lines=0
n_columns=0
firstLine="" #primeira linha do ficheiro.
lastLine="" #primeira linha do ficheiro.

x = {}# all vars of z3

#lê o ficheiro exemplo.
def read_file(filename):
    global n_columns,n_lines,linha,coluna,firstLine,lastLine
    f = open(filename, "r")
    i=0 #linha
    j=0 #coluna
    for line in f:
        l=line.split(' ')
        if (j==0):
            firstLine=line
        for c in l:

            if( j!= 0):#ignoro a 1 linha
                if (c.isdigit() and int(c) ==j):
                    linha[j-1]=l[-1]
                    n_lines+=1
                elif (c.isdigit()):
                    coluna[i] =int(c)
                    lastLine=line
                    i+=1
            else :
                if (c.isalpha()):
                    n_columns+=1
                    
        j+=1
    print("Puzzle:("+str(n_lines)+","+str(n_columns)+")")


def scopeIntsVars(solver):
    m=n_columns*n_lines
    for i in range(n_lines):
        x[i] = {}
        for j in range(n_columns):
            x[i][j] = Int('x'+ str(i)+str(j))
            solver.add(And(1<=x[i][j], x[i][j]<= m))

def allDistinct(solver):
        solver.add(Distinct([x[i][j] for i in range(n_lines) for j in range(n_columns)] ))


def line_Restriction(solver):
    #global linha
    #print(linha[0])
    for i in range(n_lines):
        solver.add(Sum([ x[i][j] for j in range(n_columns)]) == linha[i]) 

def column_Restriction(solver):
    for j in range(n_columns):
        solver.add(sum([x[i][j] for i in range(n_lines)]) == coluna[j])



    

def z3_solving():
    s=Solver()
    scopeIntsVars(s)
    allDistinct(s)
    line_Restriction(s)
    column_Restriction(s)
    #printModel(s)
    return s
    #print  (s.check())
    
def printModel(s):
    if s.check()==sat:
        m2=s.model()
        #print(m2)
        for i in range (n_lines):
            print([m2[x[i][j]].as_long() for j in range(n_columns)],sum([m2[x[i][j]].as_long() for j in range(n_columns)]))
        for j in range (n_columns):
            print(sum([m2[x[i][j]].as_long() for i in range(n_lines)]),end=' ')
    else:
        print(s.check())


def writeSolutionFile(filename,solver):
    f = open(filename, "w")
    f.write(firstLine)
    ii=0
    if solver.check()==sat:
        m=solver.model()
        for i in range (n_lines):
            f.write(str(ii+1)+ " ")
            for j in range(n_columns):
                f.write(str(m[x[i][j]].as_long())+" ")
            f.write(linha[i])
            ii+=1           
    f.write(lastLine)
    f.close()

def main():
    read_file(in_filename)
    solver=z3_solving()
    writeSolutionFile(out_filename,solver)
    print("Solução no ficheiro: "+out_filename )

if __name__ == "__main__":
    main()






