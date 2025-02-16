package com.appsdeveloperblog.ws.mockservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/response")
@Slf4j
public class StatusCheckController {

    @GetMapping("/200")
    ResponseEntity<String> response200String() {
        log.info("200");
        return ResponseEntity.ok().body("200");
    }

    @GetMapping("/500")
    ResponseEntity<String> response500String() {
        log.info("500");
        return ResponseEntity.internalServerError().build();
    }
}
