package com.basic.batch.Batch_demo;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {


    private final JobLauncher jobLauncher;
    private final Job job;

    @PostMapping("import")
    public void importCsvToBDJob(){
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();

        try {
            jobLauncher.run(job, jobParameters);
        }
        catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
               JobParametersInvalidException | JobRestartException e) {
            e.printStackTrace();
        }
    }


}