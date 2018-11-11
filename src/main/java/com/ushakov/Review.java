package com.ushakov;

public class Review {
    int id;
    int movieId;
    int userId;
    String comment;

    @Override
    public String toString() {
        return "INSERT INTO review(id, movieId, userId, comment) VALUES(" + id + ", " + movieId + ", " + userId + ", '" + comment + "');";
    }
}
