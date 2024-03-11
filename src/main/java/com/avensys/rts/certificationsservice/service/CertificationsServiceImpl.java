package com.avensys.rts.certificationsservice.service;

import java.util.List;

import java.util.Optional;

import com.avensys.rts.certificationsservice.payloadnewrequest.CertificationsListRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avensys.rts.certificationsservice.APIClient.FormSubmissionAPIClient;
import com.avensys.rts.certificationsservice.APIClient.UserAPIClient;
import com.avensys.rts.certificationsservice.customresponse.HttpResponse;
import com.avensys.rts.certificationsservice.entity.UserEntity;
import com.avensys.rts.certificationsservice.entity.CertificationsEntity;
import com.avensys.rts.certificationsservice.payloadnewrequest.FormSubmissionsRequestDTO;
import com.avensys.rts.certificationsservice.payloadnewrequest.CertificationsRequestDTO;
import com.avensys.rts.certificationsservice.payloadnewresponse.FormSubmissionsResponseDTO;
import com.avensys.rts.certificationsservice.payloadnewresponse.CertificationsResponseDTO;
import com.avensys.rts.certificationsservice.repository.UserRepository;
import com.avensys.rts.certificationsservice.repository.CertificationsRepository;
import com.avensys.rts.certificationsservice.util.MappingUtil;

import jakarta.transaction.Transactional;

@Service
public class CertificationsServiceImpl implements CertificationsService {

	private final String CANDIDATE_CERTIFICATIONS_TYPE = "candidate_certifications";

	private final Logger log = LoggerFactory.getLogger(CertificationsServiceImpl.class);
	private final CertificationsRepository certificationsRepository;

	@Autowired
	private UserAPIClient userAPIClient;

	@Autowired
	private FormSubmissionAPIClient formSubmissionAPIClient;

	@Autowired
	private UserRepository userRepository;

	public CertificationsServiceImpl(CertificationsRepository certificationsRepository, UserAPIClient userAPIClient,
			FormSubmissionAPIClient formSubmissionAPIClient) {
		this.certificationsRepository = certificationsRepository;
		this.userAPIClient = userAPIClient;
		this.formSubmissionAPIClient = formSubmissionAPIClient;
	}

	@Override
	@Transactional
	public CertificationsResponseDTO createCertifications(CertificationsRequestDTO certificationsRequestDTO) {
		log.info("Creating certifications: service");
		System.out.println("certifications: " + certificationsRequestDTO);
		CertificationsEntity savedCertificationsEntity = certificationsRequestDTOToCertificationsEntity(certificationsRequestDTO);

		// Save form data to form submission microservice
		FormSubmissionsRequestDTO formSubmissionsRequestDTO = new FormSubmissionsRequestDTO();
		formSubmissionsRequestDTO.setUserId(certificationsRequestDTO.getCreatedBy());
		formSubmissionsRequestDTO.setFormId(certificationsRequestDTO.getFormId());
		formSubmissionsRequestDTO
				.setSubmissionData(MappingUtil.convertJSONStringToJsonNode(certificationsRequestDTO.getFormData()));
		formSubmissionsRequestDTO.setEntityId(savedCertificationsEntity.getId());
		formSubmissionsRequestDTO.setEntityType(certificationsRequestDTO.getEntityType());
		HttpResponse formSubmissionResponse = formSubmissionAPIClient.addFormSubmission(formSubmissionsRequestDTO);
		FormSubmissionsResponseDTO formSubmissionData = MappingUtil
				.mapClientBodyToClass(formSubmissionResponse.getData(), FormSubmissionsResponseDTO.class);

		savedCertificationsEntity.setFormSubmissionId(formSubmissionData.getId());
		certificationsRepository.save(savedCertificationsEntity);
		return certificationsEntityToCertificationsResponseDTO(savedCertificationsEntity);
	}

	@Override
	public void createCertificationsList(CertificationsListRequestDTO certificationsListRequestDTO) {
		certificationsListRequestDTO.getCertificationsList().forEach(this::createCertifications);
	}

	@Override
	public CertificationsResponseDTO getCertificationsById(Integer id) {
		CertificationsEntity certificationsEntityFound = certificationsRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Certification is  not found"));
		return certificationsEntityToCertificationsResponseDTO(certificationsEntityFound);
	}

	@Override
	@Transactional
	public CertificationsResponseDTO updateCertifications(Integer id, CertificationsRequestDTO certificationsRequestDTO) {
		CertificationsEntity certificationsEntityFound = certificationsRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Certification is not found"));
		CertificationsEntity updatedCertificationsEntity = updateCertificationsRequestDTOToCertificationsEntity(certificationsEntityFound,
				certificationsRequestDTO);

		// Update form submission
		FormSubmissionsRequestDTO formSubmissionsRequestDTO = new FormSubmissionsRequestDTO();
		formSubmissionsRequestDTO.setUserId(certificationsRequestDTO.getUpdatedBy());
		formSubmissionsRequestDTO.setFormId(certificationsRequestDTO.getFormId());
		formSubmissionsRequestDTO
				.setSubmissionData(MappingUtil.convertJSONStringToJsonNode(certificationsRequestDTO.getFormData()));
		formSubmissionsRequestDTO.setEntityId(updatedCertificationsEntity.getId());
		formSubmissionsRequestDTO.setEntityType(certificationsRequestDTO.getEntityType());
		HttpResponse formSubmissionResponse = formSubmissionAPIClient
				.updateFormSubmission(updatedCertificationsEntity.getFormSubmissionId(), formSubmissionsRequestDTO);
		FormSubmissionsResponseDTO formSubmissionData = MappingUtil
				.mapClientBodyToClass(formSubmissionResponse.getData(), FormSubmissionsResponseDTO.class);

		updatedCertificationsEntity.setFormSubmissionId(formSubmissionData.getId());
		return certificationsEntityToCertificationsResponseDTO(updatedCertificationsEntity);
	}

	@Override
	@Transactional
	public void deleteCertifications(Integer id) {
		CertificationsEntity certificationsEntityFound = certificationsRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Certification is not found"));
		certificationsRepository.delete(certificationsEntityFound);
	}

	@Override
	public List<CertificationsResponseDTO> getCertificationsByEntityTypeAndEntityId(String entityType, Integer entityId) {
		List<CertificationsEntity> certificationsEntityList = certificationsRepository.findByEntityTypeAndEntityId(entityType,
				entityId);
		List<CertificationsResponseDTO> certificationsResponseDTOList = certificationsEntityList.stream()
				.map(this::certificationsEntityToCertificationsResponseDTO).toList();
		return certificationsResponseDTOList;
	}

	@Override
	@Transactional
	public void deleteCertificationsByEntityTypeAndEntityId(String entityType, Integer entityId) {
		List<CertificationsEntity> certificationsEntityList = certificationsRepository.findByEntityTypeAndEntityId(entityType,
				entityId);
		if (!certificationsEntityList.isEmpty()) {
			// Delete each Certification form submission before deleting
			certificationsEntityList.forEach(certificationsEntity -> {
				formSubmissionAPIClient.deleteFormSubmission(certificationsEntity.getFormSubmissionId());
				certificationsRepository.delete(certificationsEntity);
			});
		}
	}

	private CertificationsResponseDTO certificationsEntityToCertificationsResponseDTO(CertificationsEntity certificationsEntity) {
		CertificationsResponseDTO certificationsResponseDTO = new CertificationsResponseDTO();
		certificationsResponseDTO.setId(certificationsEntity.getId());
		certificationsResponseDTO.setCreatedAt(certificationsEntity.getCreatedAt());
		certificationsResponseDTO.setUpdatedAt(certificationsEntity.getUpdatedAt());
		certificationsResponseDTO.setEntityType(certificationsEntity.getEntityType());
		certificationsResponseDTO.setEntityId(certificationsEntity.getEntityId());
		certificationsResponseDTO.setFormId(certificationsEntity.getFormId());
		certificationsResponseDTO.setFormSubmissionId(certificationsEntity.getFormSubmissionId());

		// Get created by User data from user microservice
		Optional<UserEntity> userEntity = userRepository.findById(certificationsEntity.getCreatedBy());
		UserEntity userData = userEntity.get();
		certificationsResponseDTO.setCreatedBy(userData.getFirstName() + " " + userData.getLastName());

		// Get updated by user data from user microservice
		if (certificationsEntity.getUpdatedBy() == certificationsEntity.getCreatedBy()) {
			certificationsResponseDTO.setUpdatedBy(userData.getFirstName() + " " + userData.getLastName());
		} else {
			userEntity = userRepository.findById(certificationsEntity.getUpdatedBy());
			userData = userEntity.get();
			certificationsResponseDTO.setUpdatedBy(userData.getFirstName() + " " + userData.getLastName());
		}

		// Get form submission data
		HttpResponse formSubmissionResponse = formSubmissionAPIClient
				.getFormSubmission(certificationsEntity.getFormSubmissionId());
		FormSubmissionsResponseDTO formSubmissionData = MappingUtil
				.mapClientBodyToClass(formSubmissionResponse.getData(), FormSubmissionsResponseDTO.class);
		certificationsResponseDTO
				.setSubmissionData(MappingUtil.convertJsonNodeToJSONString(formSubmissionData.getSubmissionData()));
		return certificationsResponseDTO;
	}

	private CertificationsEntity updateCertificationsRequestDTOToCertificationsEntity(CertificationsEntity certificationsEntity,
			CertificationsRequestDTO certificationsRequestDTO) {
		certificationsEntity.setEntityType(certificationsRequestDTO.getEntityType());
		certificationsEntity.setEntityId(certificationsRequestDTO.getEntityId());
		certificationsEntity.setUpdatedBy(certificationsRequestDTO.getUpdatedBy());
		certificationsEntity.setFormId(certificationsRequestDTO.getFormId());
		return certificationsRepository.save(certificationsEntity);
	}

	private CertificationsEntity certificationsRequestDTOToCertificationsEntity(CertificationsRequestDTO certificationsRequestDTO) {
		CertificationsEntity certificationsEntity = new CertificationsEntity();
		certificationsEntity.setEntityType(certificationsRequestDTO.getEntityType());
		certificationsEntity.setEntityId(certificationsRequestDTO.getEntityId());
		certificationsEntity.setCreatedBy(certificationsRequestDTO.getCreatedBy());
		certificationsEntity.setUpdatedBy(certificationsRequestDTO.getUpdatedBy());
		certificationsEntity.setFormId(certificationsRequestDTO.getFormId());
		return certificationsRepository.save(certificationsEntity);
	}

}
