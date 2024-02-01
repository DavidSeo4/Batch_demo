package com.basic.batch.Batch_demo.batch;

import com.basic.batch.Batch_demo.model.Student;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Primary;

import java.util.Date;

@Primary
public class StudentProcessor implements ItemProcessor<Student, Student> {

    @Override
    public Student process(Student item) throws Exception {

        //We apply all the necessary business logic here.

        //In this case we pass the IDs to 0, since they are generated in the DB, and we define the time at which
        //each record is saved to check the progression of seconds in the DB//

        item.setId(0);
        item.setSavedAt(new Date());

        return item;
    }
}
