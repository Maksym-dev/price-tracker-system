package com.mhridin.pts_admin_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.mhridin.pts_common.repository")
@EntityScan(basePackages = "com.mhridin.pts_common.entity")
public class PtsAdminServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PtsAdminServiceApplication.class, args);
	}

}
