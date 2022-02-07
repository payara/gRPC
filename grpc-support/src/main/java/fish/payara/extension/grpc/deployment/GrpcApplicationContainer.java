package fish.payara.extension.grpc.deployment;

import org.glassfish.api.deployment.ApplicationContainer;
import org.glassfish.api.deployment.ApplicationContext;

/**
 * Application container carries Deployer's runtime data at runtime. Not needed here.
 */
public class GrpcApplicationContainer implements ApplicationContainer<GrpcApplicationContainer.GrpcDescriptor> {
    /**
     * Runtime descriptor. We have nothing to keep.
     */
    public static class GrpcDescriptor {

    }

    @Override
    public GrpcDescriptor getDescriptor() {
        return null;
    }

    @Override
    public boolean start(ApplicationContext startupContext) throws Exception {
        return true;
    }

    @Override
    public boolean stop(ApplicationContext stopContext) {
        return true;
    }

    @Override
    public boolean suspend() {
        return false;
    }

    @Override
    public void initialize() {
        ApplicationContainer.super.initialize();
    }

    @Override
    public boolean resume() throws Exception {
        return true;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }

}
