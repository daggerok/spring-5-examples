FROM openjdk:8u131-jre-alpine
#FROM openjdk:8u151-jre-alpine
MAINTAINER Maksim Kostromin https://github.com/daggerok
RUN apk --no-cache --update add busybox-suid bash curl unzip sudo openssh-client shadow \
 && addgroup app-group \
 && echo "app ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers \
 && sed -i "s/.*requiretty$/Defaults !requiretty/" /etc/sudoers \
 && adduser -h /home/app -s /bin/bash -D -u 1025 app app-group \
 && usermod -a -G wheel app \
 && apk del busybox-suid unzip openssh-client shadow \
 && rm -rf /var/cache/apk/* /tmp/*
USER app
WORKDIR /home/app
VOLUME ["/home/app"]
ENTRYPOINT java -XX:+UnlockExperimentalVMOptions \
                -XX:+UseCGroupMemoryLimitForHeap \
                -XshowSettings:vm \
                -jar ./app.jar
CMD /bin/bash
EXPOSE 8080
HEALTHCHECK --timeout=10s \
            --retries=15 \
            CMD curl -f http://127.0.0.1:8080/health || exit 1
COPY ./build/libs/*.jar ./app.jar
