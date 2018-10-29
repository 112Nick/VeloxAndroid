package ru.mail.park.velox.ui;

import android.Manifest;
import android.content.Intent;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mail.park.velox.R;
import ru.mail.park.velox.model.Page;
import ru.mail.park.velox.recycler.MenuRecyclerAdapter;
import ru.mail.park.velox.utils.AppComponent;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView btnRecyclerView;
    private MenuRecyclerAdapter btnAdapter;
    private TextView pageTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Log.d("Data", getIntent().getData().toString());


        pageTitle = findViewById(R.id.title_page);

        for (Integer i = 0; i < getIntent().getData().toString().length(); i++) {
            Log.d(i.toString(), String.valueOf(getIntent().getData().toString().charAt(i)));
        }
        String inputStr = getIntent().getData().toString();
        String uuid;
//        if (inputStr.charAt(72) == '*' || inputStr.charAt(72) == '/' ) {
            uuid = inputStr.substring(35,inputStr.length()); //TODO FIX BACK
//
//        } else {
//            uuid = inputStr.substring(35,71);
//        }
//        String uuid = inputStr.substring(35);
//        uuid = uuid.substring(0, uuid.indexOf("/"));
        AppComponent.getInstance().veloxApi.getPage(uuid).enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                Page foundPage = response.body();
                Log.d("Title", response.body().getTitle());
                pageTitle.setText(foundPage.getTitle());

                if(foundPage.getInnerPages() != null) {
                    initRecycler(true);
                    for (Integer i = 0; i < response.body().getInnerPages().length; i++) {
                        Log.d(i.toString(), response.body().getInnerPages()[i].getUuid());
                        btnAdapter.add(response.body().getInnerPages()[i]);

                    }
                } else {
                    initRecycler(false);
                    for (Integer i = 0; i < foundPage.getFieldsNames().size(); i++) {
//                        Log.d(i.toString(), foundPage.getInnerPages()[i].getUuid());
                        ArrayList<String> fieldValue = new ArrayList<>();
                        ArrayList<String> fieldName = new ArrayList<>();

                        fieldValue.add(foundPage.getFieldsValues().get(i));
                        fieldName.add(foundPage.getFieldsNames().get(i));
                        btnAdapter.add(new Page("123","Title" + i.toString(), "sdf", fieldName, fieldValue,"123", false, false, false ));


                    }
                }



            }

            @Override
            public void onFailure(Call<Page> call, Throwable t) {

            }
        });
//        for (Integer i = 0; i < 10; i++) {
//            btnAdapter.add(new Page("123","Title" + i.toString(), "sdf", new ArrayList<>(), new ArrayList<>(),"123", false, false, false ));
//        }

    }

    void initRecycler(Boolean isContainer) {
        if (isContainer) {
            btnAdapter = new MenuRecyclerAdapter(this, this::onItemClickOpen, isContainer);
        } else {
            btnAdapter = new MenuRecyclerAdapter(this, this::onItemClickNothing, isContainer);
        }
        btnRecyclerView = findViewById(R.id.menu_container);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        btnRecyclerView.setLayoutManager(mLayoutManager);
        btnRecyclerView.setAdapter(btnAdapter);
        btnRecyclerView.setHasFixedSize(true);
    }

    private void onItemClickOpen(Page page) {
        Uri myUri;
        Intent intent;
        switch (page.getTemplate()) {
//            case "wifi":
//                break;
            case "telephone":
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},0 );
                myUri = Uri.parse("tel:" + page.getFieldsValues().get(0));
                intent = new Intent(Intent.ACTION_CALL, myUri);
                break;
            case "sms":
                Log.d("123","456");
                myUri = Uri.parse("smsto:" + page.getFieldsValues().get(0));
                intent = new Intent(Intent.ACTION_SENDTO, myUri);
                intent.putExtra("sms_body", page.getFieldsValues().get(1));
                break;
//            case "event":
//                break;
//            case "ylocation":
//                break;
            default:
                myUri = Uri.parse("https://velox-app.herokuapp.com/qr/" + page.getUuid());
                intent = new Intent(this, MenuActivity.class);
                break;
        }







//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setData(myUri);
        startActivity(intent);

    }

    private void onItemClickNothing(Page page) {
//        Uri newURL = Uri.parse("https://velox-app.herokuapp.com/qr/" + page.getUuid());
//        Intent intent = new Intent(this, MenuActivity.class);
////        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setData(newURL);
//        startActivity(intent);

    }
}
