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

import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.processor.internals.ProcessorTopologyTest;
import org.apache.kafka.streams.test.ConsumerRecordFactory;
import org.apache.kafka.streams.test.OutputVerifier;
import org.junit.Before;
import org.junit.Test;

public class KieTopologyTest {

    private TopologyTestDriver driver;
    private Topology kieTopology;
    private ConsumerRecordFactory<String, String> factory = new ConsumerRecordFactory<>("control", new StringSerializer(), new StringSerializer());

    @Before
    public void setUp() {
        Properties props = new Properties();
        props.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091");
        props.setProperty(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, ProcessorTopologyTest.CustomTimestampExtractor.class.getName());
        props.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        kieTopology = KieTopology.buildLeaderTopology();
        driver = new TopologyTestDriver(kieTopology, props);
    }

    @Test
    public void testLeaderTopology(){
        //TestInputTopic<String, Long> inputTopic = driver.createInputTopic("input-topic", stringSerde.serializer(), longSerde.serializer());
        //inputTopic.pipeInput("key", 42L);
    }

    private void assertCodeIsInTopic(String code, String topic) {
        OutputVerifier.compareKeyValue(readMessage(topic), null, code);
    }

    private void sendMessage(final String message) {
        final KeyValue<String,String> kv = new KeyValue<String, String>(null, message);
        final List<KeyValue<String,String>> keyValues = java.util.Arrays.asList(kv);
        final List<ConsumerRecord<byte[], byte[]>> create = factory.create(keyValues);
        driver.pipeInput(create);
    }

    private ProducerRecord<String, String> readMessage(String topic) {
        return driver.readOutput(topic, new StringDeserializer(), new StringDeserializer());
    }
}
