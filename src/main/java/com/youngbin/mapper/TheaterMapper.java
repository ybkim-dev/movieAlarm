package com.youngbin.mapper;

import com.youngbin.domain.Theater;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TheaterMapper {
    void insert(Theater theater);
    void deleteAll();
}