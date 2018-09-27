FROM healthcheck/rabbitmq:alpine
MAINTAINER Maksim Kostromin <daggerok@gmail.com>
RUN rabbitmq-plugins enable rabbitmq_management rabbitmq_stomp --offline
EXPOSE 5672 15672
