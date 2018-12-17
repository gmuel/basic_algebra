#!/usr/bin/make -f

#change permissions to 7** to use mk-file as script

SRC=src
INC=$(SRC)/include
LIB=$(SRC)/lib
BIN=bin
LIO=$(BIN)/lib
CC=g++
OBJ=$(LIO)/hbool.o
TAR=$(LIO)/libhbools.so
all : $(TAR)

$(TAR) : $(OBJ)
	$(CC) -shared -fPIC $< -o $@

$(OBJ) : $(LIB)/hyperbools.cpp $(INC)/hyperbools.hpp
	$(CC) -fPIC -c $< -o $@
