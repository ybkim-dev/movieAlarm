package com.youngbin.controller;

import com.youngbin.bo.TheaterBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TheaterController {
    @Autowired
    TheaterBO theaterBO;

}
