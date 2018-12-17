package com.domain.icp.db.dao;

import com.domain.icp.db.vo.DomainSelling;

import java.util.List;

public interface DomainSellingDao {

    void batchSave(List<DomainSelling> domainSellingList);

    void batchUpdate(List<DomainSelling> domainSellingList);

    void batchDeleteJMDomain(List<String> punycodes);
}
