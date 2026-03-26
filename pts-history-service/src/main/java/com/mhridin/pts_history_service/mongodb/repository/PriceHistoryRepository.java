package com.mhridin.pts_history_service.mongodb.repository;

import com.mhridin.pts_history_service.mongodb.entity.PriceHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceHistoryRepository extends MongoRepository<PriceHistory, String> {

    List<PriceHistory> findAllByProductIdOrderByTimestampAsc(Long productId);
}
