package com.mhridin.pts_common.repository;

import com.mhridin.pts_common.entity.DomainConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DomainConfigRepository extends CrudRepository<DomainConfig, Long> {

    Page<DomainConfig> findAll(Pageable pageable);

    Optional<DomainConfig> findByDomainAndIsActiveTrue(String domain);
}
