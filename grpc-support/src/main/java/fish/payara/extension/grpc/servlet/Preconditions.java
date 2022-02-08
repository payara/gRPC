/*
 * Copyright 2018-2022 The gRPC Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// Portions Copyright 2021-2022 Payara Foundation and/or its affiliates
package fish.payara.extension.grpc.servlet;

public final class Preconditions {

    private Preconditions() {
    }

    protected static <T> T checkNotNull(T object, String errorMessage) {
        if (object == null) {
            if (errorMessage != null) {
                throw new NullPointerException(errorMessage);
            }
            throw new NullPointerException();
        }
        return object;
    }

    protected static void checkArgument(Object object, String errorMessage) {
        if (object == null) {
            if (errorMessage != null) {
                throw new IllegalArgumentException(errorMessage);
            }
            throw new IllegalArgumentException();
        }
    }

    protected static void checkState(Object object, String errorMessage) {
        if (object == null) {
            if (errorMessage != null) {
                throw new IllegalStateException(errorMessage);
            }
            throw new IllegalStateException();
        }
    }

}
