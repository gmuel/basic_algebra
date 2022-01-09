#!/usr/bin/make -f
BIN=bin/
SRC=src/
INC=$(SRC)include/
LIB=$(SRC)lib/
LIO=$(BIN)lib/
OBJ=$(LIO)sym_test.o
TAR=$(BIN)test
CC=g++ -g -m64

all : $(TAR)
.PHONY :$(OBJ)
$(TAR) : $(OBJ)
	$(CC) $< -o $@
$(OBJ) : $(LIB)sym_test.cpp $(INC)alg/1.1/group/group_symmetric_group.hpp
	$(CC) -c $< -o $@
clean:
	rm $(OBJ) 