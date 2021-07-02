;Ex1.1)
;	M[1][1]=2
;	M[1][2]=3
;	M[1][3]=4
;	M[2][1]=3
;	M[2][2]=4
;	M[2][3]=5
;	M[3][1]=4
;	M[3][2]=5
;	M[3][3]=6



;Ex1.2)


(set-logic QF_AUFLIA)	

(declare-const m0 (Array Int (Array Int Int)) )
(declare-const m1 (Array Int (Array Int Int)) )
(declare-const m2 (Array Int (Array Int Int)) )
(declare-const m3 (Array Int (Array Int Int)) )


(assert (= m1
	(store m0 1 
		(store (store 
			(store (select m0 1) 1 2)  
			2  3) 
		3 4))))

(assert (= m2	
	(store m1 2 
		(store (store 
			(store (select m1 2) 1 3)  
			2  4) 
		3 5))))

(assert (= m3
	(store m2 3 
		(store (store 
			(store (select m2 2) 1 4)  
			2  5) 
		3 6))))


(echo "Validade do Modelo:")
(check-sat)
;(get-model)

;para verificar que inseriu corretamente.
;(get-value ((select (select m3 1) 1))) 



;EX1.3)


; a)Se i = j entao M[i][j] != 3.

(push)
(declare-const p1 Int)

;(assert (and (> p1 0) (< p1 4)))
(assert (= (select (select m3 p1) p1) 3))

(echo "Alinea a:")
(check-sat)
(echo "")
(pop)

;R: UNSAT Logo  a afirmação está correta 






;(b) Para quaisquer i e j entre 1 e 3, M[i][j] = M[j][i].

(push)
(declare-const p1 Int)
(declare-const p2 Int)

(assert (and (> p1 0) (< p1 4)))
(assert (and (> p2 0) (< p2 4)))
(assert (not (= (select (select m3 p1) p2) (select (select m3 p2) p1))))

(echo "Alinea b:")
(check-sat)
(echo "")
(pop)

;R: UNSAT Logo  a afirmação está correta 







;(c) Para quaisquer i e j entre 1 e 3,se i<j entao M[i][j]<6.
(push)
(declare-const p1 Int)
(declare-const p2 Int)

(assert (and (> p1 0) (< p1 4)))
(assert (and (> p2 0) (< p2 4)))
(assert (not (=> (< p1 p2 ) (< (select (select m3 p1) p2) 6)   ) ))
(echo "Alinea c:")

(check-sat)
(echo "")
(pop)






;(d) Para quaisquer i, a e b entre 1 e 3, se a > b entao M[i][a] > M[i][b].

(push)
(declare-const i Int)
(declare-const p1 Int)  
(declare-const p2 Int)  
(assert (and (> i 0) (< i 4)))
(assert (and (> p1 0) (< p1 4)))
(assert (and (> p2 0) (< p2 4)))
(assert (not (=> (> p1 p2 ) (> (select (select m3 i) p1) (select (select m3 i) p2) )   ) ))

(echo "Alinea d:")
(check-sat)
(echo "")
(pop)






;(e) Para quaisquer i e j entre 1 e 3, M[i][j] + M[i+1][j+1] = M[i+1][j] + M[i][j+1].

(push)
(declare-const p1 Int)
(declare-const p2 Int)

(assert (and (> p1 0) (< p1 4)))
(assert (and (> p2 0) (< p2 4)))
(assert (not (= 
	(+ (select (select m3 p1) p2) 
		(select (select m3 (+ p1 1)) (+ p2 1)) )
	(+ (select (select m3 (+ p1 1)) p2) 
		(select (select m3 p1) (+ p2 1)) ) 
	)))

(echo "Alinea e:")
(check-sat)
(echo "")
(pop)
;(echo "Contra-exemplo")
;(push)
;(declare-const v1 Int); i=3 j=3
;(declare-const v2 Int) 
;(assert (= (+ (select (select m3 3) 3)  (select (select m3 4) 4) ) v1)) 
;(assert (= (+ (select (select m3 4) 3)  (select (select m3 3) 4) ) v2)) 
;(check-sat)
;(get-value (v1))
;(get-value (v2))
;(get-value (+ (select (select m3 3) 3)  (select (select m3 4) 4) )) 
;(get-value ((select (select m3 4) 4))) 
(exit)













