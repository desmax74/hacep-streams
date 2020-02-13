package org.kie.streams.transformer;

import java.util.Objects;

import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.kie.hacep.core.KieSessionContext;
import org.kie.remote.message.Message;

/**
 * Its purpose is to save state
 */
public class MessageTransformer implements ValueTransformer<Message, MessageAccumulator> {

    private final String storeName;
    private KeyValueStore<String, KieSessionContext> stateStore;
    private ProcessorContext context;

    public MessageTransformer(String storeName) {
        Objects.requireNonNull(storeName,
                               "Store Name can't be null");
        this.storeName = storeName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void init(ProcessorContext context) {
        this.context = context;
        stateStore = (KeyValueStore) this.context.getStateStore(storeName);
    }

    @Override
    public MessageAccumulator transform(Message value) {
        MessageAccumulator messageAccumulator = new MessageAccumulator();
        //use the state store to compute and add,
        // Decide if is best KieSession or CommandHandler to process the messages

        return messageAccumulator;
    }

    public MessageAccumulator punctuate(long timestamp) {
        return null;  /*no-op*/
    }

    @Override
    public void close() {/*no-op*/}
}
