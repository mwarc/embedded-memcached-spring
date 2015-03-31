# embedded-memcached-spring

[![Build Status](https://travis-ci.org/mwarc/embedded-memcached-spring.svg?branch=master)](https://travis-ci.org/mwarc/embedded-memcached-spring)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mwarc/embedded-memcached-spring/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mwarc/embedded-memcached-spring)

## Overview

The Spring TestContext Framework provides annotation-driven unit and integration testing support.

embedded-memcached-spring provides a way to use Jmemcache-daemon with Spring TestContext Framework.

## Annotation

The magic is in EmbeddedMemcachedDiIntegrationTestExecutionListener class which implments TestExecutionListener. This listener find EmbeddedMemcached annotation:

    @EmbeddedMemcached: annotation to start an embedded memcached server
    
*   host: host of memcached server; "127.0.0.1" by default
*   port: port of memcached server; 11214 by default

## Configuration

In your pom.xml, you have to add embedded-memcached-spring maven dependency:

    <dependency>
        <groupId>com.github.mwarc</groupId>
        <artifactId>embedded-memcached-spring</artifactId>
        <version>0.1.2</version>
    </dependency>


or when you use gradle add to build.gradle:

    dependencies {
        compile 'com.github.mwarc:embedded-memcached-spring:0.1.2'
    }

## Use cases

The following snippet use basic Spring configuration and @EmbeddedMemcached. 
EmbeddedMemcachedDiIntegrationTestExecutionListener find @EmbeddedMemcached annotation 
and try to start an embedded memcached server with default configuration.

### JUnit:
```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@TestExecutionListeners({
    EmbeddedMemcachedDiIntegrationTestExecutionListener.class,
    DependencyInjectionTestExecutionListener.class})
@EmbeddedMemcached(host = "127.0.0.1", port = 11214)
public class EmbeddedMemcachedTest {
    @Test
    public void shouldNotFindValueInMemcached() {
        ...
    }
```

### Spock:
```groovy
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@TestExecutionListeners([
        EmbeddedMemcachedDiIntegrationTestExecutionListener,
        DependencyInjectionTestExecutionListener])
@EmbeddedMemcached
class EmbeddedMemcachedSpec extends Specification {
    def "should save value and retrieve it from memcached"() {
        ...
    }
}
```

## Building

* Clone the repository
* Run `./gradlew clean build` (on Linux/Mac) or `gradlew.bat clean build` (on Windows)

## Licence

Apache Licence v2.0 (see LICENCE.txt)