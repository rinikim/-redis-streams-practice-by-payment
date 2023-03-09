package com.example.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrderEventStreamListener implements StreamListener<String, MapRecord<String, String, String>> {

    int paymentProcessId = 0;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        // 하나의 엔트리를 받아왔을 때 필드와 벨류를 맵으로 받을 수 있다.
        Map map = message.getValue();

        String userId = (String) map.get("userId");
        String productId = (String) map.get("productId");
        String price = (String) map.get("price");

        // 결제 관련 로직 처리
        // ...

        // 결제가 실행될 때마다 증가되는 값
        String paymentIdStr = Integer.toString(paymentProcessId++);

        // 결제 완료 이벤트 발행
        Map fieldMap = new HashMap<String, String>();
        fieldMap.put("userId", userId);
        fieldMap.put("productId", productId);
        fieldMap.put("price", price);
        fieldMap.put("paymentProcessId", paymentIdStr);

        redisTemplate.opsForStream().add("payment-events", fieldMap);

        System.out.println("[Order consumed] Created payment: " + paymentIdStr);
    }
}
