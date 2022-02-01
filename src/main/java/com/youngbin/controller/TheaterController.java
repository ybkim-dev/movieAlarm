package com.youngbin.controller;

import com.youngbin.bo.TheaterBO;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/app/")
public class TheaterController {
    @Autowired
    TheaterBO theaterBO;

    @RequestMapping(value = "theaters", method = RequestMethod.POST)
    public void insertTheatersByCrawling() throws IOException, JSONException {
        theaterBO.insertTheaterByCrawling();
    }
}
