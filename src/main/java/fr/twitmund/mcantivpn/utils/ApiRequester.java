package fr.twitmund.mcantivpn.utils;

import fr.twitmund.mcantivpn.Main;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
     * @throws ParseException
     */
    public static String requestApi(String Ip) throws IOException, InterruptedException, ParseException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://v2.api.iphub.info/ip"+Ip))
                .header("X-Key", Main.getAPI_KEY())
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());


        Object obj = new JSONParser().parse(response.body());
        JSONObject jo = (JSONObject) obj;




        return jo.get("block").toString();
    }
}
