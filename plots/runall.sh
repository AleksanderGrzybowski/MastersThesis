#! /bin/bash
set -e

for name in CrossJoinAndFilter FilteringAndCountingIntegers GroupingAndSumming HugeJoining SummingIntegers; do

cd ..
./gradlew clean jmh -Pinclude="${name}.*"
cd plots
./test.py ../build/reports/jmh/results.txt log "${name}"

done
