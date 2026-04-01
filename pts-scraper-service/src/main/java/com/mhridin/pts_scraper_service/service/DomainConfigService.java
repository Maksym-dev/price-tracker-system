package com.mhridin.pts_scraper_service.service;

import com.mhridin.pts_common.entity.DomainConfig;
import com.mhridin.pts_common.repository.DomainConfigRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DomainConfigService {

    private DomainConfigRepository domainConfigRepository;

    @Cacheable(value = "domainConfigs", key = "#domain")
    public DomainConfig findDomainConfigByDomain(String domain) {
        return domainConfigRepository.findByDomainAndIsActiveTrue(domain).orElse(null);
    }
}
