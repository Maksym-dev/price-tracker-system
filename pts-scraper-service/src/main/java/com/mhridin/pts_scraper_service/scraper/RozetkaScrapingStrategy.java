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
    private static final String PRICE_CSS_SELECTOR = ".product-price__big";
    private static final String AVAILABLE_STATUS_CSS_SELECTOR = ".status-label";
    private static final String AVAILABLE_STATUS_TEXT = "Є в наявності";

    public RozetkaScrapingStrategy() {
        super(ROZETKA_DOMAIN);
    }

    @Override
    protected BigDecimal extractPriceFromDocument(Document doc) {
        Element priceElement = doc.selectFirst(PRICE_CSS_SELECTOR);

        if (priceElement == null) {
            log.info("Price element not found for {}", ROZETKA_DOMAIN);
            return new BigDecimal(0);
        }

        // Clear string
        String priceText = priceElement.text().replaceAll("[^0-9,.]", "").replace(",", ".");
        return new BigDecimal(priceText);
    }

    @Override
    protected boolean extractAvailableStatusFromDocument(Document doc) {
        Element statusElement = doc.selectFirst(AVAILABLE_STATUS_CSS_SELECTOR);

        if (statusElement == null) {
            log.info("Status element not found for {}", ROZETKA_DOMAIN);
            return false;
        }

        String statusText = statusElement.text().trim();
        return statusText.equalsIgnoreCase(AVAILABLE_STATUS_TEXT);
    }
}
