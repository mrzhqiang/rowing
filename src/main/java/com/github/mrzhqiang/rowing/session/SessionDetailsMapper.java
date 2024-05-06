package com.github.mrzhqiang.rowing.session;

import com.github.mrzhqiang.rowing.third.WhoIsIpData;
import com.google.common.base.Strings;
import com.maxmind.geoip2.model.CityResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SessionDetailsMapper {

    String LOCAL_LANGUAGE = "zh-CN";
    String LOCATION_TEMPLATE = "%s %s";
    String UNKNOWN_LOCATION = "(unknown)";

    @Mapping(target = "accessType", ignore = true)
    @Mapping(target = "location", source = "addr")
    SessionDetails toDetails(WhoIsIpData data);

    @Mapping(target = "accessType", ignore = true)
    @Mapping(target = "ip", source = "response.traits.ipAddress")
    @Mapping(target = "location", source = "response")
    SessionDetails toDetails(CityResponse response);

    SessionDetails toDetails(String ip, String location, String accessType);

    default String mapLocation(CityResponse response) {
        String cityName = response.getCity().getNames().get(LOCAL_LANGUAGE);
        String countryName = response.getCountry().getNames().get(LOCAL_LANGUAGE);
        String location = UNKNOWN_LOCATION;
        if (!Strings.isNullOrEmpty(countryName) && !Strings.isNullOrEmpty(cityName)) {
            location = Strings.lenientFormat(LOCATION_TEMPLATE, countryName, cityName);
        }
        return location;
    }

}
