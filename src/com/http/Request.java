package com.http;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * This class represents task about json
 */
public class Request {
    private static final Logger logger = LogManager.getLogger(Request.class);

    /**
     * This is main method for logic demonstration
     * @param args String[] args
     */
    public static void main(String[] args) {
        FileInputStream fis;
        Properties property = new Properties();
        String url = "";
        try {
            fis = new FileInputStream("resources/config.properties");
            property.load(fis);
            url = property.getProperty("url");
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(url))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(result -> {
                        //We can simplify it using Request::parse, but I will do it
                        //manually, so you can see albums variable - json stores here.
                        List<Album> albums = parse(result);
                        return albums;
                    })
                    .join();
            logger.info("Succeed");
        } catch (HttpTimeoutException e) {
            logger.error("Timeout exception");
        } catch (IllegalArgumentException | IOException e) {
            logger.error("Error: there is no file named like that");
        }
    }

    /**
     * This method describes logic of parsing request body and
     * saving it into list
     * @param respBody String
     * @return List<Album>
     */
    public static List<Album> parse(String respBody) {
        JSONArray albums = new JSONArray(respBody);
        List<Album> albumsCollection = new ArrayList<>();
        for (int i = 0; i < albums.length(); i++) {
            JSONObject album = albums.getJSONObject(i);
            Album albumToSave = new Album(album.getInt(Constants.ID),
                    album.getInt(Constants.USER_ID),
                    album.getString(Constants.TITLE));
            albumsCollection.add(albumToSave);
        }
        return albumsCollection;
    }
}
