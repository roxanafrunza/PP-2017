module Interpreter
  (
    -- * Types
    Prog,
    Asgn,

    -- * Functions
    evalRaw,
    evalAdt,
  ) where

import qualified Data.Map as Map
import qualified Data.Either as Either
import qualified Data.Tuple as Tuple

constant = -99999

-------------------------------------------------------------------------------
--------------------------------- The Expr ADT  -------------------------------
-------------------------------------------------------------------------------
data Expr = Add Expr Expr
          | Sub Expr Expr
          | Mult Expr Expr
          | Equal Expr Expr
          | Smaller Expr Expr
          | Symbol String
          | Value Int deriving (Show, Read)

-- TODO Implement a parser for the Expr ADT.
--

-------------------------------------------------------------------------------
---------------------------------- The Prog ADT -------------------------------
-------------------------------------------------------------------------------
data Asgn = Asgn String Expr deriving (Show, Read)

data Prog = Eq Asgn
          | Seq Prog Prog
          | If Expr Prog Prog
          | For Asgn Expr Asgn Prog
          | Assert Expr
          | Return Expr deriving (Show, Read)

-- TODO Implement a parser for the Prog ADT.
--

-- TODO The *parse* function. It receives a String - the program in
-- a "raw" format and it could return *Just* a program as an instance of the
-- *Prog* data type if no parsing errors are encountered, or Nothing if parsing
-- failed.
--
-- This is composed with evalAdt to yield the evalRaw function.
parse :: String -> Maybe Prog
parse = undefined

-------------------------------------------------------------------------------
-------------------------------- The Interpreter ------------------------------
-------------------------------------------------------------------------------

-- TODO The *evalAdt* function.  It receives the program as an instance of the
-- *Prog* data type and returns an instance of *Either String Int*; that is,
-- the result of interpreting the program.
--
-- The result of a correct program is always an Int.  This is a simplification
-- we make in order to ease the implementation.  However, we wrap this Int with
-- a *Either String* type constructor to handle errors.  The *Either* type
-- constructor is defined as:
--
-- data Either a b = Left a | Right b
--
-- and it is generally used for error handling.  That means that a value of
-- *Left a* - Left String in our case - wraps an error while a value of *Right
-- b* - Right Int in our case - wraps a correct result (notice that Right is a
-- synonym for "correct" in English).
-- 
-- For further information on Either, see the references in the statement of
-- the assignment.
--
evalAdt :: Prog -> Either String Int
evalAdt prog = let
               env = getEnv Map.empty prog
               in
               if ((Either.isLeft (Tuple.fst env)) == True ) then Left (head (Either.lefts [(Tuple.fst env)]))
               else
               let
               result = Tuple.snd env
               in
               if (result == constant) then Left "Missing return"
               else Right result

-- Function to evaluate the TDA Expr. It receives an enviroment (a map 
-- containt the name of the variable and its value) and based on it,
-- it calculates the result value. If any of the variable used aren't
-- in the environment, the result would be "Unitialized variable"
-- The expressions that should return a boolean value will return
-- 0 if false and 1 if true.
evalExpression :: (Map.Map String Int) -> Expr -> Either String Int
evalExpression _ (Value v) = Right v
evalExpression env (Symbol s) = if (Map.member s env) == False then Left "Uninitialized variable"
                                else Right (Map.findWithDefault 0 s env)
evalExpression env (Add e1 e2) = let
                                 v1 = evalExpression env e1
                                 v2 = evalExpression env e2
                                 in
                                 if ( (Either.isLeft v1) == True || (Either.isLeft v2)  == True )
                                 then Left "Unintiliazed variable"
                                 else Right ( (head (Either.rights [v1] )) + (head (Either.rights [v2])) )
evalExpression env (Sub e1 e2) = let
                                 v1 = evalExpression env e1
                                 v2 = evalExpression env e2
                                 in
                                 if ( (Either.isLeft v1) == True || (Either.isLeft v2)  == True )
                                 then Left "Unintiliazed variable"
                                 else Right ( (head (Either.rights [v1] )) - (head (Either.rights [v2])) )
evalExpression env (Mult e1 e2) = let
                                 v1 = evalExpression env e1
                                 v2 = evalExpression env e2
                                 in
                                 if ( (Either.isLeft v1) == True || (Either.isLeft v2)  == True )
                                 then Left "Unintiliazed variable"
                                 else Right ( (head (Either.rights [v1] )) * (head (Either.rights [v2])) )
evalExpression env (Smaller e1 e2) = let
                                     v1 = evalExpression env e1
                                     v2 = evalExpression env e2
                                     in
                                     if ( (Either.isLeft v1) == True || (Either.isLeft v2)  == True ) 
                                     then Left "Unintiliazed variable"
                                     else
                                     let 
                                     value1 =  (head (Either.rights [v1] ))
                                     value2 =  (head (Either.rights [v2] ))
                                     in
                                     if(value1 < value2) then Right 1
                                     else Right 0
evalExpression env (Equal e1 e2) = let
                                   v1 = evalExpression env e1
                                   v2 = evalExpression env e2
                                   in
                                   if ( (Either.isLeft v1) == True || (Either.isLeft v2)  == True ) 
                                   then Left "Unintiliazed variable"
                                   else
                                   let 
                                   value1 =  (head (Either.rights [v1] ))
                                   value2 =  (head (Either.rights [v2] ))
                                   in
                                   if(value1 == value2) then Right 1
                                   else Right 0


-- Function to add a variable to the environment. First we have to evaluate
-- the expression for value. If it uses unitialized variables, the result
-- will be "Unitialized variable".

addEnv :: (Map.Map String Int) -> Asgn -> Either String (Map.Map String Int) 
addEnv env (Asgn s v) =  let
                         value = evalExpression env v
                         in
                         if( (Either.isLeft value) == True) then Left "Uninitialized variable"
                         else
                         Right (Map.insert s (head (Either.rights [value] ))  env)


-- Function to calculate the value of the program. It returns a tuple containing
-- the environment and the return value.

getEnv :: (Map.Map String Int) -> Prog -> (Either String (Map.Map String Int), Int)
getEnv env (Eq asgn) = ( (addEnv env asgn), constant)
getEnv env (If e1 p1 p2) = let
                           condition = evalExpression env e1
                           in
                           if ( (Either.isLeft condition) == True)  
                           then ( Left (head (Either.lefts [condition])), constant)
                           else
                           let
                           bool =  (head (Either.rights [condition]))
                           in
                           case bool of
                           1 -> getEnv env p1
                           0 -> getEnv env p2
getEnv env (For asgn bool inc body) = let
                                      newEnv = addEnv env asgn
                                      in
                                      if( ( Either.isLeft newEnv)  == True) 
                                      then ( Left (head (Either.lefts [newEnv])) , constant )
                                      else
                                      aux (head (Either.rights [newEnv])) bool inc body
getEnv env (Seq p1 p2) = let
                         env1 = getEnv env p1
                         newEnv = (head (Either.rights [(Tuple.fst env1)]))
                         env2 = getEnv newEnv p2
                         in
                         if ( (Either.isLeft (Tuple.fst env1)) == True) 
                         then (Left (head (Either.lefts [(Tuple.fst env1)])), (Tuple.snd env1))
                         else if ( (Either.isLeft (Tuple.fst env2)) == True) 
                         then  (Left (head (Either.lefts [(Tuple.fst env2)])), (Tuple.snd env2))
                         else if ((Tuple.snd env1) /= constant) 
                         then (Right (Map.union (head (Either.rights [(Tuple.fst env2)])) newEnv ), (Tuple.snd env1))
                         else (Right (Map.union (head (Either.rights [(Tuple.fst env2)])) newEnv ), (Tuple.snd env2))
getEnv env (Return expr) = let
                           check = evalExpression env expr
                           in
                           if( (Either.isLeft check) == True ) then ( Left (head (Either.lefts [check])), constant)
                           else
                           (Right env, (head (Either.rights [check])))
getEnv env (Assert expr) = let
                           check = evalExpression env expr
                           in
                           if( (Either.isLeft check) == True ) then (Left (head (Either.lefts [check])),constant)
                           else
                           let
                           bool =  (head (Either.rights [check]))
                           in
                           case bool of
                              1 -> (Right env, constant)
                              0 -> (Left "Assert failed", constant)


aux :: (Map.Map String Int) -> Expr -> Asgn -> Prog -> (Either String (Map.Map String Int), Int)
aux env cond incr prog = let
                         boolCond = evalExpression env cond
                         in
                         if(Either.isLeft boolCond == True) then ( Left (head (Either.lefts [boolCond])), constant)
                         else if ((head (Either.rights [boolCond])) == 0) then (Right env, constant)
                         else 
                         let
                         envProg = getEnv env prog
                         envInc = addEnv (head (Either.rights [(Tuple.fst envProg)])) incr
                         in
                         if((Either.isLeft (Tuple.fst envProg)) == True) then (Left (head (Either.lefts [(Tuple.fst envProg)])), (Tuple.snd envProg))
                         else if((Either.isLeft envInc) == True) then (Left (head (Either.lefts [envInc])), constant)
                         else
                         let 
                         updatedEnv = Map.union (head (Either.rights [envInc])) (head (Either.rights [(Tuple.fst envProg)])) 
                         in
                         aux updatedEnv cond incr prog

-- The *evalRaw* function is already implemented, but it relies on the *parse*
-- function which you have to implement.
--
-- Of couse, you can change this definition.  Only its name and type are
-- important.
evalRaw :: String -> Either String Int
evalRaw rawProg =
    case parse rawProg of
        Just prog -> evalAdt prog
        Nothing   -> Left "Syntax error"