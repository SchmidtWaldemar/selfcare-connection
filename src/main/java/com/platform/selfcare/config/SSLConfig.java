package com.platform.selfcare.config;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SSLConfig {

	/**
	 * set maximal timeout in milliseconds for request/response
	 */
    private static final long TIME_OUT = 32000;
    
    @Autowired
    private Environment environment;

    @Autowired
    private ResourceLoader resourceLoader;

    private SSLContext sslContext;
    
    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return new RestTemplate(getClientHttpRequestFactory());
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        Resource resource = resourceLoader.getResource(environment.getRequiredProperty("server.ssl.key-store"));
        try {
        	// create ssl context by keystore
            this.sslContext = SSLContextBuilder.create()
            		.loadTrustMaterial(resource.getURL(), environment.getRequiredProperty("server.ssl.key-store-password").toCharArray())
            		.build();
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | CertificateException
                | IllegalStateException | IOException e) {
            e.printStackTrace();
        }

        // request configuration
        // timeout for requests and responses
        RequestConfig config = RequestConfig.custom()
            .setResponseTimeout(TIME_OUT, TimeUnit.MICROSECONDS)
            .setConnectionRequestTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
            .build();

        // build client for https requests
        CloseableHttpClient httpClient = HttpClients.custom()
            .setDefaultRequestConfig(config)
            .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(SSLConnectionSocketFactoryBuilder.create()
                    .setSslContext(sslContext)
                    .setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build()
                )
                .build()
            )
            .build();
        
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }
}
