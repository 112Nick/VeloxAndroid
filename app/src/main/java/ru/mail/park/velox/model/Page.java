package ru.mail.park.velox.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Page {
    @SerializedName("uuid")
    private String uuid;
    @SerializedName("title")
    private String title;
    @SerializedName("template")
    private String template;
    @SerializedName("fieldsNames")
    private ArrayList<String> fieldsNames;
    @SerializedName("fieldsValues")
    private ArrayList<String> fieldsValues;
    @SerializedName("date")
    private String date;
    @SerializedName("public")
    private Boolean isPublic;
    @SerializedName("mine")
    private Boolean isMine;
    @SerializedName("static")
    private Boolean isStatic;


    public Page(String uuid, String title, String template, ArrayList<String> fieldsNames, ArrayList<String> fieldsValues, String date, Boolean isPublic, Boolean isMine, Boolean isStatic) {
        this.uuid = uuid;
        this.title = title;
        this.template = template;
        this.fieldsNames = fieldsNames;
        this.fieldsValues = fieldsValues;
        this.date = date;
        this.isPublic = isPublic;
        this.isMine = isMine;
        this.isStatic = isStatic;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public ArrayList<String> getFieldsNames() {
        return fieldsNames;
    }

    public void setFieldsNames(ArrayList<String> fieldsNames) {
        this.fieldsNames = fieldsNames;
    }

    public ArrayList<String> getFieldsValues() {
        return fieldsValues;
    }

    public void setFieldsValues(ArrayList<String> fieldsValues) {
        this.fieldsValues = fieldsValues;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Boolean getMine() {
        return isMine;
    }

    public void setMine(Boolean mine) {
        isMine = mine;
    }

    public Boolean getStatic() {
        return isStatic;
    }

    public void setStatic(Boolean aStatic) {
        isStatic = aStatic;
    }
}
