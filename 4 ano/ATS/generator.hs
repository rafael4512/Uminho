import Test.QuickCheck
import System.IO
import System.Directory(copyFile)

-- definicao dos tipos 

type Identificador = String
type Nome = String
type PosX = Float
type PosY = Float
type Raio = Int
type NIF = Int
type PKm = Float
type Peso = Float
type Lista = [(String,String,Float,Float)]

nomesMasculinos = ["Artur", "Bruno","Rafael","Joao","Jose","Antonio","Isaac","Ethan","Danilo","Kaleb","Mateus","Jesus","Andre"]

nomesFemininos = ["Ana","Joana","Catarina","Isis","Beatriz","Ilda","Liz","Rebeca","Sofia","Raquel","Abigail","Josefina","Talita"]

nomes = nomesMasculinos ++ nomesFemininos

apelidos = ["Silva","Santos","Ferreira","Veloso","Pereira","Costa","Sousa","Marques","Ribeiro","Pinto","Carvalho","Pimentel","Fernandes","Almeida","Moreira","Nunes","Gomes","Oliveira"]

nomesTransportadoras = ["CHONOPOST","CTT","UPS","NACEX","DHL","SEDEX","SCHENKER","GARLAND","TNT","MRW","DSV"]

nomesLojas = ["Worten","Fnac","PC Diga","Primark","Staples","Continente","Pingo Doce","Mini Preco","Intermarche","Seaside","Decathlon","Gato Preto","Pull and Bear","Adidas","Puma","Nike","Stradivarus"]

listaProdutos = [("p1","Cenoura",2,3),("p2","Chocolate",4,5),("p3","Regua",2,1),("p4","Rato",42,24),("p5","PS5",2,500),("p6","XBOX",1,400),("p7","Mac",3,1400),("p8","Sapatilhas",5,40),("p9","Carro",1,20000),("p10","Couve",3,0.30),("p11","Camisola",3,20),("p12","Arroz",1,0.40),("p13","Agua",44,10),("p14","Cadeira",1,30),("p15","Piano",1,400),("p16","Guitarra",1,300)]

--funcao auxiliar para encomenda
converte :: Lista -> String
converte [] = ""
converte ((a,b,c,d):[]) = a ++ "," ++ b ++ "," ++ show(c) ++ "," ++ show(d)
converte ((a,b,c,d):xs) = a ++ "," ++ b ++ "," ++ show(c) ++ "," ++ show(d) ++ "," ++ converte(xs)

-- composicao da linha do utilizador
data Utilizador = Utilizador Identificador Nome PosX PosY
instance Show Utilizador where
  show = print_nu
print_nu (Utilizador a b c d) = "Utilizador:" ++ a ++ "," ++ b ++ "," ++ show(c) ++ "," ++ show(d) ++"\n"

-- composicao da linha do voluntario
data Voluntario = Voluntario Identificador Nome PosX PosY Raio
instance Show Voluntario where
  show = print_nv
print_nv (Voluntario a b c d e) = "Voluntario:" ++ a ++ "," ++ b ++ "," ++ show(c) ++ "," ++ show(d) ++ "," ++ show(e) ++"\n"


-- composicao da linha da transportadora
data Transportadora = Transportadora Identificador Nome PosX PosY NIF Raio PKm
instance Show Transportadora where
  show = print_nt
print_nt (Transportadora a b c d e f g) = "Transportadora:" ++ a ++ "," ++ b ++ "," ++ show(c) ++ "," ++ show(d) ++ "," ++ show(e) ++ "," ++ show(f) ++ "," ++ show(g) ++"\n"

-- composicao da linha de loja
data Loja = Loja Identificador Nome PosX PosY
instance Show Loja where
  show = print_nl
print_nl (Loja a b c d) = "Loja:" ++ a ++ "," ++ b ++ "," ++ show(c) ++ "," ++ show(d) ++"\n"


-- composicao da linha da encomenda
data Encomenda = Encomenda Identificador Identificador Identificador Peso Lista
instance Show Encomenda where
  show = print_ne
print_ne (Encomenda a b c d l) = "Encomenda:" ++ a ++ "," ++ b ++ "," ++ c ++ "," ++ show(d) ++ "," ++ converte(l) ++ "\n" ++ "Aceite:" ++ a ++"\n"


--gerar identificador
genIdentificador :: Gen Identificador
genIdentificador = do a <- choose(1 :: Int ,100000 :: Int)
                      return ("u"++show(a))

--gerar Nomes
genNome :: Gen Nome
genNome = do a <- elements nomes
             b <- elements apelidos
             return(a ++ " " ++ b)

--gerar posicao X
genPosX :: Gen PosX
genPosX = choose(-99 :: Float, 99 :: Float)

--gerar posicao Y
genPosY :: Gen PosY
genPosY = choose(-99 :: Float, 99 :: Float)


--gerar utilizadores
genUtilizador :: Gen Utilizador
genUtilizador = do a <- genIdentificador
                   b <- genNome
                   c <- genPosX
                   d <- genPosY
                   return (Utilizador a b c d)


--gerar identificador voluntario
genIdentificadorV :: Gen Identificador
genIdentificadorV = do a <- choose(1 :: Int ,100000 :: Int)
                       return ("v"++show(a))


--gerar raio
genRaio :: Gen Raio
genRaio = choose(1:: Int, 300 :: Int)

--gerar voluntarios
genVoluntario :: Gen Voluntario
genVoluntario = do a <- genIdentificadorV
                   b <- genNome
                   c <- genPosX
                   d <- genPosY
                   e <- genRaio
                   return (Voluntario a b c d e)

--gerar identificador transportadora
genIdentificadorT :: Gen Identificador
genIdentificadorT = do a <- choose(1 :: Int ,100000 :: Int)
                       return ("t"++show(a))

--gerar identificador loja
genIdentificadorL :: Gen Identificador
genIdentificadorL = do a <- choose(1 :: Int ,100000 :: Int)
                       return ("l"++show(a))


--gerar preco por km
genPKm :: Gen PKm
genPKm = choose(0.1 :: Float,4.0 :: Float)

--gerar NIF
genNIF :: Gen NIF
genNIF = choose(100000000 :: Int,999999999 :: Int)

--gerar transportadoras
genTransportadora :: Gen Transportadora
genTransportadora = do a <- genIdentificadorT
                       b <- elements nomesTransportadoras
                       c <- genPosX
                       d <- genPosY
                       e <- genNIF
                       f <- genRaio
                       g <- genPKm
                       return (Transportadora a b c d e f g)

--gerar lojas
genLoja :: Gen Loja
genLoja = do a <- genIdentificadorL
             b <- elements nomesLojas
             c <- genPosX
             d <- genPosY
             return (Loja a b c d)


--gerar identificador encomenda
genIdentificadorE :: Gen Identificador
genIdentificadorE = do a <- choose(1 :: Int ,100000 :: Int)
                       return ("e"++show(a))

--gerar peso
genPeso :: Gen Peso
genPeso = choose(0.1 :: Float,500.0 :: Float)

--gerar lista de compras
genLista :: Gen Lista
genLista = do n <- choose(1,4)
              (vectorOf n $ elements listaProdutos)

--gerar encomendas
genEncomenda :: [String] -> [String] -> Gen Encomenda
genEncomenda u l = do a <- genIdentificadorE
                      b <- elements u
                      c <- elements l
                      d <- genPeso
                      p <- genLista
                      return (Encomenda a b c d p)               

--gerar numero de vezes
gerador :: Show a => String -> Gen a -> Int -> IO()
gerador file g vezes = do c <- generate (sequence [ resize n g | n <- [0,1..(vezes-1)] ])
                          mapM_ ((appendFile file).(show)) c



-- processar linha de utilizador
processa :: String -> Int -> String
processa (x:xs) y | x == 'U' = processa xs 1
                  | x == ',' && y == 2 = ""
                  | x == ':' && y == 1 = processa xs 2
                  | y == 2 = x : processa xs 2
                  | y == 1 = processa xs 1
                  | otherwise = processa xs 0

-- verifica se é utilizador
verificaUser :: String -> Bool
verificaUser [] = False
verificaUser (x:xs) | x == 'U' = True
                    | otherwise = False

-- ficar com os identificadores de utilizadores
getUsers :: [String] -> [String]
getUsers [] = []
getUsers (x:xs) | verificaUser x == True =  processa x 0 : getUsers xs
                | otherwise = getUsers xs


-- processar linha de loja
processaLoja :: String -> Int -> String
processaLoja (x:xs) y | x == 'L' = processaLoja xs 1
                  | x == ',' && y == 2 = ""
                  | x == ':' && y == 1 = processaLoja xs 2
                  | y == 2 = x : processaLoja xs 2
                  | y == 1 = processaLoja xs 1
                  | otherwise = processaLoja xs 0


-- verifica se é loja
verificaLoja :: String -> Bool
verificaLoja [] = False
verificaLoja (x:xs) | x == 'L' = True
                    | otherwise = False


-- ficar com os identificadores de lojas
getLojas :: [String] -> [String]
getLojas [] = []
getLojas (x:xs) | verificaLoja x == True =  processaLoja x 0 : getLojas xs
                | otherwise = getLojas xs


-- Main, perguntar quantidade a gerar
main :: IO()
main = do putStrLn "Nome do ficheiro para guardar LOGS: "
          file <- getLine
          putStrLn "Número de utilizadores: "
          a <- getLine
          putStrLn "Número de voluntários: "
          b <- getLine
          putStrLn "Número de transportadoras: "
          c <- getLine
          putStrLn "Número de lojas: "
          d <- getLine
          putStrLn "Número de encomendas: "
          e <- getLine
          let nu = (read a :: Int)
          let nv = (read b :: Int)
          let nt = (read c :: Int)
          let nl = (read d :: Int)
          let ne = (read e :: Int)
          gu <- gerador file genUtilizador nu
          gv <- gerador file genVoluntario nv
          gt <- gerador file genTransportadora nt
          gl <- gerador file genLoja nl
          copyFile file "copy"
          fileHandle <- openFile "copy" ReadWriteMode
          content <- hGetContents fileHandle
          let linesOfFiles = lines content
          let users = getUsers linesOfFiles
          let lojas = getLojas linesOfFiles
          ge <- gerador file (genEncomenda users lojas) ne
          print "Ficheiro Gerado"



