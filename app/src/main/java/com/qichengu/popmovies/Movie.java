package com.qichengu.popmovies;


class Movie {
    final String title;
    final String posterUrl;
    final double vote_average;
    final String synopsis;
    final String release_date;
    public Movie(String title, String url, double vote_average, String synopsis, String release_date) {
        this.title = title;
        this.posterUrl = url;
        this.vote_average = vote_average;
        this.synopsis = synopsis;
        this.release_date = release_date;
    }
}
