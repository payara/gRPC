# Payara gRPC implementation

This is the repo for the Payara gRPC implementation. To use gRPC follow the next steps
depending of the version you are using:

## Community

In order to use the gRPC implementation you must build this project:

```
mvn clean install
```

The resulted jar file with name grpc-1.0.0-SNAPSHOT.jar must be included in the 
war application with the generated stubs from the client implementation. If you want to see more details
about to create a war client, please see the following example [Payara-Examples gRPC](https://github.com/payara/Payara-Examples/tree/master/grpc).  


## Enterprise

For enterprise it is needed to include the following dependency on the war project with the generated stubs 
from client implementation

```
<dependency>
    <groupId>fish.payara.extensions</groupId>
    <artifactId>grpc</artifactId>
    <version>1.0.0</version>
</dependency>
```

Then, after deploying the application, the custom server for gRPC and the filter to resolve client gRPC calls from
client will be enabled

