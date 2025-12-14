package com.opspanel.task.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * A simple built-in Java task.
 *
 * Users can reference this task with:
 *   invokeTarget = "sampleTimeTask.printCurrentTime"
 *
 * This serves as the official example showing how to write
 * Java-based scheduled tasks inside the OpsPanel task module.
 */
@Slf4j
@Component("sampleTimeTask")
public class SampleTimeTask {

    /**
     * Print the current system time to the log.
     * This method takes no arguments and is safe to call repeatedly.
     */
    public void printCurrentTime() {
        log.info("[SampleTimeTask] Current time: {}", LocalDateTime.now());
    }
}
