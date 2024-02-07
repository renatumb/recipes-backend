package org.example.recipesbackend.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity userNotFound() {
        Map<String, String> body = new HashMap();
        body.put("message", "User not found for the given ID");
        body.put("timestamp", String.valueOf(new Date()));

        return new ResponseEntity(body,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity resourceAlreadyExistsException() {
        Map<String, String> body = new HashMap();
        body.put("message", "This identifier is already being used");
        body.put("timestamp", String.valueOf(new Date()));

        return new ResponseEntity(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity recipeNotFound(){
        Map<String, String> body = new HashMap();
        body.put("message", "Recipe not found for the given ID");
        body.put("timestamp", String.valueOf(new Date()));

        return new ResponseEntity(body,HttpStatus.NOT_FOUND);
    }
}
