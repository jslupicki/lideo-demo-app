package com.slupicki.lideo;

import com.slupicki.lideo.misc.TimeProvider;
import java.time.ZonedDateTime;
import org.springframework.stereotype.Component;

@Component
public class ConfigurableTimeProviderImpl implements TimeProvider {

  private ZonedDateTime time = ZonedDateTime.now();

  @Override
  public ZonedDateTime getTime() {
    return time;
  }

  public void setTime(ZonedDateTime time) {
    this.time = time;
  }
}
