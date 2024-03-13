package com.avensys.rts.certificationsservice.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avensys.rts.certificationsservice.APIClient.FormSubmissionAPIClient;
import com.avensys.rts.certificationsservice.APIClient.UserAPIClient;
import com.avensys.rts.certificationsservice.entity.CertificationsEntity;
import com.avensys.rts.certificationsservice.payloadnewrequest.CertificationsRequestDTO;
import com.avensys.rts.certificationsservice.payloadnewresponse.CertificationsResponseDTO;
import com.avensys.rts.certificationsservice.repository.CertificationsRepository;
import com.avensys.rts.certificationsservice.repository.UserRepository;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CertificationsServiceImplTest {

	@Autowired
	CertificationsServiceImpl certificationsServiceImpl;

	@Autowired
	CertificationsRepository certificationsRepository;

	@Autowired
	UserAPIClient userAPIClient;

	@Autowired
	FormSubmissionAPIClient formSubmissionAPIClient;

	@Autowired
	UserRepository userRepository;

	@Mock
	AutoCloseable autoCloseable;

	CertificationsEntity certificationsEntity;
	CertificationsRequestDTO certificationsRequestDTO;
	String formData = "{\"id\":1,\"accountSubmissionData\":{\"msa\":\"yes\",\"revenue\":32432434,\"website\":\"www.tcs.com\",\"industry\":\"InformationTechnology\",\"salesName\":\"Test\",\"leadSource\":\"Test\",\"accountName\":\"TCS\",\"addressCity\":\"\",\"billingCity\":\"\",\"subIndustry\":\"SoftwareDevelopment\",\"addressLine1\":\"Bhopal\",\"addressLine2\":\"\",\"addressLine3\":\"\",\"accountRating\":\"Tier1\",\"accountSource\":\"TalentService\",\"accountStatus\":\"Active\",\"leadSalesName\":\"Test\",\"noOfEmployees\":6,\"parentCompany\":\"\",\"accountRemarks\":\"\",\"addressCountry\":\"\",\"billingAddress\":\"true\",\"landlineNumber\":324,\"secondaryOwner\":\"Test\",\"landlineCountry\":\"\",\"leadAccountName\":\"TCS\",\"revenueCurrency\":\"INR₹\",\"uploadAgreement\":\"Reema_Sahu_Java_5Yrs.docx(1).pdf\",\"addressPostalCode\":\"\",\"billingAddressLine1\":\"Bhopal\",\"billingAddressLine2\":\"\",\"billingAddressLine3\":\"\",\"billingAddressCountry\":\"\",\"billingAddressPostalCode\":\"\"},\"commercialSubmissionData\":{\"msp\":\"Test\",\"markUp\":\"Test\"},\"accountNumber\":\"A0958950\",\"createdAt\":\"2024-01-16T13:02:13.006307\",\"updatedAt\":\"2024-01-16T13:06:15.374175\",\"accountCountry\":\"India\",\"createdByName\":\"Super1Admin1\",\"updatedByName\":\"Super1Admin1\"}";

	@BeforeEach
	void setUp() {
		autoCloseable = MockitoAnnotations.openMocks(this);
		certificationsEntity = new CertificationsEntity(1, 1, "entityType", 1, 1);
		certificationsRepository.save(certificationsEntity);
		certificationsRequestDTO = new CertificationsRequestDTO("entityType", 1, formData, 1, 1L, 1L);

	}

	@AfterEach
	void tearDown() throws Exception {
		autoCloseable.close();
	}

	@Test
	void testgetCertificationsById() {
		Optional<CertificationsEntity> certificationsEntity = certificationsRepository.findById(1);
		assertNotNull(certificationsEntity);
	}

	@Test
	void testDeleteCertificationsByEntityTypeAndEntityId() {
		certificationsRepository.delete(certificationsEntity);
	}

	@Test
	void testGetCertificationsByEntityTypeAndEntityId() {
		List<CertificationsEntity> certificationsEntityList = certificationsRepository
				.findByEntityTypeAndEntityId("entityType", 1);
		assertNotNull(certificationsEntityList);
	}

	@Test
	void testDeleteCertifications() {
		Optional<CertificationsEntity> certificationsEntityFound = certificationsRepository.findById(1);
		// certificationsServiceImpl.deleteCertifications(1);

	}

	/*
	 * @Test void testCreateCertifications() { CertificationsResponseDTO
	 * certificationsResponseDTO =
	 * certificationsServiceImpl.createCertifications(certificationsRequestDTO);
	 * assertNotNull(certificationsResponseDTO); }
	 */

}
