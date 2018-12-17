package com.domain.icp.db.dao;

import com.domain.icp.db.vo.BrainPowerOrder;

import java.util.List;
import java.util.Map;

public interface BrainPowerDao {

    void save(BrainPowerOrder brainPowerOrder);

    void update(BrainPowerOrder brainPowerOrder);

    BrainPowerOrder findByPunycodeAndOrderDate(BrainPowerOrder brainPowerOrder);

    List<BrainPowerOrder> listNotQueryICPInfos();

    List<BrainPowerOrder> listByOrderDate(String orderDate);

    List<BrainPowerOrder> listOrderTasks(Map<String, Object> paramMap);

    List<BrainPowerOrder> listForInitAuctionInfo();

    List<BrainPowerOrder> listForSyncAuctionInfo();

    List<BrainPowerOrder> listForBid();
}
