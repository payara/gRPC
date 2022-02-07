package fish.payara.extension.grpc.deployment;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Singleton;

import com.sun.enterprise.module.HK2Module;
import org.glassfish.api.container.Sniffer;
import org.glassfish.api.deployment.DeploymentContext;
import org.glassfish.api.deployment.archive.ArchiveType;
import org.glassfish.api.deployment.archive.ReadableArchive;
import org.glassfish.hk2.classmodel.reflect.Type;
import org.glassfish.hk2.classmodel.reflect.Types;
import org.jvnet.hk2.annotations.Service;

/**
 * Scans application archive for gRPC traits. It is important for service to have name and moduleType.
 */
@Service(name = "grpc")
@Singleton
public class GrpcSniffer implements Sniffer {
    private static final String[] CONTAINER = {GrpcContainer.GRPC_CONTAINER_NAME};

    @Override
    public boolean handles(DeploymentContext context) {
        // get result of class parsing
        Types types = context.getTransientAppMetaData(Types.class.getName(), Types.class);
        Type type = types.getBy("io.grpc.BindableService");
        // if app mentions BindableService, it's an gRPC app.
        return type != null;
    }

    /**
     * Refers to service name of container implementation.
     * @return
     */
    @Override
    public String[] getContainersNames() {
        return CONTAINER;
    }

    @Override
    public boolean handles(ReadableArchive source) {
        // only DeploymentContext overload is important
        return false;
    }

    @Override
    public String[] getURLPatterns() {
        return new String[0];
    }

    @Override
    public Class<? extends Annotation>[] getAnnotationTypes() {
        return new Class[0];
    }

    @Override
    public String[] getAnnotationNames(DeploymentContext context) {
        return new String[0];
    }

    @Override
    public String getModuleType() {
        return "gRPC";
    }

    @Override
    public HK2Module[] setup(String containerHome, Logger logger) throws IOException {
        return new HK2Module[0];
    }

    @Override
    public void tearDown() {

    }

    @Override
    public boolean isUserVisible() {
        return false;
    }

    @Override
    public boolean isJavaEE() {
        return false;
    }

    @Override
    public Map<String, String> getDeploymentConfigurations(ReadableArchive source) throws IOException {
        return null;
    }

    @Override
    public String[] getIncompatibleSnifferTypes() {
        return new String[0];
    }

    @Override
    public boolean supportsArchiveType(ArchiveType archiveType) {
        return false;
    }
}
