FROM openjdk:8u151-jre-alpine3.7
MAINTAINER Maksim Kostromin https://github.com/daggerok
RUN apk --no-cache add busybox-suid bash curl sudo \
 && adduser -h /home/appuser -s /bin/bash -D -u 1025 appuser wheel \
 && echo "appuser ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers \
 && sed -i "s/.*requiretty$/Defaults !requiretty/" /etc/sudoers \
 && apk del busybox-suid \
 && rm -rf /tmp/* /var/cache/apk/*
USER appuser
WORKDIR /home/appuser
VOLUME /home/appuser
ENTRYPOINT java -XX:+UnlockExperimentalVMOptions \
                -XX:+UseCGroupMemoryLimitForHeap \
                -XshowSettings:vm \
                -jar ./app.jar
CMD /bin/bash
EXPOSE 8080
HEALTHCHECK --timeout=1s \
            --retries=35 \
            CMD curl -f http://127.0.0.1:8080/actuator/health || exit 1
COPY --chown=appuser ./build/libs/*.jar ./app.jar
