/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
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
package org.kie.streams;

import java.util.Optional;

import org.apache.kafka.streams.StreamsConfig;
import org.kie.hacep.Config;

public class StreamsKieConfig {

    public static String APPLICATION_ID_DEFAULT = "drools";
    private String applicationID;
    private String bootstrapServerURL;

    private StreamsKieConfig() {
    }

    public static StreamsKieConfig anStreamsKieConfig() {
        return new StreamsKieConfig();
    }

    public static StreamsKieConfig getDefaultStreamsKieConfig() {
        return anStreamsKieConfig().
                withApplicationId(Optional.ofNullable(System.getenv(StreamsConfig.APPLICATION_ID_CONFIG)).orElse(APPLICATION_ID_DEFAULT)).
                withBootstrapServerURL(Config.getBootStrapServers());
    }

    public String getApplicationID() {
        return applicationID;
    }

    public String getBootstrapServerURL() {
        return bootstrapServerURL;
    }

    public StreamsKieConfig withApplicationId(String applicationID) {
        this.applicationID = applicationID;
        return this;
    }

    public StreamsKieConfig withBootstrapServerURL(String bootstrapServerURL) {
        this.bootstrapServerURL = bootstrapServerURL;
        return this;
    }
}
