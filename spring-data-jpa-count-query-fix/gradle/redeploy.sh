#!/usr/bin/env bash
# ./grdlew clean war; bash ./gradle/redeploy.sh
container=jax-rs-hateoas-links_gradle-jax-rs-hateoas-links-app_1
docker cp ./build/libs/*.war ${container}:/home/jboss-eap-7.1/jboss-eap-7.1/standalone/deployments/app.war
