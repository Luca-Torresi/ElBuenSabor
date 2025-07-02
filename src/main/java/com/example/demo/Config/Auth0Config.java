package com.example.demo.Config;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.json.auth.TokenHolder;
import com.auth0.net.AuthRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Auth0Config {

    @Value("${auth0.management.domain}")
    private String auth0ManagementDomain;

    @Value("${auth0.management.client-id}")
    private String clientId;

    @Value("${auth0.management.client-secret}")
    private String clientSecret;

    @Bean
    public ManagementAPI managementAPI() throws Exception {
        // AuthAPI constructor sometimes expects domain without https:// and trailing slash
        // Let's make sure it's clean if the problem persists with it.
        // For now, let's try with the full domain, as it might handle it internally.
        String domainForAuthAPIConstructor = auth0ManagementDomain.replace("https://", "").replace("/", "");


        AuthAPI authAPI = new AuthAPI(domainForAuthAPIConstructor, clientId, clientSecret); // Use cleaned domain here

        // The audience for the Management API is your Auth0 domain followed by "/api/v2/"
        String managementApiAudience = auth0ManagementDomain + "api/v2/";

        AuthRequest request = authAPI.requestToken(managementApiAudience);
        TokenHolder holder = request.execute();

        // Pass the full domain (with https:// and /) to the ManagementAPI constructor
        return new ManagementAPI(auth0ManagementDomain, holder.getAccessToken());
    }
}