(* Ficha 3 *)
Require Import ZArith.
Require Import List.
Require Extraction. 

Set Implicit Arguments.
Extraction Language Haskell.



(********************)
(* Exercício 1.a) *)

(* funcao indutiva para criar um lista com todos os  n elementos passados como parametro*)
Inductive mk_List (A:Type) : nat -> A -> (list A) -> Prop :=
  | mk_nil : forall (a:A), mk_List 0 a nil
  | mk_sec : forall (n:nat) (a:A) (l:list A), mk_List n a l -> mk_List (S n) a (a::l).


Theorem mk_List_prova: forall (A:Type) (n:nat) (x:A), { l:list A | mk_List n x l }.
Proof.
  intros.
  induction n.
  - exists nil. 
  constructor.
  - destruct IHn. exists (x::x0). 
  constructor. 
  assumption.
Qed.

(* Extração do código *)
Recursive Extraction mk_List_prova.


(********************)
(* Exercício 1.b) *)


(* funcao indutiva para contar sumar todos os pares retornar um lista com o resultado *)
Inductive sum_pair : list (nat*nat) -> list nat -> Prop :=
  | sum_nil : sum_pair nil nil
  | sum_sec : forall (p: (nat*nat)) (inp: list (nat*nat)) (res: list nat), sum_pair inp res -> sum_pair (p::inp) (((fst p)+(snd p))::res).


Theorem sum_pair_prova : forall (inp: list(nat*nat)), {res: list nat | sum_pair inp res}.
Proof.
  intros.
  induction inp.
  - exists nil. 
    constructor.
  - elim IHinp.
    + intros. exists ( ((fst a) + (snd a))::x ). 
    constructor. 
    assumption.
Qed.

(* Extração do código *)
Recursive Extraction sum_pair_prova.

(********************)
(* Exercício 2.a) *)
Open Scope Z_scope. 

(* Funcao do enunciado *)
Fixpoint count (z:Z) (l:list Z) {struct l} : nat :=
  match l with
  | nil => 0%nat
  | (z' :: l') => if (Z.eq_dec z z')
                  then S (count z l')
                  else count z l'
  end.

(* Os elementos difetentes de x não interferem   *)
Lemma count_prova : forall (x:Z) (a:Z) (l:list Z), x <> a ->  count x (a :: l) = count x l.
Proof.
  intros.
  simpl.
  elim (Z.eq_dec x a).
  - intros. 
    contradiction.
  - intros. 
    trivial.
Qed.




(********************)
(* Exercício 2.b) *)
(* funcao indutiva para contar o numero de vezes que um elemento aparece numa lista *)
Inductive countRel : Z -> list Z -> nat -> Prop :=
  | countNil : forall e:Z, countRel e nil 0
  | countIf : forall (e:Z) (l: list Z) (n: nat), countRel e l n -> countRel e (e::l) (S n)
  | countElse : forall (e e':Z) (l: list Z) (n: nat), e <> e' -> countRel e l n -> countRel e (e'::l) n.




(********************)
(* Exercício 2.c) *)

(* o resultado da funcao 2b tem de ser o mesmo que o da funcao count. *)
Lemma count_prova2 : forall (x:Z) (l:list Z), countRel x l (count x l).
Proof.
  intros.
  induction l.
  - simpl. constructor.
  - simpl. elim (Z.eq_dec x a).
    + intros. rewrite <- a0. 
      constructor. 
      assumption.
    + intros. destruct IHl. 
      * constructor.
        -- assumption.
        -- constructor.
      * constructor.
        -- assumption.
        -- constructor. assumption.
      * constructor.
        -- assumption.
        -- constructor.
          ++ assumption.
          ++ assumption.
Qed.



Close Scope Z_scope.






