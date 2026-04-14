package com.mhridin.pts_history_service.mongodb.repository;

import com.mhridin.pts_history_service.mongodb.entity.PriceHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceHistoryRepository extends MongoRepository<PriceHistory, String> {

    Page<PriceHistory> findAllByProductId(Long productId, Pageable pageable);
}
