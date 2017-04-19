#! /bin/bash

ssh root@192.168.8.2 'rm -rf /tmp/src'
scp -r src root@192.168.8.2:/tmp/
ssh root@192.168.8.2 'bash -c "cd /tmp/src && pdflatex content.tex content.pdf && pdflatex content.tex content.pdf"'
scp -r root@192.168.8.2:/tmp/src/content.pdf .

