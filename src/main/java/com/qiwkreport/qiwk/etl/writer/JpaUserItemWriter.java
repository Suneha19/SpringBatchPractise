package com.qiwkreport.qiwk.etl.writer;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;

import com.qiwkreport.qiwk.etl.domain.NewUser;

public class JpaUserItemWriter implements ItemWriter<NewUser> {

	@PersistenceContext
	private EntityManager entityManagerFactory;

	@Override
	public void write(List<? extends NewUser> items) throws Exception {

		JpaItemWriter<NewUser> writer = new JpaItemWriter<NewUser>();
		writer.setEntityManagerFactory(entityManagerFactory.getEntityManagerFactory());
		writer.write(items);

	}

}
