package com.slupicki.lideo.exceptions;

public class NotEnoughtSeatsInFlightException extends Exception {

  public NotEnoughtSeatsInFlightException() {
  }

  public NotEnoughtSeatsInFlightException(String message) {
    super(message);
  }

  public NotEnoughtSeatsInFlightException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotEnoughtSeatsInFlightException(Throwable cause) {
    super(cause);
  }

  public NotEnoughtSeatsInFlightException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
