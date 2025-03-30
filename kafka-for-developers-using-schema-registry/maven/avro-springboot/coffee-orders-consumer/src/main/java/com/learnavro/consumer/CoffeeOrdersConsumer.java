package com.learnavro.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.learnavro.domain.generated.CoffeeOrder;
import com.learnavro.domain.generated.CoffeeUpdateEvent;

@Component
@Slf4j
public class CoffeeOrdersConsumer {

    @KafkaListener(
            topics = {"coffee-orders"}// topic to read the message from
            , autoStartup = "${coffeeOrdersConsumer.startup:true}"
            , groupId = "${spring.kafka.consumer.group-id}")// read from application.yml
    //public void onMessage(ConsumerRecord<String, CoffeeOrder> consumerRecord) {
    public void onMessage(ConsumerRecord<String, GenericRecord> consumerRecord) {// making into genericRecords as we receive multiple events like create and update coffee order
        log.info("ConsumerRecord key: {} , value: {} ", consumerRecord.key(), consumerRecord.value());

        var genericRecord = consumerRecord.value();

        if (genericRecord instanceof CoffeeOrder) {
            var coffeeOrder = (CoffeeOrder) genericRecord;
            log.info("CoffeeOrder: {}", coffeeOrder);
        } else if (genericRecord instanceof CoffeeUpdateEvent) {
            var coffeeUpdateEvent = (CoffeeUpdateEvent) genericRecord;
            log.info("CoffeeUpdateEvent: {}", coffeeUpdateEvent);
        }
    }

}