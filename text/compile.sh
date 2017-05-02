#! /bin/bash

sudo docker run --rm -i --user="$(id -u):$(id -g)" --net=none -v "$PWD/src":/data blang/latex /bin/sh -c "pdflatex content.tex content.pdf && pdflatex content.tex content.pdf"
