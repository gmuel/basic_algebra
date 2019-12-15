#!/bin/bash
hm=$0
hm=${hm%"build_script.sh"}
echo $hm
main=1
min=0
sub=1
HME=${HOME}/git/basic_algebra/CatProjCpp02
export main min sub HME
${hm}hyperbools.mk&& ${hm}cyclics_test.mk  


