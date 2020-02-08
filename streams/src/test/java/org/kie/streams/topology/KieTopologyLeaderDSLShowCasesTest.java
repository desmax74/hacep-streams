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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.test.TestRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;

public class KieTopologyLeaderDSLShowCasesTest {

    private Logger logger = LoggerFactory.getLogger(KieTopologyLeaderDSLShowCasesTest.class);
    private TopologyTestDriver driver;
    private StringSerializer stringSerializer;
    private StringDeserializer stringDeserializer;
    private TestInputTopic<String, String> inputTopic;
    private TestOutputTopic<String, String> outputTopic;

    @Before
    public void setUp() {
        Properties props = new Properties();
        props.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "drools-test");
        props.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091");
        final StreamsBuilder builder = KieTopology.leaderStreamsBuilderDSL(s -> s.toString().toUpperCase());
        driver = new TopologyTestDriver(builder.build(), props);
        stringSerializer = new StringSerializer();
        stringDeserializer = new StringDeserializer();
        inputTopic = driver.createInputTopic("events", stringSerializer, stringSerializer);
        outputTopic = driver.createOutputTopic("control", stringDeserializer, stringDeserializer);
    }

    @After
    public void teardown(){
        try {
            if(driver!= null) {
                driver.close();
            }
        } catch (final RuntimeException e) {
            logger.warn("Ignoring exception, test failing in Windows due this exception: {}", e.getLocalizedMessage());
        }
    }

    @Test
    public void testValue() {
        inputTopic.pipeInput("9", "Hello");
        assertTrue(outputTopic.readValue().equals("HELLO"));
        assertTrue(outputTopic.isEmpty());
    }

    @Test
    public void testKeyValue() {
        inputTopic.pipeInput("9", "Hello");
        assertTrue(outputTopic.readKeyValue().equals(new KeyValue<>("9","HELLO")));
        assertTrue(outputTopic.isEmpty());
    }

    @Test
    public void testHeadersIgnoringTimestamp() {
        final Headers headers = new RecordHeaders(
                new Header[]{
                        new RecordHeader("foo", "value".getBytes())
                });
        inputTopic.pipeInput(new TestRecord<>("9", "HELLO", headers));
        assertThat(outputTopic.readRecord()).isEqualToIgnoringNullFields(new TestRecord<>("9", "HELLO",  headers));
        assertThat(outputTopic.isEmpty()).isTrue();
    }

    @Test
    public void testKeyValueList() {
        final List<String> inputList = Arrays.asList("THIS", "IS", "SPARTA", "!!!!!");
        final List<KeyValue<String, String>> expected = new LinkedList<>();
        for (String s : inputList) {
            expected.add(new KeyValue<>(null, s));
        }
        inputTopic.pipeValueList(inputList);
        assertThat(outputTopic.readKeyValuesToList()).hasSameElementsAs(expected);
    }

}
