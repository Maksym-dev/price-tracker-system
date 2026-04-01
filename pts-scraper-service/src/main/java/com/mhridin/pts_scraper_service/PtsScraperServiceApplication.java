package com.mhridin.pts_scraper_service;

import com.mhridin.pts_common.entity.DomainConfig;
import com.mhridin.pts_common.repository.DomainConfigRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = {DomainConfigRepository.class})
@EntityScan(basePackageClasses = {DomainConfig.class})
@EnableCaching
public class PtsScraperServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PtsScraperServiceApplication.class, args);
	}

}
