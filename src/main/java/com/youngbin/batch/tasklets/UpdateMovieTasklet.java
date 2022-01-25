package com.youngbin.batch.tasklets;

import com.youngbin.bo.MovieBO;
import com.youngbin.domain.Movie;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class UpdateMovieTasklet implements Tasklet {
    /**
     * @desc crawling을 통해 영화 정보를 얻어, 데이터베이스에 주기적으로 업데이트하는 tasklet
     */
    private final MovieBO movieBO;

    public UpdateMovieTasklet(MovieBO movieBO) {
        this.movieBO = movieBO;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            // movie 정보 지우고 새 크롤링 정보 덮어씌우기
            movieBO.insertMovieByCrawling();
            log.info("Sucessfully write movie information.");
        } catch (Exception e) {
            log.info("Failed to write movie information.");
        }
        return RepeatStatus.FINISHED;
    }
}
