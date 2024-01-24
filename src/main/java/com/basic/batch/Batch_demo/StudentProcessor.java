package com.basic.batch.Batch_demo;

import com.basic.batch.Batch_demo.model.Student;
import org.springframework.batch.item.ItemProcessor;

import java.util.Date;

public class StudentProcessor implements ItemProcessor<Student, Student> {

    @Override
    public Student process(Student item) throws Exception {

        //Aplicamos toda la lógica de negocio necesaria.
        //En este caso pasamos los ID a 0, ya que se generan en BD y definimos la hora a la que se
        //guarda cada registro para comprobar en la BD la progresión de segundos//

        item.setId(0);
        item.setSavedAt(new Date());

        return item;
    }
}
