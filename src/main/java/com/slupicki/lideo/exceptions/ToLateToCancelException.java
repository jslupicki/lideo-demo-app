package com.slupicki.lideo.exceptions;

public class ToLateToCancelException extends Exception {

  public ToLateToCancelException() {
  }

  public ToLateToCancelException(String message) {
    super(message);
  }

  public ToLateToCancelException(String message, Throwable cause) {
    super(message, cause);
  }

  public ToLateToCancelException(Throwable cause) {
    super(cause);
  }

  public ToLateToCancelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
