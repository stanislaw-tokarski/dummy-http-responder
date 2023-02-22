package com.github.stanislawtokarski.dummyhttpresponder.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

@RestController
public class SleepyController {

    private static final Logger log = LoggerFactory.getLogger(SleepyController.class);
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private final long napTimeMillis;

    @Autowired
    public SleepyController(@Value("${controller.delay.millis}") long napTimeMillis) {
        this.napTimeMillis = napTimeMillis;
    }

    @GetMapping("/get")
    public ResponseEntity<String> get() {
        try {
            log.info("Thread {} handling GET request. Will go to sleep for {} ms at {}",
                    currentThread().getName(), napTimeMillis, timestamp());
            TimeUnit.MILLISECONDS.sleep(napTimeMillis);
            log.info("Thread {} handling GET request. Woke up after {} ms at {}",
                    currentThread().getName(), napTimeMillis, timestamp());
            return ResponseEntity
                    .ok("OK");
        } catch (InterruptedException e) {
            log.error("Thread {} interrupted while handling GET request", timestamp());
            return ResponseEntity
                    .internalServerError()
                    .body("Ooops, something went wrong");
        }
    }

    private String timestamp() {
        return SDF.format(new Date(System.currentTimeMillis()));
    }
}
