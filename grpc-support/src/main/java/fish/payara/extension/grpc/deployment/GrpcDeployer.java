package fish.payara.extension.grpc.deployment;

import java.util.Set;

import com.sun.enterprise.deployment.Application;
import com.sun.enterprise.deployment.WebBundleDescriptor;
import com.sun.enterprise.deployment.web.AppListenerDescriptor;
import fish.payara.extension.grpc.GrpcServletInitializer;
import org.glassfish.api.deployment.Deployer;
import org.glassfish.api.deployment.DeploymentContext;
import org.glassfish.api.deployment.MetaData;
import org.jvnet.hk2.annotations.Service;

/**
 * Performs actual deployment logic.
 */
@Service
public class GrpcDeployer implements Deployer<GrpcContainer, GrpcApplicationContainer> {
    @Override
    public MetaData getMetaData() {
        return null;
    }

    @Override
    public <V> V loadMetaData(Class<V> type, DeploymentContext context) {
        return null;
    }

    @Override
    public boolean prepare(DeploymentContext context) {
        return true;
    }

    @Override
    public GrpcApplicationContainer load(GrpcContainer container, DeploymentContext context) {
        Application applicationMeta = context.getModuleMetaData(Application.class);
        // list all web applications (usually single)
        Set<WebBundleDescriptor> webBundles = applicationMeta.getBundleDescriptors(WebBundleDescriptor.class);
        for (WebBundleDescriptor webBundle : webBundles) {
            // add servlet context initializer
            webBundle.addAppListenerDescriptor(new AppListenerDescriptor() {
                @Override
                public String getListener() {
                    return GrpcServletInitializer.class.getName();
                }

                @Override
                public void setListener(String listener) {

                }

                @Override
                public String getDescription() {
                    return "gRPC initializer";
                }

                @Override
                public void setDescription(String description) {

                }

                @Override
                public void setDisplayName(String name) {

                }

                @Override
                public String getDisplayName() {
                    return "gRPC initializer";
                }

                @Override
                public void setLargeIconUri(String largeIconUri) {

                }

                @Override
                public String getLargeIconUri() {
                    return null;
                }

                @Override
                public void setSmallIconUri(String smallIconUri) {

                }

                @Override
                public String getSmallIconUri() {
                    return null;
                }
            });
        }
        return new GrpcApplicationContainer();
    }

    @Override
    public void unload(GrpcApplicationContainer appContainer, DeploymentContext context) {

    }

    @Override
    public void clean(DeploymentContext context) {

    }
}
