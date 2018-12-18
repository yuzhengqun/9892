package com.domain.icp.service.impl;


import ch.qos.logback.classic.Logger;
import com.domain.icp.db.dao.DomainSellingDao;
import com.domain.icp.db.vo.DomainSelling;
import com.domain.icp.service.DomainSellingService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DomainSellingServiceImpl implements DomainSellingService {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(DomainSellingServiceImpl.class);

    private static final Integer batchSize = 100;

    @Autowired
    private DomainSellingDao domainSellingDao;

    @Override
    public void batchSave(List<DomainSelling> domainSellingList) {
        int batchTimes = (int) Math.ceil(domainSellingList.size() / batchSize * 1d);
        List<DomainSelling> currentBatchList = null;
        for (int i = 1; i <= batchTimes; i++) {
            if (i == batchTimes) {
                currentBatchList = domainSellingList.subList((i - 1) * batchSize, domainSellingList.size());
            } else {
                currentBatchList = domainSellingList.subList((i - 1) * batchSize, i * batchSize);
            }
            domainSellingDao.batchSave(currentBatchList);
        }

    }

    @Override
    public void batchUpdate(List<DomainSelling> domainSellingList) {
        int batchTimes = (int) Math.ceil(domainSellingList.size() / batchSize * 1d);
        List<DomainSelling> currentBatchList = null;
        for (int i = 1; i <= batchTimes; i++) {
            if (i == batchTimes) {
                currentBatchList = domainSellingList.subList((i - 1) * batchSize, domainSellingList.size());
            } else {
                currentBatchList = domainSellingList.subList((i - 1) * batchSize, i * batchSize);
            }
            domainSellingDao.batchUpdate(currentBatchList);
        }
    }

    @Override
    public void batchDelete(List<String> punycodes, Integer platform) {
        int batchTimes = (int) Math.ceil(punycodes.size() / batchSize * 1d);
        List<String> currentBatchList = null;
        for (int i = 1; i <= batchTimes; i++) {
            if (i == batchTimes) {
                currentBatchList = punycodes.subList((i - 1) * batchSize, punycodes.size());
            } else {
                currentBatchList = punycodes.subList((i - 1) * batchSize, i * batchSize);
            }
            domainSellingDao.batchDeleteJMDomain(currentBatchList);
        }
    }

    @Override
    public List<DomainSelling> listPunycodeListForICPQuery(Integer size, Integer offset, Integer status) {
        Map<String, Integer> map = new HashMap<>();
        map.put("offset", offset);
        map.put("size", size);
        map.put("status", status);
        return domainSellingDao.listPunycodeListForICPQuery(map);
    }

    @Override
    public List<DomainSelling> listForQueryWXStatus(Integer size, Integer offset, Integer status) {
        Map<String, Object> map = new HashMap<>();
        map.put("offset", offset);
        map.put("size", size);
        map.put("wxStatus", status);
        return domainSellingDao.listForQueryWXStatus(map);
    }

    @Override
    public List<DomainSelling> listForWhoisQuery(Integer size, Integer offset) {
        Map<String, Object> map = new HashMap<>();
        map.put("size", size);
        map.put("offset", offset);
        return domainSellingDao.listForWhoisQuery(map);
    }
}
