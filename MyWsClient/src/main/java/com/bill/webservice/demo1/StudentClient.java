package com.bill.webservice.demo1;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.bill.webservice.demo1.model.GetStudentRequest;
import com.bill.webservice.demo1.model.GetStudentResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StudentClient extends WebServiceGatewaySupport {

    public GetStudentResponse getStudent(String id) {

        GetStudentRequest request = new GetStudentRequest();
        request.setStudentId(id);

        GetStudentResponse response = null;
        try {
            response = (GetStudentResponse) getWebServiceTemplate().marshalSendAndReceive(request);

        } catch (Exception ex) {
            if (ex.getCause() instanceof ConnectException) {
                log.error("Connect error ...", ex);
            } else if (ex.getCause() instanceof ConnectTimeoutException) {
            	log.error("Connect time out error ...", ex);
            } else if (ex.getCause() instanceof SocketTimeoutException) {
            	log.error("Read time out error ...", ex);
            } else {
            	log.error("Other error ...", ex);
            }
        }

        return response;
    }
}
