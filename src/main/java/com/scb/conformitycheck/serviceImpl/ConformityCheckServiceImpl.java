package com.scb.conformitycheck.serviceImpl;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import javax.inject.Named;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scb.conformitycheck.model.MetadataModel;
import com.scb.conformitycheck.model.RequestData;
import com.scb.conformitycheck.model.ResponseMessage;
import com.scb.conformitycheck.repository.MetadataRepository;
import com.scb.conformitycheck.service.ConformityCheckService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Named("ConformityCheckService")
public class ConformityCheckServiceImpl implements ConformityCheckService {
	@Autowired
	private MetadataRepository metadataRepository;

	@Override
	public ResponseMessage validateRequest(RequestData requestData) {
		log.info("RequestData - Transaction type : " + requestData.getTransactionType() 
		+ ", Transaction sub type : " + requestData.getTransactionSubType() + ", payload format: " + requestData.getPayloadFormat());
		ResponseMessage response = new ResponseMessage();
		MetadataModel metadataModel = getMetadataModel(requestData); // this.dozerBeanMapper.map(metadataDto, MetadataModel.class);
		List<MetadataModel> metadataList = this.metadataRepository.findByTypeSubTypeMessageType(
				metadataModel.getTransactionType(), metadataModel.getTransactionSubType(),
				metadataModel.getMessageType());

		if (metadataList == null) {
			log.info("NO any metadata details available in database with given transaction details");
			response.setResponseCode(500);
		} else {
			// for(MetadataModel entity:metadataList) {
			// log.info("MetadataId : %s %s %s %s",
			// entity.getMetadataId(),entity.getMessageType(),entity.getTransactionType(),entity.getTransactionSubType());
			// cr.setResponseCode(200);
			
			log.info("metadataList size : " + metadataList.size());
			metadataModel = metadataList.get(0);
			//log.info("XSD : \n" + metadataModel.getValidationSchema());
			log.info("XSD file size: " + metadataModel.getValidationSchema().length());
			
			try {
				String payload = requestData.getPayload();
				log.info("Creating schema factory....");
				SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
				log.info("Created schema factory.");
				//factory.setProperty(name, object);
				//Schema schema = factory.newSchema(new File("CustomerRequest.xsd"));
				Schema schema = factory.newSchema(new StreamSource(new StringReader(metadataModel.getValidationSchema())));
				log.info("Created schema for validation.");
				Validator validator = schema.newValidator();
				log.info("Created validator from schema.");
				// validator.validate(new StreamSource(new File(xmlPath)));
				validator.validate(new StreamSource(new StringReader(payload)));
				log.info("Validation successful.");
				response.setResponseCode(200);
				response.setResponseMessage("Message Conformity check passed!");

			} catch (Exception e) {
				log.info("Exception: " + e.getMessage());
				response.setResponseCode(400);
				response.setResponseMessage("Message Conformity check failed!");
				e.printStackTrace();
			}
		}

		return response;
	}

	@Override
	public MetadataModel addMetadata(MetadataModel metadata) {
		metadata.setMetadataID(getMetadataId());
		metadata.setCreateOn(getCurrentDateTime());
		metadata.setUpdatedOn(getCurrentDateTime());
		
		MetadataModel metadataModel = null;
		try {
			metadataModel = metadataRepository.findByMetadataID(metadata.getMetadataID());
		} catch (NoSuchElementException ex) {
			log.info("Error in finding metadata" + ex.getMessage());
		}
		
		
		if (metadataModel != null) {
			//metadataModel = new MetadataModel();
			log.info("Metadata already exists in db");
		} else {
			log.info("Metadata deatils being saved in db");

			metadataModel = metadataRepository.save(metadata);
			log.info("Metadata saved in db");
		}
		
		return metadataModel;
	}

	@Override
	public ResponseMessage modifyMetadata(MetadataModel metadata) {
		log.info("MetadataModel received: " + metadata);
		metadata.setUpdatedOn(getCurrentDateTime());

		ResponseMessage rm = new ResponseMessage();
		int updateCount = 0;
		
		try {
			updateCount = metadataRepository.updateById(metadata.getTransactionType(), 
					metadata.getTransactionSubType(), metadata.getMessageType(),
					metadata.getValidationSchema(), metadata.getUpdatedOn(), 
					metadata.getMetadataID());
			if (updateCount > 0) {
				rm.setResponseCode(200);
				rm.setResponseMessage("Record updated successfully.");
				log.info("DupcheckRule updated in db");
			} else {
				rm.setResponseCode(700);
				rm.setResponseMessage("Update failed. Record id did not match");
			}
			
		} catch (NoSuchElementException ex) {
			log.info("Error in finding DupcheckRule" + ex.getMessage());
			rm.setResponseCode(900);
			rm.setResponseMessage("Update failed. No Such Element Exception: " + ex.getMessage());
		}

		return rm;
	}

	@Override
	public List<MetadataModel> getMetadataByType(String transactionType) {
		log.info("Received transactionType : " + transactionType);
		List<MetadataModel> obj = metadataRepository.findByType(transactionType);
		log.info("Returned from DB  : " + obj);
		return obj;
	}

	@Override
	public MetadataModel getMetadataById(long metadataId) {
		MetadataModel obj = metadataRepository.findByMetadataID(metadataId);
		log.info("Returned from DB  : " + obj);
		return obj;
	}
	
	@Override
	public List<MetadataModel> getMetadataByTypeSubTypeMessageType(String transactionType, String subType,
			String messageType) {
		List<MetadataModel> obj = metadataRepository.findByTypeSubTypeMessageType(transactionType, subType, messageType);
		return obj;
	}

	@Override
	public List<MetadataModel> getAllMetadata() {
		List<MetadataModel> obj = metadataRepository.findAll();
		log.info("Returned from DB  : " + obj.size());
		return obj;
	}

	private MetadataModel getMetadataModel(RequestData requestData) {
		return new MetadataModel().builder().transactionType(requestData.getTransactionType())
				.transactionSubType(requestData.getTransactionSubType()).messageType(requestData.getPayloadFormat())
				.build();
	}
	
	public long getMetadataId() {
		Random random = new Random(System.nanoTime() % 100000);
		long uniqueMetadataId = random.nextInt(1000000000);
		return uniqueMetadataId;
	}
	
	public String getCurrentDateTime() {
		LocalDateTime localDateTime = LocalDateTime.now();
		return localDateTime.toString();
	}
}
