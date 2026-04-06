package com.mhridin.pts_common.dto;

import com.mhridin.pts_common.entity.Subscription;
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

    public SubscriptionDto(Subscription subscription) {
        id = subscription.getId();
        userId = subscription.getUser().getId();
        productId = subscription.getProduct().getId();
        targetPrice = subscription.getTargetPrice();
        isActive = subscription.isActive();
    }
}
