package ru.mail.park.velox.ui;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
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

public class MainActivity extends AppCompatActivity implements PageDetailedFragment.Delegate{


    private RecyclerView pagesRecyclerView;
    private PagesRecyclerAdapter pagesAdapter;



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
        appComponent.veloxApi.getPages("a-z", "all").enqueue(callback1);


        pagesAdapter = new PagesRecyclerAdapter(this, this::onItemClick);

        pagesRecyclerView = findViewById(R.id.pages_list);
//        pagesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        pagesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        pagesRecyclerView.setAdapter(pagesAdapter);
        pagesRecyclerView.setHasFixedSize(true);



        Template location = new Template("ylocation");
        Template event = new Template("event");
        Template phone = new Template("telephone");
        Template sms = new Template("sms");
        Template wifi = new Template("wifi");
        Template add = new Template("default");


    }

    private void onItemClick(Page page) {

//        PageDetailedFragment pageDetailedFragment = PageDetailedFragment.newInstance(page);
//        pageDetailedFragment.show(getFragmentManager(),"123");

//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
//        if (prev != null) {
//            ft.remove(prev);
//        }
//        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = PageDetailedFragment.newInstance(page);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onDelete(Page item) {
        pagesAdapter.remove(item);
    }
}