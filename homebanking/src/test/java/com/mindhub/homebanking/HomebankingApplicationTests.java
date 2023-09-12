package com.mindhub.homebanking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class HomebankingApplicationTests {
	@Autowired
	PasswordEncoder passwordEncoder;
	@Test
	void contextLoads() {

	}

}
