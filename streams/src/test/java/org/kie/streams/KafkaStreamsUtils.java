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

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import kafka.server.KafkaConfig;
import kafka.server.KafkaServer;
import kafka.server.NotRunning;
import kafka.utils.TestUtils;
import kafka.zk.EmbeddedZookeeper;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.utils.SystemTime;
import org.apache.kafka.common.utils.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaStreamsUtils implements AutoCloseable {

    private static final String ZOOKEEPER_HOST = "127.0.0.1";
    private static final String BROKER_HOST = "127.0.0.1";
    private static final String BROKER_PORT = "9092";
    private final static Logger logger = LoggerFactory.getLogger(KafkaStreamsUtils.class);
    private KafkaServer kafkaServer;
    private EmbeddedZookeeper zkServer;
    private String tmpDir;
    private KafkaAdminClient adminClient;
    private Logger kafkaLogger = LoggerFactory.getLogger("org.hacep");

    public Map<String, Object> getKafkaProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    public KafkaServer startServer() throws IOException {
        tmpDir = Files.createTempDirectory(Paths.get(System.getProperty("user.dir"),
                                                     File.separator, "target"),
                                           "kafkatest-").toAbsolutePath().toString();
        zkServer = new EmbeddedZookeeper();
        String zkConnect = ZOOKEEPER_HOST + ":" + zkServer.port();
        Properties brokerProps = new Properties();
        brokerProps.setProperty("zookeeper.connect", zkConnect);
        brokerProps.setProperty("broker.id", "0");
        brokerProps.setProperty("log.dirs", tmpDir);
        brokerProps.setProperty("listeners", "PLAINTEXT://" + BROKER_HOST + ":" + BROKER_PORT);
        brokerProps.setProperty("offsets.topic.replication.factor", "1");
        brokerProps.setProperty("auto.create.topics.enable", "true");
        KafkaConfig config = new KafkaConfig(brokerProps);
        Time mock = new SystemTime();
        kafkaServer = TestUtils.createServer(config, mock);
        Map<String, Object> props = getKafkaProps();
        adminClient = (KafkaAdminClient) AdminClient.create(props);
        return kafkaServer;
    }

    public void shutdownServer() {
        if (adminClient != null) {
            adminClient.close();
        }
        logger.warn("Shutdown kafka server");
        Path tmp = Paths.get(tmpDir);
        try {
            if (kafkaServer.brokerState().currentState() != (NotRunning.state())) {
                kafkaServer.shutdown();
                kafkaServer.awaitShutdown();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        kafkaServer = null;

        try {
            zkServer.shutdown();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        zkServer = null;

        try {
            logger.warn("Deleting kafka temp dir:{}", tmp.toString());
            Files.walk(tmp).
                    sorted(Comparator.reverseOrder()).
                    map(Path::toFile).
                    forEach(File::delete);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(tmp.getParent())) {
            for (Path path : directoryStream) {
                if (path.toString().startsWith("kafkatest-")) {
                    logger.warn("Deleting kafkatest folder:{}",
                                path.toString());
                    Files.walk(path).
                            sorted(Comparator.reverseOrder()).
                            map(Path::toFile).
                            forEach(File::delete);
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(),
                         e);
        }
    }

    @Override
    public void close() {
        shutdownServer();
    }
}