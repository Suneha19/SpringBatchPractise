/**
 * 
 */
package com.qiwkreport.qiwk.etl.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * This is the default configuration for a
 * {@link org.springframework.core.task.TaskExecutor} used in the
 * {@link org.springframework.batch.core.launch.support.SimpleJobLauncher} for
 * starting jobs asynchronously. Its core thread pool is configured to five
 * threads by default, which can be changed by setting the property
 * batch.core.pool.size to a different number.
 * 
 * Please note the following rules of the ThreadPoolExecutor: If the number of
 * threads is less than the corePoolSize, the executor creates a new thread to
 * run a new task. If the number of threads is equal (or greater than) the
 * corePoolSize, the executor puts the task into the queue. If the queue is full
 * and the number of threads is less than the maxPoolSize, the executor creates
 * a new thread to run a new task. If the queue is full and the number of
 * threads is greater than or equal to maxPoolSize, reject the task.
 * 
 * So with the default configuration there will be only 4 jobs/threads at the
 * same time.
 * 
 * @author Abhilash
 * 
 */
@Configuration
public class TaskExecutorConfiguration {
	
	@Value("${taskExecutor.thread.max.pool}")
	private int maxPoolSize;
	
	@Value("${taskExecutor.thread.core.pool}")
	private int corePoolSize;
	
	@Value("${taskExecutor.queue.capacity}")
	private int queueCapacity;
	
	@Value("${taskExecutor.thread.timeout}")
	private int threadTimeOut;
	
	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(corePoolSize);
		taskExecutor.setQueueCapacity(queueCapacity);
		taskExecutor.setMaxPoolSize(maxPoolSize);
		taskExecutor.setKeepAliveSeconds(threadTimeOut);
		taskExecutor.afterPropertiesSet();
		return taskExecutor;
	}


}
