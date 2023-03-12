package fr.twitmund.mcantivpn.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.twitmund.mcantivpn.Main;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiRequester {

    /**
     * return true or false if the player is using a vpn or not
     * @param Ip
     * @return String 1 = using a vpn || 0 =  not using a vpn
     * @throws IOException
     * @throws InterruptedException
     */
    public static String requestApi(String Ip) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://v2.api.iphub.info/ip"+Ip))
                .header("X-Key", Main.getAPI_KEY())
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();

        return jo.get("block").toString();
    }
}
