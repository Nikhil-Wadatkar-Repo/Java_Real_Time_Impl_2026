package com.nt.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@EnableBatchProcessing
@Component
public class BatchConfig {
    @Autowired
    private StepBuilderFactory sf;


    @Autowired
    private JobBuilderFactory jf;

    @Bean
    public Job job() {
        return jf.get("jobA")
                .incrementer(new RunIdIncrementer())
                .start(stepOne())
                .build();
    }

    //reader
    @Bean
    public ItemReader<String> myReader() {
        return () -> {
            String myReader = "my reader";
            return myReader;
        };
    }

    //processor
    @Bean
    public ItemProcessor<String, String> myProcessor() {
        return (people) -> {
            people = "Mr. " + people;
            return people;
        };
    }

    //writer
    @Bean
    public ItemWriter<String> myWriter() {
        return (myStr) -> {
            myStr =myStr;
            System.out.println(myStr);
        };
    }

    @Bean
    public Step stepOne() {
        return sf.get("stepA").<String, String>chunk(10).
                reader(myReader())
                .processor(myProcessor())
                .writer(myWriter())
                .build();
    }




}