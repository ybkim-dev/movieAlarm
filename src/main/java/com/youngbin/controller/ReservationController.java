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

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

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
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        } catch (NullPointerException | SQLException e) {
            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.setResultCode("500");
            responseDTO.setResponse("Insert Failure");
            return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
        }

    }

    @RequestMapping(value = "reservation/push", method = RequestMethod.GET)
    public ResponseEntity<?> processReservation() throws IOException {
        reservationBO.getScreenInfo("20220208", "01", "0056");
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setResultCode("200");
        responseDTO.setResponse("success");
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "reservation", method = RequestMethod.GET)
    public ResponseEntity<?> findReservation() throws IOException {
        reservationBO.reserve();
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setResultCode("200");
        responseDTO.setResponse("success");
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
