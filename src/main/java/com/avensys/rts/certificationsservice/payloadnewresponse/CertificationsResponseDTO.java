package com.avensys.rts.certificationsservice.payloadnewresponse;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificationsResponseDTO {
	private Integer id;
	private Integer formId;
	private String submissionData;
	private Integer formSubmissionId;
	private String entityType;
	private Integer entityId;
	private String createdBy;
	private String updatedBy;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
