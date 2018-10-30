package ru.mail.park.velox.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mail.park.velox.R;
import ru.mail.park.velox.model.Page;
import ru.mail.park.velox.recycler.MenuRecyclerAdapter;
import ru.mail.park.velox.utils.AppComponent;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MenuRecyclerAdapter menuAdapter;
    private TextView pageTitle;
    private TextView errorStatus;
    private ImageView errorIcon;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        pageTitle = findViewById(R.id.title_page);
        errorIcon = findViewById(R.id.error_icon);
        errorStatus = findViewById(R.id.error_status);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        errorIcon.setVisibility(View.GONE);


        String inputStr = getIntent().getData().toString();
        if (inputStr.charAt(inputStr.length()-1) == '/') {
            inputStr = inputStr.substring(0, inputStr.length()-1);
        }
        String uuid = inputStr.substring(35,inputStr.length());


        AppComponent.getInstance().veloxApi.getPage(uuid).enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                progressBar.setVisibility(View.GONE);
                if(response.code() == 200 || response.code() == 201) {
                    errorIcon.setVisibility(View.GONE);
                    Page foundPage = response.body();
                    if (foundPage.getTemplate().equals("push")) {
                        Toast.makeText(getApplicationContext(), "Push was send", Toast.LENGTH_SHORT);
                    }
                    pageTitle.setText(foundPage.getTitle());
                    if(foundPage.getInnerPages() != null) {
                        initRecycler(true);
                        for (Integer i = 0; i < foundPage.getInnerPages().length; i++) {
                            Log.d(i.toString(), foundPage.getInnerPages()[i].getUuid());
                            menuAdapter.add(foundPage.getInnerPages()[i]);
                        }
                    } else {
                        initRecycler(false);
                        for (Integer i = 0; i < foundPage.getFieldsNames().size(); i++) {
                            ArrayList<String> fieldValue = new ArrayList<>();
                            ArrayList<String> fieldName = new ArrayList<>();
                            fieldValue.add(foundPage.getFieldsValues().get(i));
                            fieldName.add(foundPage.getFieldsNames().get(i));
                            menuAdapter.add(new Page("1","Title", "s", fieldName, fieldValue,"1", false, false, false ));
                        }
                    }
                } else {
                    errorStatus.setText(String.valueOf(response.code()));
                    pageTitle.setText("Oops!");
                    errorIcon.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<Page> call, Throwable t) {
                pageTitle.setText("Oops!");
                errorStatus.setText("Check connection!");
                errorIcon.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    void initRecycler(Boolean isContainer) {
        if (isContainer) {
            menuAdapter = new MenuRecyclerAdapter(this, this::onItemClickOpen, isContainer);
        } else {
            menuAdapter = new MenuRecyclerAdapter(this, this::onItemClickNothing, isContainer);
        }
        recyclerView = findViewById(R.id.menu_container);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(menuAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void onItemClickOpen(Page page) {
        Uri myUri;
        Intent intent;
        switch (page.getTemplate()) {
            case "telephone":
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},0 );
                myUri = Uri.parse("tel:" + page.getFieldsValues().get(0));
                intent = new Intent(Intent.ACTION_CALL, myUri);
                intent.setData(myUri);
                startActivity(intent);

                break;
            case "sms":
                Log.d("123","456");
                myUri = Uri.parse("smsto:" + page.getFieldsValues().get(0));
                intent = new Intent(Intent.ACTION_SENDTO, myUri);
                intent.putExtra("sms_body", page.getFieldsValues().get(1));
                intent.setData(myUri);
                startActivity(intent);

                break;
            case "email":
                intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + page.getFieldsValues().get(0) + "&subject=" + page.getFieldsValues().get(1)
                        + "&body=" + page.getFieldsValues().get(2)));
                startActivity(intent);
                break;
            case "url":
                String url = page.getFieldsValues().get(0);
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                break;
            case "ylocation":
                String url1 = "https://yandex.ru/maps/?text=" + page.getFieldsValues().get(0);
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url1));
                startActivity(intent);
                break;
            case "whatsapp":
                PackageManager pm=getPackageManager();
                try {
                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                    waIntent.setType("text/plain");
                    PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    waIntent.setPackage("com.whatsapp");
                    startActivity(Intent.createChooser(waIntent, "Send with"));

                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case "push":
                String push = "https://velox-app.herokuapp.com/qr/" + page.getUuid();
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(push));
                startActivity(intent);
            default:
                myUri = Uri.parse("https://velox-app.herokuapp.com/qr/" + page.getUuid());
                intent = new Intent(this, MenuActivity.class);
                intent.setData(myUri);
                startActivity(intent);

                break;
        }

    }

    private void onItemClickNothing(Page page) {

    }
}
