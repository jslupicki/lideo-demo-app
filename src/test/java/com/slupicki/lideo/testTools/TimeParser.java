package com.slupicki.lideo.testTools;

import com.google.common.collect.ImmutableList;
import io.vavr.control.Try;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.assertj.core.util.Strings;

public class TimeParser {

  private static final List<DateTimeFormatter> KNOWN_DATE_TIME_FORMATS = ImmutableList.of(
      "yyyy-MM-dd",
      "yyyy-MM-dd HH:mm:ss",
      "yyyy-MM-dd HH:mm",
      "dd-MM-yyyy",
      "dd-MM-yyyy HH:mm:ss",
      "dd-MM-yyyy HH:mm"
  ).stream()
      .map(s -> DateTimeFormatter.ofPattern(s).withZone(ZoneId.systemDefault()))
      .collect(Collectors.toList());

  public static ZonedDateTime parseZonedDateTime(String time) {
    if (Strings.isNullOrEmpty(time)) {
      return null;
    }
    for (DateTimeFormatter dateTimeFormatter : KNOWN_DATE_TIME_FORMATS) {
      try {
        return Try.of(() -> ZonedDateTime.parse(time, dateTimeFormatter))
            .recover(e -> LocalDate.parse(time, dateTimeFormatter).atStartOfDay(ZoneId.systemDefault()))
            .getOrElseThrow(t -> t);
      } catch (Throwable e) {
        // ignore - wrong format
      }
    }
    throw new RuntimeException("Can't parse date of '" + time + "' - unknown format");
  }
}
