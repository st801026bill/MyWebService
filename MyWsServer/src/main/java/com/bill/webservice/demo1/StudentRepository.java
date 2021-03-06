package com.bill.webservice.demo1;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.bill.webservice.demo1.model.Student;

@Component
public class StudentRepository {
	private static final Map<String, Student> students = new HashMap<>();

    @PostConstruct
    public void initData() {
    	Student jack = new Student();
    	jack.setStudentId("1");
    	jack.setStudentName("Jack");
    	students.put(jack.getStudentId(), jack);
    	
    	Student bill = new Student();
    	bill.setStudentId("2");
    	bill.setStudentName("Bill");
    	students.put(bill.getStudentId(), bill);
    	
    	Student mary = new Student();
    	mary.setStudentId("3");
    	mary.setStudentName("Mary");
    	students.put(mary.getStudentId(), mary);    	
    }

    public Student findStudentById(String id) {
    	if(id == null) return null;
        return students.get(id);
    }
}
