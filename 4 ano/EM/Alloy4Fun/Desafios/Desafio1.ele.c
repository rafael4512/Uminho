sig Sala {
  curso : set Curso,
  edificio : set Edificio
}

sig Edificio {}
sig Curso {}

pred spec {
    //Todas as salas tem apenas um edificio
  all s:Sala | one s.edificio

    //Todos os curso tem mais de uma sala
    all c:Curso | some curso.c

    
    //todos os curso só estao em um edifcio
    all c:Curso   | one (curso.c).edificio
  
  
    //Não há dois cursos na mesma sala  
    all c1,c2:Curso |  c1->c2 not in iden implies  no (curso.c1 & curso.c2) 
  /*all s:Sala |lone s.curso*/
  /*  all disj c1,c2:Curso |  no (curso.c1 & curso.c2)  */


}

/*

// Verifica se 2  propriedades são iguasi !
pred aux{

}

pred aux2{

}

check {aux iff aux2 } for 7
*/
