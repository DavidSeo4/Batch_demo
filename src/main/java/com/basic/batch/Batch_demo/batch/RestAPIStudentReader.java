package com.basic.batch.Batch_demo.batch;

import com.basic.batch.Batch_demo.model.Student;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

public class RestAPIStudentReader implements ItemReader<Student> {

    private final String url;
    private final RestTemplate restTemplate;
    private int nextStudent;
    private List<Student> studentsList;
    private int count = 1;

    public RestAPIStudentReader(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }


    @Override
    public Student read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        if (this.studentsList == null){
            studentsList =  fetchStudents();
        }
         Student student = null;

        if (nextStudent<studentsList.size()){
            student = studentsList.get(nextStudent);
            nextStudent++;
        } else {
            nextStudent = 0;
            studentsList = null;
        }

        return student;
    }

    private List<Student> fetchStudents() {
        ResponseEntity<Student[]> response = restTemplate.getForEntity(this.url, Student[].class);
        Student[] students = response.getBody();
        System.out.println("Fetch numero " + count);
        count++;
        if (students!= null){
            return Arrays.asList(students);
        }
        return null;
    }
}
