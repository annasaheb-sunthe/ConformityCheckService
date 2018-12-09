package com.scb.conformitycheck.service;

import java.util.List;

import com.scb.conformitycheck.model.CustomerResponse;
import com.scb.conformitycheck.model.MetadataModel;
import com.scb.conformitycheck.model.RequestData;
import com.scb.conformitycheck.model.ResponseMessage;

public interface ConformityCheckService {

	public ResponseMessage validateRequest(RequestData requestData);
	
	public MetadataModel addMetadata(MetadataModel metadata);

	public ResponseMessage modifyMetadata(MetadataModel metadata);
	
	public List<MetadataModel> getMetadataByType(String transactionType);
	
	public List<MetadataModel> getMetadataByTypeSubTypeMessageType(String transactionType, String subType, String messageType);
	
	List<MetadataModel> getAllMetadata();
	
	public MetadataModel getMetadataById(long metadataId);
}
