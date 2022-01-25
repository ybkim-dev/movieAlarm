package com.youngbin.batch.jobs;

import com.youngbin.batch.tasklets.UpdateMovieTasklet;
import com.youngbin.bo.MovieBO;
import com.youngbin.domain.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UpdateMovieConfig {
    @Autowired
    private final JobBuilderFactory jobBuilderFactory;
    @Autowired
    private final StepBuilderFactory stepBuilderFactory;
    @Autowired
    private MovieBO movieBO;


    @Bean
    public UpdateMovieTasklet updateMovieTasklet(MovieBO movieBO) {
        return new UpdateMovieTasklet(movieBO);
    }

    @Bean
    public Job updateMovieJob() {
        return jobBuilderFactory.get("updateMovieJob")
                .start(updateMovieStep())
                .build();
    }

    @Bean
    public Step updateMovieStep() {
        return stepBuilderFactory.get("updateMovieStep")
                .tasklet(updateMovieTasklet(movieBO))
                .build();
    }
}
