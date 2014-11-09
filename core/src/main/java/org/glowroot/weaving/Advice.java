/*
 * Copyright 2012-2014 the original author or authors.
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

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import org.immutables.value.Value;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;

import org.glowroot.api.weaving.Pointcut;

import static com.google.common.base.Preconditions.checkNotNull;

@Value.Immutable
public abstract class Advice {

    static final Ordering<Advice> orderingByMetricName = new Ordering<Advice>() {
        @Override
        public int compare(@Nullable Advice left, @Nullable Advice right) {
            checkNotNull(left);
            checkNotNull(right);
            return left.pointcut().metricName().compareToIgnoreCase(right.pointcut().metricName());
        }
    };

    abstract Pointcut pointcut();
    abstract Type adviceType();
    abstract @Nullable Pattern pointcutClassNamePattern();
    abstract @Nullable Pattern pointcutMethodNamePattern();
    abstract @Nullable Type travelerType();
    abstract @Nullable Method isEnabledAdvice();
    abstract @Nullable Method onBeforeAdvice();
    abstract @Nullable Method onReturnAdvice();
    abstract @Nullable Method onThrowAdvice();
    abstract @Nullable Method onAfterAdvice();
    abstract List<AdviceParameter> isEnabledParameters();
    abstract List<AdviceParameter> onBeforeParameters();
    abstract List<AdviceParameter> onReturnParameters();
    abstract List<AdviceParameter> onThrowParameters();
    abstract List<AdviceParameter> onAfterParameters();
    abstract boolean reweavable();

    @Value.Derived
    ImmutableSet<Type> classMetaTypes() {
        Set<Type> metaTypes = Sets.newHashSet();
        metaTypes.addAll(getClassMetaTypes(isEnabledParameters()));
        metaTypes.addAll(getClassMetaTypes(onBeforeParameters()));
        metaTypes.addAll(getClassMetaTypes(onReturnParameters()));
        metaTypes.addAll(getClassMetaTypes(onThrowParameters()));
        metaTypes.addAll(getClassMetaTypes(onAfterParameters()));
        return ImmutableSet.copyOf(metaTypes);
    }

    @Value.Derived
    ImmutableSet<Type> methodMetaTypes() {
        Set<Type> metaTypes = Sets.newHashSet();
        metaTypes.addAll(getMethodMetaTypes(isEnabledParameters()));
        metaTypes.addAll(getMethodMetaTypes(onBeforeParameters()));
        metaTypes.addAll(getMethodMetaTypes(onReturnParameters()));
        metaTypes.addAll(getMethodMetaTypes(onThrowParameters()));
        metaTypes.addAll(getMethodMetaTypes(onAfterParameters()));
        return ImmutableSet.copyOf(metaTypes);
    }

    private static Set<Type> getClassMetaTypes(List<AdviceParameter> parameters) {
        Set<Type> types = Sets.newHashSet();
        for (AdviceParameter parameter : parameters) {
            if (parameter.kind() == ParameterKind.CLASS_META) {
                types.add(parameter.type());
            }
        }
        return types;
    }

    private static Set<Type> getMethodMetaTypes(List<AdviceParameter> parameters) {
        Set<Type> types = Sets.newHashSet();
        for (AdviceParameter parameter : parameters) {
            if (parameter.kind() == ParameterKind.METHOD_META) {
                types.add(parameter.type());
            }
        }
        return types;
    }

    @Value.Immutable
    abstract static class AdviceParameter {
        abstract ParameterKind kind();
        abstract Type type();
    }

    enum ParameterKind {
        RECEIVER, METHOD_ARG, METHOD_ARG_ARRAY, METHOD_NAME, RETURN, OPTIONAL_RETURN, THROWABLE,
        TRAVELER, CLASS_META, METHOD_META
    }
}
