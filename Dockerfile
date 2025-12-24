FROM jenkins/jenkins:latest

USER root

RUN apt-get update && apt-get install -y lsb-release

RUN curl -fsSLo /usr/share/keyrings/docker-archive-keyring.asc \
    https://download.docker.com/linux/debian/gpg
RUN echo "deb [arch=$(dpkg --print-architecture) \
    signed-by=/usr/share/keyrings/docker-archive-keyring.asc] \
    https://download.docker.com/linux/debian \
    $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list

RUN apt-get update && apt-get install -y docker-ce-cli

USER jenkins

RUN jenkins-plugin-cli --plugins adoptopenjdk docker-workflow configuration-as-code \
    build-timeout github-branch-source workflow-aggregator pipeline-graph-view timestamper \
    ws-cleanup pipeline-graph-view matrix-auth ldap email-ext dark-theme maven-plugin