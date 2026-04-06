package com.mhridin.pts_common.repository;

import com.mhridin.pts_common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    Page<Product> findAll(Pageable pageable);

    Slice<Product> findByLastUpdatedBeforeOrLastUpdatedIsNull(LocalDateTime threshold, Pageable pageable);
}
