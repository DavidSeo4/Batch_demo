package com.basic.batch.Batch_demo.controller;

import com.basic.batch.Batch_demo.dao.StudentDao;
import com.basic.batch.Batch_demo.model.Student;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {


    private final JobLauncher jobLauncher;
    private final Job jobCSVtoSQL;
    private final Job jobRestToSQL;
    private final StudentDao studentDao;

    public StudentController(JobLauncher jobLauncher, Job jobCSVtoSQL,  @Qualifier("restToSqlJob") Job jobRestToSQL, StudentDao studentDao) {
        this.jobLauncher = jobLauncher;
        this.jobCSVtoSQL = jobCSVtoSQL;
        this.jobRestToSQL = jobRestToSQL;
        this.studentDao = studentDao;
    }


    @PostMapping("import/CSV")
    public void importCsvToBDJob(){
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();

        try {
            jobLauncher.run(jobCSVtoSQL, jobParameters);
        }
        catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
               JobParametersInvalidException | JobRestartException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("import/REST")
    public void importRestToSQL(){
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();

        try {
            jobLauncher.run(jobRestToSQL, jobParameters);
        }
        catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
               JobParametersInvalidException | JobRestartException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("get_all")
    public List<Student> getAllStudents(){
       return studentDao.findAll();
    }


}
