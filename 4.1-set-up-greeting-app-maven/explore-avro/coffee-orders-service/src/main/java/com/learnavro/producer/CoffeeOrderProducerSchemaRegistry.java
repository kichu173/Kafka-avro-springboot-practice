package com.learnavro.producer;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static com.learnavro.util.CoffeeOrderUtil.buildNewCoffeeOrder;

import com.learnavro.domain.generated.CoffeeOrder;
import com.learnavro.domain.generated.OrderId;

public class CoffeeOrderProducerSchemaRegistry {

    private static final String COFFEE_ORDERS_TOPIC = "coffee-orders-sr";
    private static final Logger log = LoggerFactory.getLogger(CoffeeOrderProducerSchemaRegistry.class);


    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        //* commenting this line(StringSerializer.class.getName()) as we added Id as orderId in coffeeOrder.avsc file, to send key as well in avro record. Using  KafkaAvroSerializer for ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        //* Kafka Avro Serializer is the one which takes the record and interact with the schema registry behind the scenes and this is the one which is going to give you the schema version id as a response.
        //* From a developer perspective, we dont have to do any code to interact with schema registry. We just have to use KafkaAvroSerializer and KafkaAvroDeserializer and that's it.
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        //* Producer to interact with the schema registry, port you can find it in the docker-compose yml file.
        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");


        //* Now sending key as well in avro record, earlier key used to be null. so commented below line. Enabling to send key with KafkaProducer<OrderId, CoffeeOrder> producer = ...
        KafkaProducer<String, CoffeeOrder> producer = new KafkaProducer<>(props);
//        KafkaProducer<OrderId, CoffeeOrder> producer = new KafkaProducer<>(props);

        CoffeeOrder coffeeOrder = buildNewCoffeeOrder();
        System.out.println("Coffee Order Sent " + coffeeOrder);

        //        byte[] value = coffeeOrder.toByteBuffer().array();
        //* Now sending key as well in avro record, earlier key used to be null. so commented below line and enable producerRecord with constructor to include coffeeOrder.getId()
        ProducerRecord<String, CoffeeOrder> producerRecord =
                new ProducerRecord<>(COFFEE_ORDERS_TOPIC, coffeeOrder.getId().toString(), coffeeOrder);

//        ProducerRecord<OrderId, CoffeeOrder> producerRecord =
//                new ProducerRecord<>(COFFEE_ORDERS_TOPIC, coffeeOrder.getId(), coffeeOrder);

        var recordMetaData = producer.send(producerRecord).get();
        log.info("recordMetaData : {}" , recordMetaData);//! recordMetaData : coffee-orders-sr-0@1. So, coffee-orders-sr-0@1 means that the record was written to partition 0 of the coffee-orders-sr topic, and the offset of the record within that partition is 1. 0 is the partition number where the record was written. 1 is the offset within that partition.
        log.info("published the producer record : {}" , producerRecord);

    }

}