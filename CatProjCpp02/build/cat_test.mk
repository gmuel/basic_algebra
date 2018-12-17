#SRC1=src/include/
SRC=src/lib
CC=g++

bin/test : bin/cat_test.o
	$(CC) $< -o $@
bin/cat_test.o : $(SRC)/cat_test.cpp
	$(CC) -c $< -o $@