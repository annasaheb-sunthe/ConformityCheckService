package com.scb.conformitycheck.serviceImpl;

import java.io.File;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.springframework.stereotype.Service;

import com.scb.conformitycheck.model.CustomerRequestData;
import com.scb.conformitycheck.model.CustomerResponse;
import com.scb.conformitycheck.service.CustomerConformityService;
//import com.scb.serviceImpl.CustomerRequestServiceImpl;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class CustomerConformityServiceImpl implements CustomerConformityService {

	@Override
	public CustomerResponse customerConformityCheckService(CustomerRequestData customerRequestData) {/*
		CustomerResponse response = new CustomerResponse();
		if(message == null || message.isEmpty()) {
			response.setResponseCode(400);
			response.setResponseMessage("Message cannot be empty!");
		}
		SchemaFactory factory = 
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema;
		try {
			schema = factory.newSchema(new File("CustomerRequest.xml"));
			
			Validator validator = schema.newValidator();
			Source source = new StreamSource(message);
			validator.validate(source);
			response.setResponseCode(200);
			response.setResponseMessage("Message Conformity check passed!");
		} catch (IOException | SAXException e) {
			response.setResponseCode(400);
			response.setResponseMessage("Message Conformity check failed!");
		}
		return response;
	*/


		CustomerResponse response = new CustomerResponse();
		if(customerRequestData == null) {
			response.setResponseCode(400);
			response.setResponseMessage("Message cannot be empty!");
			return response; 
		} 
		try {
			 	String xmlString = customerRequestData.toString();
	    	   /* JAXBContext jaxbContext = JAXBContext.newInstance(CustomerRequestData.class);
		        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		        StringWriter sw = new StringWriter();
		        jaxbMarshaller.marshal(customerRequestData, sw);
		        String xmlString = sw.toString();
		        System.out.println(xmlString);*/
		        
			 	SchemaFactory factory =
	            SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	            Schema schema = factory.newSchema(new File("CustomerRequest.xsd"));
	            Validator validator = schema.newValidator();
	           // validator.validate(new StreamSource(new File(xmlPath)));
	            validator.validate(new StreamSource(new StringReader(xmlString)));
	            response.setResponseCode(200);
				response.setResponseMessage("Message Conformity check passed!");
	            
	      } catch (Exception e){
	         System.out.println("Exception: "+e.getMessage());
	         response.setResponseCode(400);
			 response.setResponseMessage("Message Conformity check failed!");
	      }
	      return response;
	}

}
