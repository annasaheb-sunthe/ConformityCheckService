package com.scb.conformitycheck.serviceImpl;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

public class SchemaValidator {

	public static void main(String[] args) {
		SchemaValidator validator = new SchemaValidator();
		
		System.out.println("Validating schema....");
		validator.validateRequest();

	}

	
	private boolean validateRequest() {
		System.out.println("validateRequest method - Validating schema....");
		boolean validationResult = true;
		
		try {
			File xsdFile = new File("D:/10641260/Projects/2018/SCB/Microservices PoC/Sample Data/STD_FORMAT/pacs.008.001.04.xsd");
			File xmlFile = new File("D:/10641260/Projects/2018/SCB/Microservices PoC/Sample Data/STD_FORMAT/OutwardCreditTransferRequestInvalid.xml");
			
			System.out.println("Creating schema factory....");
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

			System.out.println("Created schema factory.");
			Schema schema = factory.newSchema(new File(xsdFile.getAbsolutePath()));
			
	        Validator validator = schema.newValidator();
	        System.out.println("Created schema for validation.");
	        validator.validate(new StreamSource(new File(xmlFile.getAbsolutePath())));
	        System.out.println("Validation successful.");
//			String payload = new FileReader().;
//			System.out.println("Creating schema factory....");
//			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//			System.out.println("Created schema factory.");
//			//factory.setProperty(name, object);
//			//Schema schema = factory.newSchema(new File("CustomerRequest.xsd"));
//			Schema schema = factory.newSchema(new StreamSource(new StringReader(metadataModel.getValidationSchema())));
//			System.out.println("Created schema for validation.");
//			Validator validator = schema.newValidator();
//			System.out.println("Created validator from schema.");
//			// validator.validate(new StreamSource(new File(xmlPath)));
//			validator.validate(new StreamSource(new StringReader(payload)));
//			System.out.println("Validation successful.");
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			validationResult = false;
			e.printStackTrace();
		}

		System.out.println("validateRequest method - validationResult : " + validationResult);
		return validationResult;
	}
}
