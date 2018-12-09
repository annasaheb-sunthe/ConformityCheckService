package com.scb.conformitycheck.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scb.conformitycheck.model.MsAuditLog;

public interface CustomerConformityCheckRepo extends JpaRepository<MsAuditLog, Long> {

}
