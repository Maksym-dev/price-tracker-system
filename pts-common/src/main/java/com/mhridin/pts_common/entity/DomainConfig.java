package com.mhridin.pts_common.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "domain_configs")
@Data
public class DomainConfig implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String domain;

    @Column(name = "price_selector", nullable = false)
    private String priceSelector;

    @Column(name = "available_status_selector")
    private String availableStatusSelector;

    @Column(name = "available_status_text")
    private String availableStatusText;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;
}
