package com.cheng.o2o.config.quartz;

import com.cheng.o2o.service.ProductSellDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 定时任务 Quartz 配置
 *
 * @author cheng
 *         2018/4/22 13:51
 */
@Configuration
public class QuartzConfiguration {

    @Autowired
    ProductSellDailyService productSellDailyService;
    @Autowired
    private MethodInvokingJobDetailFactoryBean jobDetailFactory;
    @Autowired
    private CronTriggerFactoryBean productSellDailyTriggerFactory;

    /**
     * 创建 jobDetailFactory 并返回
     *
     * @return
     */
    @Bean(name = "jobDetailFactory")
    public MethodInvokingJobDetailFactoryBean createJobDetail() {

        // new 出 JobDetailFactory 对象，此工厂主要用来创建一个jobDetail，即创建一个任务
        // 由于定时任务跟不上就是执行一个方法。所以用这个工厂比较方便
        MethodInvokingJobDetailFactoryBean jobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
        // 设置 jobDetail 的名字
        jobDetailFactoryBean.setName("product_sell_daily_job");
        // 设置 jobDetail 的组名
        jobDetailFactoryBean.setGroup("job_product_sell_daily_group");
        // 对于相同的 jobDetail，当指定多个 Trigger 时，很可能第一个 job 完成之前，第二个 job 就开始了。
        // 指定 concurrent 为 false，多个 job 不会并发运行，第二个 job 将不会在第一个 job 完成之前开始。
        jobDetailFactoryBean.setConcurrent(false);
        // 指定运行任务的类
        jobDetailFactoryBean.setTargetObject(productSellDailyService);
        // 指定运行任务的方法
        jobDetailFactoryBean.setTargetMethod("dailyCalculate");

        return jobDetailFactoryBean;
    }

    /**
     * 创建 cronTrigger 并返回
     *
     * @return
     */
    @Bean("productSellDailyTriggerFactory")
    public CronTriggerFactoryBean createProductSellDailyTriggerFactory() {

        // 创建 triggerFactory 实例
        CronTriggerFactoryBean triggerFactory = new CronTriggerFactoryBean();
        // 设置 triggerFactory 的名字
        triggerFactory.setName("product_sell_daily_trigger");
        // 设置 triggerFactory 的组名
        triggerFactory.setGroup("job_product_sell_daily_group");
        // 绑定 jobDetail
        triggerFactory.setJobDetail(jobDetailFactory.getObject());
        // 设置 cron 表达式   每天凌晨0点执行一次
        triggerFactory.setCronExpression("0 0 0 * * ? *");

        return triggerFactory;
    }

    /**
     * 创建调度工厂并反返回
     *
     * @return
     */
    @Bean("schedulerFactory")
    public SchedulerFactoryBean createSchedulerFactory() {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setTriggers(productSellDailyTriggerFactory.getObject());
        return schedulerFactory;
    }
}
