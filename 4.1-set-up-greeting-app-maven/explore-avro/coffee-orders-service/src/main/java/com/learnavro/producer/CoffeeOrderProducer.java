package com.learnavro.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static com.learnavro.util.CoffeeOrderUtil.buildNewCoffeeOrder;

import com.learnavro.domain.generated.CoffeeOrder;

public class CoffeeOrderProducer {

    private static final String COFFEE_ORDERS_TOPIC = "coffee-orders";


    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());

        KafkaProducer<String, byte[]> producer = new KafkaProducer<>(props);

//        Thread shutdownHook = new Thread(producer::close);
//        Runtime.getRuntime().addShutdownHook(shutdownHook);

        CoffeeOrder coffeeOrder = buildNewCoffeeOrder();

        byte[] value = coffeeOrder.toByteBuffer().array();// converting it to byte array because we are using ByteArraySerializer in VALUE_SERIALIZER_CLASS_CONFIG props

        ProducerRecord<String, byte[]> producerRecord =
                new ProducerRecord<>(COFFEE_ORDERS_TOPIC, value);

        var recordMetaData = producer.send(producerRecord).get();

        System.out.println("recordMetaData : " + recordMetaData);

    }

}