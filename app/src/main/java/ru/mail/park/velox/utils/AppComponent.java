package ru.mail.park.velox.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mail.park.velox.api.VeloxApi;

public class AppComponent {

    private static AppComponent instance = null;


    public final VeloxApi veloxApi;
    public Bitmap bitmap;
    public final Context context;

    public static AppComponent getInstance() {
        return instance;
    }

    private AppComponent(Context context) {
        this.context = context;

//        JavaNetCookieJar jncj = new JavaNetCookieJar(CookieHandler.getDefault());
//        OkHttpClient httpClient = new OkHttpClient.Builder()
//                .build();
//
//        httpClient.interceptors().add(new AddCookiesInterceptor(context));
//        httpClient.interceptors().add(new ReceivedCookiesInterceptor(context));

        OkHttpClient httpClient = new OkHttpClient();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(new AddCookiesInterceptor(context)); // VERY VERY IMPORTANT
        builder.addInterceptor(new ReceivedCookiesInterceptor(context)); // VERY VERY IMPORTANT
        httpClient = builder.build();

        veloxApi = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .baseUrl(VeloxApi.BASE_URL)
                .build()
                .create(VeloxApi.class);

//        bitmap = QRCodeHelper
//                .newInstance(context)
//                .setContent("Test")
//                .setErrorCorrectionLevel(ErrorCorrectionLevel.Q)
//                .setMargin(2)
//                .getQRCOde();
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new AppComponent(context);
        }
    }

    public static Bitmap generateQR(String text) {
        return instance.bitmap = QRCodeHelper
                .newInstance(instance.context)
                .setContent(text)
                .setErrorCorrectionLevel(ErrorCorrectionLevel.Q)
                .setMargin(2)
                .getQRCOde();
    }

}
