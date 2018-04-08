package com.springapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan
@PropertySource("classpath:credentials.properties")
public class SpringbootBigqueryAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootBigqueryAppApplication.class, args);
	}
}
