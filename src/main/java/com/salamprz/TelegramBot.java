package com.salamprz;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class TelegramBot {
    @ConfigProperty(name = "telegram.bot.token")
    String token;
    @ConfigProperty(name = "telegram.chat.id")
    String chatId;
    private Client telegramClient;
    private WebTarget baseTarget;

    @PostConstruct
    void initClient() {
        telegramClient = ClientBuilder.newClient();
        baseTarget = telegramClient.target("https://api.telegram.org/bot{token}")
                .resolveTemplate("token", this.token);
    }

    @PreDestroy
    void destroyClient() {
        telegramClient.close();
    }

    public void sendMessage(String message) {
        try {
            Response response = baseTarget.path("sendMessage")
                    .queryParam("chat_id", chatId)
                    .queryParam("text", message)
                    .request().get();

            JsonObject jsonObject = response.readEntity(JsonObject.class);

            if (!jsonObject.getBoolean("ok", false)) {
                System.err.println("Could not successfully send message");
            }
        } catch (Exception exception) {
            System.err.println("Could not successfully send message");
            exception.printStackTrace();
        }
    }
}
