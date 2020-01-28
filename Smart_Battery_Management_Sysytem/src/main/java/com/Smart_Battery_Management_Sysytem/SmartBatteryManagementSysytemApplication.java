package com.Smart_Battery_Management_Sysytem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableMongoRepositories("com.sbms.repositories")
public class SmartBatteryManagementSysytemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartBatteryManagementSysytemApplication.class, args);
	}

}
