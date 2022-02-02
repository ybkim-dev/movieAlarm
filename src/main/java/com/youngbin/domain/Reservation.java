package com.youngbin.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Reservation {
    public Integer id;
    public String movieTitle;
    public String areaCode;
    public String theaterCode;
    public String time;
}
