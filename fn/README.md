
```bash
./mvnw -f fn/pom.xml >/dev/null
java -jar fn/target/*.jar
http :8080/person id=1 name=ololo
http :8080/person name=trololo
http :8080/person/1
http :8080/person/
```
