package com.youngbin.mapper;

import com.youngbin.domain.Reservation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ReservationMapper {
    void insert(Reservation reservation);
}
