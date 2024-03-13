package com.avensys.rts.certificationsservice.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.avensys.rts.certificationsservice.entity.CertificationsEntity;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CertificationsRepositoryTest {

	@Autowired
	CertificationsRepository certificationsRepository;

	@Test
	void testFindByEntityTypeAndEntityId() {
		List<CertificationsEntity> certificationsList = certificationsRepository
				.findByEntityTypeAndEntityId("entityType", 1);
		assertNotNull(certificationsList);
	}

}
