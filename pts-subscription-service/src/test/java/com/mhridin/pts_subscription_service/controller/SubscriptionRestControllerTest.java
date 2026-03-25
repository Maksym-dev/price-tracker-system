package com.mhridin.pts_subscription_service.controller;

import com.mhridin.pts_common.dto.SubscriptionDto;
import com.mhridin.pts_common.entity.Product;
import com.mhridin.pts_common.entity.Subscription;
import com.mhridin.pts_common.entity.User;
import com.mhridin.pts_common.exception.SubscriptionNotFoundException;
import com.mhridin.pts_common.kafka.PriceCheckEvent;
import com.mhridin.pts_common.repository.ProductRepository;
import com.mhridin.pts_common.repository.SubscriptionRepository;
import com.mhridin.pts_common.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionRestControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private KafkaTemplate<String, PriceCheckEvent> kafkaTemplate;

    @InjectMocks
    private SubscriptionRestController subscriptionRestController;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(subscriptionRestController, "priceCheckTopic", "test-topic");
    }

    @Test
    void testGetAllSubscriptions() {
        List<Subscription> all = new ArrayList<>();
        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setProduct(new Product());
        subscription.setUser(new User());
        all.add(subscription);

        when(subscriptionRepository.findAll()).thenReturn(all);

        List<SubscriptionDto> allSubscriptions = subscriptionRestController.getAllSubscriptions();

        verify(subscriptionRepository, times(1)).findAll();
        assertThat(allSubscriptions).isNotEmpty();
    }

    @Test
    void testGetSubscriptionById() {
        Subscription subscription = new Subscription();
        subscription.setProduct(new Product());
        subscription.setUser(new User());
        long subscriptionId = 1L;
        subscription.setId(subscriptionId);

        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(subscription));

        ResponseEntity<SubscriptionDto> subscriptionById = subscriptionRestController.getSubscriptionById(subscriptionId);

        verify(subscriptionRepository, times(1)).findById(subscriptionId);
        SubscriptionDto result = subscriptionById.getBody();
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(subscriptionId);
    }

    @Test
    void testGetSubscriptionByIdThrowSubscriptionNotFoundException() {
        long subscriptionId = 1L;

        when(subscriptionRepository.findById(subscriptionId)).thenThrow(new SubscriptionNotFoundException("Subscription with id " + subscriptionId + " not found"));

        assertThrows(SubscriptionNotFoundException.class, () -> subscriptionRestController.getSubscriptionById(subscriptionId));

        verify(subscriptionRepository, times(1)).findById(subscriptionId);
    }

    @Test
    void testCreateSubscription() {
        User user = new User();
        user.setId(1L);
        String url = "https://example.com/item";

        Product product = new Product();
        product.setId(10L);
        product.setUrl(url);

        Subscription subscription = new Subscription();

        SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setUserId(user.getId());
        subscriptionDto.setProductId(product.getId());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);
        when(kafkaTemplate.send(anyString(), anyString(), any(PriceCheckEvent.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        subscriptionRestController.createSubscription(subscriptionDto);

        ArgumentCaptor<PriceCheckEvent> eventCaptor = ArgumentCaptor.forClass(PriceCheckEvent.class);
        verify(kafkaTemplate).send(eq("test-topic"), eq("10"), eventCaptor.capture());

        PriceCheckEvent capturedEvent = eventCaptor.getValue();
        assertThat(capturedEvent.getProductId()).isEqualTo(10L);
        assertThat(capturedEvent.getUrl()).isEqualTo(url);
    }

    @Test
    void testUpdateSubscription() {
        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setTargetPrice(BigDecimal.valueOf(100));
        Subscription fromDb = new Subscription();
        fromDb.setId(1L);
        fromDb.setTargetPrice(BigDecimal.valueOf(50));

        when(subscriptionRepository.findById(subscription.getId())).thenReturn(Optional.of(fromDb));
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        subscriptionRestController.updateSubscription(1L, subscription);

        ArgumentCaptor<Subscription> eventCaptor = ArgumentCaptor.forClass(Subscription.class);
        verify(subscriptionRepository, times(1)).findById(subscription.getId());
        verify(subscriptionRepository, times(1)).save(eventCaptor.capture());

        Subscription captured = eventCaptor.getValue();
        assertThat(captured.getId()).isEqualTo(1L);
        assertThat(captured.getTargetPrice()).isEqualTo(subscription.getTargetPrice());
    }

    @Test
    void testUpdateSubscriptionThrowsIllegalStateException() {
        Subscription subscription = new Subscription();
        subscription.setId(1L);

        assertThrows(IllegalStateException.class, () -> subscriptionRestController.updateSubscription(2L, subscription));

        verify(subscriptionRepository, times(0)).findById(subscription.getId());
        verify(subscriptionRepository, times(0)).save(subscription);
    }

    @Test
    void testUpdateSubscriptionThrowsSubscriptionNotFoundException() {
        Subscription subscription = new Subscription();
        subscription.setId(1L);

        when(subscriptionRepository.findById(subscription.getId())).thenReturn(Optional.empty());

        assertThrows(SubscriptionNotFoundException.class, () -> subscriptionRestController.updateSubscription(1L, subscription));

        verify(subscriptionRepository, times(1)).findById(subscription.getId());
        verify(subscriptionRepository, times(0)).save(subscription);
    }

    @Test
    void testDeleteSubscription() {
        Subscription fromDb = new Subscription();
        fromDb.setId(1L);

        when(subscriptionRepository.findById(fromDb.getId())).thenReturn(Optional.of(fromDb));

        subscriptionRestController.deleteSubscription(1L);

        verify(subscriptionRepository, times(1)).findById(fromDb.getId());
        verify(subscriptionRepository, times(1)).delete(fromDb);
    }

    @Test
    void testDeleteSubscriptionThrowsSubscriptionNotFoundException() {
        Subscription fromDb = new Subscription();
        fromDb.setId(1L);

        when(subscriptionRepository.findById(fromDb.getId())).thenReturn(Optional.empty());

        assertThrows(SubscriptionNotFoundException.class, () -> subscriptionRestController.deleteSubscription(1L));

        verify(subscriptionRepository, times(1)).findById(fromDb.getId());
        verify(subscriptionRepository, times(0)).delete(fromDb);
    }
}