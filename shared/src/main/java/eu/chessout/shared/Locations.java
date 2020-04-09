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
}
