module Problem2 where

import DurationMonad
import Problem1

{- 1. *Labelled* graph structure: nodes and labelled adjacency matrix
(i.e. the labelled edges of the graph)
-}
adjT :: (Node,Node) -> Maybe Int
adjT p = case p of
           (A,B) -> Just 2
           (A,C) -> Just 3  
           (A,F) -> Just 6 
           (B,A) -> Just 30
           (B,C) -> Just 0
           (B,E) -> Just 4
           (B,F) -> Just 3
           (C,A) -> Just 60
           (C,B) -> Just 3
           (C,D) -> Just 50
           (D,C) -> Just 2
           (D,E) -> Just 3
           (E,B) -> Just 1
           (E,D) -> Just 3
           (E,F) -> Just 2
           (F,A) -> Just 4
           (F,B) -> Just 5
           (F,E) -> Just 3
           (_,_) -> Nothing

-- 2. Auxiliary functions
{- Given a node n and a list of nodes ns the function returns the nodes
in ns that can be reached from n in one step together with the time
necessary to reach them.
-}

tadjacentNodes :: Node -> [Node] -> [Duration Node]
tadjacentNodes n ns =  do  
                        v <- map (\x -> (x,adjT(n,x)) ) allNodes; 
                        res <- [v]>>= (\(a,x) -> case x of {Just y -> [Duration(y,a)]; _ ->[]})
                        return res

-- f = (\x -> case x of {Just y -> [y]; _ ->[]}) 
-- 3. Main body
{- For each node a in ns, if a is not already in p the function creates
   a new path (like in the previous problem) and computes its cost.
-}
-- !! to implement !!
taddToEnd :: Duration Path -> [Duration Node] -> [Duration Path]
taddToEnd p ns = do 
                  s1 <- filter (\(d,v) -> not (elem v (getValue p) ) )  (getPair <$> ns)
                  f <- (\(d,v) -> Duration(getDuration p + d,(getValue p) ++ [v]))  <$> [s1]; 
                  return f

-- .......
hCyclesCost :: Node -> [Duration Path]
hCyclesCost n = do  
   c1 <- (taddToEnd (Duration(0,[n])) $ tadjacentNodes n allNodes) ; 
   c2 <- taddToEnd c1 $ tadjacentNodes  ((last . getValue ) c1 ) allNodes;
   c3 <- taddToEnd c2 $ tadjacentNodes  ((last . getValue ) c2 ) allNodes;
   c4 <- taddToEnd c3 $ tadjacentNodes  ((last . getValue ) c3 ) allNodes;
   c5 <- taddToEnd c4 $ tadjacentNodes  ((last . getValue ) c4 ) allNodes;
   c6 <- rem_non_cycles2 c5
   return c6
-- the main program                                    
tsp = minimum . hCyclesCost

catmaybes =  (\x -> case x of {Just y -> [y]; _ ->[]})
rem_non_cycles2::Duration Path->[Duration Path]
rem_non_cycles2 (Duration (d,path)) 
 | elem (head path) (adjacentNodes (last path) allNodes) = let v =  ((head.catmaybes .adjT)  (last path,head path))
                                                           in  [ wait v  (Duration (d,path++[(head path)]))] 
 | otherwise = []