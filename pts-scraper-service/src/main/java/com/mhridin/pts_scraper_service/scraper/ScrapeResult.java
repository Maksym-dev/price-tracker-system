package com.mhridin.pts_scraper_service.scraper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScrapeResult {
    private BigDecimal price;
    private boolean availableStatus;
}
