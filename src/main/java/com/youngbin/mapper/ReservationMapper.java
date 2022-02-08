package com.youngbin.mapper;

import com.youngbin.domain.Reservation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;


@Mapper
@Repository
public interface ReservationMapper {
    void insert(Reservation reservation);

    ArrayList<Reservation> findByDateAndTheater(String date, String theaterCode, String areaCode, Integer offset);

    void updatePush(Integer id);

}
