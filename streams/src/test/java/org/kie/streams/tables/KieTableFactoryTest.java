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

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class KieTableFactoryTest {

  @Test
  public void createTableTest(){
      assertNotNull(KieTableFactory.createTable("events"));
  }

  @Test
  public void createTableWithConsumedTest(){
    assertNotNull(KieTableFactory.createTable("events", Consumed.with(Serdes.String(), Serdes.String())));
  }

  @Test
  public void createTableWithConsumedAndMaterializedTest(){
    assertNotNull(KieTableFactory.createTable("events", Consumed.with(Serdes.String(), Serdes.String()), Materialized.with(Serdes.String(), Serdes.String())));
  }

  @Test
  public void createGlobalTableTest(){
    assertNotNull(KieTableFactory.createGlobalTable("events"));
  }

  @Test
  public void createGlobalTableWithConsumedTest(){
    assertNotNull(KieTableFactory.createGlobalTable("events", Consumed.with(Serdes.String(), Serdes.String())));
  }

  @Test
  public void createGlobalTableWithConsumedAndMaterializedTest(){
    assertNotNull(KieTableFactory.createGlobalTable("events", Consumed.with(Serdes.String(), Serdes.String()), Materialized.with(Serdes.String(), Serdes.String())));
  }
}
