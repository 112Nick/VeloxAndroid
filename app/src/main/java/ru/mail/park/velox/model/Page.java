package ru.mail.park.velox.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Page implements Parcelable {
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
    @SerializedName("innerPages")
    private  Page[] innerPages;


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

//    private Page(Parcel in) {
//        uuid = in.readString();
//        title = in.readString();
//        template = in.readString();
//        fieldsNames = in.readArrayList(ClassLoader.getSystemClassLoader());
//        fieldsValues = in.readArrayList(ClassLoader.getSystemClassLoader());
//        date = in.readString();
//
//    }
//
//    public static final Parcelable.Creator<Page> CREATOR = new Parcelable.Creator<Page>() {
//        @Override
//        public Page createFromParcel(Parcel in) {
//            return new Page(in);
//        }
//
//        @Override
//        public Page[] newArray(int size) {
//            return new Page[size];
//        }
//    };


    protected Page(Parcel in) {
        uuid = in.readString();
        title = in.readString();
        template = in.readString();
        fieldsNames = in.createStringArrayList();
        fieldsValues = in.createStringArrayList();
        date = in.readString();
        byte tmpIsPublic = in.readByte();
        isPublic = tmpIsPublic == 0 ? null : tmpIsPublic == 1;
        byte tmpIsMine = in.readByte();
        isMine = tmpIsMine == 0 ? null : tmpIsMine == 1;
        byte tmpIsStatic = in.readByte();
        isStatic = tmpIsStatic == 0 ? null : tmpIsStatic == 1;
    }

    public static final Creator<Page> CREATOR = new Creator<Page>() {
        @Override
        public Page createFromParcel(Parcel in) {
            return new Page(in);
        }

        @Override
        public Page[] newArray(int size) {
            return new Page[size];
        }
    };

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

    public Page[] getInnerPages() {
        return innerPages;
    }

    public void setInnerPages(Page[] innerPages) {
        this.innerPages = innerPages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeString(title);
        dest.writeString(template);
        dest.writeStringList(fieldsNames);
        dest.writeStringList(fieldsValues);
        dest.writeArray(innerPages);
        dest.writeString(date);
        dest.writeByte((byte) (isPublic == null ? 0 : isPublic ? 1 : 2));
        dest.writeByte((byte) (isMine == null ? 0 : isMine ? 1 : 2));
        dest.writeByte((byte) (isStatic == null ? 0 : isStatic ? 1 : 2));
    }


//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(uuid);
//        dest.writeString(title);
//        dest.writeString(template);
//        dest.writeStringList(fieldsNames);
//        dest.writeStringList(fieldsValues);
//        dest.writeArray(innerPages);
//        dest.writeString(date);
//        dest.writeByte((byte) (isPublic == null ? 0 : isPublic ? 1 : 2));
//        dest.writeByte((byte) (isMine == null ? 0 : isMine ? 1 : 2));
//        dest.writeByte((byte) (isStatic == null ? 0 : isStatic ? 1 : 2));
//    }

}
