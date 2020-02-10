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
package org.kie.streams.partitioner;

import org.apache.kafka.clients.producer.internals.DefaultPartitioner;
import org.apache.kafka.common.Cluster;

/**
 * Class to use in the configuration
 * with:
 * partitioner.class=org.kie.streams.partitioner.KeyPartitioner
 * <p>
 * if related data needs to be on the same partition
 */
public class KeyPartitioner extends DefaultPartitioner {

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {

        Object newKey = null;
        if (key != null) {
            // 1)cast the key to your custom key
            // 2)retrive the custom key and assign to the newKey
            // 3) get bytes from newKey and assign to keyBytes
        }
        return super.partition(topic, key, keyBytes, value, valueBytes, cluster);
    }
}
