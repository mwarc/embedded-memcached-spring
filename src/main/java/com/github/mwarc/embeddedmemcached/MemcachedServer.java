package com.github.mwarc.embeddedmemcached;

public interface MemcachedServer {

    void start(String host, int port);

    void clean();
}
