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

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.ValueMapper;

public class KieTopology {

    public static StreamsBuilder leaderStreamsBuilderDSL(ValueMapper mapper) {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        streamsBuilder.stream("events", Consumed.with(Serdes.String(), Serdes.String())).
                mapValues(mapper).
                to("control", Produced.with(Serdes.String(), Serdes.String()));
        return streamsBuilder;
    }

    public static StreamsBuilder replicaStreamsBuilder(ValueMapper mapper) {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        streamsBuilder.stream("control", Consumed.with(Serdes.String(), Serdes.String()))
                .mapValues(mapper)
                .to("control", Produced.with(Serdes.String(), Serdes.String()));
        return streamsBuilder;
    }
}