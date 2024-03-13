package com.avensys.rts.certificationsservice.service;

import java.util.List;

import com.avensys.rts.certificationsservice.payloadnewrequest.CertificationsListRequestDTO;
import com.avensys.rts.certificationsservice.payloadnewrequest.CertificationsRequestDTO;
import com.avensys.rts.certificationsservice.payloadnewresponse.CertificationsResponseDTO;

public interface CertificationsService {

    CertificationsResponseDTO createCertifications(CertificationsRequestDTO contactNewRequestDTO);

    void createCertificationsList(CertificationsListRequestDTO certificationsListRequestDTO);

    CertificationsResponseDTO getCertificationsById(Integer id);

    CertificationsResponseDTO updateCertifications(Integer id, CertificationsRequestDTO contactNewRequestDTO);

    void deleteCertifications(Integer id);

    List<CertificationsResponseDTO> getCertificationsByEntityTypeAndEntityId(String entityType, Integer entityId);

    void deleteCertificationsByEntityTypeAndEntityId(String entityType, Integer entityId);
}
