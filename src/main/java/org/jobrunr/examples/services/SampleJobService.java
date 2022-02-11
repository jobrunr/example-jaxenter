package org.jobrunr.examples.services;

import org.springframework.stereotype.Service;

@Service
public class SampleJobService {

    public void executeSampleJob(String input) throws InterruptedException {
        Thread.sleep(8000); // simulate a long running job
        System.out.println("Executing sample job with input: " + input);
    }
}
