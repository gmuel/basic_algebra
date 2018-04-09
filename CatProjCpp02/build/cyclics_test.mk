CC=g++ -g
BIN=bin/
SRC=src/
SRC1=$(SRC)include/
SRC2=$(SRC)lib/
HDR1=$(addprefix group_base, .hpp _impl.hpp)
HDR2=$(addprefix $(SRC1),  $(HDR1) n_dihedral.hpp)
OBJ=$(addprefix $(BIN), cyclics_test.o cyclics_fun.o cyclics.o)
$(BIN)cyclics_test : $(OBJ)
	$(CC) $^ -o $@
$(BIN)cyclics_test.o : $(SRC2)cyclics_test.cpp $(HDR2)
	$(CC) -c $< -o $@
$(BIN)%.o : $(SRC2)%.cpp $(SRC1)%.hpp
	$(CC) -c $< -o $@
#$(BIN)cyclics_fun.o : $(SRC2)cyclics_fun.cpp $(SRC1)cyclics_fun.hpp
#	 $(CC) -c $< -o $@
#$(BIN)cyclics.o : $(SRC2)cyclics.cpp $(SRC1)cyclics.hpp
#	$(CC) -c $< -o $@