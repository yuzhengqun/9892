package com.domain.icp.service.impl;

import com.domain.icp.db.dao.BrainPowerDao;
import com.domain.icp.db.vo.BrainPowerOrder;
import com.domain.icp.service.BrainPowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
