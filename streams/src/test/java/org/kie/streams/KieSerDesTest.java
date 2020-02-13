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

import java.util.ArrayDeque;

import org.junit.Before;
import org.junit.Test;
import org.kie.remote.message.ControlMessage;

import static org.junit.Assert.*;

public class KieSerDesTest {

    private KieSerializer serializer;
    private KieDeserializer deserializer;

    @Before
    public void setup() {
        serializer = new KieSerializer();
        deserializer = new KieDeserializer(ControlMessage.class);
    }

    @Test
    public void deserializerTest() {
        ControlMessage msg = new ControlMessage("13",
                                                new ArrayDeque());
        msg.setOffset(1l);
        byte[] bytes = serialize(msg);
        ControlMessage deserMsg = (ControlMessage) deserializer.deserialize("control",
                                                                            bytes);
        assertNotNull(deserMsg);
        assertEquals(msg.getId(),
                     deserMsg.getId());
        assertTrue(msg.getOffset() == deserMsg.getOffset());
        assertTrue(msg.getSideEffects().size() == deserMsg.getSideEffects().size());
    }

    @Test
    public void serializerTest() {
        ControlMessage msg = new ControlMessage("13",
                                                new ArrayDeque());
        msg.setOffset(1l);
        byte[] bytes = serialize(msg);
        assertNotNull(bytes);

        ControlMessage deserMsg = (ControlMessage) deserializer.deserialize("control",
                                                                            bytes);
        assertNotNull(deserMsg);
        assertEquals(msg.getId(),
                     deserMsg.getId());
        assertTrue(msg.getOffset() == deserMsg.getOffset());
        assertTrue(msg.getSideEffects().size() == deserMsg.getSideEffects().size());
    }

    private byte[] serialize(ControlMessage msg) {
        byte[] bytes = serializer.serialize("control",
                                            msg);
        return bytes;
    }

    private ControlMessage deserialize(byte[] bytes) {
        Object o = deserializer.deserialize("control",
                                            bytes);
        return (ControlMessage) o;
    }
}
