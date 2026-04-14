package com.mhridin.pts_history_service.controller;

import com.mhridin.pts_history_service.dto.PriceHistoryDto;
import com.mhridin.pts_history_service.mongodb.repository.PriceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {

    private final PriceHistoryRepository repository;

    @GetMapping("/{productId}")
    public ResponseEntity<Page<PriceHistoryDto>> getProductHistory(@PathVariable Long productId,
                                                                   @PageableDefault(sort = "timestamp", direction = Sort.Direction.ASC)
                                                                   Pageable pageable) {
        Page<PriceHistoryDto> list = repository.findAllByProductId(productId, pageable)
                .map(h -> new PriceHistoryDto(h.getPrice(), h.getTimestamp()));
        return ResponseEntity.ok(list);
    }
}
