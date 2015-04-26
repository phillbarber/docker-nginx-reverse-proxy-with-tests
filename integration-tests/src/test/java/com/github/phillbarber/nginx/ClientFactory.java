package com.github.phillbarber.nginx;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class ClientFactory {

    public static Client getClient() {
        ClientConfig config = new DefaultClientConfig();
        return Client.create(config);
    }
}
