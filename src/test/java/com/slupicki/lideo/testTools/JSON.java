package com.slupicki.lideo.testTools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public final class JSON {

  public static final ObjectMapper MAPPER;

  static {
    MAPPER = new ObjectMapper();
    MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
  }
}
