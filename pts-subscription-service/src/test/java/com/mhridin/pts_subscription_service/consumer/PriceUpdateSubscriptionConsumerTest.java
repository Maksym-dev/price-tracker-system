package com.mhridin.pts_subscription_service.consumer;

import com.mhridin.pts_common.entity.Product;
import com.mhridin.pts_common.entity.Subscription;
import com.mhridin.pts_common.kafka.PriceUpdateEvent;
import com.mhridin.pts_common.repository.ProductRepository;
import com.mhridin.pts_common.repository.SubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceUpdateSubscriptionConsumerTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private PriceUpdateSubscriptionConsumer priceUpdateSubscriptionConsumer;

    @Test
    void testConsumePriceUpdate() {
        PriceUpdateEvent event = new PriceUpdateEvent();
        event.setStatus("SUCCESS");
        event.setProductId(1L);
        event.setNewPrice(BigDecimal.valueOf(100));
        Product product = new Product();
        product.setId(1L);
        Subscription subscription = new Subscription();
        subscription.setId(2L);
        subscription.setProduct(product);
        subscription.setTargetPrice(BigDecimal.valueOf(50));

        when(productRepository.findById(event.getProductId())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(subscriptionRepository.findAllByProductIdAndIsActiveTrue(product.getId())).thenReturn(List.of(subscription));

        priceUpdateSubscriptionConsumer.consumePriceUpdate(event);

        verify(productRepository, times(1)).findById(event.getProductId());
        verify(productRepository, times(1)).save(any(Product.class));
        verify(subscriptionRepository, times(1)).findAllByProductIdAndIsActiveTrue(product.getId());
    }

    @Test
    void testConsumePriceUpdateWithFailedStatus() {
        PriceUpdateEvent event = new PriceUpdateEvent();
        event.setStatus("FAILED");
        event.setProductId(1L);
        event.setNewPrice(BigDecimal.valueOf(100));
        Product product = new Product();
        product.setId(1L);
        Subscription subscription = new Subscription();
        subscription.setId(2L);
        subscription.setProduct(product);
        subscription.setTargetPrice(BigDecimal.valueOf(50));

        priceUpdateSubscriptionConsumer.consumePriceUpdate(event);

        verify(productRepository, times(0)).findById(event.getProductId());
        verify(productRepository, times(0)).save(any(Product.class));
        verify(subscriptionRepository, times(0)).findAllByProductIdAndIsActiveTrue(product.getId());
    }

    @Test
    void testConsumePriceUpdateAndNotify() {
        PriceUpdateEvent event = new PriceUpdateEvent();
        event.setStatus("SUCCESS");
        event.setProductId(1L);
        event.setNewPrice(BigDecimal.valueOf(100));
        event.setAvailableStatus(true);
        Product product = new Product();
        product.setId(1L);
        Subscription subscription = new Subscription();
        subscription.setId(2L);
        subscription.setProduct(product);
        subscription.setTargetPrice(BigDecimal.valueOf(150));

        when(productRepository.findById(event.getProductId())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(subscriptionRepository.findAllByProductIdAndIsActiveTrue(product.getId())).thenReturn(List.of(subscription));

        priceUpdateSubscriptionConsumer.consumePriceUpdate(event);

        verify(productRepository, times(1)).findById(event.getProductId());
        verify(productRepository, times(1)).save(any(Product.class));
        verify(subscriptionRepository, times(1)).findAllByProductIdAndIsActiveTrue(product.getId());
    }
}