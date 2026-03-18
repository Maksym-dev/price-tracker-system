package com.mhridin.pts_common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SubscriptionDto {
    private Long id;
    private Long userId;
    private Long productId;
    private BigDecimal targetPrice;
    private boolean isActive;
}
