package com.huyntd.superapp.gundam_shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class GundamShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(GundamShopApplication.class, args);
	}

}
