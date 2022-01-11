#!/bin/bash

dir=`pwd`

RED=RED
NC=
#RED='\033[0;31m'
#NC='\033[0m' # No Color

depotDir=$dir/depot
if [[ "inJekins" == "$1" ]]; then
  echo "rasign the depot dir for Jenkis which can't find it's own $BUILD_NUMBER variable!"
  cd ..
  upDir=`pwd`
  depotDir=$upDir/depot
  cd $dir
else 
  echo "Using the depot directory in the current dir."
fi

function build_project() {
  echo building $1 with version $2  
  if [[ -d $dir/$1 ]]; then
    echo "" #the dir exists
  else
    echo -e "${RED} The directory $DIR/$1 does NOT Exist ${NC}"
    echo "Please run gitsub.sh first"
    exit 0
  fi
  cd $dir/$1
  status=$?
  gradle publishToMavenLocal -Ptag=$2
  status=$?
  if [[ "0" != "$status" ]]; then
   echo -e "${RED} Build.sh Failed with exit code $status ${NC}"
   exit 0;
  fi
}

function run_tests() {
  echo running tests in $1 
  cd $dir/$1
  status=$?
  gradle build
  status=$?
  if [[ "0" != "$status" ]]; then
   echo -e "${RED} Build.sh Failed with exit code $status ${NC}"
   exit 0;
  fi
  echo "copying tests to $depotDir/tests "
  cp build/test-results/test/*.xml $depotDir/tests
}

# A Fabricate Style Depot :)
# Without the Fabricate Style Concurrency :(
if [[ -d $depotDir ]]; then
  rm -fr $depotDir
fi
mkdir $depotDir
mkdir $depotDir/tests

build_project i_tests4j.adligo.org v0_1+_SNAPSHOT
build_project i_pipe.adligo.org v0_4+_SNAPSHOT

build_project mockito_ext.adligo.org v0_1+_SNAPSHOT
build_project tests4j4jj.adligo.org v0_1+_SNAPSHOT
run_tests tests4j4jj_tests.adligo.org

build_project pipe.adligo.org v0_1+_SNAPSHOT
run_tests pipe_tests.adligo.org 
