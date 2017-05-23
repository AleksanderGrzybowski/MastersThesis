#! /bin/bash
set -e

rm -rf /tmp/memoryUsage

cd ..

echo -n "0.05 store " >> /tmp/memoryUsage
./gradlew  -Psize="0.05" -Ptype="store" memoryUsage   | grep Usage: >> /tmp/memoryUsage
tail -1 /tmp/memoryUsage

echo -n "0.10 store " >> /tmp/memoryUsage
./gradlew  -Psize="0.10" -Ptype="store" memoryUsage   | grep Usage: >> /tmp/memoryUsage
tail -1 /tmp/memoryUsage

echo -n "0.15 store " >> /tmp/memoryUsage
./gradlew  -Psize="0.15" -Ptype="store" memoryUsage   | grep Usage: >> /tmp/memoryUsage
tail -1 /tmp/memoryUsage

echo -n "0.20 store " >> /tmp/memoryUsage
./gradlew  -Psize="0.20" -Ptype="store" memoryUsage   | grep Usage: >> /tmp/memoryUsage
tail -1 /tmp/memoryUsage

echo -n "0.25 store " >> /tmp/memoryUsage
./gradlew  -Psize="0.25" -Ptype="store" memoryUsage   | grep Usage: >> /tmp/memoryUsage
tail -1 /tmp/memoryUsage

echo -n "0.30 store " >> /tmp/memoryUsage
./gradlew  -Psize="0.30" -Ptype="store" memoryUsage   | grep Usage: >> /tmp/memoryUsage
tail -1 /tmp/memoryUsage



echo -n "0.05 h2 " >> /tmp/memoryUsage
./gradlew  -Psize="0.05" -Ptype="h2" memoryUsage   | grep Usage: >> /tmp/memoryUsage
tail -1 /tmp/memoryUsage

echo -n "0.10 h2 " >> /tmp/memoryUsage
./gradlew  -Psize="0.10" -Ptype="h2" memoryUsage   | grep Usage: >> /tmp/memoryUsage
tail -1 /tmp/memoryUsage

echo -n "0.15 h2 " >> /tmp/memoryUsage
./gradlew  -Psize="0.15" -Ptype="h2" memoryUsage   | grep Usage: >> /tmp/memoryUsage
tail -1 /tmp/memoryUsage

echo -n "0.20 h2 " >> /tmp/memoryUsage
./gradlew  -Psize="0.20" -Ptype="h2" memoryUsage   | grep Usage: >> /tmp/memoryUsage
tail -1 /tmp/memoryUsage

echo -n "0.25 h2 " >> /tmp/memoryUsage
./gradlew  -Psize="0.25" -Ptype="h2" memoryUsage   | grep Usage: >> /tmp/memoryUsage
tail -1 /tmp/memoryUsage

echo -n "0.30 h2 " >> /tmp/memoryUsage
./gradlew  -Psize="0.30" -Ptype="h2" memoryUsage   | grep Usage: >> /tmp/memoryUsage
tail -1 /tmp/memoryUsage
