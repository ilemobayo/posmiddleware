package com.elarasolutions.posmiddleware.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserNotFoundAdvice {

  @ResponseBody
  @ExceptionHandler(GenericNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String userNotFoundHandler(GenericNotFoundException ex) {
    return ex.getMessage();
  }

}