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
package org.kie.streams.topology;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.apache.kafka.streams.processor.FailOnInvalidTimestamp;
import org.kie.streams.processor.KieLeaderProcessor;
import org.kie.streams.processor.KieReplicaProcessor;

public class KieTopology {

    private static Serde<String> stringSerde = Serdes.String();
    private static Deserializer<String> stringDeserializer = stringSerde.deserializer();
    public static String TOPIC_CONTROL = "control";
    public static String TOPIC_EVENTS = "events";
    public static String TOPIC_DESTINATION = "destination";
    private static KieLeaderProcessor kieLeaderProcessor = new KieLeaderProcessor();
    private static KieReplicaProcessor kieReplicaProcessor = new KieReplicaProcessor();

    public static StreamsBuilder leaderStreamsBuilderDSL(ValueMapper mapper) {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        streamsBuilder.stream(TOPIC_EVENTS, Consumed.with(Serdes.String(), Serdes.String())).
                mapValues(mapper).
                to(TOPIC_CONTROL, Produced.with(Serdes.String(), Serdes.String()));
        return streamsBuilder;
    }

    public static StreamsBuilder replicaStreamsBuilder(ValueMapper mapper) {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        streamsBuilder.stream(TOPIC_CONTROL, Consumed.with(Serdes.String(), Serdes.String()))
                .mapValues(mapper)
                .to(TOPIC_DESTINATION, Produced.with(Serdes.String(), Serdes.String()));
        return streamsBuilder;
    }

    public static Topology leaderStreamsBuilderProcessors(ValueMapper mapper) {
        Topology topology = new Topology();

        topology.addSource(Topology.AutoOffsetReset.LATEST,
                           "startNode",
                           new FailOnInvalidTimestamp(),
                           stringDeserializer, new StringDeserializer(),
                           TOPIC_EVENTS)
                .addProcessor("leaderNode",
                              ()-> kieLeaderProcessor,
                              "startNode")
                .addProcessor("replicaNode",
                              ()-> kieReplicaProcessor,
                              "startNode");//parentNode
        return topology;
    }
}