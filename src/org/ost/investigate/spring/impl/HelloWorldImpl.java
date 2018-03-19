package org.ost.investigate.spring.impl;

import org.ost.investigate.spring.HelloWorld;

public class HelloWorldImpl implements HelloWorld {

    public void sayHello(String name) {
        System.out.println("Hello "+name);
    }

}