package ru.mail.park.velox.model;

public class InitItem {
    public String stringToCode;
    public Integer tmpIconSource;

    public InitItem() {
        this.stringToCode = "123";
        this.tmpIconSource = 0;
    }

    public InitItem(String stringToCode, Integer source) {
        this.stringToCode = stringToCode;
        this.tmpIconSource = source;
    }
}
