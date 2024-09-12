package org.justme.articlestest.security.exception;

import lombok.extern.slf4j.Slf4j;
import org.justme.articlestest.data.response.ResponseFail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class RESTControllerExceptionHandler {

    @ControllerAdvice
    public class RESTControllersExceptionHandler extends ResponseEntityExceptionHandler {


        @ExceptionHandler({Exception.class})
        public ResponseEntity<?> handleCustomException(Exception ex) {
            logger.error(ex.getMessage(), ex);

            return createResponseFail(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler({AccessDeniedException.class})
        public ResponseEntity<?> handleSecurityException(Exception ex) {
            logger.debug(ex.getMessage(), ex);

            return createResponseFail(ex.getMessage(), HttpStatus.FORBIDDEN);
        }

        private ResponseEntity<?> createResponseFail(String message, HttpStatus httpStatus) {

            return new ResponseEntity<>(new ResponseFail(message), httpStatus);
        }
    }
}
