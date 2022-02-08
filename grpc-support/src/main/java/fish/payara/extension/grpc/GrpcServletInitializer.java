package fish.payara.extension.grpc;

import java.util.EnumSet;
import java.util.Set;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import io.grpc.BindableService;

@WebListener
@Dependent
public class GrpcServletInitializer implements ServletContextListener {
    @Inject
    BeanManager beanManager;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        if (beanManager != null) { // one cannot be sure
            Set<Bean<?>> grpcServices = beanManager.getBeans(BindableService.class);

            // Create the filter
            final Filter filter = new GrpcFilter(beanManager, grpcServices.toArray(new Bean[0]));

            // Register the filter
            final FilterRegistration.Dynamic registration = ctx.addFilter(filter.getClass().getName(), filter);

            // Configure the filter
            // TODO: parse the method name from the prototype generation
            registration.setAsyncSupported(true);
            registration.addMappingForUrlPatterns( //
                    EnumSet.of(DispatcherType.ASYNC, DispatcherType.REQUEST) //
                    , false, "/*");
        }
    }
}
