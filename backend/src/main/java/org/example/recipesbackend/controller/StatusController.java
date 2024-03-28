package org.example.recipesbackend.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StatusController {

    @GetMapping("/status")
    public ResponseEntity status() {
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "status", "OK",
                "LocalTime", LocalDateTime.now(),
                "ZonedDateTime", ZonedDateTime.now(),
                "Locale", Locale.getDefault(),
                "Zone", ZoneId.systemDefault()
        ));
    }
}
