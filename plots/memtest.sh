#! /bin/bash
set -e

sudo rm -rf /tmp/memoryUsage

cd ..

for scaleFactor in 0.05 0.10 0.15 0.20 0.25; do 

    sudo rm -rf /tmp/mss
    sudo docker kill tmp-mysql-diskusage || true
    sudo docker rm tmp-mysql-diskusage || true
    sudo docker run -d --name tmp-mysql-diskusage -v /tmp/mss:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=password -p 127.0.0.1:7777:3306 -ti mysql --max_allowed_packet=320M --secure-file-priv= --log-warnings=100
    sleep 10
    echo -n "$scaleFactor mysql xxx " >> /tmp/memoryUsage
    ./gradlew  -Psize="$scaleFactor" diskUsage 
    sudo du -sb /tmp/mss | awk '{print $1;}' >> /tmp/memoryUsage
    tail -1 /tmp/memoryUsage

done


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
