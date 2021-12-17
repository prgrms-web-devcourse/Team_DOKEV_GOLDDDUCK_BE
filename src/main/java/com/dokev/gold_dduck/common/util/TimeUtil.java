package com.dokev.gold_dduck.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeUtil {

    public static LocalDateTime seoulTimeToUtc(LocalDateTime localDateTime) {
        ZonedDateTime seoulTime = localDateTime.atZone(ZoneId.of("Asia/Seoul"));
        ZonedDateTime utcTime = seoulTime.withZoneSameInstant(ZoneId.of("UTC"));
        return utcTime.toLocalDateTime();
    }

    public static LocalDateTime utcToSeoul(LocalDateTime localDateTime) {
        ZonedDateTime utcTime = localDateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime seoulTime = utcTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"));
        return seoulTime.toLocalDateTime();
    }

}
