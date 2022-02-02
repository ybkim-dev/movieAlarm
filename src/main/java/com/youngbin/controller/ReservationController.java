package com.youngbin.controller;

import com.youngbin.bo.ReservationBO;
import com.youngbin.vo.ReservationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/app/")
public class ReservationController {
    @Autowired
    ReservationBO reservationBO;

    @RequestMapping(value = "reservation", method = RequestMethod.POST)
    public void insertReservation(@RequestBody ReservationVO reservationVO) {
        reservationBO.insertReservation(reservationVO);
    }

}
