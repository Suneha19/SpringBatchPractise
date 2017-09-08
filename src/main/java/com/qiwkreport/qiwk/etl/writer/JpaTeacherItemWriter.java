package com.qiwkreport.qiwk.etl.writer;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;

import com.qiwkreport.qiwk.etl.domain.NewTeacher;

public class JpaTeacherItemWriter implements ItemWriter<NewTeacher> {

	@PersistenceContext
	private EntityManager entityManagerFactory;

	@Override
	public void write(List<? extends NewTeacher> items) throws Exception {

		JpaItemWriter<NewTeacher> writer = new JpaItemWriter<NewTeacher>();
		writer.setEntityManagerFactory(entityManagerFactory.getEntityManagerFactory());
		writer.write(items);

	}

}
