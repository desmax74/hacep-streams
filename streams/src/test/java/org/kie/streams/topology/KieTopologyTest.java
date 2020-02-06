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

import java.util.Properties;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class KieTopologyTest {

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
        final StreamsBuilder builder = KieTopology.leaderStreamsBuilder();
        driver = new TopologyTestDriver(builder.build(), props);
        stringSerializer = new StringSerializer();
        stringDeserializer = new StringDeserializer();
        inputTopic = driver.createInputTopic("events", stringSerializer, stringSerializer);
        outputTopic = driver.createOutputTopic("control", stringDeserializer, stringDeserializer);
    }

    @After
    public void teardown(){
        if(driver!= null) {
            driver.close();
        }
    }

    @Test
    public void testOnlyValue() {
        inputTopic.pipeInput("9L", "Hello");
        assertTrue(outputTopic.readValue().equals("HELLO"));
        assertTrue(outputTopic.isEmpty());
    }
}
