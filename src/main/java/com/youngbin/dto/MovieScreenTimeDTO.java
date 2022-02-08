package com.youngbin.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MovieScreenTimeDTO {
    public String movieTitle;
    public List<String> screenTime;
    public List<String> emptySeat;
}
