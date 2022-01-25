package com.youngbin.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class Movie {
    public Integer movieId;
    public String movieTitle;
    public String movieImageSource;
    public Double movieScore;
    public Date openingDate;
    public String reservationLink;
    public Boolean isOpened;
}
