/*
 * Copyright 2013-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.glowroot.weaving;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.io.Resources;
import org.immutables.value.Value;
import org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.glowroot.api.weaving.Mixin;
import org.glowroot.api.weaving.MixinInit;

@Value.Immutable
public abstract class MixinType {

    private static final Logger logger = LoggerFactory.getLogger(MixinType.class);

    public static MixinType from(Mixin mixin, Class<?> implementation) throws IOException {
        ImmutableMixinType.Builder builder = ImmutableMixinType.builder();
        builder.addTargets(mixin.target());
        builder.implementation(Type.getType(implementation));
        for (Class<?> iface : implementation.getInterfaces()) {
            builder.addInterfaces(Type.getType(iface));
        }
        String initMethodName = null;
        for (Method method : implementation.getDeclaredMethods()) {
            if (method.getAnnotation(MixinInit.class) != null) {
                if (initMethodName != null) {
                    logger.error("mixin has more than one @MixinInit: {}",
                            implementation.getName());
                    continue;
                }
                if (method.getParameterTypes().length > 0) {
                    logger.error("@MixinInit method cannot have any parameters: {}",
                            implementation.getName());
                    continue;
                }
                if (method.getReturnType() != void.class) {
                    logger.warn("@MixinInit method must return void: {}", implementation.getName());
                    continue;
                }
                initMethodName = method.getName();
            }
        }
        builder.initMethodName(initMethodName);
        ClassLoader loader = implementation.getClassLoader();
        String resourceName = implementation.getName().replace('.', '/') + ".class";
        URL url;
        if (loader == null) {
            url = ClassLoader.getSystemResource(resourceName);
        } else {
            url = loader.getResource(resourceName);
        }
        if (url == null) {
            throw new IllegalStateException("Could not find resource: " + resourceName);
        }
        builder.implementationBytes(Resources.toByteArray(url));
        return builder.build();
    }

    abstract List<String> targets();
    abstract Type implementation();
    abstract List<Type> interfaces();
    @Nullable
    abstract String initMethodName();
    abstract byte[] implementationBytes();
}
