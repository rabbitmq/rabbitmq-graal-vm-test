# RabbitMQ GraalVM Test

RabbitMQ Java libraries test on [GraalVM](https://www.graalvm.org/).

## RabbitMQ AMQP Java Client

The test consists in a simple RPC request-reply scenario that builds
on top [RabbitMQ AMQP Java client](https://github.com/rabbitmq/rabbitmq-java-client).
The RPC server starts as a traditional Java application and the RPC client is a native
image built with GraalVM.

Pre-requisites:
 * Local RabbitMQ broker running with all the defaults
 * [GraalVM](https://www.graalvm.org/) installed

Built the project:

    ./mvnw clean package

Start the RPC server:

    ./mvnw compile exec:java

In another shell, build the native image:

    native-image -jar target/rabbitmq-graal-vm-test-full.jar

Launch the RPC client with the native image:

    ./rabbitmq-graal-vm-test-full

This should output in the console:

    Using RabbitMQ AMQP Client 5.5.0
    Sending: hello
    Received: *** hello ***

Check in the first shell that the RPC server exited after it processed
the request.

It is possible to use another version of the AMQP Java client:

    ./mvnw clean package -Damqp-client.version=5.4.3
    

## License ##

Licensed under the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html).

_Sponsored by [Pivotal](http://pivotal.io)_
