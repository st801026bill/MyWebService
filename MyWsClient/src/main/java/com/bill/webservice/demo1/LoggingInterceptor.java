package com.bill.webservice.demo1;

import java.io.ByteArrayOutputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingInterceptor implements ClientInterceptor {
	@Override
    public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            messageContext.getRequest().writeTo(out);
            String outStr = new String(out.toString("UTF-8"));
            log.info("== req == messageContext:{}", outStr);
        } catch (Exception e) {
        	log.error("error...", e);
        }

        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            messageContext.getResponse().writeTo(out);
            String outStr = new String(out.toString("UTF-8"));
            log.info("== res == messageContext:{}", outStr);
        } catch (Exception e) {
        	log.error("error...", e);
        }
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            messageContext.getResponse().writeTo(out);
            String outStr = new String(out.toString("UTF-8"));
            log.info("== fault == messageContext:{}", outStr);
        } catch (Exception e) {
        	log.error("error...", e);
        }
        return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Exception ex)
            throws WebServiceClientException {
        if (ex != null) {
            if (ex instanceof ConnectException) {
            	log.info("== aftercomplete == do with Connect error...");
            } else if (ex instanceof ConnectTimeoutException) {
            	log.info("== aftercomplete == do with Connect timeout error...");
            } else if (ex instanceof SocketTimeoutException) {
            	log.info("== aftercomplete == do with Read timeout ...");
            } else {
            	log.error("== aftercomplete == do with other error ...", ex);
            }
        } else {
        	log.info("== aftercomplete == do something ...");
        }
    }
}
