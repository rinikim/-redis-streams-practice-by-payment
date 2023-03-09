package com.example.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

import java.time.Duration;

@Configuration
public class RedisStreamConfig {

    @Autowired
    private OrderEventStreamListener orderEventStreamListener;

    /**
     * 스트림을 구독하기 위한 준비
     * @param factory
     * @return
     */
    @Bean
    public Subscription subscription(RedisConnectionFactory factory) {
        // 리스너 컨테이너 등록하기 위해 옵션객체 생성
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions options = StreamMessageListenerContainer
                .StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(Duration.ofSeconds(1))
                .build();

        // 리스너 컨테이너 생성
        StreamMessageListenerContainer listenerContainer = StreamMessageListenerContainer.create(factory, options);

        // receiveAutoAck() 메세지를 받아가면서 처리해준 것을 표시해주겠다.
        // 해주지 않으면 이벤트 엔트리가 다른 컨슈머 그룹에 갈 수도 있다.
        // payment-service-group 인 컨슈머 그룹에서 (컨슈머 이름은 instance-1) 스트림은 order-events 키값에서 lastConsumed 로 읽어오게 된다.
        Subscription subscription = listenerContainer.receiveAutoAck(Consumer.from("payment-service-group", "instance-1"),
                StreamOffset.create("order-events", ReadOffset.lastConsumed()), orderEventStreamListener);

        listenerContainer.start();
        return subscription;
    }
}
