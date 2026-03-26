package com.mhridin.pts_history_service.mongodb.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "price_history")
@Data
@Builder
public class PriceHistory {

    @Id
    private String id;

    @Indexed(name = "productIdIndex")
    private Long productId;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal price;

    @Indexed(name = "timestampIndex")
    private LocalDateTime timestamp;
}
