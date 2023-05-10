/*
 * JBoss, Home of Professional Open Source.
 *
 * Copyright 2023 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
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

package org.jboss.resteasy.concurrent;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Never to be used outside of this package.
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
class SecurityActions {

    static int getCoreThreads(final String name) {
        final String value = getSystemProperty(name);
        if (value == null) {
            return Math.max(5, Runtime.getRuntime().availableProcessors());
        }
        return Integer.parseInt(value);
    }

    static void registerShutdownHook(final Thread hook) {
        if (System.getSecurityManager() == null) {
            Runtime.getRuntime().addShutdownHook(hook);
        } else {
            AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
                Runtime.getRuntime().addShutdownHook(hook);
                return null;
            });
        }
    }

    static void removeShutdownHook(final Thread hook) {
        if (System.getSecurityManager() == null) {
            Runtime.getRuntime().removeShutdownHook(hook);
        } else {
            AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
                Runtime.getRuntime().removeShutdownHook(hook);
                return null;
            });
        }
    }

    private static String getSystemProperty(final String name) {
        if (System.getSecurityManager() == null) {
            return System.getProperty(name);
        }
        return AccessController.doPrivileged((PrivilegedAction<String>) () -> System.getProperty(name));
    }
}
