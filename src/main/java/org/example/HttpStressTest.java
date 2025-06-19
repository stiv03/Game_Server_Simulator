package org.example;

import com.google.gson.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HttpStressTest {

    private static final int PLAYER_COUNT = 300;
    private static final String BASE_URL = "http://localhost:8080";

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(300);
        HttpClient client = HttpClient.newHttpClient();

        List<String> userIds = fetchUserIds(client);

        if (userIds.isEmpty()) {
            System.err.println("No users found in database. Aborting test.");
            return;
        }


        if (userIds.size() > PLAYER_COUNT) {
            userIds = userIds.subList(0, PLAYER_COUNT);
        }


        String sessionCreateBody = "{\"difficulty\":\"NORMAL\"}";
        String creatorId = userIds.get(0);
        HttpRequest createSessionRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/session/create?creatorId=" + creatorId))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(sessionCreateBody))
                .build();

        HttpResponse<String> createSessionResponse = client.send(createSessionRequest, HttpResponse.BodyHandlers.ofString());
        if (createSessionResponse.statusCode() != 200) {
            System.err.println("Failed to create session: " + createSessionResponse.body());
            return;
        }

        String sessionId = extractSessionId(createSessionResponse.body());


        for (String userId : userIds) {
            HttpRequest joinRequest = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/session/" + sessionId + "/join?userId=" + userId))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            client.send(joinRequest, HttpResponse.BodyHandlers.discarding());
        }

        for (int i = 0; i < userIds.size(); i++) {
            final int index = i;
            List<String> finalUserIds = userIds;
            executor.submit(() -> {
                try {
                    String userId = finalUserIds.get(index);
                    Random random = new Random();
                    String[] directions = { "UP", "DOWN", "LEFT", "RIGHT" };

                    for (int j = 0; j < 100000; j++) {

                        String dir = directions[random.nextInt(directions.length)];
                        String moveUrl = BASE_URL + "/player/" + userId + "/move?direction=" + dir;
                        HttpRequest moveRequest = HttpRequest.newBuilder()
                                .uri(URI.create(moveUrl))
                                .POST(HttpRequest.BodyPublishers.noBody())
                                .build();
                        client.send(moveRequest, HttpResponse.BodyHandlers.discarding());

                       
                        String targetId;
                        do {
                            targetId = finalUserIds.get(random.nextInt(finalUserIds.size()));
                        } while (targetId.equals(userId));

                        String attackUrl = BASE_URL + "/player/" + userId + "/attack";
                        String attackBody = String.format("{\"targetId\":\"%s\"}", targetId);
                        HttpRequest attackRequest = HttpRequest.newBuilder()
                                .uri(URI.create(attackUrl))
                                .header("Content-Type", "application/json")
                                .POST(HttpRequest.BodyPublishers.ofString(attackBody))
                                .build();
                        client.send(attackRequest, HttpResponse.BodyHandlers.discarding());
                        Thread.sleep(5000);
                    }
                } catch (Exception e) {
                    System.err.println("Error for user " + index + ": " + e.getMessage());
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("HTTP Stress Test Completed.");
    }

    private static List<String> fetchUserIds(HttpClient client) {
        List<String> userIds = new ArrayList<>();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/user"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.err.println("Failed to fetch users: " + response.body());
                return userIds;
            }

            JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();
            for (JsonElement el : jsonArray) {
                JsonObject obj = el.getAsJsonObject();
                if (obj.has("id")) {
                    userIds.add(obj.get("id").getAsString());
                }
            }

        } catch (Exception e) {
            System.err.println("Error fetching users: " + e.getMessage());
        }
        return userIds;
    }

    private static String extractSessionId(String responseBody) {
        int start = responseBody.indexOf("id\":\"") + 5;
        int end = responseBody.indexOf("\"", start);
        return responseBody.substring(start, end);
    }
}
