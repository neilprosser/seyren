/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.seyren.core;

import java.lang.management.ManagementFactory;

import com.codahale.metrics.JvmAttributeGaugeSet;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.ClassLoadingGaugeSet;
import com.codahale.metrics.jvm.FileDescriptorRatioGauge;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;

public final class Metrics {
    
    private static final MetricRegistry REGISTRY = new MetricRegistry();
    
    static {
        REGISTRY.register("jvm.attribute", new JvmAttributeGaugeSet());
        REGISTRY.register("jvm.buffers", new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
        REGISTRY.register("jvm.classloader", new ClassLoadingGaugeSet());
        REGISTRY.register("jvm.filedescriptor", new FileDescriptorRatioGauge());
        REGISTRY.register("jvm.gc", new GarbageCollectorMetricSet());
        REGISTRY.register("jvm.memory", new MemoryUsageGaugeSet());
        REGISTRY.register("jvm.threads", new ThreadStatesGaugeSet());
    }
    
    private Metrics() {
    }
    
    public static MetricRegistry registry() {
        return REGISTRY;
    }
    
}
