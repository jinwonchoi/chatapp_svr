package com.gencode.issuetool.dao;

import java.util.List;
import java.util.Optional;

import com.gencode.issuetool.obj.CustomerInfo;

public interface CustomerInfoDao extends Dao<CustomerInfo> {

	Optional<List<CustomerInfo>> findByCustomerId(String customerId);

}
