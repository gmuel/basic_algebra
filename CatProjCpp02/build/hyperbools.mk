SRC=src
SRC1=$(SRC)/include
SRC2=$(SRC)/lib
BIN=bin
CC=g++
OBJ=$(BIN)/hbool.o
TAR=$(BIN)/libhbools.so
all : $(TAR)

$(TAR) : $(OBJ)
	$(CC) -shared -fPIC $< -o $@

$(OBJ) : $(SRC2)/hyperbools.cpp $(SRC1)/hyperbools.hpp
	$(CC) -fPIC -c $< -o $@
