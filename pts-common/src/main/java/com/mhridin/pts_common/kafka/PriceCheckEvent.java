package com.mhridin.pts_common.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceCheckEvent {
    private Long productId;
    private String url;
}
