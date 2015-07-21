package luu.makehtml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.quartz.Scheduler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.CronTriggerBean;

public class RunMain_WX5656 implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	public void runMain() {
		new MakeHtmlChinaWuLiu().run_data();
	}
	
	// 初始化时分秒
	public void initTime() {
		Properties ps = new Properties();
		InputStream is = RunMain_WX5656.this.getClass().getClassLoader()
				.getResourceAsStream("config.properties");
		try {
			ps.load(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String hh = ps.getProperty("hh");
		String mm = ps.getProperty("mm");
		String ss = ps.getProperty("ss");

		try {
			Scheduler scheduler = (Scheduler) applicationContext.getBean("schedulerFactoryBean");
			CronTriggerBean trigger = (CronTriggerBean) scheduler.getTrigger("trigger", Scheduler.DEFAULT_GROUP);
			String dbCronExpression = ss + " " + mm + " " + hh + " * * ?";
			trigger.setCronExpression(dbCronExpression);
			scheduler.rescheduleJob("trigger", Scheduler.DEFAULT_GROUP, trigger);
			System.out.println(trigger.getCronExpression());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
}