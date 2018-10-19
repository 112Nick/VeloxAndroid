package ru.mail.park.velox.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mail.park.velox.R;
import ru.mail.park.velox.model.Page;
import ru.mail.park.velox.model.Template;
import ru.mail.park.velox.recycler.PagesRecyclerAdapter;
import ru.mail.park.velox.recycler.TemplatesRecyclerAdapter;
import ru.mail.park.velox.utils.AppComponent;

public class MainActivity extends AppCompatActivity {


    private RecyclerView pagesRecyclerView;
    private PagesRecyclerAdapter pagesAdapter;


    private RecyclerView templatesRecyclerView;
    private TemplatesRecyclerAdapter templatesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final AppComponent appComponent = AppComponent.getInstance();

        Callback<List<Page>> callback1 = new Callback<List<Page>>() {
            @Override
            public void onResponse(@NonNull Call<List<Page>> call, @NonNull Response<List<Page>> response) {
                Log.d("PAGES", String.valueOf(response.code()));
                Log.d("PAGES", String.valueOf(response.body()));
                if (response.body() != null) {
                    for (int i = 0; i < response.body().size(); i++) {
                        pagesAdapter.add(response.body().get(i));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Page>> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.d("PAGES", "OnFailure");
            }
        };
        appComponent.veloxApi.getPosts("a-z", "all").enqueue(callback1);


        pagesAdapter = new PagesRecyclerAdapter(this);

        pagesRecyclerView = findViewById(R.id.pages_list);
//        pagesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        pagesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        pagesRecyclerView.setAdapter(pagesAdapter);
        pagesRecyclerView.setHasFixedSize(true);


        templatesAdapter = new TemplatesRecyclerAdapter(this);

        templatesRecyclerView = findViewById(R.id.templates_list);
        templatesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        templatesRecyclerView.setAdapter(templatesAdapter);
        templatesRecyclerView.setHasFixedSize(true);


        Template location = new Template("ylocation");
        Template event = new Template("event");
        Template phone = new Template("telephone");
        Template sms = new Template("sms");
        Template wifi = new Template("wifi");
        Template add = new Template("default");

        templatesAdapter.add(location);
        templatesAdapter.add(event);
        templatesAdapter.add(phone);
        templatesAdapter.add(sms);
        templatesAdapter.add(wifi);
        templatesAdapter.add(add);


    }
}