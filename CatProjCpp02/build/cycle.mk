#!/usr/bin/make -f
CC=g++ -g -m64 -fPIC
INC=src/include/
LIB=src/lib/
BIN=bin/
BLB=$(BIN)lib/
OBJ=group_cycle
TAR=$(BLB)lib$(OBJ).so.1.0.1
# all : $(TAR)

$(TAR) : $(BIN)$(OBJ).o
	$(CC) -shared $< -o $@
# 
$(BIN)$(OBJ).o : $(LIB)$(OBJ).cpp #  $(INC)$(OBJ).hpp
	$(CC) -c $< -o $@