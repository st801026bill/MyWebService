本範例出處: https://www.tpisoftware.com/tpu/articleDetails/1969

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

**1. 添加依賴**  
dependency
```sql
1. spring-boot-starter-web-services : Spring-WS 的集成套件
2. httpclient : 用於設定連線逾時時間相關物件
```

**2. 生成 Java 物件**  
首先從 Server 端下載 WSDL 文件並放入 resources 資料夾中  
```src/main/resources/student.wsdl```  
有兩種生成方式，一種是用 maven plugin、另一種是用 command line 方式產生，這裡使用plugin當作範例。
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

**3. 創建 Client 服務** : ```StudentClient.java```  
Client 服務可以繼承 Spring 的 WebServiceGatewaySupport 來實現，其中使用 WebServiceTemplate 的 marshalSendAndReceive 方法執行與 Web Service 的 SOAP 訊息交換；若不想繼承 WebServiceGatewaySupport ，也可以用創建 WebServiceTemplate 的 Bean 的方式來實做 Client 服務。

在訊息交換的流程中我們捕捉了三種常見 Exception，
ConnectException：連線被拒絕，例如當 Server 端關閉時。
ConnectTimeoutException：連線途中或是等待可用的連線中逾時。
SocketTimeoutException：Server 處理回應中逾時。

**4. 配置 Client 服務** : ```WebClientConfig.java```  
Jaxb2Marshaller：  
我們為序列化工具設定 JAXB 物件（Java Architecture for XML Binding，將 Java 類映射為 XML 的表示方式）的位置，也就是方才我們從 WSDL 生成 Java物件的資料夾路徑。  

WebServiceMessageSender：  
為了避免等待 Server 端連線或是回應時間過長導致後續流程受阻，這裡用 WebServiceMessageSender 的實作類 HttpComponentsMessageSender 來控制等待的時間，setConnectionTimeout 表示與 Server 建立連線時的等待時間、setReadTimeout 表示等待 Server 回應的時間，單位都是毫秒（milliseconds）。  

接者將以上配置設定到 Client 物件：  
setDefaultUri 設定Server 端的地址，即與 WSDL 檔中 <soap:address location> 的值相同 ；setMarshaller、setUnmarshaller 設定序列化工具；setMessageSender 設定逾時時間；至於 setInterceptors 則是為了印出 SOAP 訊息所加的攔截器。  

**5. 設置攔截器** : ```LoggingInterceptor.java```  
藉由實做 ClientInterceptor 的方法對 SOAP 的請求回應再次加工。一次完整的請求回應會經過 handleRequest、handleResponse（或handleFault）、afterCompletion；若是出現 TimeoutException 或是 ConnectException 則是只有 handleRequest 和 afterCompletion。由於攔截器可以設置多個，返回的布林值是 True 時表示要繼續下個攔截器。

**6. 執行** : 
1. 啟動 MyWsServer 與 MyWsClient
2. curl Get http://localhost:8081/wsclient/endpoint/1
