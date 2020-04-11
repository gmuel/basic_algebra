#!/usr/bin/make -f
BIN=$(PWD)bin
SRC=$(PWD)src
TAR=$(BIN)/ring_test
LIS=$(SRC)/lib
LIB=$(BIN)/lib
CYC=$(BIN)/lib/cyclics.o
MAI=$(SRC)/lib/mat_test.cpp
CYS=$(SRC)/lib/cyclics.cpp
CYH=$(SRC)/include/cyclics.hpp
COM=$(BIN)/lib/ring_test.o
CC=g++ -g
$(TAR) : $(COM) $(CYC)
	$(CC) $^ -o $@

$(CYC) : $(CYS) $(CYH)
	$(CC) -c $< -o $@ 
	
$(COM) : $(LIS)/ring_test.cpp $(SRC)/include/ring_base_impl.hpp
	$(CC) -c $< -o $@