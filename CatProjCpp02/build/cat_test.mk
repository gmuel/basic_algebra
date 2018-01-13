SRC1=../src/include/
SCR2=../src/lib/
CC  = g++

bin/test : $(SRC2)cat_test.cpp
	$(CC) $< -o $@
