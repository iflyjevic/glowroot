/*
 * Copyright 2011-2014 the original author or authors.
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
package org.glowroot.local.store;

import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.io.CharSource;

import org.glowroot.collector.Existence;
import org.glowroot.collector.ImmutableTrace;
import org.glowroot.collector.Trace;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

class TraceTestData {

    private static final AtomicInteger counter = new AtomicInteger();

    static Trace createTrace() {
        return ImmutableTrace.builder()
                .id("abc" + counter.getAndIncrement())
                .active(false)
                .partial(false)
                .startTime(1)
                .captureTime(11)
                .duration(MILLISECONDS.toNanos(10))
                .transactionType("unit test")
                .transactionName("test transaction name")
                .headline("test headline")
                .user("j")
                .customAttributes("{\"abc\":\"xyz\", \"xyz\":\"abc\"}")
                .customAttributesForIndexing(ImmutableSetMultimap.of("abc", "xyz", "xyz", "abc"))
                .entriesExistence(Existence.NO)
                .outlierProfileExistence(Existence.NO)
                .profileExistence(Existence.NO)
                .build();
    }

    static CharSource createEntries() {
        return CharSource.wrap("[{\"offset\":0,\"duration\":0,\"index\":0,"
                + "\"level\":0,\"message\":{\"text\":\"Level One\","
                + "\"detail\":{\"arg1\":\"a\",\"arg2\":\"b\","
                + "\"nested1\":{\"nestedkey11\":\"a\",\"nestedkey12\":\"b\","
                + "\"subnestedkey1\":{\"subnestedkey1\":\"a\",\"subnestedkey2\":\"b\"}},"
                + "\"nested2\":{\"nestedkey21\":\"a\",\"nestedkey22\":\"b\"}}}},"
                + "{\"offset\":0,\"duration\":0,\"index\":1,\"level\":1,"
                + "\"message\":{\"text\":\"Level Two\",\"detail\":{\"arg1\":\"ax\","
                + "\"arg2\":\"bx\"}}},{\"offset\":0,\"duration\":0,\"index\":2,"
                + "\"level\":2,\"message\":{\"text\":\"Level Three\","
                + "\"detail\":{\"arg1\":\"axy\",\"arg2\":\"bxy\"}}}]");
    }
}
