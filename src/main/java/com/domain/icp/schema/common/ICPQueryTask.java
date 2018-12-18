package com.domain.icp.schema.common;

import ch.qos.logback.classic.Logger;
import com.domain.icp.db.vo.DomainSelling;
import com.domain.icp.service.DomainSellingService;
import com.domain.icp.util.JMICPUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ICPQueryTask {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(ICPQueryTask.class);

    @Autowired
    private DomainSellingService domainSellingService;

    @Scheduled(cron = "0/2 * * * * *")
    public void initICPInfo() {
        List<DomainSelling> tasks = domainSellingService.listPunycodeListForICPQuery(500, 0, 0);
        List<DomainSelling> domainSellingList = new ArrayList<>();
        for (DomainSelling task : tasks) {
            try {
                DomainSelling domainSelling = JMICPUtil.getICPInfo(task.getPunycode());
                domainSellingList.add(domainSelling);
                Thread.sleep(1000);
            } catch (Exception e) {
                logger.error("punycode:" + task.getPunycode() + " 查询备案信息失败", e);
            }
        }
        domainSellingService.batchUpdate(domainSellingList);
        if (!tasks.isEmpty()) {
            logger.info("initICPInfo查询备案信息成功，查询数量:" + tasks.size());
        }
    }

    @Scheduled(cron = "0/2 * * * * *")
    public void updateICPInfo() {
        List<DomainSelling> tasks = domainSellingService.listPunycodeListForICPQuery(500, 0, 1);
        List<DomainSelling> domainSellingList = new ArrayList<>();
        for (DomainSelling task : tasks) {
            try {
                DomainSelling domainSelling = JMICPUtil.getICPInfo(task.getPunycode());
                domainSellingList.add(domainSelling);
                Thread.sleep(1000);
            } catch (Exception e) {
                logger.error("punycode:" + task.getPunycode() + " 查询备案信息失败", e);
            }
        }
        domainSellingService.batchUpdate(domainSellingList);
        if (!tasks.isEmpty()) {
            logger.info("updateICPInfo查询备案信息成功，查询数量:" + tasks.size());
        }
    }


    @Scheduled(cron = "0/2 * * * * *")
    public void initWXStatus() {
        List<DomainSelling> tasks = domainSellingService.listForQueryWXStatus(500, 0, 0);
        for (DomainSelling task : tasks) {
            try {
                String wxStatus = JMICPUtil.getWXStatusInfo(task.getWxStatusUrl());
                if ("未拦截".equals(wxStatus)) {
                    task.setWxStatus(1);
                } else if ("已拦截".equals(wxStatus)) {
                    task.setWxStatus(2);
                }
            } catch (Exception e) {

            }
        }
        domainSellingService.batchUpdate(tasks);
        if (!tasks.isEmpty()) {
            logger.info("initWXStatus查询微信状态成功，查询数量:" + tasks.size());
        }
    }


    @Scheduled(cron = "0/2 * * * * *")
    public void updateWXStatus() {
        List<DomainSelling> tasks = domainSellingService.listForQueryWXStatus(500, 0, 1);
        for (DomainSelling task : tasks) {
            try {
                String wxStatus = JMICPUtil.getWXStatusInfo(task.getWxStatusUrl());
                if ("已拦截".equals(wxStatus)) {
                    task.setWxStatus(2);
                }
            } catch (Exception e) {

            }
        }
        domainSellingService.batchUpdate(tasks);
        if (!tasks.isEmpty()) {
            logger.info("updateWXStatus查询微信状态成功，查询数量:" + tasks.size());
        }
    }
}
