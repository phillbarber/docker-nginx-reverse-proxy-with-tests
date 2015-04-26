package com.github.phillbarber.nginx;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.sun.jersey.api.client.ClientResponse;
import org.hamcrest.CoreMatchers;

import javax.ws.rs.core.Response;

import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.phillbarber.nginx.ClientFactory.getClient;
import static org.junit.Assert.assertThat;

public class RequestChecker {


    public static void checkRequestToCorrectURIAndMethodIsProxiedThroughToMockedServer(WireMockRule wireMockRule, URI uri, RequestMethod method) {

        wireMockRule.stubFor(new MappingBuilder(method, (urlEqualTo(uri.getPath()))).willReturn(aResponse().withBody("Dummy Content")));
        ClientResponse response = getClient().resource(uri).method(method.toString(), ClientResponse.class);
        assertThat("Incorrect status returned from uri: " + uri+ " expected: " + Response.Status.OK.getStatusCode() + " actual: "+ response.getStatus(), response.getStatus(), CoreMatchers.is(Response.Status.OK.getStatusCode()));
        wireMockRule.verify(new RequestPatternBuilder(method, (urlEqualTo(uri.getPath()))));
    }

}
