package org.gy.demo.mixin;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.jackson.JacksonMixin;
import org.gy.demo.entity.HelloWorldNew;

@JacksonMixin(HelloWorldNew.class)
public abstract class HelloWorldMixin {

    @JsonProperty("username")
    private String name;
}
