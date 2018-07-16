FROM openjdk:8

WORKDIR /home
COPY /deploy /home
RUN chmod +x /home/run.sh
RUN chmod +x /home/wait-for-it.sh
EXPOSE 8181

ENTRYPOINT ["/bin/bash", "run.sh"]

