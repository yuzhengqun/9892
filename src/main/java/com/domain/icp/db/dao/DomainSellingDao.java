package com.domain.icp.db.dao;

import com.domain.icp.db.vo.DomainSelling;

import java.util.List;
import java.util.Map;

public interface DomainSellingDao {

    void batchSave(List<DomainSelling> domainSellingList);

    void batchUpdate(List<DomainSelling> domainSellingList);

    void batchDeleteJMDomain(List<String> punycodes);

    List<DomainSelling> listPunycodeListForICPQuery(Map<String, Integer> map);

    List<DomainSelling> listForQueryWXStatus(Map<String, Object> map);
}
