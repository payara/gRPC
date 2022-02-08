package fish.payara.extension.grpc.deployment;

import javax.inject.Singleton;

import org.glassfish.api.container.Container;
import org.glassfish.api.deployment.Deployer;
import org.jvnet.hk2.annotations.Service;

/**
 * Static part of service implementation, that points to deployer service. We have no other static parts of implementation,
 * we could start network connectors here and such.
 */
@Service(name = GrpcContainer.GRPC_CONTAINER_NAME)
@Singleton
public class GrpcContainer implements Container {

    public static final String GRPC_CONTAINER_NAME = "fish.payara.extension.grpc.deployment.GrpcContainer";

    @Override
    public Class<? extends Deployer> getDeployer() {
        return GrpcDeployer.class;
    }

    @Override
    public String getName() {
        return "gRPC";
    }
}
