package com.bill.webservice.ws.endpoint.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.bill.webservice.ws.endpoint.model.GetStudentRequest;
import com.bill.webservice.ws.endpoint.model.GetStudentResponse;
import com.bill.webservice.ws.endpoint.model.Student;

@Endpoint
public class StudentEndpoint {
	private static final String NAMESPACE_URI = "http://www.webservice.bill.com/student";

    @Autowired
    StudentRepository studentRepository;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getStudentRequest")
    public @ResponsePayload GetStudentResponse getStudentById(@RequestPayload GetStudentRequest request)
            throws InterruptedException {
    	GetStudentResponse response = new GetStudentResponse();
        Student student = studentRepository.findStudentById(request.getStudentId());
        response.setStudent(student);
        return response;
    }
}
