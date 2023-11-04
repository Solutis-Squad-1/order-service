package br.com.solutis.squad1.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"br.com.solutis.squad1.authorizationmodule", "br.com.solutis.squad1.orderservice"})
public class OrderserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderserviceApplication.class, args);
	}

}
