package com.chms.churchmanageapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class ChurchManageApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChurchManageApiApplication.class, args);
	}

}
