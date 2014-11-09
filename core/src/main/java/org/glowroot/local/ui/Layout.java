/*
 * Copyright 2014 the original author or authors.
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
package org.glowroot.local.ui;

import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import org.immutables.common.marshal.Marshaling;
import org.immutables.value.Json;
import org.immutables.value.Value;

@Value.Immutable
@Json.Marshaled
public abstract class Layout {

    public abstract boolean jvmHeapDump();
    public abstract String footerMessage();
    public abstract boolean passwordEnabled();
    public abstract List<LayoutPlugin> plugins();
    public abstract List<String> transactionTypes();
    public abstract String defaultTransactionType();
    public abstract List<String> transactionCustomAttributes();
    public abstract long fixedAggregateIntervalSeconds();
    public abstract long fixedGaugeIntervalSeconds();

    @Value.Derived
    public String version() {
        return Hashing.sha1().hashString(Marshaling.toJson(this), Charsets.UTF_8).toString();
    }

    @Value.Immutable
    @Json.Marshaled
    public abstract static class LayoutPlugin {
        public abstract String id();
        public abstract String name();
    }
}
