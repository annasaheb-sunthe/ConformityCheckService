package com.scb.conformitycheck.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "METADATA")
@Getter
@Setter
public class MetadataEntity {
	
	@Column(name="METADATA_ID")
	@Range(min=10, max=10)
	@NotNull
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer metadataId;
	
	@Column(name="TRANSACTION_TYPE")
	@Range(min=15, max=100)
	@NotNull
	private String transactionType;
	
	@Column(name="TRANSACTION_SUB_TYPE")
	@Range(min=15, max=100)
	@NotNull
	private String subType;
	
	@Column(name="MESSAGE_TYPE")
	@Range(min=10, max=50)
	@NotNull
	private String messageType;
	
	@Column(name="VALIDATION_SCHEMA")
	private String validationSchema;
	
	@Column(name="CREATE_ON")
	@Temporal(TemporalType.DATE)
	@NotNull
	private Date createOn;
	
	@Column(name="UPDATED_ON")
	@Temporal(TemporalType.DATE)
	@NotNull
	private Date updatedOn;
}
