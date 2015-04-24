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

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.seyren.core.util.config.SeyrenConfig;

@Named
public class GraphiteReporting {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GraphiteReporting.class);
    
    private GraphiteReporter graphiteReporter;
    
    @Inject
    public GraphiteReporting(SeyrenConfig seyrenConfig) {
        boolean enabled = seyrenConfig.getCarbonMetricsEnabled();
        
        if (!enabled) {
            return;
        }
        
        String host = seyrenConfig.getCarbonHost();
        
        if (StringUtils.isEmpty(host)) {
            LOGGER.warn("Metrics have been enabled but CARBON_HOST is not set");
            return;
        }
        
        int port = seyrenConfig.getCarbonPort();
        long sendInterval = seyrenConfig.getCarbonSendInterval();
        String prefix = StringUtils.defaultString(seyrenConfig.getCarbonPrefix(), null);
        
        graphiteReporter = GraphiteReporter.forRegistry(Metrics.registry())
                .prefixedWith(prefix)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build(new Graphite(host, port));
        graphiteReporter.start(sendInterval, TimeUnit.SECONDS);
    }
    
}
