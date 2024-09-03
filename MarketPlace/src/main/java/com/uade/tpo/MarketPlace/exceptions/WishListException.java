package com.uade.tpo.MarketPlace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El producto ya se encuentra registrado de la lista de favoritos")
public class WishListException extends Exception{
    
}
