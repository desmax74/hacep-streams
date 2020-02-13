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
package org.kie.streams.transformer;

import org.kie.hacep.consumer.CommandHandler;
import org.kie.hacep.core.GlobalStatus;
import org.kie.hacep.core.infra.election.State;
import org.kie.hacep.exceptions.ProcessCommandException;
import org.kie.remote.command.RemoteCommand;
import org.kie.remote.command.VisitableCommand;

public class MessageAccumulator {

    private CommandHandler commandHandler;

    public MessageAccumulator() {
    }

    public MessageAccumulator(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public void process(RemoteCommand command,
                        State state) {
        //Think about move the visitor here
        boolean execute = state.equals(State.LEADER) || command.isPermittedForReplicas();
        if (execute) {
            VisitableCommand visitable = (VisitableCommand) command;
            try {
                visitable.accept(commandHandler);
            } catch (Exception e) {
                GlobalStatus.setNodeLive(false);
                throw new ProcessCommandException(e.getMessage(),
                                                  e.getCause());
            }
        }
    }
}
