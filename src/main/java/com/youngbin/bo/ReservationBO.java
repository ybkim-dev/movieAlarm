package com.youngbin.bo;

import com.youngbin.domain.Movie;
import com.youngbin.domain.Reservation;
import com.youngbin.domain.Theater;
import com.youngbin.dto.MovieScreenTimeDTO;
import com.youngbin.mapper.ReservationMapper;
import com.youngbin.vo.ReservationVO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationBO {
    @Autowired
    MovieBO movieBO;

    @Autowired
    TheaterBO theaterBO;

    @Autowired
    ReservationMapper reservationMapper;

    public void insertReservation(ReservationVO reservationVO) throws NullPointerException, SQLException {
        Movie selectedMovie = movieBO.findByName(reservationVO.getMovieTitle());
        Theater selectedTheater = theaterBO.findByName(reservationVO.getTheaterName());

        Reservation reservation = Reservation.builder()
                .movieTitle(selectedMovie.getMovieTitle())
                .areaCode(selectedTheater.getAreaCode())
                .theaterCode(selectedTheater.getTheaterCode())
                .day(reservationVO.getDay())
                .time(reservationVO.getTime())
                .build();
        if (reservation.getDay() == null || reservation.getTime() == null) {
            throw new SQLException("null exists");
        }
        reservationMapper.insert(reservation);
    }

    /**
     * @desc 일자, 장소코드, 영화관 코드를 입력받아 해당 날짜, 영화관의 상영정보 크롤링
     */
    public List<MovieScreenTimeDTO> getScreenInfo(String datetime, String areaCode, String theaterCode) throws IOException {
        String connectionURL = "http://www.cgv.co.kr/common/showtimes/iframeTheater.aspx?areacode=" + areaCode
                + "&theatercode=" + theaterCode + "&date=" + datetime + "&screencodes=&screenratingcode=&regioncode=";

        Document document = getConnection(connectionURL);
        Elements screenInfos = document.select("body > div > div.sect-showtimes > ul > li");
        List<MovieScreenTimeDTO> screenInfoList = new ArrayList<>();
        for (Element screenInfo : screenInfos) {
            screenInfoList.add(parseFromElement(screenInfo));
        }
        return screenInfoList;
    }


    /**
     * @desc 상영 테이블의 상영날짜 파싱
     */
    public ArrayList<String> getScreenDate(String datetime, String areaCode, String theaterCode) throws IOException {
        String connectionURL = "http://www.cgv.co.kr/common/showtimes/iframeTheater.aspx?areacode=" + areaCode
                + "&theatercode=" + theaterCode + "&date=" + datetime + "&screencodes=&screenratingcode=&regioncode=";

        Document document = getConnection(connectionURL);
        Elements screenDateElements = document.select("div.day a");
        ArrayList<String> screenDates = new ArrayList<String>();
        for (Element screenDateElement : screenDateElements) {
            String href = screenDateElement.attr("href");
            screenDates.add(href.split("&")[2].replace("date=", ""));
        }
        return screenDates;
    }

    public Document getConnection(String connectionURL) throws IOException {
        Document document = Jsoup.connect(connectionURL).get();
        return document;
    }

    /**
     * @desc 크롤링한 도큐먼트에서 영화 시간과 좌석 결과를 MovieScreenTimeDTO에 저장
     */
    public MovieScreenTimeDTO parseFromElement(Element screenInfo) {
        String movieTitle = screenInfo.select("div.col-times div.info-movie a strong").text();
        MovieScreenTimeDTO movieScreenTimeDTO = new MovieScreenTimeDTO();
        List<String> screenTimes = new ArrayList<String>();
        List<String> emptySeats = new ArrayList<String>();
        Elements timeTablesInfo = screenInfo.select("div.info-timetable ul li em");
        Elements emptySeatsInfo = screenInfo.select("div.info-timetable ul li span");
        timeTablesInfo.forEach(screenTime -> {
            screenTimes.add(screenTime.text().replace(":", ""));
        });
        emptySeatsInfo.forEach(emptySeat -> {
            if (emptySeat.text().equals("잔여좌석")) return;
            else {
                String preprocessed = emptySeat.text().replace("잔여좌석", "");
                String finalProcessed = preprocessed.replace("석", "");
                emptySeats.add(finalProcessed);
            }
        });

        movieScreenTimeDTO.setMovieTitle(movieTitle);
        movieScreenTimeDTO.setEmptySeat(emptySeats);
        movieScreenTimeDTO.setScreenTime(screenTimes);
        return movieScreenTimeDTO;
    }

    /**
     * @desc reservation 테이블에서 영화 예약 서비스 요청 목록 요일, 영화관 별로 가져오ㅇ기
     */
    public ArrayList<Reservation> findByDateAndTheater(String date, String theaterCode, String areaCode) {

        ArrayList<Reservation> result = reservationMapper.findByDateAndTheater(date, theaterCode, areaCode, 0);

        return result;
    }

    /**
     * @desc 크롤링 결과와 영화 예약 서비스 요청 목록을 대조하여 push 알림
     */
    public void reserve() throws IOException {
        // time formatter 설정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");

        // 영화관 정보 및 날짜 설정
        ArrayList<Theater> theaters = theaterBO.findAll();
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));


        for (Theater theater : theaters) {
            ArrayList<String> dates = getScreenDate(now.format(DateTimeFormatter.ofPattern("yyyyMMdd")), theater.getAreaCode(), theater.getTheaterCode());
            for (String date : dates) {
                // 영화관 실제 상영 정보 크롤링 결과 리스트
                List<MovieScreenTimeDTO> movieScreenTimeDTOList = this.getScreenInfo(date, theater.getAreaCode(), theater.getTheaterCode());
                // 해당 날짜, 영화의 reservation 요청 리스트
                List<Reservation> reservationList = this.findByDateAndTheater(date, theater.getTheaterCode(), theater.getAreaCode());

                for (MovieScreenTimeDTO movieScreenTimeDTO : movieScreenTimeDTOList) {
                    for (Reservation reservation : reservationList) {
                        LocalTime reservationTime = LocalTime.parse(reservation.getTime(), formatter);
                        for (int i = 0; i < movieScreenTimeDTO.getScreenTime().size(); i++) {
                            if (reservation.getMovieTitle().equals(movieScreenTimeDTO.getMovieTitle())) {
                                if (movieScreenTimeDTO.getScreenTime().get(i) == "마감") {
                                    continue;
                                }

                                LocalTime screenTime = LocalTime.parse(movieScreenTimeDTO.getScreenTime().get(i), formatter);
                                // 시간대가 동일하며 좌석이 있는 경우 예약처리
                                if (reservationTime.getHour() == screenTime.getHour() && Integer.parseInt(movieScreenTimeDTO.getEmptySeat().get(i)) > 0) {
                                    reservationMapper.updatePush(reservation.getId());
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}
