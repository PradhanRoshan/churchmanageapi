package com.chms.churchmanageapi;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.text.SimpleDateFormat;

@SpringBootApplication
@EnableAsync
@EnableCaching
@EntityScan("com.chms.churchmanageapi.domain")
@ComponentScan(basePackages = {"com.chms.churchmanageapi.*"})
//@SpringBootApplication(scanBasePackages = "com.chms.churchmanageapi")
public class ChurchManageApiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ChurchManageApiApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
		return application.sources(ChurchManageApiApplication.class);
	}

	@Bean("threadPoolTaskExexutor")
	public TaskExecutor getAsyncExecutor(){
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(20);
		executor.setMaxPoolSize(1000);
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setThreadNamePrefix("Async-");
		return executor;
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		objectMapper.registerModule(new JavaTimeModule());
//		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
		return objectMapper;
	}

}
