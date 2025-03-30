package com.learnavro.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.learnavro.domain.generated.Address;
import com.learnavro.domain.generated.CoffeeOrder;
import com.learnavro.domain.generated.CoffeeUpdateEvent;
import com.learnavro.domain.generated.OrderLineItem;
import com.learnavro.domain.generated.Store;
import com.learnavro.dto.CoffeeOrderDTO;
import com.learnavro.dto.CoffeeOrderUpdateDTO;
import com.learnavro.producer.CoffeeOrderProducer;
import com.learnavro.producer.CoffeeOrderUpdateProducer;

//! Service layer is responsible for converting CoffeeOrderDTO into CoffeeOrder(com.learnavro.domain.generated) avro records and service layer is going to also interact with kafka producer class, that is going to take care of sending/publishing the record to the Kafka topic
@Service
public class CoffeeOrderService {

    CoffeeOrderProducer coffeeOrderProducer;
    CoffeeOrderUpdateProducer coffeeOrderUpdateProducer;

//        public CoffeeOrderService(CoffeeOrderProducer coffeeOrderProducer) {
//            this.coffeeOrderProducer = coffeeOrderProducer;
//        }

    public CoffeeOrderService(CoffeeOrderProducer coffeeOrderProducer, CoffeeOrderUpdateProducer coffeeOrderUpdateProducer) {
        this.coffeeOrderProducer = coffeeOrderProducer;
        this.coffeeOrderUpdateProducer = coffeeOrderUpdateProducer;
    }

   //* converting CoffeeOrderDTO into CoffeeOrder avro record
    public CoffeeOrderDTO newOrder(CoffeeOrderDTO coffeeOrderDTO) {
        var coffeeOrderAvro = mapToCoffeeOrder(coffeeOrderDTO);
        // setting the id from the avro record to the DTO
        coffeeOrderDTO.setId(coffeeOrderAvro.getId().toString());
        //DB saving this order
        coffeeOrderProducer.sendMessage(coffeeOrderAvro);
        return coffeeOrderDTO;
    }

    private CoffeeOrder mapToCoffeeOrder(CoffeeOrderDTO coffeeOrderDTO) {

        Store store = getStore(coffeeOrderDTO);

        var orderLineItems = buildOrderLineItems(coffeeOrderDTO);

        return CoffeeOrder.newBuilder()
                          .setId(UUID.randomUUID())
                          .setName(coffeeOrderDTO.getName())
                          .setNickName(coffeeOrderDTO.getNickName())
                          .setStore(store)
                          .setOrderLineItems(orderLineItems)
                          .setStatus(coffeeOrderDTO.getStatus())
                          .setOrderedTime(Instant.now())
//                          .setOrderedTime(CoffeeOrderDTO.getOrderedTime().toInstant(ZoneOffset.UTC)) // setting up from the rest api value
                          .setPickUp(coffeeOrderDTO.getPickUp())
                          .setStatus(coffeeOrderDTO.getStatus())
                          .build();

    }

    private List<OrderLineItem> buildOrderLineItems(CoffeeOrderDTO coffeeOrderDTO) {

        return coffeeOrderDTO.getOrderLineItems()
                             .stream().map(orderLineItem ->
                                                   new OrderLineItem(
                                                           orderLineItem.getName(),
                                                           orderLineItem.getSize(),
                                                           orderLineItem.getQuantity(),
                                                           orderLineItem.getCost()
                                                   )
                )
                             .collect(Collectors.toList());
    }

    private Store getStore(CoffeeOrderDTO coffeeOrderDTO) {
        var storeDTO = coffeeOrderDTO.getStore();

        var store = new Store(storeDTO.getStoreId(),
                              new Address(storeDTO.getAddress().getAddressLine1(),
                                          storeDTO.getAddress().getCity(),
                                          storeDTO.getAddress().getState(),
                                          storeDTO.getAddress().getCountry(),
                                          storeDTO.getAddress().getZip()
                              ));
        return store;
    }

    public CoffeeOrderUpdateDTO updateOrder(String orderId, CoffeeOrderUpdateDTO coffeeOrderUpdateDTO) {
        var coffeeOrderUpdateAvro = mapToCoffeeOrderUpdate(orderId, coffeeOrderUpdateDTO);
        coffeeOrderUpdateProducer.sendUpdateMessage(orderId, coffeeOrderUpdateAvro);
        return coffeeOrderUpdateDTO;
    }

    private CoffeeUpdateEvent mapToCoffeeOrderUpdate(String orderId, CoffeeOrderUpdateDTO coffeeOrderUpdateDTO) {

        return CoffeeUpdateEvent
                .newBuilder()
                .setId(UUID.fromString(orderId))
                .setStatus(coffeeOrderUpdateDTO.getOrderStatus())
                .build();
    }
}
