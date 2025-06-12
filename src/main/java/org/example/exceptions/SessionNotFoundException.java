package org.example.exceptions;

public class SessionNotFoundException extends RuntimeException {
  public SessionNotFoundException() {
    super(ErrorMessages.format(ErrorMessages.INVALID_SESSION));
  }
}
