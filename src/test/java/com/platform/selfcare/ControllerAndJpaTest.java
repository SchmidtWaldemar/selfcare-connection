package com.platform.selfcare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.client.TestRestTemplate.HttpClientOption;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MariaDBContainer;

import com.platform.selfcare.entity.Role;
import com.platform.selfcare.entity.User;
import com.platform.selfcare.enums.RoleType;
import com.platform.selfcare.repository.RoleRepository;
import com.platform.selfcare.repository.UserRepository;

import io.github.bonigarcia.wdm.WebDriverManager;

@TestPropertySource(locations="classpath:test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("ssl")
class ControllerAndJpaTest {

	@Value("${server.ssl.key-store}")
	private Resource trustStore;

	@Value("${server.ssl.key-store-password}")
	private String trustStorePassword;

	private TestRestTemplate restTemplate;
	
	private ChromiumDriver driver;

	@LocalServerPort
	private Integer port;
	
	private final String serverUrl = "https://localhost:";
	
	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserRepository userRepository;

	static MariaDBContainer<?> mariadb = new MariaDBContainer<>("mariadb:11.4.2");

	@BeforeAll
	static void beforeAll() {
		//WebDriverManager.chromiumdriver().setup();
		//WebDriverManager.firefoxdriver().setup();
		mariadb.start();
	}

	@AfterAll
	static void afterAll() {
		mariadb.stop();
	}

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mariadb::getJdbcUrl);
		registry.add("spring.datasource.username", mariadb::getUsername);
		registry.add("spring.datasource.password", mariadb::getPassword);
		/**
		 * use that property to create tables out of the box
		 * otherwise create schema.sql file in root directory and paste all required table create statements
		 */
		registry.add("spring.jpa.hibernate.ddl-auto", ControllerAndJpaTest::getCreate);
	}
	
	public static String getCreate() {
        return "create";
    }

	@BeforeEach
	void setUp() {
		SSLContext sslContext = null;
		try {
			sslContext = new SSLContextBuilder()
					.loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray())
					.build();
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | CertificateException
				| IOException e) {
			e.printStackTrace();
		}

		CloseableHttpClient httpClient = HttpClients.custom()
	        .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
	            .setSSLSocketFactory(SSLConnectionSocketFactoryBuilder.create()
	                .setSslContext(sslContext)
	                .setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
	                .build()
	            )
	            .build()
	        )
	        .build();
		
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(
            httpClient);
        RestTemplateBuilder rtb = new RestTemplateBuilder()
            .requestFactory(() -> factory)
            .rootUri(serverUrl + port);
        
        this.restTemplate = new TestRestTemplate(rtb, null, null, HttpClientOption.SSL);
        
        //this.driver = new ChromeDriver();
	}
	
	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			this.driver.quit();
		}
	}

	@Test
	void whenRestCallPing_thenResponsePong() {
		ResponseEntity<String> result = restTemplate.getForEntity("/ping", String.class);
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals("pong", result.getBody());
	}
	
	@Test
	void whenSavingTheUsers_thenFindLastOnePersisted() {
		Role userRole = new Role(RoleType.USER);
		Role ur = this.roleRepository.save(userRole);
		Set<Role> roles = new HashSet<Role>();
		roles.add(ur);

		User u1 = new User("max@mail.com", "pw1");
		User u2 = new User("muster@mail.com", "pw2");

		u1.setRoles(roles);
		u2.setRoles(roles);

		List<User> customers = List.of(u1, u2);
		userRepository.saveAll(customers);

		assertThat(userRepository.findAll()).hasSizeGreaterThan(0).last().extracting(User::getEmail)
			.isEqualTo("muster@mail.com");
	}
	
	@Test
	public void getLoginPage() {
		//this.driver.get(serverUrl + port + "/login");
		//assertEquals("Datenschutzfehler", this.driver.getTitle());
	}
}
