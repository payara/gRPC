/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) [2021-2022] Payara Foundation and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://github.com/payara/Payara/blob/master/LICENSE.txt
 * See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * The Payara Foundation designates this particular file as subject to the "Classpath"
 * exception as provided by the Payara Foundation in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package fish.payara.extension.grpc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fish.payara.extension.grpc.servlet.ServletAdapter;
import fish.payara.extension.grpc.servlet.ServletAdapterBuilder;
import io.grpc.BindableService;

public class GrpcFilter extends HttpFilter {

    private static final long serialVersionUID = 1L;


    class Reference {
        private final Bean<BindableService> bean;

        CreationalContext<BindableService> ctx;
        BindableService instance;

        Reference(Bean<BindableService> bean) {
            ctx = beanManager.createCreationalContext(bean);
            instance = bean.create(ctx);
            this.bean = bean;
        }

        void dispose() {
            bean.destroy(instance, ctx);
        }
    }

    private final Map<Bean<BindableService>, Reference> services;

    private final BeanManager beanManager;

    private ServletAdapter adapter;

    protected GrpcFilter(BeanManager bm, Bean<BindableService>... serviceBeans) {
        services = new HashMap<>();
        for (Bean<BindableService> serviceBean : serviceBeans) {
            services.put(serviceBean, null);
        }
        this.beanManager = bm;
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        final String method = req.getMethod();

        // Ignore non-gRPC requests
        if (method == null || !ServletAdapter.isGrpc(req)) {
            chain.doFilter(req, res);
            return;
        }

        switch (method.toUpperCase()) {
        case "GET":
            adapter.doGet(req, res);
            break;
        case "POST":
            adapter.doPost(req, res);
            break;
        default:
            throw new UnsupportedOperationException("Unsupported HTTP method: " + method);
        }
    }

    @Override
    public void init() throws ServletException {

        // For each service
        final Iterator<Entry<Bean<BindableService>, Reference>> iterator = services.entrySet().iterator();
        while (iterator.hasNext()) {
            final Entry<Bean<BindableService>, Reference> entry = iterator.next();
            if (entry.getValue() == null) {

                entry.setValue(new Reference(entry.getKey()));
            }
        }

        if (adapter == null) {
            ServletAdapterBuilder builder = new ServletAdapterBuilder();

            // Register each service
            for (Reference service : services.values()) {
                builder = builder.addService(service.instance);
            }

            this.adapter = builder.buildServletAdapter();
        }

        super.init();
    }

    @Override
    public void destroy() {

        // For each service
        for (Reference value : services.values()) {
            if (value != null) {
                value.dispose();
            }
        }
        services.clear();

        if (adapter != null) {
            adapter.destroy();
        }

        super.destroy();
    }

}
