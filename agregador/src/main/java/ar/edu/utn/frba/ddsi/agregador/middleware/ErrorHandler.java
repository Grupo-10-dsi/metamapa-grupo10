package ar.edu.utn.frba.ddsi.agregador.middleware;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    private ResponseEntity<Map<String,Object>> json(HttpStatus status, String message) {
        Map<String,Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNoHandler(NoHandlerFoundException e) {
        return json(HttpStatus.NOT_FOUND, "Endpoint no encontrado: " + e.getRequestURL());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String expected = e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "tipo correcto";
        return json(HttpStatus.BAD_REQUEST, "Valor inválido para parámetro '" + e.getName() + "': '" + e.getValue() + "' (esperado " + expected + ")");
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handlerResponseStatusException(ResponseStatusException e) {
        HttpStatus status = HttpStatus.valueOf(e.getStatusCode().value());
        String msg = e.getReason() != null ? e.getReason() : e.getMessage();
        return json(status, msg);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e) {
        return json(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerException(Exception e) {
        int statusCode = getStatusFromException(e);
        HttpStatus status = HttpStatus.valueOf(statusCode);
        return json(status, status.is5xxServerError() ? "Error interno" : e.getMessage());
    }

    private int getStatusFromException(Exception e) {
        if (e instanceof ResponseStatusException rse) {
            return rse.getStatusCode().value();
        }
        if (e instanceof IllegalArgumentException) {
            return 400;
        }
        return 500;
    }
}