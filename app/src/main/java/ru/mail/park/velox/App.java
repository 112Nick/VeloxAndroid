package ru.mail.park.velox;

import android.app.Application;

import ru.mail.park.velox.utils.AppComponent;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppComponent.init(this);
    }
}
