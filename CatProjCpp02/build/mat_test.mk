#!/usr/bin/make -f
BIN=$(PWD)/bin
SRC=$(PWD)/src
TAR=$(BIN)/mat_test
LIS=$(SRC)/lib
LIB=$(BIN)/lib
CYC=$(BIN)/lib/cyclics.o
MAI=$(SRC)/lib/mat_test.cpp
CYS=$(SRC)/lib/cyclics.cpp
CYH=$(SRC)/include/cyclics.hpp
COM=$(BIN)/lib/mat_test.o
CC=g++ -g
$(TAR) : $(COM) $(CYC)
	$(CC) $^ -o $@

$(CYC) : $(CYS) $(CYH)
	$(CC) -c $< -o $@ 
	
$(COM) : $(LIS)/mat_test.cpp $(SRC)/include/alg/1.1/algebra/algebra_matrix_base.hpp
	$(CC) -c $< -o $@