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
package org.kie;

import java.util.Properties;
import java.lang.String;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.test.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;

import org.apache.kafka.streams.integration.utils.EmbeddedKafkaCluster;
import org.apache.kafka.test.StreamsTestUtils;

public class StreamsTest {

  private KafkaStreams kafkaStreams;
  private StreamsConfig streamsConfig;
  private Properties producerConfig;
  private Properties consumerConfig;
  private Properties properties;

  private static final String TOPIC_ONE = "topicOne";
  private static final String TOPIC_TWO = "topicTwo";
  private static final String DEST_TOPIC = "out-topic";


  @ClassRule
  public static final EmbeddedKafkaCluster kafka = new EmbeddedKafkaCluster(1);

  @BeforeClass
  public static void setUpAll() throws Exception {
    kafka.createTopic(TOPIC_ONE);
    kafka.createTopic(TOPIC_TWO);
    kafka.createTopic(DEST_TOPIC);
  }


  @Before
  public void setUp() {
    String stringSerdeClassName = Serdes.String().getClass().getName();
    properties = StreamsTestUtils.getStreamsConfig("test",
                                                   kafka.bootstrapServers(),
                                                   stringSerdeClassName,
                                                   stringSerdeClassName,
                                                   new Properties());
    producerConfig = TestUtils.producerConfig(kafka.bootstrapServers(), StringSerializer.class, StringSerializer.class);
    consumerConfig = TestUtils.consumerConfig(kafka.bootstrapServers(), StringDeserializer.class, StringDeserializer.class);
  }

  @After
  public void tearDown() {
    if (kafkaStreams != null) {
      kafkaStreams.close();
    }
  }

}
