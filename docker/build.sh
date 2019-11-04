#! /bin/bash

set -eo pipefail

if [ $# -ne 1 ]; then
    echo "Usage: ./build.sh <VERSION>"
    echo "Example: ./build.sh 1.0.0"
    echo """Note: Jenkins will call the command to build and pull image to Docker Registry,
      and the 'VERSION' is calculated from Git branch or tag name selected"""
    exit 1
fi


JAR="../target/tdoer-auth-*.jar"

echo "Target jar is: $JAR,  and version is: $1"

# copy target jar file into the Dockerfile's current directory
echo "Step 1: Copy $JAR into $(pwd)"
sh -c "cp -fr "$JAR" $(pwd)"

if [ $? -eq 0 ]; then
     echo "DONE"
  else
     echo "Failed"
     exit 1
fi

IMAGE_TAG="tdoer/tdoer-auth:$1"

echo "Step 2: Build image: ${IMAGE_TAG}"
docker build -t ${IMAGE_TAG} .

if [ $? -eq 0 ]; then
     echo "DONE"
  else
     echo "Failed"
     exit 1
fi

echo "Step 3: Push image to docker registry"
docker push ${IMAGE_TAG}

if [ $? -eq 0 ]; then
     echo "DONE"
  else
     echo "Failed"
     exit 1
fi

echo "Step 4: Remove $(pwd)/$(basename ${JAR})"
rm -rf $(pwd)/$(basename ${JAR})
echo "Done"