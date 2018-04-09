BIN=../bin/
SRC=../src/
SRC1=$(SRC)include/
SRC2=$(SRC)lib/
OBJ = $(BIN)test_list.o
TAR = $(BIN)test
CC=g++ -g
all : $(TAR)
.PHONY :$(OBJ)
$(TAR) : $(OBJ)
	$(CC) $< -o $@
$(OBJ) : $(SRC2)test_list.cpp $(SRC1)test_util.hpp
	$(CC) -c -D _USE_LISTS_N_TESTS $< -o $@
clean:
	rm $(OBJ) 