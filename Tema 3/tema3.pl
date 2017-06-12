:- use_module(library(assoc)).

valnum(X):- char_type(X, alnum), char_type(X, ascii).
vother(X):- member(X, [';','<','+','-','*','(',')','{','}']).
validc(X):- valnum(X) ; vother(X) ;  X == '='.

lparseq(['='|L],'==',L).
lparseq([X|L],'=',[X|L]):-dif(X,'=').
lparseq([],'=',[]).

lparsealn([X|L],L2,R,L3):- valnum(X), lparsealn(L, [X|L2], R, L3).
lparsealn([X|L],L2,R,[X|L]):- \+valnum(X), reverse(L2, L3), atom_chars(R, L3).
lparsealn([],L2,R,[]):- reverse(L2, L3), atom_chars(R, L3).

lparse2(['='|L],L2,L3):- lparseq(L,R,L4), lparse2(L4,[R|L2],L3).
lparse2([X|L],L2,L3):- valnum(X),lparsealn(L,[X],R,L4), lparse2(L4,[R|L2],L3).
lparse2([X|L],L2,L3):- vother(X), lparse2(L,[X|L2],L3).
lparse2([X|L],L2,L3):- \+validc(X), lparse2(L,L2,L3).
lparse2([],L2,L3):- reverse(L2,L3).

lparse(S, L):- atom_chars(S, L2), lparse2(L2,[],L),!.

% parseaza lista, creeaza context si evalueaza program
auxiliary(L, R) :- conv_atoms(L, Atoms), 
				   conv_progs(Atoms, List),
				   create_context(Env), 
				   eval_progs(List,Env, Value),
					(
						Value \= 'x' ->  R = Value; 
									     R = 'x'
					).
% daca auxiliar e false, inseamna ca avem eroare
parseInputAux(L,R) :- auxiliary(L,R), !.
parseInputAux(_, R) :- R = 'e'.

parseInput(F,R):- read_file_to_string(F,S,[]), lparse(S,L), parseInputAux(L,R), !.

% Parsam lista de elemente. 
% Totul ramane neschimbat inafara de variabile si numere.
conv_atoms(List, Atoms) :-
        phrase(atoms(Atoms), List).

atoms([T|Ts]) -->
        atm(T), !,
        atoms(Ts).
atoms([]) --> "".

atm(';') --> [';'].
atm('{') --> ['{'].
atm('}') --> ['}'].
atm('(') --> ['('].
atm(')') --> [')'].

% operatori booleni
atm(opBool(<))  --> ['<'].
atm(opBool(==)) --> ['=='].

% operatori aritmetici
atm(opAdd(+)) --> ['+'].
atm(opAdd(-)) --> ['-'].
atm(opMult(*)) --> ['*'].

% operatorul egal (pentru assign)
atm(=) --> [=].

% cuvintele cheie raman neschimbate
atm(assert) --> [assert].
atm(if)     --> [if].
atm(then)   --> [then].
atm(else)   --> [else].
atm(return) --> [return].
atm(for)    --> [for].

% numere si variabile
atm(num(R)) --> [N], { atom_number(N, R) }.
atm(var(V)) --> [V], { atom_chars(V, L), only_letters(L) }.

only_letters([H|T]) :- char_type(H, alpha), only_letters(T).
only_letters([]).

only_digits([H|T]) :- char_type(H, digit), only_digits(T).
only_digits([]).

% Parsez lista de atomi pentru a construi o lista de programe
conv_progs(List, Atoms) :- phrase(progs(Atoms), List).
progs([T|Ts]) -->
       stm(T), !,
       progs(Ts).
progs([]) --> [].

% parsare instructiuni principale
stm(Asgn)    		 --> assign(Asgn), [;].
stm(if(Cond,S1,S2))  --> [if], ['('], cond(Cond), [')'], [then], stm_block(S1),
						 [else], stm_block(S2).
stm(assert(Cond)) 	 --> [assert], ['('], cond(Cond), [')'], [;].
stm(for(Asgn, Cond, Inc, S))  --> [for], ['('], assign(Asgn), [;], cond(Cond),
								  [;], assign(Inc), [')'],  stm_block(S).
stm(return(E))       --> [return], exp(E), [';'].
stm(empty_block) --> [';'].

% parsare bloc de instructiuni
stm_block(S) --> ['{'], stms(S), ['}'].

stms(S)   --> stm(S1), stmr(S1, S).
stms(empty_block) --> [].

stmr(S1, seq(S1, S)) --> stm(S2), stmr(S2, S).
stmr(S, S)           --> [].

% parsare operatii boolene
cond(compOp(Op,A,B)) --> exp(A), [opBool(Op)], exp(B).

%parsare assign
assign(asgn(Var, E))  --> [var(Var)], ['='], exp(E).

% parsare operatii aritnetice 
exp(E)      --> term(E1), expr(E1, E).
expr(E1, E) --> [opAdd(Op)], term(E2), expr(opAritm(Op, E1, E2), E).
expr(E, E)  --> [].

term(E)      --> elem(E1), termr(E1, E).
termr(E1, E) --> [opMult(Op)], elem(E2), termr(opAritm(Op, E1, E2), E).
termr(E, E)  --> [].

elem(num(N))  --> [num(N)].
elem(var(Id)) --> [var(Id)].


% creare si modificare context
create_context(Env) :- empty_assoc(Env). 
add_to_context(Name, Value, Env, NewEnv) :-
	put_assoc(Name, Env, Value, NewEnv).
get_from_context( Name, Value, Env) :- 
	get_assoc(Name, Env, Value).


% evaluare operatii de comparare
eval(compOp(Op, E1, E2), Env, Res) :-
	eval(E1, Env, V1),
	eval(E2, Env, V2),
	eval_op(Op, V1, V2, Res).

% evaluare operatii aritmetice
eval(opAritm(Op, E1, E2), Env, Res) :-
	eval(E1, Env, V1),
	eval(E2, Env, V2),
	eval_op(Op, V1, V2, Res).

% evaluarea variabila
eval(var(V), Env, Value) :-
    get_from_context(V, Value, Env).

% evaluare numar
eval(num(N), _, N).

% calcul rezultat operatii
eval_op(==, A, B, Res) :- (A = B -> Res = 1;
								    Res = 0).

eval_op(<, A, B, Res) :- (A < B -> Res = 1;
								   Res = 0).
eval_op(+, A, B, Res) :- Res is (A + B).
eval_op(-, A, B, Res) :- Res is (A - B).
eval_op(*, A, B, Res) :- Res is (A * B).


% evaluare lista de programe

eval_progs([P|Ps], Env, Value) :-
	eval_prog(P, Env, NewEnv, CurrentValue),
	(
		CurrentValue \= 'x' -> Value = CurrentValue;
							   !, eval_progs(Ps,NewEnv,Value)
	).
eval_progs([], _, _).

% evaluarea unui program
eval_prog(empty_block, Env, Env, 'x').
eval_prog(asgn(V, E), Env, NewEnv, 'x') :-
	eval(E, Env, Value),
	add_to_context(V, Value, Env, NewEnv).
eval_prog(return(E), Env, Env, Value) :-
	eval(E, Env, Value).
eval_prog(if(Cond,S1,S2), Env, NewEnv, Value) :-
	eval(Cond,Env, CurrentValue), 
	(
		CurrentValue = 1 -> eval_prog(S1, Env, NewEnv, Value);
					 		eval_prog(S2, Env, NewEnv, Value)
	).
eval_prog(seq(S1, S2), Env, NewEnv, Value) :-
	eval_prog(S1, Env, Aux, Value),
	(
		S1 = return(_) -> NewEnv = Aux;
						  eval_prog(S2, Aux, NewEnv, Value)
	).
eval_prog(assert(Cond), Env, Env, Value) :-
	eval(Cond, Env, Res),
	(
		Res = 1 -> Value = 'x';
				   Value = 'a'
	).
eval_prog(for(Asgn, Cond, Inc, Body), Env, NewEnv, Value) :-
	eval_prog(Asgn, Env, AuxEnv, _),
	eval_for(Cond, Inc, Body, AuxEnv, NewEnv, _, Value).

eval_for(Cond, Inc, Body, Env, NewEnv, Aux, Value) :-
	eval(Cond, Env, Res),
	(
		Res = 1 -> eval_prog(Body, Env, Env1, CurrentValue),
				   eval_prog(Inc, Env1, Env2, _),
				   eval_for(Cond,Inc,Body, Env2, NewEnv, CurrentValue, Value);
				   NewEnv = Env, Value = Aux
	).