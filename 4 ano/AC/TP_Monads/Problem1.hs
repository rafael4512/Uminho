{-# LANGUAGE InstanceSigs #-}
module Problem1 where
import Data.List
-- 1. Graph structure: nodes and adjacency matrix (i.e. the edges) 
data Node = A | B | C | D | E | F deriving (Show,Eq,Ord)

adj :: (Node,Node) -> Bool
adj p = case p of
  (A,B) -> True
  (A,C) -> True  
  (A,F) -> True 
  (B,A) -> True
  (B,C) -> True
  (B,E) -> True
  (B,F) -> True
  (C,A) -> True
  (C,B) -> True
  (C,D) -> True
  (D,C) -> True
  (D,E) -> True
  (E,B) -> True
  (E,D) -> True
  (E,F) -> True
  (F,A) -> True
  (F,B) -> True
  (F,E) -> True
  (_,_) -> False

type Path = [Node]

-- 2. Auxiliary functions
adjacentNodes :: Node -> [Node] -> [Node]
adjacentNodes n ns = filter (\x -> adj(n,x)) ns

allNodes :: [Node]
allNodes = [A,B,C,D,E,F]

choice :: ([a],[a]) -> [a]
choice = uncurry (++)



-- 3. Main body
{- For each node a in ns, if a is not already in p the function
   creates a new path by adding to the end of p the element a.
   
   p - Caminho até ao momento.
   ns - Lista de nos adjacentes.
-}

{-
addtoEnd :: Path -> [Node] -> [Path]
addtoEnd p ns = let ns_filtered= ns \\ p in  -- filtrar todos os nós adjecentes que ainda não pertencem ao caminho( \\ é operador diferença).
  map (\x ->  p ++ [x]) ns_filtered
-}

addtoEnd :: Path -> [Node] -> [Path]
addtoEnd p ns = let ns_filtered= ns \\ p in-- filtrar todos os nós adjecentes que ainda não pertencem ao caminho( \\ é operador diferença).
   (\x -> p++[x]) <$> ns_filtered



-- Computes all Hamiltonian cycles starting from a given node
-- Important Tip : "x <- mother s" = "mother s >>= (\x ->" 
hCycles :: Node -> [Path]
hCycles n = do 
          c1 <- (addtoEnd [n] $ adjacentNodes n allNodes) -- C1 é iterador de uma função por compreensão. 
          c2 <- (addtoEnd c1 $ adjacentNodes (last c1) allNodes) 
          c3 <- (addtoEnd c2 $ adjacentNodes (last c2) allNodes) 
          c4 <- (addtoEnd c3 $ adjacentNodes (last c3) allNodes) 
          c5 <- (addtoEnd c4 $ adjacentNodes (last c4) allNodes) 
          c6 <- rem_non_cycles c5 -- c5 = pensar como um iterador de um map.No primeiro caso pode ser um coisa assim :[A,B,C,D,E,F], sendo que irá iterar todos os caminhos.
          return c6          

-- Verifica se um dado caminho é um ciclo hamiltoniano. Caso não seja não é devolvido.
rem_non_cycles::Path->[Path]
rem_non_cycles [] = []
rem_non_cycles path 
  | elem (head path) (adjacentNodes (last path) allNodes) = [ choice (path,[(head path)])] -- Verifica se é possivel alcançar o nó inicial e junta-o ao caminho 
  | otherwise =[]


