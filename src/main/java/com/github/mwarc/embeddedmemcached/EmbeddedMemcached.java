package com.github.mwarc.embeddedmemcached;

import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
@TestExecutionListeners({JMemcachedTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public @interface EmbeddedMemcached {

    String host() default "127.0.0.1";

    int port() default 11214;
}
