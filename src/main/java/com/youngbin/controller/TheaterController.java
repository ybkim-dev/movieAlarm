package com.youngbin.controller;

import com.youngbin.bo.TheaterBO;
import com.youngbin.dto.TheaterDTO;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/app/")
public class TheaterController {
    @Autowired
    TheaterBO theaterBO;

    @RequestMapping(value = "theaters", method = RequestMethod.POST)
    public void insertTheatersByCrawling() throws IOException, JSONException {
        theaterBO.insertTheaterByCrawling();
    }

    @RequestMapping(value = "theater", method = RequestMethod.POST)
    public ResponseEntity<?> findByName(@RequestBody HashMap<String, String> theater) {
        TheaterDTO theaterDTO = new TheaterDTO();
        theaterDTO.setResultCode("200");
        theaterDTO.setResponse(theaterBO.findByName(theater.get("theaterName")));

        return new ResponseEntity<>(theaterDTO, HttpStatus.OK);
    }
}
