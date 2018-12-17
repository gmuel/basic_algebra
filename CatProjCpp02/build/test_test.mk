BIN=../bin/
SRC=../src/
INC=$(SRC)include/
LIB=$(SRC)lib/
LIO=$(BIN)lib/
OBJ = $(LIO)test_list.o
TAR = $(BIN)test
CC=g++ -g
all : $(TAR)
.PHONY :$(OBJ)
$(TAR) : $(OBJ)
	$(CC) $< -o $@
$(OBJ) : $(LIB)test_list.cpp $(INC)test_util.hpp
	$(CC) -c -D _USE_LISTS_N_TESTS $< -o $@
clean:
	rm $(OBJ) 