package com.scb.conformitycheck.service;



//import org.jvnet.hk2.annotations.Service;

import com.scb.conformitycheck.model.CustomerRequestData;
import com.scb.conformitycheck.model.CustomerResponse;

public interface CustomerConformityService{

	public CustomerResponse customerConformityCheckService(CustomerRequestData customerRequestData);
}
