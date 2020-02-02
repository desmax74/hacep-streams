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
package org.kie.hacep.core.infra.consumer;

import java.util.Iterator;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerInterceptor implements org.apache.kafka.clients.consumer.ConsumerInterceptor {

  private Logger logger = LoggerFactory.getLogger(ConsumerInterceptor.class);

  public ConsumerInterceptor(){
    logger.info("Built ConsumerInterceptor");
  }

  @Override
  public ConsumerRecords onConsume(ConsumerRecords consumerRecords) {
    logger.info("Intercepted ConsumerRecords {}", buildMessage(consumerRecords.iterator()));
    return consumerRecords;
  }

  @Override
  public void onCommit(Map map) {
    logger.info("Commit information {}", map);
  }

  @Override
  public void close() {
    logger.info("close ConsumerInterceptor");
  }

  @Override
  public void configure(Map<String, ?> map) {
    logger.info("configure:{}", map);
  }

  private String buildMessage(Iterator<ConsumerRecord<Object, Object>> consumerRecords) {
    StringBuilder stringBuilder = new StringBuilder();
    while (consumerRecords.hasNext()) {
      stringBuilder.append(consumerRecords.next());
    }
    return stringBuilder.toString();
  }
}
