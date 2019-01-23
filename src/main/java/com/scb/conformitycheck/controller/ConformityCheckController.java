package com.scb.conformitycheck.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.scb.conformitycheck.model.AuditLog;
import com.scb.conformitycheck.model.MetadataModel;
import com.scb.conformitycheck.model.RequestData;
import com.scb.conformitycheck.model.ResponseMessage;
import com.scb.conformitycheck.service.ConformityCheckService;
import com.scb.conformitycheck.service.CustomerConformityService;
import com.scb.conformitycheck.serviceImpl.InternalApiInvoker;
import com.scb.conformitycheck.utils.ReceiverConstants;
import com.scb.conformitycheck.utils.ServiceUtil;

import lombok.extern.log4j.Log4j2;

@Component
@RestController 
@Log4j2
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(ReceiverConstants.CONFORMITY_CHECK_URL)
public class ConformityCheckController {
	@Autowired
	private CustomerConformityService customerConformityService;
	
	@Autowired
	private ConformityCheckService conformityCheckService;
	
	@Autowired
	private ServiceUtil commonMethods;
	
	@Autowired
	private InternalApiInvoker internalApiInvoker;

	@RequestMapping(value = ReceiverConstants.CONFORMITY_CHECK_REQUEST_HANDLE_URL, produces = { "application/json", "application/xml" })
	public ResponseEntity<ResponseMessage> requestHandle(@RequestBody RequestData requestData) {
		log.info("RequestData - Transaction type : " + requestData.getTransactionType() 
		+ ", Transaction sub type : " + requestData.getTransactionSubType() + ", payload format: " + requestData.getPayloadFormat());
		
		AuditLog auditLog = commonMethods.getAuditLog(requestData, "INITIATED", "Request for conformity check initiated");
		ResponseEntity<AuditLog> responseAuditLog = internalApiInvoker.auditLogApiCall(auditLog);
		
		//log.info("ConformitycheckRequestController - Request data : " + requestData);
		ResponseMessage responseMessage = conformityCheckService.validateRequest(requestData);
		ResponseEntity<ResponseMessage> re = null; 
		
		if (responseMessage.getResponseCode() != 200) {
			auditLog = commonMethods.getAuditLog(requestData, "FAILED", responseMessage.getResponseMessage());
		} else {
			auditLog = commonMethods.getAuditLog(requestData, "COMPLETED", "Request validation for transaction type: " + requestData.getTransactionType() + " successfully");
		}

		responseAuditLog = internalApiInvoker.auditLogApiCall(auditLog);
		return new ResponseEntity<ResponseMessage>(responseMessage, HttpStatus.OK);
	}

	@RequestMapping(value = ReceiverConstants.ADD_METADATA_URL, method = RequestMethod.POST, produces = { "application/xml", "application/json"})
	public ResponseEntity<MetadataModel> addMetadata(@RequestHeader Map<String, String> requestMap, @RequestBody MetadataModel metadataModel) {
		log.info("RequestHeader received "+ requestMap);
		log.info("Received metadataModel : " + metadataModel);
		//boolean flag = 	conformityCheckService.addMetadata(metadataModel);
		ResponseMessage rm = null;
		ResponseEntity<MetadataModel> re = null;
		
		MetadataModel metadata = conformityCheckService.addMetadata(metadataModel);
		if(metadata != null) {
			//rm = new ResponseMessage().builder().responseCode(201).responseMessage("Successfully created").build();
			re = new ResponseEntity<MetadataModel>(metadata, HttpStatus.CREATED);
		} else {
			//rm = new ResponseMessage().builder().responseCode(500).responseMessage("Metadata already exists").build();
			re = new ResponseEntity<MetadataModel>(metadata, HttpStatus.CONFLICT);
		}
		return re;
	}
	
	//@RequestMapping(value = ReceiverConstants.MODIFY_METADATA_URL, method = RequestMethod.POST, produces = { "application/xml", "application/json"})
	@PutMapping("/modifyMetadata")
	public ResponseEntity<ResponseMessage> modifyMetadata(@RequestHeader Map<String, String> requestMap, @RequestBody MetadataModel metadataModel) {
		log.info("RequestHeader received "+ requestMap);
		log.info("Received metadataModel : " + metadataModel);

		ResponseMessage rm = new ResponseMessage();
		
		rm = conformityCheckService.modifyMetadata(metadataModel);
		
		if (rm.getResponseCode() != 200) {
			log.info("Data not able to udpate into DB - ResponseMessage : " + rm);
			return new ResponseEntity<ResponseMessage>(rm, HttpStatus.CONFLICT);
		}

		log.info("Data updated into DB - ResponseMessage : " + rm);
		return new ResponseEntity<ResponseMessage>(rm, HttpStatus.OK);
	}
	
	@RequestMapping(value = ReceiverConstants.GET_METADATA_TYPE_URL, method = RequestMethod.GET, produces = { "application/xml", "application/json"})
	public ResponseEntity<List<MetadataModel>> getMetadataByType(@RequestHeader Map<String, String> requestMap, @RequestBody MetadataModel metadataModel) {
		log.info("RequestHeader received "+ requestMap);
		log.info("Received metadataModel : " + metadataModel);
		List<MetadataModel> metadataList = conformityCheckService.getMetadataByType(metadataModel.getTransactionType());
		log.info("List of metadataModel resturned from DB  : " + metadataList);
		return new ResponseEntity<List<MetadataModel>>(metadataList, HttpStatus.OK);
	}
	
	@RequestMapping(value = ReceiverConstants.ALL_METADATA_URL, method = RequestMethod.GET, produces = { "application/xml", "application/json"})
	public ResponseEntity<List<MetadataModel>> getAllMetadata(@RequestHeader Map<String, String> requestMap) {
		log.info("In ConformityCheckController.getAllMetadata method ");
		log.info("RequestHeader received "+ requestMap);
		List<MetadataModel> metadataList = conformityCheckService.getAllMetadata();
		log.info("List of metadataModel resturned from DB  : " + metadataList.size());
		return new ResponseEntity<List<MetadataModel>>(metadataList, HttpStatus.OK);
	}
	
	@RequestMapping(value = ReceiverConstants.GET_METADATA_BY_ID_URL, method = RequestMethod.GET, produces = {"application/xml", "application/json"})
	public ResponseEntity<MetadataModel> getMetadataById(@RequestHeader Map<String, String> requestMap, @PathVariable("metadataId") long metadataId) {
		log.info("RequestHeader received "+ requestMap);
		log.info("Request Body : " + metadataId);
		MetadataModel metadata = conformityCheckService.getMetadataById(metadataId);
		log.info("MetadataModel resturned from DB  : " + metadata);
		return new ResponseEntity<MetadataModel>(metadata, HttpStatus.OK);
	}
	
	@RequestMapping(value = ReceiverConstants.DELETE_METADATA_URL, method = RequestMethod.DELETE, produces = {"application/xml", "application/json"})
    	public ResponseEntity<Void> deleteBusinessRule(@PathVariable("MetadataId") long MetadataId) {
		log.info("Received metadataId :"+ MetadataId);		
		conformityCheckService.DeleteMetadataModel(MetadataId);
		log.info("Deleted metadata records with id  :" + MetadataId);
        	return new ResponseEntity<Void>(HttpStatus.OK);  	
    }
}
