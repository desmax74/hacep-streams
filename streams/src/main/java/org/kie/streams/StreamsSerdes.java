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
package org.kie.streams;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;

import org.apache.kafka.common.serialization.Serializer;
import org.kie.remote.message.ControlMessage;

public class StreamsSerdes {

    public static Serde<ControlMessage> MessagePatternSerde() {
        return new ControlMessageSerde();
    }

    public static final class ControlMessageSerde extends WrapperSerde<ControlMessage> {
        public ControlMessageSerde(){
            super(new KieSerializer<>(), new KieDeserializer<>(ControlMessage.class) );
        }
    }

    private static class WrapperSerde<T> implements Serde<T> {

        private Serializer<T> serializer;
        private Deserializer<T> deserializer;

        public WrapperSerde(Serializer<T> serializer, Deserializer<T> deserializer) {
            this.serializer = serializer;
            this.deserializer = deserializer;
        }

        @Override
        public Serializer<T> serializer() {
            return serializer;
        }

        @Override
        public Deserializer<T> deserializer() {
            return deserializer;
        }

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) { }

        @Override
        public void close() {}
    }
}
