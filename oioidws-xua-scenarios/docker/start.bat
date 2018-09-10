docker-compose down

call mvn clean install -U -f ..\SAML-H\pom.xml
call mvn clean install -U -f ..\sts\pom.xml

copy ..\sts\target\sts-1.0.0.jar sts\deploy

docker build -t sts sts
docker build -t idp idp

docker-compose up
