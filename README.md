##Building
####Prerequisites
 * Maven 3.6 (tested on 3.6.3)
 * Java 11 (tested on OpenJdk 11)

Despite the Java 11 requirement, the source code should work under Java 8. This was made just to make the project to use the latest Java LTS release. To run it under Java 8 just replace
```xml
        <java.version>11</java.version>
```
with
```xml
        <java.version>1.8</java.version>
```
in `pom.xml`.
#### Building the project
To build just run the maven as
```bash
mvn clean package
```

##Running
After you've built the project run it as an executable jar:
```bash
java -jar target/the_thing-1.0.0.jar
```
The just follow the instructions you see in the console.