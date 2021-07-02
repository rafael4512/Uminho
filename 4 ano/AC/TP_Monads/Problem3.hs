module Problem3 where
import Problem2
import Problem1
import DurationMonad

import System.IO  
import Control.Monad
import Data.List
import StateMonad

------------------ 
-- P1 
-- Ler e Escrever os grafos para ficheiros
------------------   

-- Escreve o grafo num ficheiro.
-- PARAM: filename -  o nome do ficheiro; bool- flag caso queiramos utilizar o grafo com pesos ou não.
graphToFile :: FilePath -> Bool -> IO ()
graphToFile filename  bool 
    | bool = writeFile filename graphToStr2 --Grafos com pesos nas transicoes
    | otherwise  = writeFile filename graphToStr



-- le um grafo de um ficheiro.
-- PARAM: filename - Nome do ficheiro.
readFromFile :: FilePath -> IO ()
readFromFile filename  = do  
                            f <- openFile filename ReadMode  -- abrir o descritor
                            contents <- hGetContents f
                            let g = (words <$> (lines contents) ) in
                                let grafo =  toGraph ((length . head) g)  g in 
                                    print grafo

                             
                            hClose f                     -- fechar o descritor
------------------ 
-- CASTS EM HASKELL.

type Grafo =[(Node,Node,Int)]
-- Axiliar Functions

toInt :: [String] -> [Int]
toInt = map read

toNode ::String -> Node
toNode "A" = A
toNode "B" = B
toNode "C" = C
toNode "D" = D
toNode "E" = E
toNode "F" = F

toGraph::Int ->[[String]]->Grafo
toGraph  2 l= map convertTypes2 l 
toGraph  3 l= map convertTypes l 
toGraph  _ l= map convertTypes l 
 

convertTypes::[String] ->(Node,Node,Int)
convertTypes [h,h2,h3]= (toNode h,toNode h2,read h3)

convertTypes2::[String] ->(Node,Node,Int)
convertTypes2 [h,h2]= (toNode h,toNode h2,0)


-- cria as combinaçoes de todos os pares possiveis.
pairs :: [a] -> [(a, a)]
pairs l = [(x,y) | x <-l, y<-  l]

toStr::(Node,Node)-> String
toStr (a,b) = show a ++ " "++ show b ++ "\n" 

toStr2::(Node,Node)-> String
toStr2 (a,b) = show a ++ " "++ show b ++ ( (\x -> case adjT x  of {Just y -> " " ++ show y ; _ ->"" }) (a,b) )++ "\n" 


--Tranforma um grafo numa string
graphToStr = do 
                p <-pairs allNodes
                p2 <- filter (\x -> adj x) [p]   
                str <- toStr p 
                return str


graphToStr2 = do 
                p <-pairs allNodes
                p2 <- filter (\x -> case (adjT x) of {Just y -> True; _ ->False}) [p]   
                str <- toStr2 p 
                return str






------------------ 
-- P2
------------------   
--Configs do veiculo elétrico.
--  NOTA :::: SE der tempo para fazer: INSERIR O TEMPO QUE DEMORA CADA VIAGEM. E variar consumos.
-- e preços.
type Battery = Int
consuption_KM = 1  -- Consumo do veiculo por km;
batery_state::Int
batery_state  = 40 -- Estado inicial da bateria;




-- normaliza um inteiro entre 0 e 100.
normalize :: Int -> Int
normalize x 
    | x <0 = 0
    | x >100 = 100
    |otherwise = x

--Muda o estado da bateria somando o inteiro passado.
change_bat::Int -> State Battery ()
change_bat v =  State $ \x -> ((),normalize  (x+v )) 

-- Coloca o valor do estado 
tos :: State Battery Int 
tos = State $ \x-> (x,x)

--teste para a função changebat
test :: State Battery Int
test = do 
    change_bat 10
    change_bat 10
    change_bat $ -5
    tos


  
-- evalState2 test batery_state
------------------ 
-- Main
  

-- Esta main calcula para todos os Nodes
main2 = (concat . calcula_todas_rotas) <$> allNodes
-- Passando um nó calcula todos as rotas, respetivos pontos de carga, a sua percentagem e o estado atual da bateria em cada cidade
main n = (concat . calcula_todas_rotas) n

--recebe um nó
calcula_todas_rotas node= do
    route <- hCycles node -- Passa a distancia para bateria necessária para a rota.
    return $  (\x -> case ((null . snd. snd)  x) of {True-> []; False ->[x]})  (route,(solver.result) route)
    --(\d ->  d route
    --let res = evalState2 test batery_state
    --print res
    --return x


-- Cidades onde é possivel abastecer x porcento.Esta percentagem depende de quanto tempo irá ficar numa cidade. -- passar o nome para charging_stations
charging_stations :: Node -> Maybe Int
charging_stations p = case p of
    A -> Just 10
    C -> Just 30 -- é possivel abastecer num maximo até 30 % da bateria.
    F -> Just 70 -- porque tem bastante tempo nesta cidade.
    _ -> Nothing





---------------------------------
-- Algoritmo com o Veiculo.



{-
    1. Calcular todos os ciclos hamiltonianos ( Função do Ex 2 ) 
    2. Calcula a bateria necessaria em cada nodo e todas as possibilidades de carregamento.
    3. Apartir do 2 , calcular a bateria atual em cada  nodo (todas as possibilidades).
    4. Detetar e remover quais são as rotas que contem em algum momento a bateria a 0 ! 
    5. Calcular o caminho que carrega menos tempo.
-}


-- calcula a bateria necessaria para realizar cada transição
calcula_bat_necessaria::[Node] -> [Int]
calcula_bat_necessaria []= []
calcula_bat_necessaria [a] = []
calcula_bat_necessaria (x:x2:xs) = ((\p -> case p of {Just y -> y* consuption_KM; _ ->0}) (adjT (x,x2) )): calcula_bat_necessaria (x2:xs)




-- calcula todas os pontos onde se pode carregar .
calcula_chargingPoint::[Node]->[Int] 
calcula_chargingPoint []= []
calcula_chargingPoint [a] = []
calcula_chargingPoint (x:xs) = ((\p -> case p of {Just y -> y ; _ ->0}) (charging_stations x )): calcula_chargingPoint xs







-------------------------------------------------------------




-------------------------------------------------------------
-- Algoritmo para criar todas as combinaçoes de recarga.
replaceNth :: Int -> a -> [a] -> [a]
replaceNth _ _ [] = []
replaceNth n newVal (x:xs)
    | n == 0 = newVal:xs
    | otherwise = x:replaceNth (n-1) newVal xs



genlist0:: Int-> [Int]
genlist0 0 = []
genlist0 n = 0:genlist0 (n-1)


--Calcula todas as possibilidades de recarga
allpossibilities :: [Int] ->[[Int]]
allpossibilities l = init $ createlists (length l) l (powerset (getindex 0 l ))
            

--l original
-- l2 list gerada com 0 
-- e a outra são os indices dos elementos inalterados.
createlist:: [Int]->[Int]->[Int]->[Int]
createlist l l2 [] = l2
createlist l l2 (x:xs) = createlist l (replaceNth x (l!!x)  l2) xs 


createlists::Int->[Int] -> [[Int]] -> [[Int]] 
createlists a l [] = []
createlists a l (x:xs) = (createlist l (genlist0 (length l)) x):createlists a l xs


getindex::Int -> [Int] -> [Int] 
getindex  a [] = [] 
getindex  a (x:xs) = if x /= 0 then a:(getindex (a+1) xs) else (getindex (a+1) xs )

powerset :: [a] -> [[a]]
powerset [] = [[]]
powerset (x:xs) = map (x:) (powerset xs) ++ powerset xs
-------------------------------------------------------------
-- verifica se um lista de inteiros 
verify_allzeros ::[Int]-> Bool
verify_allzeros [] = True
verify_allzeros (h:hs) 
    |h == 0 = verify_allzeros hs 
    |otherwise = False

--verifica se existe um elemento 0 na lista, se sim dá true
any_zero  l = any (\x -> x==0) l 


--calcula a percentagem de bateria a aumentar/diminuir em cada transição ao estado atual da bateria.
apply l =  do {   y<- (allpossibilities. calcula_chargingPoint) l ; return (zipWith (+) (negate<$>(calcula_bat_necessaria l)) y )}


--Soma de 2 valores normalizada, entre 0 a 100.
norm_sum a b = normalize $ (+) a b

-- rota=[B,A,F,E,D,C,B]
-- Calcula, para todas as possiveis configurações de carregamento , a bateria atual em cada momento(Node) da viagem.
verifica_viagem rota = do 
    l <- apply rota 
    let aux = (normalize <$> scanl (norm_sum)  batery_state l ) in 
        return  (zipWith (+) l (calcula_bat_necessaria rota ) , aux++[ normalize $(last aux ) -((last . calcula_bat_necessaria) rota )  ] )
        



-- filtra todas as viagens onde a bateria não chega a 0.
result  x = filter (not.any_zero .snd) $ verifica_viagem x 
 
-- Serve para Canalizar a rota onde tem menor percentagem de carga.
solver [] = ([],[])
solver x = minimum x 

--result[B,A,C,D,E,F,B]


--f x = do { y <- choice(return(x-1),return(x+1)); choice(return(y-1),return(y+ 1)) }
