package com.avensys.rts.certificationsservice.controller;

import com.avensys.rts.certificationsservice.payloadnewrequest.CertificationsListRequestDTO;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.avensys.rts.certificationsservice.constant.MessageConstants;
import com.avensys.rts.certificationsservice.payloadnewrequest.CertificationsRequestDTO;
import com.avensys.rts.certificationsservice.payloadnewresponse.CertificationsResponseDTO;
import com.avensys.rts.certificationsservice.service.CertificationsServiceImpl;
import com.avensys.rts.certificationsservice.util.JwtUtil;
import com.avensys.rts.certificationsservice.util.ResponseUtil;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/certifications")
public class CertificationsController {

	private final Logger log = LoggerFactory.getLogger(CertificationsController.class);
	private final CertificationsServiceImpl certificationsServiceImpl;
	private final MessageSource messageSource;

	@Autowired
	private JwtUtil jwtUtil;

	public CertificationsController(CertificationsServiceImpl certificationsServiceImpl, MessageSource messageSource) {
		this.certificationsServiceImpl = certificationsServiceImpl;
		this.messageSource = messageSource;
	}

	@PostMapping("/add")
	public ResponseEntity<Object> createCertifications(@Valid @RequestBody CertificationsRequestDTO certificationsRequestDTO,
			@RequestHeader(name = "Authorization") String token) {
		log.info("Create a certifications : Controller ");
		Long userId = jwtUtil.getUserId(token);
		certificationsRequestDTO.setCreatedBy(userId);
		certificationsRequestDTO.setUpdatedBy(userId);
		CertificationsResponseDTO certificationsResponseDTO = certificationsServiceImpl.createCertifications(certificationsRequestDTO);
		return ResponseUtil.generateSuccessResponse(certificationsResponseDTO, HttpStatus.CREATED,
				messageSource.getMessage(MessageConstants.MESSAGE_CREATED, null, LocaleContextHolder.getLocale()));
	}

	@PostMapping("/add/list")
	public ResponseEntity<Object> createCertificationsList(@RequestBody CertificationsListRequestDTO certificationsListRequestDTO,
			@RequestHeader(name = "Authorization") String token) {
		log.info("Create a certifications : Controller ");
		Long userId = jwtUtil.getUserId(token);
		certificationsListRequestDTO.getCertificationsList().forEach(certificationsRequestDTO -> {
			certificationsRequestDTO.setCreatedBy(userId);
			certificationsRequestDTO.setUpdatedBy(userId);
		});
		certificationsServiceImpl.createCertificationsList(certificationsListRequestDTO);
		return ResponseUtil.generateSuccessResponse(null, HttpStatus.CREATED,
				messageSource.getMessage(MessageConstants.MESSAGE_CREATED, null, LocaleContextHolder.getLocale()));
	}

	@GetMapping("/entity/{entityType}/{entityId}")
	public ResponseEntity<Object> getCertificationsByEntityTypeAndEntityId(@PathVariable String entityType,
			@PathVariable Integer entityId) {
		log.info("Get certifications by entity type and entity id : Controller ");
		return ResponseUtil.generateSuccessResponse(
				certificationsServiceImpl.getCertificationsByEntityTypeAndEntityId(entityType, entityId), HttpStatus.OK,
				messageSource.getMessage(MessageConstants.MESSAGE_SUCCESS, null, LocaleContextHolder.getLocale()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteCertifications(@PathVariable Integer id) {
		log.info("Delete certifications : Controller ");
		certificationsServiceImpl.deleteCertifications(id);
		return ResponseUtil.generateSuccessResponse(null, HttpStatus.OK,
				messageSource.getMessage(MessageConstants.MESSAGE_SUCCESS, null, LocaleContextHolder.getLocale()));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> updateCertifications(@PathVariable Integer id,
			@Valid @RequestBody CertificationsRequestDTO certificationsRequestDTO,
			@RequestHeader(name = "Authorization") String token) {
		log.info("Update certifications : Controller ");
		Long userId = jwtUtil.getUserId(token);
		certificationsRequestDTO.setUpdatedBy(userId);
		CertificationsResponseDTO certificationsResponseDTO = certificationsServiceImpl.updateCertifications(id, certificationsRequestDTO);
		return ResponseUtil.generateSuccessResponse(certificationsResponseDTO, HttpStatus.OK,
				messageSource.getMessage(MessageConstants.MESSAGE_SUCCESS, null, LocaleContextHolder.getLocale()));
	}

	/**
	 * This endpoint is to delete certifications by entity type and entity id
	 * 
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	@DeleteMapping("/entity/{entityType}/{entityId}")
	public ResponseEntity<Object> deleteCertificationsByEntityTypeAndEntityId(@PathVariable String entityType,
			@PathVariable Integer entityId) {
		log.info("Delete certifications by entity type and entity id : Controller ");
		certificationsServiceImpl.deleteCertificationsByEntityTypeAndEntityId(entityType, entityId);
		return ResponseUtil.generateSuccessResponse(null, HttpStatus.OK,
				messageSource.getMessage(MessageConstants.MESSAGE_SUCCESS, null, LocaleContextHolder.getLocale()));
	}
}
