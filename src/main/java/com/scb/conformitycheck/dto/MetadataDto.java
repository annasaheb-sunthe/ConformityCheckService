package com.scb.conformitycheck.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MetadataDto {
	
    private Integer metadataId;
	
	private String transactionType;
	
	private String transactionSubType;
	
	private String messageType;
	
	private String validationSchema;
	
	private Date createOn;
	
	private Date updatedOn;

}
