本範例引用出處: https://www.tpisoftware.com/tpu/articleDetails/1969

**一. 建立Web Service Server(MyWsServer)** 
---

**1. add Dependency**  
dependency
```sql
1. spring-boot-starter-web-services : Spring-WS 的集成套件
2. wsdl4j : 用於生成wsdl文件
```

**2. 建立XSD檔**  
build xsd file - describe for request & reponse pojo
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

**3. create java pojo**
There two way to create java pojo : use maven plugin & use command line, the example is using plugin
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

**4. add service data source** : ```StudentRepository.java```

**5. add SOAP Web Service Endpoint** : ```StudentEndpoint.java```  
business endpoint :
@Endpoint ：用 Spring WS 將類註冊為 Web 服務端點  
@PayloadRoot ：根據 namespace 和 localPart 屬性來分派處理方法。當消息中的元素符合名稱時，該方法就會被調用。  
@ResponsePayload ：指示此方法返回一個要映射的物件  
@RequestPayload：指示此方法接受請求中要映射的物件  

**6. add SOAP Web Service Config** : ```WebServiceConfig.java```  
@EnableWs：enabled Spring WS  
messageDispatcherServlet：  
用來處理接受 SOAP 請求，裡面設定 ApplicationContext 使 Spring-WS 可以發現其他 Bean；setTransformWsdlLocations = true 用於轉換 WSDL 中<soap:address>的 location 屬性，反映此服務的地址。  
defaultWsdl11Definition：  
use xsd to create WSDL ，此方法上的 Bean name 即是 WSDL 的名稱。

**7. Test**  
Aftre service start, enter http://localhost:8080/studentService/student.wsdl then we can see the description of service.
use Postman to simulate sending SOAP request:
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
And get SOAP response
    
**二. build Web Service Client(MyWsClient)** 
---

**1. add dependency**  
dependency
```sql
1. spring-boot-starter-web-services : Spring-WS 的集成套件
2. httpclient : 用於設定連線逾時時間相關物件
```

**2. create Java pojo**  
download WSDL file from Server & put into resources folder
```src/main/resources/student.wsdl```  
There two way to create java pojo : use maven plugin & use command line, the example is using plugin
```sql
<plugin>
    <groupId>org.jvnet.jaxb2.maven2</groupId>
    <artifactId>maven-jaxb2-plugin</artifactId>
    <version>0.14.0</version>
    <executions>
	<execution>
	    <goals>
		<goal>generate</goal>
	    </goals>
	</execution>
    </executions>
    <configuration>
	<schemaLanguage>WSDL</schemaLanguage>
	<generateDirectory>${project.basedir}/src/main/java</generateDirectory>
	<generatePackage>com.bill.webservice.demo1.model</generatePackage>
	<schemaDirectory>${project.basedir}/src/main/resources</schemaDirectory>
	<schemaIncludes>
	    <include>student.wsdl</include>
	</schemaIncludes>
    </configuration>
</plugin>
```

**3. create Client service** : ```StudentClient.java```  

there are three common Exception，
ConnectException：connection refuse，such as Server is closed。
ConnectTimeoutException：connect timeout。
SocketTimeoutException：Server process timeout。

**4. build Client service** : ```WebClientConfig.java```  

**5. 設置攔截器** : ```LoggingInterceptor.java```  

**6. 執行** : 
1. 啟動 MyWsServer 與 MyWsClient
2. curl Get http://localhost:8081/wsclient/endpoint/1
