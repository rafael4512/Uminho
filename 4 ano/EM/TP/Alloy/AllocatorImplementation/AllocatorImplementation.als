-- open util/sequniv
open util/integer
-----------------------------
-- Modelação do sistema
-----------------------------

/** Os clientes do sistema, onde podem realizar pedidos de recursos*/
sig Clients{
	var requests :set Resources,
	var holding:set Resources
}
/** O reponsavel por atribuir os recursos aos clientes.*/
one sig Allocator{
	var unsat: Clients -> set Resources,
	var alloc:  Clients -> set Resources,

}

/** Todos os recursos do sistema*/
sig Resources{}


/** Recursos disponiveis, ou seja, ainda não foram alocados a nenhum cliente.*/
var sig Available in Resources{}


/**Fila de pedidos feitos por clientes*/
one sig Queue{
	var sched: seq Clients
}

/** A rede é onde as mensagens ficam armazenadas .*/
one sig Network{
	var messages: set (Type->Clients -> Resources )
}
/** Tipo das mensagens*/
abstract sig Type{}
one sig request, allocate, return extends Type {}

-----------------------------
-- Especificação do sistema
-----------------------------
fact Init {
	no requests
	no holding
	no unsat
	no alloc
	no messages 
	no sched
	Resources in Available
}

/**Coloca os clientes na fila numa ordem aleatória, dado um set.*/
pred  permSeqs[s: set Clients ]{
	Queue.sched'.elems=s
}	

/**O cliente faz um pedido para o allocador.*/
pred Request(c:Clients,s:Resources){
	--pre condition:
	no (c.requests + c.holding)
	
	--efect 
	requests' = requests + (c->s) 
	Network.messages' = Network.messages + (request->c->s)
	
	--maintain
	Allocator.unsat'=Allocator.unsat
	alloc'=alloc
	Available'=Available
 	sched'=sched
	holding'=holding
}
/**O alocador recebe um pedido de um cliente.*/
pred RReq(t:Type ,c:Clients, s: Resources ){
	--pre condition
	t in request
	(c->s) in request.(Network.messages) 
	--efect 
	 no (Allocator.alloc &  (c->s) )
	--let cl = (request.m).Resources  | no cl <:  (return.(Network.messages))  -- caso tenha algum return por fazer ,tem de o devolver antes de poder volar a pedir

	Network.messages'=Network.messages -  (t->c->s)
	Allocator.unsat'=Allocator.unsat +(c->s) 

	--maintain
	alloc'=alloc
	Available'=Available
 	sched'=sched
	requests'=requests
	holding'=holding
	
}	

/**O alocator depois de receber um pedidos, envia uma msg  para o cliente a avisar que vai allocar.*/
pred Allocate(c:Clients,s:Resources){	
	one (c & (Allocator.unsat).s ) 
	s in (Available & c.(Allocator.unsat) )
	
	some i:Queue.sched.indsOf[c]{ 
		no (Queue.sched.subseq[0, sub[i,1]].elems.requests :> s) --nenhum anteriormente tem requests com os mesmos recursos
		and 
		no (s - c.(Allocator.unsat)) => Queue.sched' = Queue.sched.delete[i] --remove o da fila 
					 else sched' = sched
	}
	Network.messages'= Network.messages + (allocate->c->s)
	unsat'=unsat - (Allocator->c->s)	
	alloc'=alloc + (Allocator ->c->s )
	Available'=Available -  s

	--maintain
	requests'=requests
	holding'=holding


}	

/**O cliente  recebe o aviso do allocator de informar que os recursos foram alocados.*/
pred RAlloc(t:Type ,c:Clients, s: Resources ){

--pre conditions
	t in allocate
	(c->s) in t.(Network.messages)
	 
--efect 	
	Network.messages'= Network.messages - (t->c->s)
	requests' =requests - (c->s)
	holding'= holding + (c->s)
	
--maintain 
	unsat'=unsat
	alloc'=alloc
	sched'=sched
	Available'=Available 
}

/**O cliente manda uma msg a avisar que já não precisa dos recursos.*/
pred Return(c:Clients,s:Resources){
	some s
	s in c.holding

	holding'= holding - (c->s)
	Network.messages'= Network.messages + (return->c->s)

	unsat'=unsat
	alloc'=alloc
	sched'=sched
	requests'=requests	
	Available'=Available
}
/** O allocator recebe uma msg de um cliente a avisar que já nao precisa dos recursos.*/
pred RRet(t:Type ,c:Clients, s: Resources ){
	t in return 
	(c->s) in t.(Network.messages)

	Network.messages'= Network.messages - (t->c->s)
	alloc'=alloc - (Allocator -> c->s)
	Available'=Available + s 

	
	unsat'=unsat
	sched'=sched
	requests'=requests
	holding'=holding

}

/**Todos os clientes que têm pedidos mas não estão na fila.*/
fun toSchedule : set Clients{
	 (Allocator.unsat).Resources -  Queue.sched.elems
}
/**Coloca vários  pedidos do cliente na fila.*/
pred Schedule{

	some toSchedule
	permSeqs[toSchedule] -- coloca vários na queue de uma vez

	requests'=requests
	holding'=holding
	Network.messages'= Network.messages
	Available'=Available 
}
/**Coloca um  pedidos do cliente na fila.*/
pred Schedule2{
	some toSchedule
	some cl: toSchedule |  Queue.sched'=Queue.sched.add[cl] -- coloca um a um na queue

	requests'=requests
	holding'=holding

	Available'=Available 
	unsat'=unsat
	alloc'=alloc
	Network.messages'= Network.messages
}


pred Nop {
	requests'=requests
	holding'=holding
	Network.messages'= Network.messages
	alloc' = alloc
	unsat' =unsat
	Available'=Available
	sched'=sched
}

-------------------------- 
-- Simulação
-------------------------- 

/**Possiveis transições do sistema, equivalente ao next e state no TLA.*/
fact transitions{
	always (Nop or some s :Resources, a:Clients, t:Type| Request[a,s] or RReq[t,a,s]or Schedule2 or Allocate[a,s] or RAlloc[t,a,s] or Return[a,s] or RRet[t,a,s])
}

/**Se quiser  executar com mais de 8 clientes, 
aumentar o scope dos Ints (para ir até 15- basta colocar no "run"-for 5 Int) */
run{ } for exactly 1 Clients , exactly 2 Resources, 15 steps
--run{ } for exactly 2 Clients , exactly 3 Resources, 15 steps

fact liveness {

	all c :Clients | (eventually always (some c.holding) ) implies (always eventually (no c.requests and Return[c,c.holding]))
	some s :set Resources | all c:Clients |{
		(eventually always  some Resources) implies ( always eventually Allocate[c,s] )
	}
	some s :set Resources | some c:Clients |{
		(eventually always (some request.(Network.messages))) implies always eventually   RReq[request,c,s] 
		(eventually always (some allocate.(Network.messages))) implies always eventually RAlloc[allocate,c,s]
		(eventually always (some return.(Network.messages))) implies always eventually RRet[return,c,s] 
	}
	--condição extra para o garantir vai existir um momento onde não há clientes insatisfeitos .
	(eventually always some Clients.requests) implies (always eventually no Clients.requests)
}


-----------------------------
-- Propriedades a verificar
-----------------------------

fun RequestsInTransit[c:Clients] : set Resources {
	c.(request.(Network.messages))
}

fun AllocsInTransit[c:Clients] : set Resources {
	c.(allocate.(Network.messages))
}

fun ReturnsInTransit[c:Clients] : set Resources {
	c.(return.(Network.messages))
}

assert Invariant {
	all c : Clients {
		lone RequestsInTransit[c]
		c.requests = c.(Allocator.unsat) + RequestsInTransit[c] + AllocsInTransit[c]
		c.(Allocator.alloc) = c.holding + AllocsInTransit[c] + ReturnsInTransit[c]
	}
}

check Invariant

assert ResourceMutex {
--	always (all disj c1,c2 :Clients | no (c1.holding & c2.holding))
	always (all disj  c1,c2 : Clients | some (c1.holding & c2.holding) implies c1 = c2)
}

check ResourceMutex

assert ClientsWillReturn {
	always (all c : Clients | no c.requests implies eventually no c.holding)
}

check ClientsWillReturn

assert ClientsWillObtain {
	always (all c : Clients, r : Resources | r in c.requests implies eventually r in c.holding)
}

check ClientsWillObtain

assert InfOftenSatisfied {
	always eventually (no Clients.requests)
}

check InfOftenSatisfied

-----------------------------
-----------------------------









