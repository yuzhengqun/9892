package com.domain.icp.service;

import com.domain.icp.db.vo.DomainSelling;

import java.util.List;

public interface DomainSellingService {


    void batchSave(List<DomainSelling> domainSelling);

    void batchUpdate(List<DomainSelling> domainSelling);

    void batchDelete(List<String> punycodes, Integer platform);
}
