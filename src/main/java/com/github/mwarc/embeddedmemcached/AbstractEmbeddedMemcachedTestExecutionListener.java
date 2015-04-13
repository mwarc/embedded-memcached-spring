package com.github.mwarc.embeddedmemcached;

import com.google.common.base.Preconditions;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class AbstractEmbeddedMemcachedTestExecutionListener extends AbstractTestExecutionListener {

    private static boolean initialized;
    private MemcachedServer server;

    public AbstractEmbeddedMemcachedTestExecutionListener(MemcachedServer server) {
        this.server = server;
    }

    protected void startServer(TestContext testContext) {
        EmbeddedMemcached embeddedMemcached = Preconditions.checkNotNull(
            AnnotationUtils.findAnnotation(testContext.getTestClass(), EmbeddedMemcached.class),
            "EmbeddedMemcachedTestExecutionListener must be used with @EmbeddedMemcached on "
                    + testContext.getTestClass()
        );
        String host = Preconditions.checkNotNull(embeddedMemcached.host(), "@EmbeddedMemcached host must not be null");
        int port = Preconditions.checkNotNull(embeddedMemcached.port(), "@EmbeddedMemcached port must not be null");
        Preconditions.checkArgument(port > 0, "@EmbeddedMemcached port must not be > 0");

        if (!initialized) {
            server.start(host, port);
            initialized = true;
        }
    }

    protected void cleanServer() {
        server.clean();
    }
}
