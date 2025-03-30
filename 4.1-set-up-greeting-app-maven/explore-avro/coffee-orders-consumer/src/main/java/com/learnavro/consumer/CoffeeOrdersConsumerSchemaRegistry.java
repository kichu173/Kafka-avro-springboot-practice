package com.learnavro.consumer;


import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import com.learnavro.domain.generated.CoffeeOrder;
import com.learnavro.domain.generated.CoffeeUpdateEvent;
import com.learnavro.domain.generated.OrderId;

public class CoffeeOrdersConsumerSchemaRegistry {

    private static final Logger log = LoggerFactory.getLogger(CoffeeOrdersConsumerSchemaRegistry.class);
    private static final String COFFEE_ORDERS_TOPIC = "coffee-orders-sr";

    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "coffee.consumer7");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "coffee.consumer.sr");
        //* commenting this line(StringDeserializer.class.getName()) as we added Id as orderId in coffeeOrder.avsc file, to send key as well in avro record. Using  KafkaAvroSerializer for ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        //* VALUE_DESERIALIZER_CLASS_CONFIG is set to Kafka Avro Deserializer, is making sure that the schema registry compatibility type is backward (https://docs.confluent.io/platform/current/schema-registry/fundamentals/schema-evolution.html).
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        //props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CustomAvroDeserializer.class.getName());
        props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        //* This is going to make sure when we read the record it's going to be parsed to CoffeeOrder. By default if you are not giving below SPECIFIC_AVRO_READER_CONFIG then it considered as false and makes it to be GenericRecord rather the avrorecord of CoffeeOrder
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
//        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        //* If we are making KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG to be false in props,
        //* then the data we are receiving in consumer is treated as Generic record, set value to false and uncomment the below line to see how to handle Generic record.
        //* Generic record is also used when multiple related events are sent to topic and we want to read all of them. For ex: RecordNameStrategy
        //  KafkaConsumer<OrderId, GenericRecord> consumer = new KafkaConsumer<>(props);
//        KafkaConsumer<String, CoffeeOrder> consumer = new KafkaConsumer<>(props);
//        KafkaConsumer<OrderId, CoffeeOrder> consumer = new KafkaConsumer<>(props);
        KafkaConsumer<String, GenericRecord> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Collections.singletonList(COFFEE_ORDERS_TOPIC));
        log.info("Consumer Started");
        while(true) {
             ConsumerRecords<String, GenericRecord> records = consumer.poll(Duration.ofMillis(100));
            // ConsumerRecords<OrderId, GenericRecord> records = consumer.poll(Duration.ofMillis(100));
            try{
//                ConsumerRecords<String, CoffeeOrder> records = consumer.poll(Duration.ofMillis(100));
//                ConsumerRecords<OrderId, CoffeeOrder> records = consumer.poll(Duration.ofMillis(100));
                 for(ConsumerRecord<String, GenericRecord> record : records) {
//                  for(ConsumerRecord<OrderId, GenericRecord> record : records) {
//                for(ConsumerRecord<String, CoffeeOrder> record : records) {
//                    for(ConsumerRecord<OrderId, CoffeeOrder> record : records) {
                         GenericRecord coffeeOrder =record.value();
//                    CoffeeOrder coffeeOrder =record.value();
                    log.info("Consumed message: \n" + record.key() + " : " + coffeeOrder.toString());
                    log.info("generic record: {}", coffeeOrder );

                    //* We are able to differentiate between CoffeeOrder and CoffeeUpdateEvent because we are instanceOf and converting to that specific type
                     if(coffeeOrder instanceof CoffeeOrder){
                         var coffeeOrderGeneric =(CoffeeOrder) coffeeOrder;
                         log.info("coffeeOrder : {}", coffeeOrderGeneric);
                     }else if (coffeeOrder instanceof CoffeeUpdateEvent){
                         var coffeeOrderUpdateEvent =(CoffeeUpdateEvent) coffeeOrder;
                         log.info("coffeeOrderUpdateEvent : {}", coffeeOrderUpdateEvent);
                     }
                }
            }
            catch (Exception e){
                log.error("Exception is : {} ", e.getMessage(), e);
            }finally {
                consumer.commitSync();
//                log.info("Committed the offset catch");
            }

        }
    }
}