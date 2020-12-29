
sig Clients{
	var unsat: set Resources,
	var alloc: set Resources

}
sig Resources{}

// Resources are available iff they have not been allocated.
sig Available extends Resources{}

fact Resources_available{
	Available in (Resources -  ( Clients.alloc))
}
/* Initially, no resources have been requested or allocated. */
fact Init {
	no unsat
	no alloc
}

/* A client c may request a set of resources provided that all of its  
 previous requests have been satisfied and that it doesn't hold any   resources.  */
pred Request(c:Clients,s:Resources){
	--pre condition:
	no (c.unsat + c.alloc)
	--efect
	unsat' = unsat + (c->s) 
	alloc'=alloc
}

/*Allocation of a set of available resources to a client that         
 requested them (the entire request does not have to be filled).  */
pred Allocate(c:Clients,s:Resources){
	--pre condition
	some s	
	s in (Available & c.unsat)

	unsat'=unsat - (c->s)
	alloc' =alloc + (c->s)
}	

/* Client c returns a set of resources that it holds. It may do so 
 even before its full request has been honored.   */
pred Return(c:Clients,s:Resources){
	//pre condition
	s in c.alloc
	c.alloc'=c.alloc - s
	
	all a :Clients -c |a.alloc' =a.alloc
	unsat'=unsat
}



pred Nop {
	alloc' = alloc
	unsat' =unsat
}

--equivalente ao next e state no TLA.
fact transitions{
	always (Nop or some a:Clients ,s:Resources | Request[a,s] or Allocate[a,s] or Return[a,s] )
}



run{} for exactly 2 Clients , exactly 4 Resources


-------------------------- 
--Não existem clientes com recursos alocados em comum.
pred ResourceMutex{
	all disj c1,c2 :Clients | no (c1.alloc & c2.alloc)
}
--Garante que os clientes não fiquem com recursos alocados.
pred ClientsWillReturn{
	always (all c:Clients | no c.unsat  implies (eventually  no c.alloc))

}

--Todos os recursos pedidos irão ser atribuidos inevitavelmente.
pred ClientsWillObtain{
	always (all c:Clients ,r:Resources | r in  c.unsat implies (eventually r in  c.alloc ))
}

--Existe um padrão onde inevitavelmente, os clientes não tem pedidos não satizfeitos.
pred InfOftenSatisfied {
	always eventually  (no Clients.unsat )
}


-------------------------- 
pred Symmetry{
//Symmetry == Permutations(Clients) \cup Permutations(Resources)
}




fact fairness_SimpleAllocator{
	all c:Clients {
		 (eventually always (some c.alloc) )implies (always eventually  Return[c,c.alloc])
		 (always eventually  some Resources) implies some s:Resources | (always eventually Allocate[c,s])
	}
}

/* The following version states a weaker fairness requirement for the  
 clients: resources need be returned only if the entire request has  
 been satisfied.  */                                        
pred fairness_SimpleAllocator2{
	all c:Clients {
		 (eventually always (some c.alloc) )implies (always eventually (no c.unsat and Return[c,c.alloc]))
		 (always eventually  some Resources) implies some s:Resources | (always eventually Allocate[c,s])
	}
}


//check { no (Clients.unsat + Clients.alloc)  implies (no Clients.unsat and no Clients.alloc) }

























