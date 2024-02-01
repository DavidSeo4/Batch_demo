package com.basic.batch.Batch_demo.batch;

import com.basic.batch.Batch_demo.model.Student;
import org.springframework.batch.item.ItemProcessor;

public class RestAPIStudentProcessor implements ItemProcessor<Student, Student> {
    @Override
    public Student process(Student item) throws Exception {

        item.setFirstname("New " + item.getFirstname());

        return item;
    }
}
