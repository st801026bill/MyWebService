//
// 此檔案是由 JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 所產生 
// 請參閱 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 一旦重新編譯來源綱要, 對此檔案所做的任何修改都將會遺失. 
// 產生時間: 2021.01.11 於 12:43:23 PM CST 
//


package com.bill.webservice.demo1.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type 的 Java 類別.
 * 
 * <p>下列綱要片段會指定此類別中包含的預期內容.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="student" type="{http://www.webservice.bill.com/student}student"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "student"
})
@XmlRootElement(name = "getStudentResponse")
public class GetStudentResponse {

    @XmlElement(required = true)
    protected Student student;

    /**
     * 取得 student 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link Student }
     *     
     */
    public Student getStudent() {
        return student;
    }

    /**
     * 設定 student 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link Student }
     *     
     */
    public void setStudent(Student value) {
        this.student = value;
    }

}
