package com.github.phillbarber.nginx;

import com.github.tomakehurst.wiremock.client.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class FrontEndTest {


    public static final int APPLICATION_PORT = 3000;
    public static URI ALLOWED_URI;
    public static URI OLD_URI;
    public static URI NEW_URI;
    public static URI DENIED_URI;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(APPLICATION_PORT));

    private static final String NGINX_ADDRESS = "http://localhost";

    @Before
    public void setUp() throws Exception {
        ALLOWED_URI = new URI(NGINX_ADDRESS + "/allowed");
        OLD_URI  = new URI(NGINX_ADDRESS + "/old-page");
        NEW_URI = new URI(NGINX_ADDRESS + "/new-page");
        DENIED_URI = new URI(NGINX_ADDRESS + "/denied");
    }

    @Test
    public void givenRequestToDeniedPage_whenRequested_then404ReturnedAndRequestNotMadeToWiremock() throws Exception{
        assertThat(getResponse(DENIED_URI).getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
        checkNoRequestsGotSentToWireMock(wireMockRule);
    }

    @Test
    public void givenRequestToAllowedPage_whenRequested_thenRequestPassedThroughToApplication() throws Exception{
        wireMockRule.stubFor(get(urlEqualTo(ALLOWED_URI.getPath())).willReturn(aResponse()));
        assertThat(getResponse(ALLOWED_URI).getStatus(), is(Response.Status.OK.getStatusCode()));
        wireMockRule.verify(getRequestedFor(urlEqualTo(ALLOWED_URI.getPath())));
    }

    @Test
    public void givenRequestToOldPage_whenRequested_thenResponseIsRedirectToNewPage() throws Exception{
        ClientResponse response = getResponse(OLD_URI);
        assertThat(response.getStatus(), is(Response.Status.MOVED_PERMANENTLY.getStatusCode()));
        assertThat(response.getHeaders().getFirst("Location"), equalTo(NEW_URI.toString()));
        checkNoRequestsGotSentToWireMock(wireMockRule);
    }



    private ClientResponse getResponse(URI uri) {
        Client client = ClientFactory.getClient();
        client.setFollowRedirects(false);
        return client.resource(uri).get(ClientResponse.class);
    }

    private void checkNoRequestsGotSentToWireMock(WireMockRule wireMockRule) {
        assertThat("A request got through to wiremock when it should have been rejected by Front End",
                wireMockRule.findAll(RequestPatternBuilder.allRequests()).size(), CoreMatchers.is(0));
    }




}
