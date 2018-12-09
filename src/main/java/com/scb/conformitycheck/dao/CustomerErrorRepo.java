package com.scb.conformitycheck.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.scb.conformitycheck.model.MsErrorLog;



public interface CustomerErrorRepo extends JpaRepository<MsErrorLog, Long> {

}
