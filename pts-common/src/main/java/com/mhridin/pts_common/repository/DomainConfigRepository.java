package com.mhridin.pts_common.repository;

import com.mhridin.pts_common.entity.DomainConfig;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DomainConfigRepository extends CrudRepository<DomainConfig, Long> {

    Optional<DomainConfig> findByDomainAndIsActiveTrue(String domain);
}
