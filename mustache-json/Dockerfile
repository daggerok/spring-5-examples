FROM openjdk:8u151-jre-alpine
MAINTAINER Maksim Kostromin https://github.com/daggerok
RUN apk --no-cache --update add busybox-suid bash curl unzip sudo openssh-client shadow \
 && addgroup appuser-group \
 && echo "appuser ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers \
 && sed -i "s/.*requiretty$/Defaults !requiretty/" /etc/sudoers \
 && adduser -h /home/appuser -s /bin/bash -D -u 1025 appuser appuser-group \
 && usermod -a -G wheel appuser \
 && apk del busybox-suid unzip openssh-client shadow \
 && rm -rf /var/cache/apk/* /tmp/*
USER appuser
WORKDIR /home/appuser
VOLUME ["/home/appuser"]
ENTRYPOINT java -XX:+UnlockExperimentalVMOptions \
                -XX:+UseCGroupMemoryLimitForHeap \
                -XshowSettings:vm \
                -jar ./app.jar
CMD /bin/bash
EXPOSE 8080
HEALTHCHECK --interval=1s \
            --timeout=1s \
            --retries=33 \
            CMD curl -f http://127.0.0.1:8080/ || exit 1
COPY --chown=appuser ./build/libs/*.jar ./app.jar
