package com.qiwkreport.qiwk.etl.domain;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class EmployeeProcessor implements ItemProcessor<Employee, NewEmployee> {

  private String threadName;

  public String getThreadName() {
    return threadName;
  }

  public void setThreadName(String threadName) {
    this.threadName = threadName;
  }

  @Override
  public NewEmployee process(Employee item) throws Exception {
	  NewEmployee employee=new NewEmployee(item.getId(),item.getFirstName(),item.getLastName());
    System.out.println(threadName + " processing : "
        + item.getId() + " : " + item.getFirstName()+" : "+item.getLastName());
    return employee;
  }
}
