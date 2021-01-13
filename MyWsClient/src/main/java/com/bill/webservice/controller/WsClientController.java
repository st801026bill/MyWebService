package com.bill.webservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bill.webservice.demo1.StudentClient;
import com.bill.webservice.demo1.model.GetStudentResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/wsclient")
public class WsClientController {
	
	@Autowired
	StudentClient studentClient;
	
	@GetMapping(value = "/endpoint/{id}") 
	public GetStudentResponse service1(@PathVariable(name="id") String id) {
		log.info("Got Request Body: {}", id);
		return studentClient.getStudent(id);
	}
	
}
