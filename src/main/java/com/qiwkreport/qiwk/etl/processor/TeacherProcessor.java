package com.qiwkreport.qiwk.etl.processor;

import org.springframework.batch.item.ItemProcessor;

import com.qiwkreport.qiwk.etl.domain.NewTeacher;
import com.qiwkreport.qiwk.etl.domain.OldTeacher;

public class TeacherProcessor implements ItemProcessor<OldTeacher, NewTeacher> {
	

	@Override
	public NewTeacher process(OldTeacher oldTeacher) throws Exception {

		return new NewTeacher(oldTeacher.getId(),oldTeacher.getTeacherName(),oldTeacher.getTeacherAge());
	}

}
