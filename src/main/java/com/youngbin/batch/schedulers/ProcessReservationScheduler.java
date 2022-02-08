package com.youngbin.batch.schedulers;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ProcessReservationScheduler {

    private final Job processReservationJob;
    private final JobLauncher jobLauncher;

    @Scheduled(cron = "0 */5 * * * *") // 5분마다 동작.
    public void executeJob() {
        try {
            jobLauncher.run(
                    processReservationJob,
                    new JobParametersBuilder()
                            .addString("datetime", LocalDateTime.now().toString())
                            .toJobParameters()
            );
        } catch (JobExecutionException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
