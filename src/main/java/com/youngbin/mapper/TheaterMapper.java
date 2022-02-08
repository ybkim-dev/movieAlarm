package com.youngbin.mapper;

import com.youngbin.domain.Theater;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface TheaterMapper {
    Theater findByName(String theaterName);

    ArrayList<Theater> findAll();

    void insert(Theater theater);

    void deleteAll();
}
