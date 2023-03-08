package com.example.PaymentService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * order event 를 수신해서 실제로 결제를 처리하고 새로운 payment event 를 생성한다.
 * 레디스에서 실시간으로 발행되는 메세지를 수신하기 위해서는 블로킹을 통한 폴링을 계속 할 수 없으니까
 * 비동기로 수신시에 받을 수 있는 리스너가 필요하다. -> OrderEventStreamListener
 */
@SpringBootApplication
public class PaymentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentServiceApplication.class, args);
	}

}
