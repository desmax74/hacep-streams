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

import java.util.Properties;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.ValueMapper;

public class InfraStreamsFactory<K,V> {
    
    public KStream<K,V> getKStream(String srcTopic, Serde<K> keySerde, Serde<V> valueSerde) {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        return streamsBuilder.stream(srcTopic, Consumed.with(keySerde, valueSerde));
    }

    public static StreamsConfig getStreamsConfig(Properties props) {
        return new StreamsConfig(props);
    }

}
