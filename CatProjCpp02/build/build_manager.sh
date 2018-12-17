#!/bin/bash
# script variables
# list of make files
lst=
# success flag
sccss=0
# failure make file
mkfail=
opts=
remklst=
ignore=
args="$*"
# helper function
help_text(){
	echo "build_manager.sh [options] [make_file(s)]"
	echo
	echo " options:"
	echo "		-h, --help	print this message"
	echo "		-v			verbose, print all"
	echo "		-n 			only test run make -f "
	echo "					process, checks the"
	echo "					return status of make"
	echo "		-i			ignore errors in make"
	echo " make_files:"
	echo "		- 	list of *.mk - files, if empty all"
	echo "			make files in build/ are run"
	echo 

}
opt_err(){
	echo "Unsupported option(s): $*"
	echo "Aborting..."	
}
get_args(){
	#for arg in "$*"
	#do
		opts=$(echo "$args" | grep "-") #) && continue
	
		#ignore=$(echo "$opts" | grep '-i')
		lst=$(echo "$args" | grep '.mk') #&& continue
	#done
	echo $opts
	echo $lst
}
no_args(){
	(( list_makes && test_make ))
	if [ $sccss == 1 ]; then
		echo "the following list of make files require a remake:"
		for i in $remklst; do
			echo "$i"
		done
	fi
}
list_makes() {
	lst=$(ls *.mk)
	sccss=$?
}
test_make() {
	for i in $lst; do
		make -n -f $i
		sccss = $?
		if [ $sccss == 2 ]; then
			mkfail = $i
			[ "$ignore" == "" ] && break
		elif [ $sccss == 1 ]; then
			remklst += $i 
		fi
	done
}
scripty(){
	get_args $*
	[ "$lst" == "" ] && no_args
}
main(){
	case "$1" in 
		 "-h"|"--help" )
			help_text
			;;
		* )
			get_args $*
			;;
	esac
	
	exit 0
}
get_args $*
#main $*