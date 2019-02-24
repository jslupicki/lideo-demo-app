package com.slupicki.lideo.testTools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class JSON {

  public static final ObjectMapper MAPPER;

  static {
    MAPPER = new ObjectMapper()
        .registerModule(new Jdk8Module())
        .registerModule(new JavaTimeModule());
    MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    MAPPER.setDateFormat(new StdDateFormat());
    MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
  }
}
