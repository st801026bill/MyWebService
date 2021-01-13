package com.bill.webservice.demo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.transport.WebServiceMessageSender;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

@Configuration
public class WebClientConfig {
	
	@Autowired
	StudentClient client;
	
	@Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.bill.webservice.demo1.model");
        return marshaller;
    }

    @Bean
    public WebServiceMessageSender webServiceMessageSender() {
        HttpComponentsMessageSender sender = new HttpComponentsMessageSender();
        sender.setConnectionTimeout(5 * 1000);
        sender.setReadTimeout(5 * 1000);
        return sender;
    }

    @Bean
    public StudentClient wsClient(Jaxb2Marshaller marshaller, WebServiceMessageSender sender) {
        client.setDefaultUri("http://localhost:8080/studentService");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        client.setMessageSender(sender);
        ClientInterceptor[] ci = {new LoggingInterceptor()};
        client.setInterceptors(ci);
        return client;
    }
}
