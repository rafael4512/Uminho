-----------------------------
-- Modelação do sistema
-----------------------------

/** Os clientes do sistema, onde estes guardam informação sobre os seus pedidos e
	recursos armazenados. */
sig Clients{
	var unsat: set Resources,
	var alloc: set Resources

}
/** Todos os Recursos do sistema */
sig Resources{}

/** Recursos disponiveis, ou seja, ainda não foram alocados a nenhum cliente.*/
var sig Available in Resources{}

-----------------------------
-- Especificação do sistema
-----------------------------

/**Inicialmente, não existem recursos pedidos e alocados. */
fact Init {
	no unsat
	no alloc
	Resources in Available
}

/**Um cliente c pode pedir um conjunto de recursos, desde que todos os seus
    pedidos anteriores tenham sido  satisfeitos, e tambem não possui recursos alocados. */
pred Request(c:Clients,s:set Resources){
	--pre condition:
	no (c.unsat + c.alloc)
	--efect
	unsat' = unsat + (c->s) 
	alloc'=alloc
	Available'=Available
}
  
/** Alocação de um conjunto de recursos disponíveis a um cliente.
   (O pedido não precisa ser interamente satisfeito).*/
pred Allocate(c:Clients,s:Resources){
	--pre condition
	some s	
	s in (Available & c.unsat)

	unsat'=unsat - (c->s)
	alloc' =alloc + (c->s)
	Available'=Available -s
}	

/** O cliente c retorna um conjunto de recursos que contém. Pode faze-lo
    antes do pedido ser completamente atendido.*/

pred Return(c:Clients,s:Resources){
	//pre condition
	s in c.alloc
	c.alloc'=c.alloc - s
	
	all a :Clients -c |a.alloc' =a.alloc
	unsat'=unsat
	Available'=Available +s
}


/**Mantem o estado atual do sistema*/
pred Nop {
	alloc' = alloc
	unsat' =unsat
	Available'=Available
}
-----------------------------
-- Simulação
-----------------------------

/**Possiveis transições do sistema, equivalente ao next e state no TLA.*/
fact transitions{
	some s :set Resources{
		always (Nop or some a:Clients |  Request[a,s] or Allocate[a,s] or Return[a,s] )
	}
}


run{} for exactly 2 Clients , exactly 3 Resources
--run{} for exactly 5 Clients , exactly 3 Resources ,15 steps


-----------------------------
-- Propriedades a verificar
-----------------------------

/**Não existem clientes com recursos alocados em comum.*/
assert ResourceMutex{
	all disj c1,c2 :Clients | no (c1.alloc & c2.alloc)
}
check ResourceMutex

/**Garante que os clientes não fiquem com recursos alocados.*/
assert ClientsWillReturn{
	always (all c:Clients | no c.unsat  implies (eventually  no c.alloc))

}
check ClientsWillReturn

/**Todos os recursos pedidos irão ser atribuidos inevitavelmente.*/
assert ClientsWillObtain{
	always (all c:Clients ,r:Resources | r in  c.unsat implies (eventually r in  c.alloc ))
}

check ClientsWillObtain

/**Existe um padrão onde inevitavelmente, os clientes não tem pedidos não satizfeitos.*/
assert InfOftenSatisfied {
	always eventually  (no Clients.unsat )
}

check InfOftenSatisfied

-----------------------------
--  Propriedades de Fairness
-----------------------------
fact fairness_SimpleAllocator{
	all c:Clients {
		(eventually always (some c.alloc) )implies (always eventually  Return[c,c.alloc])

		(always eventually  some  Resources) implies ( always eventually some c.alloc )

	}
	(eventually always  some Clients.unsat) implies (always eventually no Clients.unsat)
	
}

/** Esta proprieedade especifica uma fairness mais fraca que a anterior, pois, para os clientes-
      só podem retornar quando os pedidos sejam inteiramente satisfeitos. */                                        
pred fairness_SimpleAllocator2{
	all c:Clients {
		 (eventually always (some c.alloc) )implies (always eventually (no c.unsat and Return[c,c.alloc]))
		
		 (always eventually  some  Resources) implies ( always eventually some c.alloc )
	}
	(eventually always  some Clients.unsat) implies (always eventually no Clients.unsat)
}


//check { no (Clients.unsat + Clients.alloc)  implies (no Clients.unsat and no Clients.alloc) }

























