#!/usr/bin/make -f

#change permissions to 7** to use mk-file as script
HME=${HOME}/git/basic_algebra/CatProjCpp02
SRC=${HME}/src
INC=$(SRC)/include
LIB=$(SRC)/lib
BIN=${HME}/bin
LIO=$(BIN)/lib
CC=g++ -g
OBJ=$(LIO)/hbool.o
TAR=$(LIO)/libhbools.so
TAR1=$(TAR).$(main)
TAR2=$(TAR1).$(min)
TAR3=$(TAR2).$(sub)
all : $(TAR)
.PHONY : clean
$(TAR) : $(TAR1)
	[ -f $@ ] && rm $@; ln -s $< $@
$(TAR1) : $(TAR2)
	[ -f $@ ] && rm $@; ln -s $< $@
$(TAR2) : $(TAR3)
	[ -f $@ ] && rm $@; ln -s $< $@
$(TAR3) : $(OBJ)
	$(CC) -shared -fPIC $< -o $@
$(OBJ) : $(LIB)/hyperbools.cpp $(INC)/hyperbools.hpp
	$(CC) -fPIC -c $< -o $@
