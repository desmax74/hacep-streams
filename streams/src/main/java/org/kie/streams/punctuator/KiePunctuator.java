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
package org.kie.streams.punctuator;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.Punctuator;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.kie.remote.message.Message;

/**
 * Use this class when you want to execute an action on after a specific interval of time
 */
public class KiePunctuator implements Punctuator {

    private ProcessorContext context;
    private KeyValueStore<String, Message> keyValueStore;
    private Object threshold;

    public KiePunctuator(Object threshold,
                         ProcessorContext context,
                         KeyValueStore<String, Message> keyValueStore) {
        this.context = context;
        this.keyValueStore = keyValueStore;
    }

    @Override
    public void punctuate(long timestamp) {
        KeyValueIterator<String, Message> performanceIterator = keyValueStore.all();
        while (performanceIterator.hasNext()) {
            KeyValue<String, Message> keyValue = performanceIterator.next();
            String key = keyValue.key;
            Message msg = keyValue.value;
            if (msg != null) {
                if (check(threshold)) {
                    //do check and then forward
                    context.forward(key,
                                    msg);
                } else {
                    //change the msg
                    context.forward(key,
                                    msg);
                }
            }
        }
    }

    private boolean check(Object threshold) {
        //do checks and return an appropriate value
        return true;
    }
}
