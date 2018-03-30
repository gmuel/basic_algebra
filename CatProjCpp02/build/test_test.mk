BIN=bin/
SRC=src/
SRC1=$(SRC)include/
SRC2=$(SRC)lib/
CC=g++ -g
$(BIN)test : $(BIN)test.o $(BIN)cyclics_fun.o
	$(CC) $< -o $@
$(BIN)test.o : $(SRC2)test_list.cpp $(SRC1)test_util.hpp
	$(CC) -c $< -o $@
$(BIN)cyclics_fun.o : $(SRC2)cyclics_fun.cpp $(SRC1)cyclics_fun.hpp
	 $(CC) -c $< -o $@