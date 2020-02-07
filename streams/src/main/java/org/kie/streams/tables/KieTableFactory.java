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
package org.kie.streams.tables;

import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;

public class KieTableFactory<K, V> {

  public KTable<K,V> createTable(final String topic){
    StreamsBuilder streamsBuilder = new StreamsBuilder();
    return streamsBuilder.table(topic);
  }

  public KTable<K,V> createTable(final String topic, final Consumed<K,V> consumed){
    StreamsBuilder streamsBuilder = new StreamsBuilder();
    return streamsBuilder.table(topic, consumed);
  }

  public KTable<K,V> createTable(final String topic, final Consumed<K, V> consumed, final Materialized<K, V, KeyValueStore<Bytes, byte[]>> materialized){
    StreamsBuilder streamsBuilder = new StreamsBuilder();
    return streamsBuilder.table(topic, consumed, materialized);
  }

  public GlobalKTable<K,V> createGlobalTable(final String topic){
    StreamsBuilder streamsBuilder = new StreamsBuilder();
    return streamsBuilder.globalTable(topic);
  }

  public GlobalKTable<K,V> createGlobalTable(final String topic, final Consumed<K,V> consumed){
    StreamsBuilder streamsBuilder = new StreamsBuilder();
    return streamsBuilder.globalTable(topic, consumed);
  }

  public GlobalKTable<K,V> createGlobalTable(final String topic, final Consumed<K, V> consumed, final Materialized<K, V, KeyValueStore<Bytes, byte[]>> materialized){
    StreamsBuilder streamsBuilder = new StreamsBuilder();
    return streamsBuilder.globalTable(topic, consumed, materialized);
  }
}
