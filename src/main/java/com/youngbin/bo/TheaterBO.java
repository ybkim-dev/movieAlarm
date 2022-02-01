package com.youngbin.bo;

import com.youngbin.domain.Theater;
import com.youngbin.mapper.TheaterMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TheaterBO {
    @Autowired
    TheaterMapper theaterMapper;

    private final static String crawlingURL = "http://www.cgv.co.kr/theaters/?areacode=01&theaterCode=0056";

    /**
     * @desc 크롤링하여 그 결과를 파싱하고 데이터베이스에 저장.
     */
    public void insertTheaterByCrawling() throws IOException, JSONException {
        theaterMapper.deleteAll();
        Document document = getCrawlingConnection(crawlingURL);
        String theaterJSON = parseTheaterJSON(document);
        List<Theater> theaters = convert(theaterJSON);
        theaters.stream()
                .forEach(theaterMapper::insert);
    }

    public Theater findByName(String theaterName) {
        return theaterMapper.findByName(theaterName);
    }

    /**
     * @desc 데이터베이스 내의 영화관 정보 삭제.
     */
    public void deleteAll() {
        theaterMapper.deleteAll();
    }

    /**
     * @desc 크롤링 커넥션 생성 함수.
     */
    private Document getCrawlingConnection(String crawlingURL) throws IOException {
        return Jsoup.connect(crawlingURL).get();
    }

    /**
     *
     * @desc 크롤링 결과에서 영화관 정보 JSON만 String 형태로 파싱.
     */
    private String parseTheaterJSON(Document document) {
        Elements scripts = document.getElementsByTag("script");

        String theaterJSON = scripts.stream()
                .filter(script -> script.data().contains("var theaterJsonData"))
                .map(script -> {
                    Pattern pattern = Pattern.compile("(var theaterJsonData = )([^;]*)");
                    Matcher matcher = pattern.matcher(script.data());
                    return matcher;
                })
                .filter(matcher -> matcher.find())
                .findAny()
                .map(matcher -> matcher.group(2))
                .orElse(null);
        return theaterJSON;
    }

    /**
     * @desc JSON 데이터에서 필요한 부분 가져와서 List<Theater>에 담기.
     */
    private List<Theater> convert(String theaterJsonData) throws JSONException {

        List<Theater> theaters = new ArrayList<>();

        JSONArray theaterInfoJsonArray = new JSONArray(theaterJsonData);
        for(int i=0;i<theaterInfoJsonArray.length();i++){
            JSONObject curRegion = theaterInfoJsonArray.getJSONObject(i);

            String regionName = curRegion.getString("RegionName");
            String regionNameEng = curRegion.getString("RegionName_ENG");

            JSONArray AreaTheaterDetailList = curRegion.getJSONArray("AreaTheaterDetailList");
            for(int j=0;j<AreaTheaterDetailList.length();j++){
                JSONObject curTheaterInfo = AreaTheaterDetailList.getJSONObject(j);

                Theater theater = Theater.builder()
                        .theaterCode(curTheaterInfo.getString("TheaterCode"))
                        .theaterName(curTheaterInfo.getString("TheaterName"))
                        .theaterNameEng(curTheaterInfo.getString("TheaterName_ENG"))
                        .areaCode(curTheaterInfo.getString("RegionCode"))
                        .areaName(regionName)
                        .areaNameEng(regionNameEng)
                        .build();

                theaters.add(theater);
            }
        }
        return theaters;
    }
}
