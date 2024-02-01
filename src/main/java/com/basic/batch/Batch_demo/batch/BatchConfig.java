package com.basic.batch.Batch_demo.batch;

import com.basic.batch.Batch_demo.dao.StudentDao;
import com.basic.batch.Batch_demo.model.Student;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BatchConfig {

    private final StudentDao studentDao;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;


    public BatchConfig(StudentDao studentDao, JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        this.studentDao = studentDao;
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
    }

    @Bean
    public StudentProcessor processor(){
        return new StudentProcessor();
    }

    @Bean RestAPIStudentProcessor restAPIStudentProcessor(){return new RestAPIStudentProcessor();
    }

    public RepositoryItemWriter<Student> repositoryItemWriter(){
        RepositoryItemWriter<Student> writer = new RepositoryItemWriter<>();
        writer.setRepository(studentDao);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public FlatFileItemReader<Student> itemReader(){
        FlatFileItemReader<Student> itemReaderStudent = new FlatFileItemReader<>();
        itemReaderStudent.setResource(new FileSystemResource("src/main/resources/students.csv"));
        itemReaderStudent.setName("csvReader");
        itemReaderStudent.setLinesToSkip(1);
        itemReaderStudent.setLineMapper(lineMapperStudent());
        return itemReaderStudent;
    }

    @Bean
    @StepScope
    public RestAPIStudentReader restAPIStudentReader(){
        return new RestAPIStudentReader("http://localhost:8080/students/get_all", new RestTemplate());
    }

    private LineMapper<Student> lineMapperStudent() {
        DefaultLineMapper<Student> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "firstname", "lastname", "age");

        BeanWrapperFieldSetMapper<Student> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Student.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public Step importCSVstep(){
        return new StepBuilder("csvImport", jobRepository)
                .<Student, Student>chunk(100, platformTransactionManager)
                .reader(itemReader())
                .processor(processor())
                .writer(repositoryItemWriter())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step RESTtoSQLstep(){
        return new StepBuilder("csvImport", jobRepository)
                .<Student, Student>chunk(100, platformTransactionManager)
                .reader(restAPIStudentReader())
                .processor(restAPIStudentProcessor())
                .writer(repositoryItemWriter())
                .build();
    }

    @Bean
    @Primary
    public Job runCSVjob(){
        return new JobBuilder("importStudentsFromCSV", jobRepository)
                .start(importCSVstep())
                .build();
    }

    @Bean
    public Job restToSqlJob(){
        return new JobBuilder("importStudentsFromRestToSQL", jobRepository)
                .start(RESTtoSQLstep())
                .build();
    }


    @Bean
    public TaskExecutor taskExecutor(){
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }
}
