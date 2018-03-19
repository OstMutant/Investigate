package org.ost.investigate.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * http://websystique.com/spring/spring-4-hello-world-example-annotation-tutorial-full-example/
 */
public class AppMain {

    public static void main(String args[]) {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(HelloWorldConfig.class);
        HelloWorld bean = (HelloWorld) context.getBean("helloWorldBean");
        bean.sayHello("Spring 4");
        context.close();
    }
}
