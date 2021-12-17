package com.dokev.gold_dduck.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeUtil {

    public static LocalDateTime seoulTimeToUtc(LocalDateTime localDateTime){
        ZonedDateTime zonedDateTime1 = localDateTime.atZone(ZoneId.of("Asia/Seoul"));
        ZonedDateTime utc = zonedDateTime1.withZoneSameInstant(ZoneId.of("UTC"));
        return utc.toLocalDateTime();
    }

}
