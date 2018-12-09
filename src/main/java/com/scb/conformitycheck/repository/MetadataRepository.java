package com.scb.conformitycheck.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scb.conformitycheck.model.MetadataModel;

@Repository
public interface MetadataRepository extends JpaRepository<MetadataModel, MetadataModel> {
	
	@Query(value="SELECT * FROM MetadataModel sd WHERE sd.transactionType = ?1", nativeQuery=true)
	List<MetadataModel> findByType(String transactionType);

	@Query(value="SELECT * FROM MetadataModel md WHERE md.transactionType = ?1 AND md.transactionSubType=?2 AND md.messageType=?3", nativeQuery=true)
	public List<MetadataModel> findByTypeSubTypeMessageType(String transactionType, String subType, String messageType);
	
//	@Query(value="SELECT * FROM metadata", nativeQuery=true)
//	public List<MetadataModel> getAllMetadata();
	
	@Query(value="SELECT * FROM MetadataModel md WHERE md.metadataID = ?1", nativeQuery=true)
	public MetadataModel findByMetadataID(long metadataID);
	
	@Modifying
	@Transactional
	@Query("UPDATE MetadataModel d SET d.transactionType=:transactionType, d.transactionSubType=:transactionSubType, d.messageType=:messageType, d.validationSchema=:validationSchema, d.updatedOn=:updatedOn WHERE d.metadataID=:metadataID")
	public int updateById(@Param("transactionType") String transactionType, @Param("transactionSubType") String transactionSubType, @Param("messageType") String messageType, @Param("validationSchema") String validationSchema, @Param("updatedOn") String updatedOn, @Param("metadataID") long metadataID);
}
