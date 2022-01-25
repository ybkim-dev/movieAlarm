package com.youngbin.bo;

import com.youngbin.batch.tasklets.UpdateMovieTasklet;
import com.youngbin.domain.Movie;
import com.youngbin.mapper.MovieMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

@Service
public class MovieBO {

    @Autowired
    MovieMapper movieMapper;

    // 영화 정보 크롤링 URL
    private final static String movieChartCrawlingURL = "http://www.cgv.co.kr/movies/?lt=1&ft=0";

    public ArrayList<HashMap<String, Object>> findAll() {
        return movieMapper.findAll();
    }

    /**
     * @desc DB 내의 movie 테이블의 모든 원소 제거.
     */
    public void deleteAll() {
        movieMapper.deleteAll();
    }

    /**
     * @desc CGV 홈페이지 내의 영화 목록들로 크롤링하여 DB 초기화 작업 진행.
     */
    public void insertMovieByCrawling() throws IOException, ParseException {
        // crawling data를 DB에 추가하기 전에 이전 데이터 모두 제거.
        this.deleteAll();

        // connection
        Document document = this.getCrawlingConnection(movieChartCrawlingURL);

        Elements moviesInfo = document.select("div.sect-movie-chart li");
        // DB에 추가.
        moviesInfo.stream()
                .map(this::parseFromElement)
                .filter(Objects::nonNull)
                .forEach(movieMapper::insert);
    }

    /**
     * @desc 크롤링 커넥션 생성 함수.
     */
    public Document getCrawlingConnection(String crawlingURL) throws IOException {
        return Jsoup.connect(crawlingURL).get();
    }

    /**
     * @desc 크롤링한 영화 데이터 파싱 함수
     */
    public Movie parseFromElement(Element movieInfo) {
        // 영화 포스터 이미지 url
        String movieImageSource = movieInfo.select(".thumb-image img")
                .attr("abs:src");
        if(movieImageSource == "") return null;
        // 영화 제목
        String movieTitle = movieInfo.select(".title").text();

        // 예매율
        String movieScoreStr = movieInfo.select(".score strong span").text().replace("%", "");
        Double movieScore = Double.parseDouble(movieScoreStr);

        // 개봉 날짜
        String openingDateStr = movieInfo.select(".txt-info").text().substring(0,10);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Date openingDate = null;
        try {
            openingDate = dateFormat.parse(openingDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        // 개봉 여부
        Elements dDay = movieInfo.select(".dday");
        Boolean isOpened = (dDay.isEmpty()) ? true : false;

        // 예매 링크
        String reservationLink = movieInfo.select(".like a").attr("abs:href");

        return Movie.builder()
                .movieTitle(movieTitle)
                .movieImageSource(movieImageSource)
                .movieScore(movieScore)
                .openingDate(openingDate)
                .isOpened(isOpened)
                .reservationLink(reservationLink)
                .build();
    }

}
