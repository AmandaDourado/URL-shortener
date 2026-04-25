package com.example.demo.scheduled;

import com.example.demo.services.LinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledLinksExpired {

    @Autowired
    private LinkService linkService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledLinksExpired.class);

    @Scheduled(fixedRate = 120000)
    public void deleteExpiredLinks() {
        LOGGER.info("Cleaning expired links");

        linkService.deleteExpiredLinks();

        LOGGER.info("Expired links cleaned");
    }
}
