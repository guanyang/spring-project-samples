package org.gy.demo.nativesample.mixin;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.gy.demo.nativesample.entity.HelloWorldNew;
import org.springframework.boot.jackson.JsonMixin;

@JsonMixin(HelloWorldNew.class)
public abstract class HelloWorldMixin {

    @JsonProperty("username")
    private String name;
}
