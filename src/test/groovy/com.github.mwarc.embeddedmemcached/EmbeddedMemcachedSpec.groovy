package com.github.mwarc.embeddedmemcached

import net.spy.memcached.MemcachedClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import static com.jayway.awaitility.Awaitility.await
import static com.jayway.awaitility.Duration.FIVE_SECONDS
import static com.jayway.awaitility.Duration.ONE_SECOND

@ContextConfiguration(locations = "classpath:applicationContext.xml")
@EmbeddedMemcached
class EmbeddedMemcachedSpec extends Specification {

    def key = "someKey"
    def value = "someValue"
    def expirationInSeconds = 1800

    @Autowired
    MemcachedClient memcachedClient

    def cleanup() {
        memcachedClient.delete(key)
    }

    def "should not find value in memcached"() {
        when:
        def value = memcachedClient.get(key)

        then:
        !value
    }

    def "should save value and retrieve it from memcached"() {
        when:
        memcachedClient.set(key, expirationInSeconds, value)

        then:
        await().atMost(ONE_SECOND).until {
            assert memcachedClient.get(key) == value
        }
    }

    def "should save value and not retrieve it after expired time"() {
        when:
        memcachedClient.set(key, 1, value)

        then:
        await().atMost(FIVE_SECONDS).until {
            assert !memcachedClient.get(key)
        }
    }

    def "should delete value from memcached" () {
        given:
        memcachedClient.set(key, expirationInSeconds, value)

        when:
        memcachedClient.delete(key)

        then:
        await().atMost(ONE_SECOND).until {
            assert !memcachedClient.get(key)
        }
    }
}
