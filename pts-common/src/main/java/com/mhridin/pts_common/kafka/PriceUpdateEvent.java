package com.mhridin.pts_common.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceUpdateEvent {
    private Long productId;
    private BigDecimal newPrice;
    private String status; // SUCCESS, FAILED
}
