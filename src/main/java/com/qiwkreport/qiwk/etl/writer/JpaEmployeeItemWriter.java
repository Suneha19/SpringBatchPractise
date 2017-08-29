package com.qiwkreport.qiwk.etl.writer;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;

import com.qiwkreport.qiwk.etl.domain.NewEmployee;

public class JpaEmployeeItemWriter implements ItemWriter<NewEmployee> {

	@PersistenceContext
	private EntityManager entityManagerFactory;

	@Override
	public void write(List<? extends NewEmployee> items) throws Exception {

		JpaItemWriter<NewEmployee> writer = new JpaItemWriter<NewEmployee>();
		writer.setEntityManagerFactory(entityManagerFactory.getEntityManagerFactory());
		writer.write(items);

	}

}
