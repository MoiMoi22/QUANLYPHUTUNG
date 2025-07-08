package com.tld_store.DemoDao;

import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoDaoApplication {

	public static void main(String[] args) throws SQLException {
		SpringApplication.run(DemoDaoApplication.class, args);
	}

}
