package org.jobrunr.examples.jobs;

import org.jobrunr.jobs.lambdas.JobRequestHandler;
import org.springframework.stereotype.Component;

@Component
public class SampleJobRequestHandler implements JobRequestHandler<SampleJobRequest> {

    @Override
    public void run(SampleJobRequest jobRequest) throws InterruptedException {
        Thread.sleep(8000); // simulate a long running job
        System.out.println("Running sample job request in background: " + jobRequest.getInput());
    }
}
