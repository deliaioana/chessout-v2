package eu.chessout.shared;

public class Locations {
    public static String buildGameLocation(String tournamentId, String roundId, String tableId) {
        return Constants.LOCATION_GAME
                .replace(Constants.TOURNAMENT_KEY, tournamentId)
                .replace(Constants.ROUND_NUMBER, String.valueOf(roundId))
                .replace(Constants.TABLE_NUMBER, String.valueOf(tableId));
    }

    public static String buildFollowersLocation(String playerId) {
        return Constants.LOCATION_GLOBAL_FOLLOWERS_BY_PLAYER
                .replace(Constants.PLAYER_KEY, playerId);
    }

    public static String buildDevicesLocation(String userId) {
        return Constants.LOCATION_MY_DEVICES
                .replace(Constants.USER_KEY, userId);
    }

    public static String url(String location, String accessToken) {
        return Constants.FIREBASE_URL + location
                + ".json?access_token=" + accessToken;
    }
}
