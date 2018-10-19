package ru.mail.park.velox.model;

import ru.mail.park.velox.R;

public class Template {
    private String template;
    private String tmpname;
    private int drawable;


    public Template(String template) {
        this.template = template;
        switch (template) {
            case "wifi":
                drawable = R.drawable.ic_wifi_24dp;
                tmpname = "Wifi";
                break;
            case "telephone":
                drawable = R.drawable.ic_phone_24dp;
                tmpname = "Phone";
                break;
            case "sms":
                drawable = R.drawable.ic_email_24dp;
                tmpname = "SMS";
                break;
            case "event":
                drawable = R.drawable.ic_event_24dp;
                tmpname = "Event";
                break;
            case "ylocation":
                drawable = R.drawable.ic_location_24dp;
                tmpname = "Location";
                break;
            default:
                drawable = R.drawable.ic_add_24dp;
                tmpname = "Custom";
                break;
        }
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getTmpname() {
        return tmpname;
    }

    public void setTmpname(String tmpname) {
        this.tmpname = tmpname;
    }
}
