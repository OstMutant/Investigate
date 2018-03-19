package org.ost.investigate.spring;

import org.ost.investigate.spring.impl.HelloWorldImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

@Configuration
public class HelloWorldConfig {

    @Bean(name="helloWorldBean")
    @Description("This is a sample HelloWorld Bean")
    public HelloWorld helloWorld() {
        return new HelloWorldImpl();
    }

}
