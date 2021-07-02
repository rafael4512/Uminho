(* ================================= Ex 1 ================================= *)
Section EX_1.

Variables A B C D:Prop. 




Lemma ex_1_1 : (A -> C)/\(B -> C) -> (A /\ B) -> C.
Proof.
  intros.
  apply H.
  apply H0.
Qed.



Lemma ex_1_2 : ~A \/ ~B  -> ~ (A /\ B) .
Proof.
  intros.
  intro.
  destruct H.
  apply H.
  destruct H0.
  - contradiction.
  - destruct H0.
  contradiction. 
Qed.


Lemma ex_1_3 : (A -> (B \/ C)) /\(B -> D) /\ (C ->D) -> (A->D).
Proof.
  intros.
  destruct H.
  destruct H1.
  destruct H.
  -assumption.
  -apply H1.
  assumption.
  - apply H2.
  assumption.
Qed.


Lemma ex_1_4 : (A /\ B) -> ~(~A \/ ~B).
Proof.
  intros.
  intro.
  destruct H.
  destruct H0.
  contradiction.
  contradiction.
Qed.


End EX_1.


(* ================================= Ex 2 ================================= *)
Section EX_2.

Variable X : Set.
Variables P Q R: X -> Prop.

Lemma ex2_1: (forall x, (P x) -> (Q x)) -> (forall y , ~(Q y)) -> (forall x, ~(P x)).
Proof.
  intros.
  intro.
  destruct (H0 x).
  apply H.
  assumption.
Qed.


Lemma ex2_2: (forall x, (P x) \/ (Q x)) -> (exists y , ~(Q y)) -> (forall x, (R x)-> ~(P x)) -> (exists x,~(R x)).
Proof.
  intros.
  destruct H0 .
  exists x.
  intro.
  destruct (H1 x).
  assumption.
  destruct (H x).
  assumption.
  contradiction.
Qed.


End EX_2.


(* ============================== Ex 3 ==================================== *)
Section EX_3.

Hypothesis excluded_middle : forall P : Prop, P \/ ~P.
Variables A B:Prop.

Lemma ex3_1: (~A -> B) -> (~ B -> A ).
Proof.
  intros.
  destruct excluded_middle with A.
  assumption.
  destruct H0.
  apply H.
  assumption.
Qed.

Variable X : Set.
Variables P : X -> Prop.




Lemma ex3_2: ~(exists x, ~(P x))  -> forall x, (P x) .
Proof.
  intros.
  destruct excluded_middle with (P x).
  - exact H0.
  - destruct H.
  exists x.
  assumption.

Qed.


Lemma ex3_3: ~(forall x, ~(P x) ) -> (exists x , (P x)) .
Proof.
  intros.
  destruct excluded_middle with (exists x, P x).
  - exact H0.
  - destruct H.
  intro x0.
  destruct excluded_middle with (~P x0). 
  assumption.
  unfold not.
  intro.
  destruct H0.
  exists x0.
  assumption.
Qed.





End EX_3.



