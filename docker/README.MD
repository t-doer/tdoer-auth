# common/tdoer-auth

- Tag: tdoer/tdoer-auth:0.0.1
- Base Image: tdoer/alpine-openjdk8-jre-java:1.0.0

## Build

Example version: 1.0.0

```
docker build -t tdoer/tdoer-auth:0.0.1 .
```

## Build and Push

 Usage: 
 
 ```
 ./build.sh <VERSION>
 ```
 
 Example:
  
 ```
 ./build.sh 0.0.1
 ```

## Run    

```
docker run --name tdoer-auth -p 7080:7080 -p 7081:7081 -d tdoer/tdoer-auth:0.0.1
```
