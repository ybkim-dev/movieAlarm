package com.youngbin.controller;


import com.youngbin.bo.MovieBO;
import com.youngbin.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;


@RestController
@RequestMapping("/api/v1/app/")
public class MovieController {
    @Autowired
    MovieBO movieBO;

    /**
     * @desc : 모든 영화 목록 평점 순으로 정렬하여 반환.
     * @return : 평점 순으로 정렬된 영화 리스
     */
    @RequestMapping(value = "movies", method = RequestMethod.GET)
    public ResponseEntity<?> findAll() {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setResultCode("200");
        responseDTO.setResponse(movieBO.findAll());

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @RequestMapping(value= "movie", method = RequestMethod.POST)
    public ResponseEntity<?> findByName(@RequestBody HashMap<String, String> movie) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setResultCode("200");
        responseDTO.setResponse(movieBO.findByName(movie.get("movieName")));

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "movies", method = RequestMethod.DELETE)
    public void deleteAll() {
        movieBO.deleteAll();
    }

    @RequestMapping(value = "movies", method = RequestMethod.POST)
    public void insertMoviesByCrawling() throws IOException, ParseException {
        movieBO.insertMovieByCrawling();
    }
}
