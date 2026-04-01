package com.mhridin.pts_scraper_service.scraper;

import com.mhridin.pts_common.entity.DomainConfig;
import com.mhridin.pts_scraper_service.service.DomainConfigService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.mhridin.pts_common.utils.DomainConfigUtils.getDomain;

@Component
@AllArgsConstructor
@Slf4j
public class ConfigurableJsoupScraping implements ScrapingStrategy {

    private final DomainConfigService domainConfigService;

    @Override
    public ScrapeResult fetch(String url) {
        try {
            String domain = getDomain(url);

            DomainConfig config = domainConfigService.findDomainConfigByDomain(domain);

            if (config == null) {
                throw new RuntimeException("No config for domain: " + domain);
            }

            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(5000)
                    .get();

            BigDecimal price = extractPriceFromDocument(doc, config.getPriceSelector());
            boolean availableStatus = extractAvailableStatusFromDocument(doc, config.getAvailableStatusSelector(),
                    config.getAvailableStatusText());
            return new ScrapeResult(price, availableStatus);
        } catch (Exception e) {
            log.error("Error scraping Jsoup site {}: {}", url, e.getMessage());
            return new ScrapeResult(new BigDecimal(0), false);
        }
    }

    @Override
    public boolean supports(String url) {
        return domainConfigService.findDomainConfigByDomain(getDomain(url)) != null;
    }

    protected BigDecimal extractPriceFromDocument(Document doc, String priceSelector) {
        Element priceElement = doc.selectFirst(priceSelector);

        if (priceElement == null) {
            log.warn("Price element not found for selector: {}", priceSelector);
            return new BigDecimal(0);
        }

        // Clear string
        String priceText = priceElement.text().replaceAll("[^0-9,.]", "").replace(",", ".");
        return new BigDecimal(priceText);
    }

    protected boolean extractAvailableStatusFromDocument(Document doc, String availableStatusSelector,
                                                         String availableStatusText) {
        Element statusElement = doc.selectFirst(availableStatusSelector);

        if (statusElement == null) {
            log.warn("Status element not found for selector: {}", availableStatusSelector);
            return false;
        }

        String statusText = statusElement.text().trim();
        return statusText.equalsIgnoreCase(availableStatusText);
    }
}
