package com.github.kirillf.materialdemo.cards;

/**
 * Created by k.filimonov on 11/04/2017.
 */

class Card {

    private final String title;
    private final String message;

    Card(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
