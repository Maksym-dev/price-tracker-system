package com.mhridin.pts_admin_service.controller;

import com.mhridin.pts_common.entity.DomainConfig;
import com.mhridin.pts_common.repository.DomainConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/domain-configs")
public class DomainConfigRestController {

    private final DomainConfigRepository domainConfigRepository;

    @Autowired
    public DomainConfigRestController(DomainConfigRepository domainConfigRepository) {
        this.domainConfigRepository = domainConfigRepository;
    }

    @GetMapping
    public ResponseEntity<Page<DomainConfig>> getAllDomainConfigs(@PageableDefault(sort = "domain", direction = Sort.Direction.ASC)
                                                      Pageable pageable) {
        Page<DomainConfig> all = domainConfigRepository.findAll(pageable);
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DomainConfig> getDomainConfigById(@PathVariable Long id) {
        return ResponseEntity.ok(domainConfigRepository.findById(id).orElseThrow(() -> new RuntimeException("DomainConfig with id " + id + " not found")));
    }

    @PostMapping
    public ResponseEntity<DomainConfig> createDomainConfig(@RequestBody DomainConfig domainConfig) {
        return ResponseEntity.status(HttpStatus.CREATED).body(domainConfigRepository.save(domainConfig));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDomainConfig(@PathVariable("id") Long id, @RequestBody DomainConfig domainConfig) {
        if (!Objects.equals(domainConfig.getId(), id)) {
            throw new IllegalStateException("DomainConfig id and path variable are not the same");
        }
        DomainConfig fromDB = domainConfigRepository.findById(id).orElse(null);
        if (fromDB == null) {
            throw new RuntimeException("DomainConfig with id " + id + " not found");
        }
        fromDB.setDomain(domainConfig.getDomain());
        fromDB.setPriceSelector(domainConfig.getPriceSelector());
        fromDB.setAvailableStatusSelector(domainConfig.getAvailableStatusSelector());
        fromDB.setAvailableStatusText(domainConfig.getAvailableStatusText());
        fromDB.setActive(domainConfig.isActive());
        domainConfigRepository.save(fromDB);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDomainConfig(@PathVariable("id") Long id) {
        DomainConfig fromDB = domainConfigRepository.findById(id).orElse(null);
        if (fromDB == null) {
            throw new RuntimeException("DomainConfig with id " + id + " not found");
        }
        domainConfigRepository.delete(fromDB);
        return ResponseEntity.noContent().build();
    }
}
