package com.elarasolutions.posmiddleware.config;

public class GenericNotFoundException extends RuntimeException {
  public GenericNotFoundException(Long id) {
     super("Could not find user " + id);
  }
}