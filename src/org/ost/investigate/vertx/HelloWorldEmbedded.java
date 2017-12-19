package org.ost.investigate.vertx;

import io.vertx.core.Vertx;

public class HelloWorldEmbedded {
    public static void main(String[] args) {
        Vertx.vertx().createHttpServer().requestHandler(req -> req.response().end("Hello World!")).listen(8080);
    }
}
