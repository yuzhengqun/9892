package com.domain.icp.schema.brainpower;

import com.domain.icp.db.vo.BrainPowerOrder;
import com.domain.icp.service.BrainPowerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BidTask {
    private static Logger logger = LoggerFactory.getLogger(BidTask.class);

    private static String uuid = "227988";
    private static String secretKey = "ab80d37b37a2f363";
    private static String openUrl = "http://openapi.juming.com/";

    @Autowired
    private BrainPowerService brainPowerService;

    @Scheduled(cron = "0 0/10 * * * *")
    public void bid() {
        List<BrainPowerOrder> brainPowerOrderList = brainPowerService.listForBid();
        //TODO 1 查询备案信息 2 查询当前拍卖信息
    }
}
