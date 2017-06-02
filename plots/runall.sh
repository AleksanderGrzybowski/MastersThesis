#! /bin/bash
set -e

for name in CrossJoinAndFilter FilteringAndCountingIntegers GroupingAndSumming SummingIntegers SortingStrings; do

cd ..
./gradlew clean jmh -Pinclude="${name}.*"
cd plots
./test.py ../build/reports/jmh/results.txt log "${name}"

done

for name in Tpc1Benchmark Tpc4Benchmark Tpc6Benchmark Tpc12Benchmark Tpc14Benchmark ; do

cd ..
./gradlew clean jmh -Pinclude="${name}.*"
cd plots
./test.py ../build/reports/jmh/results.txt lin "${name}"

done
