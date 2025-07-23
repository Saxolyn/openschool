package com.openschool.infrastructure.utils;

import java.time.Instant;
import java.time.ZoneId;

public class TimeUtils {

    public static Instant getCurrentTime() {
        return Instant.now().atZone(ZoneId.of("UTC+07:00")).toInstant();
    }
}
