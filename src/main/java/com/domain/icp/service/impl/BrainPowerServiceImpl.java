package com.domain.icp.service.impl;

import com.domain.icp.db.dao.BrainPowerDao;
import com.domain.icp.db.vo.BrainPowerOrder;
import com.domain.icp.service.BrainPowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BrainPowerServiceImpl implements BrainPowerService {

    @Autowired
    private BrainPowerDao brainPowerDao;

    @Override
    public void saveOrUpdate(BrainPowerOrder brainPowerOrder) {
        if (brainPowerDao.findByPunycodeAndOrderDate(brainPowerOrder) != null) {
            brainPowerDao.update(brainPowerOrder);
        } else {
            brainPowerDao.save(brainPowerOrder);
        }
    }

    @Override
    public List<BrainPowerOrder> listNotQueryICPInfos() {
        return brainPowerDao.listNotQueryICPInfos();
    }

    @Override
    public void update(BrainPowerOrder brainPowerOrder) {
        brainPowerDao.update(brainPowerOrder);
    }

    @Override
    public List<BrainPowerOrder> listByOrderDate(String orderDate) {
        return brainPowerDao.listByOrderDate(orderDate);
    }

    @Override
    public List<BrainPowerOrder> listOrderTasks(String suffix, String orderDate, Integer type, Integer wxStatus, Integer icpDL) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("suffix", suffix);
        paramMap.put("orderDate", orderDate);
        paramMap.put("type", type);
        paramMap.put("wxStatus", wxStatus);
        paramMap.put("icpDL", icpDL);
        return brainPowerDao.listOrderTasks(paramMap);
    }

    @Override
    public List<BrainPowerOrder> listForInitAuctionInfo() {
        return brainPowerDao.listForInitAuctionInfo();
    }

    @Override
    public List<BrainPowerOrder> listForSyncAuctionInfo() {
        return brainPowerDao.listForSyncAuctionInfo();
    }

    @Override
    public List<BrainPowerOrder> listForBid() {
        return brainPowerDao.listForBid();
    }
}
