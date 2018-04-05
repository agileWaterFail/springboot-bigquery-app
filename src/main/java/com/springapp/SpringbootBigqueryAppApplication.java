package com.springapp;

import controller.SearchController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@SpringBootApplication
@ComponentScan(basePackageClasses = SearchController.class)
public class SpringbootBigqueryAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootBigqueryAppApplication.class, args);
	}
}