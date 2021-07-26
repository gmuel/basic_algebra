#!/usr/bin/make -f
CC=g++ -g -m64 -fPIC
INC=include/
LIB=lib/
BIN=bin/
OBJ=cycle
TAR=$(BIN)$(OBJ).so.1.0.1
all : $(TAR)

$(TAR) : $(BIN)$(OBJ).o
	$(CC) -shared $< -o $@

$(BIN)%.o : $(LIB)%.cpp $(INC)%.h
	$(CC) -c $< -o $@