#!/usr/bin/make -f
CC=g++ -g -m64
INC=src/include/
LIB=src/lib/
BIN=bin/
BLB=$(BIN)lib/
OBJ=group_cycle
OBC=test_cycle
HOM=/home/iworker/git/basic_algebra/CatProjCpp02/
LIP=$(HOM)$(BLB)
LIO=$(BLB)lib$(OBJ).so.1.0.1
TAR=$(BIN)$(OBC)
all : $(TAR)

$(TAR) : $(BIN)$(OBC).o $(LIO)
	$(CC) $< -L$(LIP) -Wl,-rpath=$(LIP) -l$(OBJ) -o $@
$(LIO) : $(BIN)$(OBJ).o
	$(CC) -fPIC -shared $< -o $@
#
$(BIN)$(OBJ).o : $(LIB)$(OBJ).cpp #  $(INC)$(OBJ).hpp
	$(CC) -fPIC -c $< -o $@
$(BIN)$(OBC).o : $(LIB)$(OBC).cpp #  $(INC)$(OBJ).hpp
	$(CC) -c $< -o $@
	