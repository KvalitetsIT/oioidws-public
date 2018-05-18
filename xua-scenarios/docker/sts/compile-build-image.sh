(cd ../../sts/; mvn clean install;)
cp ../../sts/target/sts-1.0.0.jar deploy/
docker build -t sts .
