package com.slupicki.lideo.rest;

import com.slupicki.lideo.exceptions.AlreadyExistException;
import com.slupicki.lideo.exceptions.NotFoundException;
import com.slupicki.lideo.exceptions.NotLoggedInException;
import com.slupicki.lideo.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AllControllerAdvices {

    @ResponseBody
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    String unathorizedAccessHandler(UnauthorizedException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(AlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String alreadyExist(AlreadyExistException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notExist(NotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(NotLoggedInException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    String notLoggedIn(NotLoggedInException ex) {
        return ex.getMessage();
    }


}
