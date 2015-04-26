package com.github.phillbarber.nginx;

import com.github.tomakehurst.wiremock.client.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.sun.jersey.api.client.ClientResponse;
import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertThat;

public class FrontEndTest {


    public static final int APPLICATION_PORT = 3000;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(APPLICATION_PORT));

    private static final String NGINX_ADDRESS = "http://localhost:8080";



    @Test
    public void givenRequestToHealthcheck_then404ReturnedAndRequestNotMadeToWiremock() throws Exception{
        assertThat(getResponse("/healthcheck").getStatus(), CoreMatchers.is(Response.Status.NOT_FOUND.getStatusCode()));
        checkNoRequestsGotSentToWireMock(wireMockRule);
    }


    private ClientResponse getResponse(String uri) {
        return ClientFactory.getClient().resource(NGINX_ADDRESS + uri).get(ClientResponse.class);
    }

    private void checkNoRequestsGotSentToWireMock(WireMockRule wireMockRule) {
        assertThat("A request got through to wiremock when it should have been rejected by Front End",
                wireMockRule.findAll(RequestPatternBuilder.allRequests()).size(), CoreMatchers.is(0));
    }




}
