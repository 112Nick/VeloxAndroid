package ru.mail.park.velox.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mail.park.velox.R;
import ru.mail.park.velox.model.Message;
import ru.mail.park.velox.model.Token;
import ru.mail.park.velox.utils.AppComponent;

public class OAuthActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth);

        final AppComponent appComponent = AppComponent.getInstance();

        Button yandexBtn = findViewById(R.id.yandex_btn);

        yandexBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Token token = new Token("AQAAAAActojvAAU6vTTQOs_iLkA7kOsJAMOVEU8");

                Callback<Message> callback = new Callback<Message>() {

                    @Override
                    public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                        Log.d("LOGIN", String.valueOf(response.code()));
                        Log.d("LOGIN", String.valueOf(response.body().getMessage()));
                        startMainActivity();

                    }

                    @Override
                    public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        Log.d("LOGIN", "OnFailure");
                        showToast("FAILED");
                    }
                };
                appComponent.veloxApi.loginYandex(token).enqueue(callback);
            }
        });


    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void showToast(String text) {
        Toast.makeText(this, text,
                Toast.LENGTH_LONG).show();
    }
}
