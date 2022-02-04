# Payara gRPC implementation

This is the repo for the Payara gRPC implementation. To use gRPC follow the next steps:

To use the gRPC implementation you must build the current project:

```
mvn clean install
```

The resulted jar with name grpc-[version number].jar, must be included into the payara modules folder. 
Other requirement is to include a ServletContainerInitializer implementation as part of the files of the WAR archive.

### Example

```
public class CustomServletInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        CDI.current().getBeanManager().fireEvent(ctx);
    }
}
```

And the extension file configurator under the META-INF.services folder:

- WEB.INF
  - META-INF
    - services
      - javax.servlet.ServletContainerInitializer

And within the content of this file the name of the ServletContainerInitializer implementation class

If you want to see more details about to create a war client, please see the following example [Payara-Examples gRPC](https://github.com/payara/Payara-Examples/tree/master/grpc).  


