package com.ushakov;

public class Movie {
    int id;
    String nameRussian;
    String nameNative;
    int yearOfRelease;
    double rating;
    double price;
    String description;
    int posterId;
    int genreGroupId;
    int countryGroupId;

    @Override
    public String toString() {
        return "INSERT INTO movie( id, nameRussian, nameNative, yearOfRelease, rating, price, description, posterId, genreGroupId, countryGroupId) VALUES(" +
                id + ", '" + nameRussian + "', '" + nameNative + "', " + yearOfRelease + ", " + rating + ", " + price + ", '" +
                description + "', " + posterId + ", " + genreGroupId + ", " + countryGroupId + ");";
    }
}
