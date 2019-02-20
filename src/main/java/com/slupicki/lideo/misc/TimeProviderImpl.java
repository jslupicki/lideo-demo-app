package com.slupicki.lideo.misc;

import java.time.ZonedDateTime;

public class TimeProviderImpl implements TimeProvider {

  @Override
  public ZonedDateTime getTime() {
    return ZonedDateTime.now();
  }
}
