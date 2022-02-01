package com.youngbin.mapper;

import com.youngbin.domain.Movie;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
@Repository
public interface MovieMapper {
    ArrayList<HashMap<String, Object>> findAll();
    Movie findByName(String movieName);
    void deleteAll();
    void insert(Movie movie);
}
