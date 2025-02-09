package com.avensys.rts.certificationsservice.payloadnewrequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CertificationsListRequestDTO {
	List<CertificationsRequestDTO> certificationsList;
}
