package com.scb.conformitycheck.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @Builder @Entity @Table(name="MetadataModel") @NoArgsConstructor @AllArgsConstructor @ToString @XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public class MetadataModel {
	@Id
	@Column
	private long metadataID;
	@Column
	private String transactionType;
	@Column
	private String transactionSubType;
	@Column
	private String messageType;
	@Column(length=100000)
	private String validationSchema;
	@Column
	private String createOn;
	@Column
	private String updatedOn;
}
