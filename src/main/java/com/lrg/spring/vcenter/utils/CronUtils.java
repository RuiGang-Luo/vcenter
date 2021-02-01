package com.lrg.spring.vcenter.utils;

import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Date;

public class CronUtils {

        private static Logger logger = LoggerFactory.getLogger(CronUtils.class);
        /**
         * 校验在当前时间是否满足cron时间规则表达式
         * @param cron
         * @param date
         * @return
         * @throws ParseException
         */
        public static Boolean filterWithCronTime(String cron, Date date) throws ParseException {
            CronExpression cronExpression = new CronExpression(cron);
            boolean resCron = cronExpression.isSatisfiedBy(date);
            return resCron;
        }

//        public static void main(String[] args) throws ParseException {
//            String cron = "0 0 10,11,15 * * ? ";
//            System.out.println(filterWithCronTime(cron, "HH"));//true，我当前时间为15:36，
//            System.out.println(filterWithCronTime(cron, "HHmm"));//false，我当前时间为15:36，
//        }

}
