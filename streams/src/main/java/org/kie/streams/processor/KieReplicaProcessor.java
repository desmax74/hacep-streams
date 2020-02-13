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
package org.kie.streams.processor;

import org.apache.kafka.streams.processor.AbstractProcessor;
import org.kie.remote.message.ControlMessage;

public class KieReplicaProcessor extends AbstractProcessor<String, ControlMessage> {

    private String replicaNode;
    private String leaderNode;

    public KieReplicaProcessor() {
    }

    public KieReplicaProcessor(String leaderNode,
                               String replicaNode) {
        this.leaderNode = leaderNode;
        this.replicaNode = replicaNode;
    }

    @Override
    public void process(String key,
                        ControlMessage msg) {
        //msg.
        //@TODO
        context().forward(key,
                          "value");
    }
}
