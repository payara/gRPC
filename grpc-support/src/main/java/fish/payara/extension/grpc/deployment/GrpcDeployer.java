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
