**一. 建立Web Service Server(MyWsServer)** 
---

**1. 添加依賴**  
dependency
```sql
1. spring-boot-starter-web-services : Spring-WS 的集成套件
2. wsdl4j : 用於生成wsdl文件
```

**2. 建立XSD檔**  
依據先契約的方式，我們在 resources 中建立 xsd 檔 ─ 用來描述服務請求和回應的實體
```sql
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
	targetNamespace="http://www.webservice.bill.com/student"
	xmlns:tns="http://www.webservice.bill.com/student" elementFormDefault="qualified">
	<xs:element name="getStudentRequest">
		<xs:complexType>
			<xs:sequence>
				<xs:element name="student_id" type="xs:string" />  
			</xs:sequence>
		</xs:complexType>
	</xs:element>
	<xs:element name="getStudentResponse">
		<xs:complexType>
			<xs:sequence>
				<xs:element name="student" type="tns:student" />
			</xs:sequence>
		</xs:complexType>
	</xs:element>
	<xs:complexType name="student">
		<xs:sequence>
			<xs:element name="student_id" type="xs:string" />
			<xs:element name="student_name" type="xs:string" />
		</xs:sequence>
	</xs:complexType>
</xs:schema>
```  

**3. 生成 Java 物件**  
有兩種生成方式，一種是用 maven plugin、另一種是用 command line 方式產生，這裡使用plugin當作範例。
```sql
<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>jaxb2-maven-plugin</artifactId>
  <version>1.6</version>
  <executions>
    <execution>
      <id>xjc</id>
      <goals>
        <goal>xjc</goal>
      </goals>
    </execution>
  </executions>
  <configuration>
    <schemaDirectory>${project.basedir}/src/main/resources/xsd/</schemaDirectory>
    <outputDirectory>${project.basedir}/src/main/java/</outputDirectory>
    <clearOutputDir>false</clearOutputDir>
  </configuration>
</plugin>
```

**4. 加入服務資料來源** : ```StudentRepository.java```

**5. 加入 SOAP Web Service Endpoint** : ```StudentEndpoint.java```  
Endpoint 負責提供業務邏輯的進入點，配置的細節如下：  
@Endpoint ：用 Spring WS 將類註冊為 Web 服務端點  
@PayloadRoot ：根據 namespace 和 localPart 屬性來分派處理方法。當消息中的元素符合名稱時，該方法就會被調用。  
@ResponsePayload ：指示此方法返回一個要映射的物件  
@RequestPayload：指示此方法接受請求中要映射的物件  

**6. 加入SOAP Web Service配置** : ```WebServiceConfig.java```  
@EnableWs：啟用 Spring WS 功能。  
messageDispatcherServlet：  
用來處理接受 SOAP 請求，裡面設定 ApplicationContext 使 Spring-WS 可以發現其他 Bean；setTransformWsdlLocations = true 用於轉換 WSDL 中<soap:address>的 location 屬性，反映此服務的地址。  
defaultWsdl11Definition：  
使用 xsd 檔創建 WSDL ，此方法上的 Bean name 即是 WSDL 的名稱。

**7. 測試**  
服務啟動後，輸入http://localhost:8080/studentService/student.wsdl 可查看該服務的描述。  
以 Postman 模擬發送 SOAP 請求訊息：  
method：POST  
Content-Type：text/xml  
URL： http://localhost:8080/studentService  
```sql
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:gs="http://www.webservice.bill.com/student">
    <soapenv:Header/>
    <soapenv:Body>
        <gs:getStudentRequest>
            <gs:student_id>1</gs:student_id>
        </gs:getStudentRequest>
    </soapenv:Body>
</soapenv:Envelope>
```
即可得到服務返回的 SOAP 回應訊息
    
**二. 建立Web Service Client(MyWsClient)** 
---
