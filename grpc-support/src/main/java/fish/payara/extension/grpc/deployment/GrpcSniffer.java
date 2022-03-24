/*
 *
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 *  Copyright (c) 2022 Payara Foundation and/or its affiliates. All rights reserved.
 *
 *  The contents of this file are subject to the terms of either the GNU
 *  General Public License Version 2 only ("GPL") or the Common Development
 *  and Distribution License("CDDL") (collectively, the "License").  You
 *  may not use this file except in compliance with the License.  You can
 *  obtain a copy of the License at
 *  https://github.com/payara/Payara/blob/master/LICENSE.txt
 *  See the License for the specific
 *  language governing permissions and limitations under the License.
 *
 *  When distributing the software, include this License Header Notice in each
 *  file and include the License file at glassfish/legal/LICENSE.txt.
 *
 *  GPL Classpath Exception:
 *  The Payara Foundation designates this particular file as subject to the "Classpath"
 *  exception as provided by the Payara Foundation in the GPL Version 2 section of the License
 *  file that accompanied this code.
 *
 *  Modifications:
 *  If applicable, add the following below the License Header, with the fields
 *  enclosed by brackets [] replaced by your own identifying information:
 *  "Portions Copyright [year] [name of copyright owner]"
 *
 *  Contributor(s):
 *  If you wish your version of this file to be governed by only the CDDL or
 *  only the GPL Version 2, indicate your decision by adding "[Contributor]
 *  elects to include this software in this distribution under the [CDDL or GPL
 *  Version 2] license."  If you don't indicate a single choice of license, a
 *  recipient has the option to distribute your version of this file under
 *  either the CDDL, the GPL Version 2 or to extend the choice of license to
 *  its licensees as provided above.  However, if you add GPL Version 2 code
 *  and therefore, elected the GPL Version 2 license, then the option applies
 *  only if the new code is made subject to such option by the copyright
 *  holder.
 *
 */

package fish.payara.extension.grpc.deployment;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.logging.Logger;

import jakarta.inject.Singleton;

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
