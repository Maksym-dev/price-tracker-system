package com.mhridin.pts_scraper_service.scraper;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class RozetkaScrapingStrategy extends AbstractJsoupScraping {

    private static final String ROZETKA_DOMAIN = "rozetka.com.ua";
    private static final String CSS_SELECTOR = ".product-price__big";

    public RozetkaScrapingStrategy() {
        super(ROZETKA_DOMAIN);
    }

    @Override
    protected BigDecimal extractPriceFromDocument(Document doc) {
        Element priceElement = doc.selectFirst(CSS_SELECTOR);

        if (priceElement == null) {
            log.info("Price element not found for {}", ROZETKA_DOMAIN);
            return new BigDecimal(0);
        }

        // Clear string
        String priceText = priceElement.text().replaceAll("[^0-9,.]", "").replace(",", ".");
        return new BigDecimal(priceText);
    }
}
