mvn clean compile install
copy target\axis2ws-client-jar-sample-0.1.0-SNAPSHOT.jar local\jar
cd local
sbt publishLocal