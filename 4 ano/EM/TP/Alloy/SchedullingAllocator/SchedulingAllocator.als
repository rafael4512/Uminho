-- open util/sequniv
open util/integer

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


/**Fila de pedidos feitos por clientes*/
one sig Queue{
	var sched: seq Clients
}



-----------------------------
-- Especificação do sistema
-----------------------------


/**Inicialmente, não existem recursos pedidos e alocados. */
fact Init {
	no unsat
	no alloc
	Resources in Available
	no sched
}

/**Coloca os clientes na fila numa ordem aleatória.*/
pred  permSeqs[s: set Clients ]{
	Queue.sched'.elems=s
}	



/** Um cliente  pode pedir um conjunto de recursos, desde que todos os seus
pedidos anteriores tenham sido  satisfeitos e não possui recursos alocados. */
pred Request(c:Clients,s:Resources){
	--pre condition:
	no (c.unsat + c.alloc)
	--efect 
	unsat' = unsat + (c->s) 
	alloc'=alloc
	Available'=Available
 	sched'=sched
}

/** Alocação de um conjunto de recursos disponíveis a um cliente.
   (O pedido não precisa ser interamente satisfeito).*/
pred Allocate(c:Clients,s:Resources){
	some s 
	s in (Available & c.unsat)
	some i:Queue.sched.indsOf[c]{ 
		no (Queue.sched.subseq[0, sub[i,1]].elems.unsat & s)
		and 
		no (s - c.unsat) => Queue.sched' = Queue.sched.delete[i]
					 else sched' = sched
	}

	alloc'=alloc + (c->s)
	Available'=Available -s
	unsat'=unsat - (c->s)
}	

/** O cliente c retorna um conjunto de recursos que contém. Pode faze-lo
    antes do pedido ser completamente atendido.*/
pred Return(c:Clients,s:Resources){
	//pre condition
	s in c.alloc
	c.alloc'=c.alloc - s
	
	all a :Clients -c |a.alloc' =a.alloc
	unsat'=unsat
	sched'=sched
	Available'=Available +s
}


/**Todos os clientes que têm pedidos mas não estão na fila.*/
fun toSchedule : set Clients{
	 unsat.Resources -  Queue.sched.elems
}

/**Todos os alocados (para o tema)*/
fun allAlloc :set Clients {
	alloc.Resources
}

/**Coloca varios na fila numa transição do sistema*/
pred Schedule{
	//pre condition
	some toSchedule
	//efect
	permSeqs[toSchedule] -- coloca vários na queue de uma vez
	//maintain
	unsat'=unsat
	alloc' =alloc
	Available'=Available 
}

/**Numa transição do sistema apenas coloca um de cada vez na fila. */
pred Schedule2{
	some toSchedule
	some cl: toSchedule |  Queue.sched'=Queue.sched.add[cl] -- coloca um a um na queue
	unsat'=unsat
	alloc' =alloc
	Available'=Available 
}

-------------------------- 
pred Nop {
	alloc' = alloc
	unsat' =unsat
	Available'=Available
	sched'=sched
}

-------------------------- 
-- Simulação
-------------------------- 

--equivalente ao next e state no TLA.
fact transitions{
	some s :set Resources{
		always (Nop or some a:Clients |  Request[a,s] or Allocate[a,s] or Return[a,s] or Schedule )
	}
}


fact  Liveness {
	all c:Clients | {
		(eventually always (some c.alloc) ) implies (always eventually (no c.unsat and Return[c,c.alloc]))
		( eventually always  some Resources) implies ( always eventually some c.alloc )
	}
 	-- se apartir de um momento for possivel dar shedule tem de o fazer.
	(eventually always (some toSchedule )) implies (always eventually  Schedule)
	--condição extra para o garantir vai existir um momento onde não há clientes insatisfeitos .
	(eventually always  some Clients.unsat) implies (always eventually no Clients.unsat)
}

/**Se quiser  executar com mais de 8 clientes, 
aumentar o scope dos Ints (para ir até 15- basta colocar no "run"-for 5 Int) */
--run{} for exactly 2 Clients , exactly 3 Resources
run{} for exactly 3 Clients , exactly 3 Resources ,15 steps

-----------------------------
-- Propriedades a verificar
-----------------------------

/**Não existem clientes com recursos alocados em comum.*/
assert ResourceMutex{
	always (all disj c1,c2 :Clients | no (c1.alloc & c2.alloc))
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

/**Existe um padrão onde inevitavelmente, os clientes não tem pedidos não satisfeitos.*/
assert InfOftenSatisfied {
	always eventually  (no Clients.unsat )
}

check InfOftenSatisfied


/** Retorna todos os clientes que não estão na fila de pedidos.*/
fun UnscheduledClients : set Clients{
	Clients - Queue.sched.elems 
}


/**Todos os Recusos disponiveis quando o Cliente  i (posicao na fila) retornar. */
fun PrevResources[i:Int] : set Resources{
	--todos os clientes que fizeram pedidos antes do cliente i.
	let c= Queue.sched.subseq[0, sub[i,1] ].elems |
	Available + c.unsat + c.alloc+UnscheduledClients.alloc
	
}
/** 	Propriedasdes do alocador , por ordem:
       1)Todos os clientes na fila tem pedidos pendentes
	2)Todos os clientes prontos para ir para a fila tem pedidos.
	3)Todos os clientes  da fila nunca possuem um recurso allocado, 
	   se este ainda pedido por um processo anterior(este tb na fila) 
	4)Todos os clientes da fila podem pedir recursos de clientes anteriores, 
	   se estes os tiverem libertados .*/

assert AllocatorInvariant{
	all c: Queue.sched.elems | some c.unsat
 	all c1:toSchedule | some c1.unsat
 	all i:Queue.sched.inds | all j:(Queue.sched.inds - sub[#Queue.sched,1] ) | no ( Queue.sched[i].alloc & Queue.sched[j].unsat )
--	all i:Queue.sched.inds | all j:Int | j>=0 and j<i  implies no ( Queue.sched[i].alloc & Queue.sched[j].unsat )
	all c:Queue.sched.elems |  let  i = Queue.sched.indsOf[c] | c.unsat in   PrevResources[i]
}

   
check AllocatorInvariant

-------------------------- 






