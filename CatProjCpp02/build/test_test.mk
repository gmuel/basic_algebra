BIN=bin/
SRC=src/
SRC1=$(SRC)include/
SRC2=$(SRC)lib/

$(BIN)test : $(SRC2)test_list.cpp $(SRC1)test_util.hpp
	g++ -g $< -o $@