package com.zsh.ricky.zsh.model;

public class Card {
    private int cardNumber;
    private String cardName;
    private String photoNumber;
    private int cardHP;
    private int cardAttack;
    private int cardType;

    public Card(){}

    public Card(int cardNumber, String cardName, String photoNumber, int cardHP, int cardAttack, int cardType) {
        this.cardNumber = cardNumber;
        this.cardName = cardName;
        this.photoNumber = photoNumber;
        this.cardHP = cardHP;
        this.cardAttack = cardAttack;
        this.cardType = cardType;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getPhotoNumber() {
        return photoNumber;
    }

    public void setPhotoNumber(String photoNumber) {
        this.photoNumber = photoNumber;
    }

    public int getCardHP() {
        return cardHP;
    }

    public void setCardHP(int cardHP) {
        this.cardHP = cardHP;
    }

    public int getCardAttack() {
        return cardAttack;
    }

    public void setCardAttack(int cardAttack) {
        this.cardAttack = cardAttack;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardNumber=" + cardNumber +
                ", cardName='" + cardName + '\'' +
                ", photoNumber='" + photoNumber + '\'' +
                ", cardHP=" + cardHP +
                ", cardAttack=" + cardAttack +
                ", cardType=" + cardType +
                '}';
    }
}
