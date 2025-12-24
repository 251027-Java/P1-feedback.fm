
destroy:
    docker rm -f -v jenkins jenkins-docker
    docker volume rm -f jenkins-data

init-network:
    docker network create jenkins

init-dind:
    docker run --name jenkins-docker -v jenkins-data:/var/jenkins --rm -d \
    --network jenkins --privileged --network-alias docker -e DOCKER_TLS_CERTDIR=/certs \
    -v jenkins-docker-certs:/certs/client -p 2376:2376 docker:dind

build-jenkins:
    docker build -t myjenkins:latest .

init-jenkins:
    docker run --name jenkins --network jenkins -e DOCKER_HOST=tcp://docker:2376 \
    -e DOCKER_CERT_PATH=/certs/client -e DOCKER_TLS_VERIFY=1 \
    -v jenkins-data:/var/jenkins_home -v jenkins-docker-certs:/certs/client:ro \
    -p 8080:8080 -p 50000:50000 myjenkins:latest 

init: init-dind build-jenkins init-jenkins

start:
    docker start jenkins

upload-config:
    docker cp jenkins.yaml jenkins:/var/jenkins_home/jenkins.yaml
