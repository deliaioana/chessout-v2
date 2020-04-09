package eu.chessout.v2.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import eu.chessout.shared.Constants;
import eu.chessout.shared.Locations;
import eu.chessout.shared.model.Game;
import eu.chessout.shared.model.User;

public class RestFirebase {

    private static final Logger errorLogger = Logger.getLogger(RestFirebase.class.getName());
    private static final Logger logger = Logger.getLogger(RestFirebase.class.getName());

    /**
     * it gets the content from firebase using the REST api.
     *
     * @param locationUrl
     * @return
     */
    private static String restFirebaseGetContent(String locationUrl) {

        String responseJson = "no-response";

        try {
            URL url = new URL(locationUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            StringBuilder sb = new StringBuilder();
            int result = conn.getResponseCode();
            if (result == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                responseJson = sb.toString();

            } else {
                errorLogger.info("chess-data-error: " + conn.getResponseMessage() + ", errorCode = " + result);
                throw new IllegalStateException("chess-data-error: response = " + result + ", request = " + locationUrl);
            }

        } catch (IOException e) {
            errorLogger.info("chess-data-error: " + e.getMessage());
            throw new IllegalStateException(e.getMessage());
        }

        logger.info("chess-data-restFirebaseGetContent locationUrl=" + locationUrl);
        logger.info("chess-data-restFirebaseGetContent response=" + responseJson);
        return responseJson;
    }

    public static Game getGame(String tournamentId, String roundId, String tableId) {
        String gameLocation = Locations.buildGameLocation(tournamentId, roundId, tableId);
        String accessToken = MyAuth.getAccessToken();

        String gameUrl = Constants.FIREBASE_URL + gameLocation + ".json?access_token=" + accessToken;

        String gameJson = restFirebaseGetContent(gameUrl);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Game game = objectMapper.readValue(gameJson, Game.class);
            return game;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new IllegalStateException("Not able to decode game: " + e);
        }
    }

    public static List<User> getFollowers(String playerKey) {
        logger.info("getFollowers");
        String followersLocation = Locations.buildFollowersLocation(playerKey);
        String followersUrl = Constants.FIREBASE_URL + followersLocation
                + ".json?access_token=" + MyAuth.getAccessToken();
        String jsonUsers = restFirebaseGetContent(followersUrl);
        if (jsonUsers == null) {
            return new ArrayList<>();
        }
        int indexNull = jsonUsers.indexOf("null");
        if (indexNull == 0) {
            return new ArrayList<>();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        List<User> users = new ArrayList<>();
        logger.info("Time to decode: " + jsonUsers);

        return new ArrayList<>();
    }
}
