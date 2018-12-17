#!/bin/bash
hm=$0
hm=${hm%"build_script.sh"}

${hm}hyperbools.mk && ${hm}cyclics_test.mk
