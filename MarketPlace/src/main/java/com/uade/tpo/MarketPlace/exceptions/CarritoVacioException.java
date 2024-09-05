package com.uade.tpo.MarketPlace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El carrito está vacío.")
public class CarritoVacioException extends Exception {

}
