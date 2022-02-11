package org.jobrunr.examples.jobs;

import org.jobrunr.jobs.lambdas.JobRequest;

public class SampleJobRequest implements JobRequest {

    private final String input;

    protected SampleJobRequest() {
        // needed for Jackson Deserialization
        this.input = null;
    }

    public SampleJobRequest(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    @Override
    public Class<SampleJobRequestHandler> getJobRequestHandler() {
        return SampleJobRequestHandler.class;
    }
}
