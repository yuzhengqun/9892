package com.domain.icp.schema.brainpower;

import com.domain.icp.db.vo.BrainPowerOrder;
import com.domain.icp.service.BrainPowerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class DLICPTask {

    private static Logger logger = LoggerFactory.getLogger(DLICPTask.class);

    @Autowired
    private BrainPowerService brainPowerService;

//    @Scheduled(cron = "0/2 * * * * *")
    public void exe() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(System.currentTimeMillis());
        List<BrainPowerOrder> brainPowerOrderList = brainPowerService.listByOrderDate(currentDate);
        Map<String, Integer> icpNoCountMap = this.getIcpNoCountMap(brainPowerOrderList);
        for (BrainPowerOrder brainPowerOrder : brainPowerOrderList) {
            try {
                String icpNo = brainPowerOrder.getIcpNo();
                String[] array = icpNo.split("-");
                if (array.length != 2) {
                    continue;
                }
                if (!"1".equals(array[1]) || (icpNoCountMap.get(array[0])).intValue() > 1) {
                    brainPowerOrder.setIcpDL(2);
                } else {
                    brainPowerOrder.setIcpDL(1);
                }
                brainPowerService.update(brainPowerOrder);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private Map<String, Integer> getIcpNoCountMap(List<BrainPowerOrder> brainPowerOrders) {
        Map<String, Integer> result = new HashMap<>();
        for (BrainPowerOrder brainPowerOrder : brainPowerOrders) {
            try {
                String icpNo = brainPowerOrder.getIcpNo();
                String[] array = icpNo.split("-");
                if (array.length != 2) {
                    continue;
                }
                String key = array[0];
                if (result.containsKey(key)) {
                    result.put(key, result.get(key) + 1);
                } else {
                    result.put(key, 1);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return result;
    }
}
