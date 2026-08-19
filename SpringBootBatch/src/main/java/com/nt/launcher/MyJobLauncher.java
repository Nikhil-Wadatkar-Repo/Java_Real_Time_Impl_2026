package com.nt.launcher;

import java.util.HashMap;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyJobLauncher implements CommandLineRunner {
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;
    @Override
    public void run(String... args) throws Exception {
    	
    	HashMap<Integer, Integer> map=new HashMap<>();
    	System.out.println(map.put(1, 2));
        jobLauncher.run(job,new JobParametersBuilder()
                .addLong("time",System.currentTimeMillis())
                .toJobParameters());
    }
}
