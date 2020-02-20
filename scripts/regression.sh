#!/bin/bash

if [[ "$#" != "3" || "$1" == "--help" || "$1" == "-h" ]]; then
    echo "Usage: regression.sh <ligtbeam_output_cur> <ligtbeam_output_prev> <results>"
    exit 1
fi

echo "===========getting results from $1=============="
grep "^avg" $1 | sed s/[a-zA-Z_\ :"\t"]*//g > cur.txt
grep "^##" $1 | cut -d "'" -f 2 | sed "s/'/,/g" > test_names.txt
cat cur.txt

echo "===========getting results from $2 =============="
grep "^avg" $2 | sed s/[a-zA-Z_\ :"\t"]*//g > prev.txt
cat prev.txt

echo "===========combining them=============="
paste -d "," cur.txt prev.txt > comb.txt
cat comb.txt

echo "===========comparing=============="
awk -F"," '{ if ($1>$5+$5*0.1) {print "* ref("$5"),\tavg "$1",\tratio "$1/$5} else {print "  ref("$5"),\tavg "$1",\tratio "$1/$5} }' comb.txt > comp.txt
grep "^avg" $1 | sed 's/ \+ /\t/g' | rev | cut -f 1 | rev > u.txt
paste -d '\t' comp.txt u.txt > $3

paste -d";" $3 test_names.txt 2>&1 | (
    while IFS="$(printf ';')"  read -r s testname; do
        echo $testname
        duration=`echo "$s" | cut -f 2 | tr -d "[:space:]" | tr -d ','`
        duration=${duration#"avg"}
        failed=`echo "$s" | cut -c1 | grep -c "*"`
        passed=`echo "$s" | cut -c1 | grep -c " "`
        state=0
        [ $passed -eq 1 ] && state=1
        [ $failed -eq 1 ] && state=2
        echo \#\#teamcity[testStarted name=\'$testname\']
        echo "$s"
        [ $state -eq 2 ] && echo \#\#teamcity[testFailed name=\'$testname\' message=\'$s\']
        echo \#\#teamcity[testFinished name=\'$testname\' duration=\'$duration\']
        failed=0
        passed=0
    done
)

cat $3 | tr '\t' ';' | column -t -s ';' | tee $3

rm cur.txt
rm test_names.txt
rm prev.txt
rm comb.txt
rm comp.txt
rm u.txt

