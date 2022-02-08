package com.youngbin.batch.jobs;

import com.youngbin.batch.tasklets.ProcessReservationTasklet;
import com.youngbin.bo.ReservationBO;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ProcessReservationConfig {
    @Autowired
    private final JobBuilderFactory jobBuilderFactory;

    @Autowired
    private final StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ReservationBO reservationBO;

    @Bean
    public ProcessReservationTasklet processReservationTasklet(ReservationBO reservationBO) { return new ProcessReservationTasklet(reservationBO); }


    @Bean
    public Job processReservationJob() {
        return jobBuilderFactory.get("processReservationJob")
                .start(processReservationStep())
                .build();
    }

    @Bean
    public Step processReservationStep() {
        return stepBuilderFactory.get("processReservationStep")
                .tasklet(processReservationTasklet(reservationBO))
                .build();
    }
}
