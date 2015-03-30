package com.github.mwarc.embeddedmemcached.testutils;


import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactory;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

@Configuration
public class MemcachedConfiguration {

    private final String instances;
    private final long timeout;

    public MemcachedConfiguration() {
        instances = "127.0.0.1:11214";
        timeout = 1000;
    }

    public MemcachedConfiguration(String instances, long timeout) {
        this.instances = instances;
        this.timeout = timeout;
    }

    @Bean
    public MemcachedClient memcachedClient() throws IOException {
        return new MemcachedClient(connectionFactory(), getAddresses());
    }

    private ConnectionFactory connectionFactory() {
        ConnectionFactoryBuilder builder = new ConnectionFactoryBuilder();
        builder.setOpTimeout(timeout);
        return builder.build();
    }

    private List<InetSocketAddress> getAddresses() {
        return AddrUtil.getAddresses(instances);
    }
}
