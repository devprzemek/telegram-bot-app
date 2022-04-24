package com.salamprz;

import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalTime;
import java.util.UUID;

@ApplicationScoped
public class MessageCalculator {
    @Inject
    TelegramBot telegramBot;

    @Scheduled(every = "20s")
    public void getMessage() {
        telegramBot.sendMessage(createMessage());
    }
    private String createMessage() {
        return "Local time is " + LocalTime.now() + " : " + UUID.randomUUID();
    }
}
