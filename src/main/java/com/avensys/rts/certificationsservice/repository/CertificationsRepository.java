package com.avensys.rts.certificationsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avensys.rts.certificationsservice.entity.CertificationsEntity;

import java.util.List;

public interface CertificationsRepository extends JpaRepository<CertificationsEntity, Integer> {
    List<CertificationsEntity> findByEntityTypeAndEntityId(String entityType, Integer entityId);
}
