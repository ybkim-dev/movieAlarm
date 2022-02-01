package com.youngbin.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Theater {
    public Integer id;
    public String theaterName;
    public String theaterCode;
    public String areaName;
    public String areaCode;
    public String theaterNameEng;
    public String areaNameEng;
}
