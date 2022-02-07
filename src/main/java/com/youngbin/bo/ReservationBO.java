package com.youngbin.bo;

import com.youngbin.domain.Movie;
import com.youngbin.domain.Reservation;
import com.youngbin.domain.Theater;
import com.youngbin.mapper.ReservationMapper;
import com.youngbin.vo.ReservationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationBO {
    @Autowired
    MovieBO movieBO;

    @Autowired
    TheaterBO theaterBO;

    @Autowired
    ReservationMapper reservationMapper;

    public void insertReservation(ReservationVO reservationVO) throws NullPointerException {
        /**
         * TODO : find By Name 예외처리 (data 없는 경우 해결)
         */
        Movie selectedMovie = movieBO.findByName(reservationVO.getMovieTitle());
        Theater selectedTheater = theaterBO.findByName(reservationVO.getTheaterName());

        Reservation reservation = Reservation.builder()
                .movieTitle(selectedMovie.getMovieTitle())
                .areaCode(selectedTheater.getAreaCode())
                .theaterCode(selectedTheater.getTheaterCode())
                .time(reservationVO.getTime())
                .build();

        reservationMapper.insert(reservation);
    }
}
