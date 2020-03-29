package eu.chessout.shared.model;

import java.text.Collator;

/**
 * Created by Bogdan Oloeriu on 6/4/2016.
 */
public class Player implements Comparable<Player> {
    private String name;
    private String email;
    private int elo;
    private int clubElo;

    private String userKey;
    private String playerKey;
    private String clubKey;


    public Player() {
    }

    public Player(String name, String email, String clubKey, int elo, int clubElo) {
        this.name = name;
        this.email = email;
        this.clubKey = clubKey;
        this.elo = elo;
        this.clubElo = clubElo;
    }

    @Override
    public int compareTo(Player another) {
        Collator defaultCollator = Collator.getInstance();
        return defaultCollator.compare(this.getName(), another.getName());
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getClubElo() {
        return clubElo;
    }

    public void setClubElo(int clubElo) {
        this.clubElo = clubElo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getPlayerKey() {
        return playerKey;
    }

    public void setPlayerKey(String playerKey) {
        this.playerKey = playerKey;
    }

    public String getClubKey() {
        return clubKey;
    }

    public void setClubKey(String clubKey) {
        this.clubKey = clubKey;
    }

}
