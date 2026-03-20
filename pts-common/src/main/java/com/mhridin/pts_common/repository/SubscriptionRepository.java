package com.mhridin.pts_common.repository;

import com.mhridin.pts_common.entity.Subscription;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {

    @EntityGraph(attributePaths = {"user"})
    List<Subscription> findAllByProductIdAndIsActiveTrue(Long id);
}
