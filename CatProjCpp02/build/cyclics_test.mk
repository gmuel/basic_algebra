#!/usr/bin/make -f
CC=g++ -g
BIN=bin/
SRC=src/
INC=$(SRC)include/
LIB=$(SRC)lib/
LIO=$(BIN)lib/
HDR1=$(addprefix group_base, .hpp _impl.hpp)
HDR2=$(addprefix $(INC),  $(HDR1) n_dihedral.hpp)
OBJ=$(addprefix $(LIO), cyclics_test.o cyclics_fun.o cyclics.o)
$(BIN)cyclics_test : $(OBJ)
	$(CC) $^ -o $@
$(LIO)cyclics_test.o : $(LIB)cyclics_test.cpp $(HDR2)
	$(CC) -c $< -o $@
$(LIO)%.o : $(LIB)%.cpp $(INC)%.hpp
	$(CC) -c $< -o $@
#$(BIN)cyclics_fun.o : $(LIB)cyclics_fun.cpp $(INC)cyclics_fun.hpp
#	 $(CC) -c $< -o $@
#$(BIN)cyclics.o : $(LIB)cyclics.cpp $(INC)cyclics.hpp
#	$(CC) -c $< -o $@