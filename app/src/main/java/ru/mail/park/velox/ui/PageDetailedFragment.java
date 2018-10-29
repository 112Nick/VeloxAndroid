package ru.mail.park.velox.ui;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mail.park.velox.R;
import ru.mail.park.velox.model.InitItem;
import ru.mail.park.velox.model.Message;
import ru.mail.park.velox.model.Page;
import ru.mail.park.velox.utils.AppComponent;

import static android.support.v4.content.ContextCompat.getDrawable;

public class PageDetailedFragment extends DialogFragment {

    private ImageView preview;
    private Button openBtn;
    private Button downloadBtn;
    private Button shareBtn;
    private Button deleteBtn;
    private TextView title;

    private Page item;
    private Delegate delegate;

    public static PageDetailedFragment newInstance(Page item) {
        PageDetailedFragment itemDialogFragment = new PageDetailedFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("ITEM", item);
        itemDialogFragment.setArguments(bundle);
        return itemDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            item = arguments.getParcelable("ITEM");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.page_detailed_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        preview = view.findViewById(R.id.preview);
        openBtn = view.findViewById(R.id.open_btn);
        downloadBtn = view.findViewById(R.id.download_btn);
        shareBtn = view.findViewById(R.id.share_btn);
        deleteBtn = view.findViewById(R.id.delete_btn);
        title = view.findViewById(R.id.title);

        InitItem initItem = initItem(item);

        title.setText(item.getTitle());
        preview.setImageBitmap(AppComponent.generateQR(initItem.stringToCode));

        deleteBtn.setOnClickListener(v -> {
            Callback<Message> callback = new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    delegate.onDelete(item);
                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {
                    t.printStackTrace();
                }

            };
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            AppComponent.getInstance().veloxApi.deletePage(item.getUuid()).enqueue(callback);

        });
//        preview.setImageDrawable(myDrawable);
        downloadBtn.setOnClickListener(v -> {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0 );
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            Bitmap bitmap = ((BitmapDrawable)preview.getDrawable()).getBitmap();
            String savedImageURL = MediaStore.Images.Media.insertImage(
                    getActivity().getContentResolver(),
                    bitmap,
                    item.getTitle(),
                    "QR"
            );

        });

//        shareBtn.setOnClickListener();



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if ( getParentFragment() instanceof Delegate) {
            delegate = (Delegate)((MainActivity)getActivity());
//        }
    }

    //    @Override
//    public int show(FragmentTransaction transaction, String tag) {
//        return super.show(transaction, tag);
//    }

    public interface Delegate {
        void onDelete(Page item);


    }


        private InitItem initItem(Page page) {
        InitItem result = new InitItem();
        if (!page.getStatic()) {
//                result.tmpIconSource = R.drawable.ic_dynamic_24dp;
            result.tmpIconSource = 0;
            result.stringToCode = "https://velox-app.herokuapp.com/" + page.getUuid();
            return result;
        }
        if (page.getTemplate() == null) {
            result.tmpIconSource = R.drawable.ic_add_24dp;
            result.stringToCode = page.getTitle() + "\n";
            for (int i = 0; i < page.getFieldsValues().size(); i++) {
                result.stringToCode += "\n" + page.getFieldsNames().get(i) + ": " + page.getFieldsValues().get(i);
            }
            return result;
        }
        switch (page.getTemplate()) {
            case "wifi":
                result.tmpIconSource = R.drawable.ic_wifi_24dp;
                result.stringToCode = "WIFI:T:WPA;S:" + page.getFieldsValues().get(0) + ";P:" + page.getFieldsValues().get(1) + ";;";
                break;
            case "telephone":
                result.tmpIconSource = R.drawable.ic_phone_24dp;
                result.stringToCode = "tel:" + page.getFieldsValues().get(0);
                break;
            case "sms":
                result.tmpIconSource = R.drawable.ic_email_24dp;
                result.stringToCode = "SMSTO:" + page.getFieldsValues().get(0) + ":" + page.getFieldsValues().get(1);
                break;
            case "event":
                result.tmpIconSource = R.drawable.ic_event_24dp;
                result.stringToCode = "BEGIN:VEVENT\nSUMMARY:" + page.getFieldsValues().get(0) +
                        "\nLOCATION:" + page.getFieldsValues().get(1) +
                        "\nDTSTART:" + page.getFieldsValues().get(2) + "00" + "\nDTEND:" +
                        page.getFieldsValues().get(3) + "00" + "\nEND:VEVENT";
                break;
            case "ylocation":
                result.tmpIconSource = R.drawable.ic_location_24dp;
                try {
                    result.stringToCode = new URL("https://yandex.ru/maps/?text=" + page.getFieldsValues().get(0)).toString();
                } catch (Exception e) {
                    result.stringToCode = "https://yandex.ru/maps/?text=" + page.getFieldsValues().get(0);
                }
                break;
            default:
                result.tmpIconSource = R.drawable.ic_add_24dp;
                result.stringToCode = page.getTitle() + "\n";
                for (int i = 0; i < page.getFieldsValues().size(); i++) {
                    result.stringToCode += "\n" + page.getFieldsNames().get(i) + ": " + page.getFieldsValues().get(i);
                }
                break;
        }
        return result;

    }

}

