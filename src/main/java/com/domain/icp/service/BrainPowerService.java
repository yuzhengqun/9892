package com.domain.icp.service;

import com.domain.icp.db.vo.BrainPowerOrder;

import java.util.List;

public interface BrainPowerService {

    void saveOrUpdate(BrainPowerOrder brainPowerOrder);

    List<BrainPowerOrder> listNotQueryICPInfos();

    void update(BrainPowerOrder brainPowerOrder);

    List<BrainPowerOrder> listByOrderDate(String orderDate);

    List<BrainPowerOrder> listOrderTasks(String suffix, String orderDate, Integer type, Integer wxStatus, Integer icpDL);

    List<BrainPowerOrder> listForInitAuctionInfo();

    List<BrainPowerOrder> listForSyncAuctionInfo();

    List<BrainPowerOrder> listForBid();
}
