package com.domain.icp.db.dao;

import com.domain.icp.db.vo.BrainPowerOrder;

public interface BrainPowerDao {

    void save(BrainPowerOrder brainPowerOrder);

    void update(BrainPowerOrder brainPowerOrder);

    BrainPowerOrder findByPunycodeAndOrderDate(BrainPowerOrder brainPowerOrder);

}
