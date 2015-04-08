package com.github.mwarc.embeddedmemcached;

import net.spy.memcached.MemcachedClient;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.concurrent.Callable;

import static com.jayway.awaitility.Awaitility.await;
import static com.jayway.awaitility.Duration.FIVE_SECONDS;
import static com.jayway.awaitility.Duration.ONE_SECOND;
import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@TestExecutionListeners({JMemcachedTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
@EmbeddedMemcached(host = "127.0.0.1", port = 11214)
public class EmbeddedMemcachedTest {

    private final String key = "someKey";
    private final String value = "someValue";
    private final int expirationInSeconds = 1800;

    @Autowired
    private MemcachedClient memcachedClient;

    @After
    public void tearDown() {
        memcachedClient.delete(key);
    }

    @Test
    public void shouldNotFindValueInMemcached() {
        //when
        String value = (String)memcachedClient.get(key);

        //then
        assertEquals(null, value);
    }

    @Test
     public void shouldSaveValueAndRetrieveItFromMemcached() {
        //when
        memcachedClient.set(key, expirationInSeconds, value);

        //then
        await().atMost(ONE_SECOND).until(isValueInMemcached(value));
    }

    @Test
    public void shouldSaveValueAndNotRetrieveItAfterExpiredTime() {
        //when
        memcachedClient.set(key, 1, value);

        //then
        await().atMost(FIVE_SECONDS).until(isValueInMemcached(null));
    }

    @Test
    public void shouldDeleteValueFromMemcached() {
        //given
        memcachedClient.set(key, expirationInSeconds, value);

        //when
        memcachedClient.delete(key);

        //then
        await().atMost(ONE_SECOND).until(isValueInMemcached(null));
    }

    private Callable<Boolean> isValueInMemcached(final String value) {
        return new Callable<Boolean>() {
            public Boolean call() throws Exception {
                String valueFromMemcached = (String)memcachedClient.get(key);
                return (valueFromMemcached == null ? value == null : valueFromMemcached.equals(value));
            }
        };
    }
}
