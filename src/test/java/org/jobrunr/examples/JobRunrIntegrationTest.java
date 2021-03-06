package org.jobrunr.examples;

import org.jobrunr.storage.StorageProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.jobrunr.jobs.states.StateName.SCHEDULED;
import static org.jobrunr.jobs.states.StateName.SUCCEEDED;
import static org.jobrunr.utils.StringUtils.substringAfter;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = DEFINED_PORT, classes = JobRunrApplication.class)
public class JobRunrIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    StorageProvider storageProvider;

    @Test
    public void givenEndpoint_whenJobEnqueuedViaJobLambda_thenJobIsProcessedWithin30Seconds() {
        String responseBody = enqueueJobViaRest("/via-job-lambda", "from-test");
        assertThat(responseBody).startsWith("Job Enqueued");

        final UUID enqueuedJobId = UUID.fromString(substringAfter(responseBody, ": "));
        await()
                .atMost(30, TimeUnit.SECONDS)
                .until(() -> storageProvider.getJobById(enqueuedJobId).hasState(SUCCEEDED));
    }

    @Test
    public void givenEndpoint_whenJobEnqueuedViaJobRequest_thenJobIsProcessedWithin30Seconds() {
        String responseBody = enqueueJobViaRest("/via-job-request", "from-test");
        assertThat(responseBody).startsWith("Job Enqueued");

        final UUID enqueuedJobId = UUID.fromString(substringAfter(responseBody, ": "));
        await()
                .atMost(30, TimeUnit.SECONDS)
                .until(() -> storageProvider.getJobById(enqueuedJobId).hasState(SUCCEEDED));
    }

    private String enqueueJobViaRest(String path, String input) {
        return restTemplate.getForObject("http://localhost:8080" + path + "/enqueue-example-job?name=" + input, String.class);
    }
}
