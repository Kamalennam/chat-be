package com.chat.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class handleErrorPage implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<String> handleError() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Custom error page or JSON");
    }
}

