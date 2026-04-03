package com.mhridin.pts_common.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {
    private String email;
    private String productTitle;
    private BigDecimal newPrice;
    private String url;
}
