#!/bin/bash
hm=$0
hm=${hm%"build_script.sh"}
echo $hm
${hm}hyperbools.mk && ${hm}cyclics_test.mk && pth=${hm%"build/"}lib/libhbools.so<<
