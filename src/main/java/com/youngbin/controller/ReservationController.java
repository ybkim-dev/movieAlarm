package com.youngbin.controller;

import com.youngbin.bo.ReservationBO;
import com.youngbin.dto.ResponseDTO;
import com.youngbin.vo.ReservationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> insertReservation(@RequestBody ReservationVO reservationVO) {
        try {
            reservationBO.insertReservation(reservationVO);
            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.setResultCode("200");
            responseDTO.setResponse("Insert Success");
        d    return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        } catch (NullPointerException e) {
            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.setResultCode("500");
            responseDTO.setResponse("Insert Failure");
            return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
        }

    }

}
