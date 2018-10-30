package ru.mail.park.velox.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mail.park.velox.R;
import ru.mail.park.velox.model.Page;
import ru.mail.park.velox.recycler.PagesRecyclerAdapter;
import ru.mail.park.velox.utils.AppComponent;

public class MainActivity extends AppCompatActivity implements PageDetailedFragment.Delegate{


    private RecyclerView pagesRecyclerView;
    private PagesRecyclerAdapter pagesAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView errorIcon;
    private TextView errorStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        pagesRecyclerView = findViewById(R.id.pages_list);
        errorIcon = findViewById(R.id.error_icon);
        errorStatus = findViewById(R.id.error_status);
        errorIcon.setVisibility(View.GONE);
        errorStatus.setVisibility(View.GONE);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPages();
            }
        });

        getPages();

        pagesAdapter = new PagesRecyclerAdapter(this, this::onItemClick, this::onBtnClick);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        pagesRecyclerView.setLayoutManager(mLayoutManager);
        pagesRecyclerView.setAdapter(pagesAdapter);
        pagesRecyclerView.setHasFixedSize(true);
    }

    private void getPages() {
        swipeRefreshLayout.setRefreshing(true);
        final AppComponent appComponent = AppComponent.getInstance();
        Callback<List<Page>> callback1 = new Callback<List<Page>>() {
            @Override
            public void onResponse(@NonNull Call<List<Page>> call, @NonNull Response<List<Page>> response) {
                swipeRefreshLayout.setRefreshing(false);
                pagesAdapter.removeAll();
                if (response.code() == 200 || response.code() == 201) {
                    errorIcon.setVisibility(View.GONE);
                    errorStatus.setVisibility(View.GONE);
                    if (response.body() != null) {
                        for (int i = 0; i < response.body().size(); i++) {
                            pagesAdapter.add(response.body().get(i));
                        }
                    }
                } else {
                    errorIcon.setVisibility(View.VISIBLE);
                    if (response.code() == 404) {
                        errorStatus.setText("You don't have any pages");
                    } else {
                        errorStatus.setText(response.code());
                    }
                    errorStatus.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<Page>> call, @NonNull Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                pagesAdapter.removeAll();
                errorIcon.setVisibility(View.VISIBLE);
                errorStatus.setText("Check connection");
                errorStatus.setVisibility(View.VISIBLE);
                t.printStackTrace();
            }
        };
        appComponent.veloxApi.getPages("date", "all").enqueue(callback1);
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
        Uri myUri = Uri.parse("https://velox-app.herokuapp.com/qr/" + page.getUuid());
        Intent intent = new Intent(this, MenuActivity.class);
        intent.setData(myUri);
        startActivity(intent);

//        DialogFragment newFragment = PageDetailedFragment.newInstance(page);
//        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    private void onBtnClick(Page page) {
        DialogFragment newFragment = PageDetailedFragment.newInstance(page);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }




    @Override
    public void onDelete(Page item) {
        pagesAdapter.remove(item);
    }
}