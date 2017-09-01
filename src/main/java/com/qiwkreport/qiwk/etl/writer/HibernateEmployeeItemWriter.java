/*package com.qiwkreport.qiwk.etl.writer;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.HibernateItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.qiwkreport.qiwk.etl.domain.NewEmployee;

public class HibernateEmployeeItemWriter implements ItemWriter<NewEmployee> {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void write(List<? extends NewEmployee> items) throws Exception {
		HibernateItemWriter<NewEmployee> hibernateItemWriter=new HibernateItemWriter<>();
		hibernateItemWriter.setSessionFactory(sessionFactory);
		hibernateItemWriter.afterPropertiesSet();
	}

}
*/