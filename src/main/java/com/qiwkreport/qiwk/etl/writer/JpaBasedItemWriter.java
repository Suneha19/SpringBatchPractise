package com.qiwkreport.qiwk.etl.writer;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;

public class JpaBasedItemWriter <T> implements ItemWriter<T>{

	@PersistenceContext
	private EntityManager entityManagerFactory;

	@Override
	public void write(List<? extends T> items) throws Exception {
		JpaItemWriter<T> writer = new JpaItemWriter<T>();
		writer.setEntityManagerFactory(entityManagerFactory.getEntityManagerFactory());
		writer.write(items);
	}

}
