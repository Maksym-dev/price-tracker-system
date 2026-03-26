package com.mhridin.pts_history_service.controller;

import com.mhridin.pts_history_service.dto.PriceHistoryDto;
import com.mhridin.pts_history_service.mongodb.repository.PriceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {

    private final PriceHistoryRepository repository;

    @GetMapping("/{productId}")
    public List<PriceHistoryDto> getProductHistory(@PathVariable Long productId) {
        return repository.findAllByProductIdOrderByTimestampAsc(productId)
                .stream()
                .map(h -> new PriceHistoryDto(h.getPrice(), h.getTimestamp()))
                .toList();
    }
}
