#!/bin/bash

set -e

if ! [ -f "$1" ]; then
  echo "no file"
  exit 1;
fi

ssh tess mkdir -p rs
scp $1 tess:rs/img.jpg 1>-
ssh tess bash -c "'
tesseract -l ces rs/img.jpg ans 2>-
cat ans.txt
rm ans.txt
rm -rf rs
'"

#ssh univm mkdir -p rs
#scp $1 univm:rs/img.jpg 1>-
#ssh univm bash -c "'
#export TESSDATA_PREFIX="/home/vagrant/tesseract-3.03/tessdata"
#tesseract -l ces rs/img.jpg ans 2>-
#cat ans.txt
#rm ans.txt
#rm -rf rs
#'"

#scp $1 univm:rs/img.jpg 2>-
#ssh univm TESSDATA_PREFIX="/home/vagrant/tesseract-3.03/tessdata" tesseract -l ces rs/img.jpg ans 2>-
#ssh univm cat ans.txt
#ssh univm rm ans.txt
#ssh univm rm -rf rs
