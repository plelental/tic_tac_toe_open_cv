language: java
jdk:
  - oraclejdk8
script:
  - mvn test
- mvn install -DskipTests=true -Dmaven.javadoc.skip=true -B -V
