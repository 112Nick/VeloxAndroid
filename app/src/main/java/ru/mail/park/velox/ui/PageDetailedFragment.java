package ru.mail.park.velox.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import android.util.Log;
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
        downloadBtn = view.findViewById(R.id.download_btn);
        shareBtn = view.findViewById(R.id.share_btn);
        deleteBtn = view.findViewById(R.id.delete_btn);
        title = view.findViewById(R.id.title);

        InitItem initItem = initItem(item);

        title.setText(item.getTitle());
        Log.d("swich", initItem.stringToCode);
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

        shareBtn.setOnClickListener(v -> {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");

            String shareBody = "https://velox-app.herokuapp.com/qr/" + item.getUuid();
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        });



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
            result.stringToCode = "https://velox-app.herokuapp.com/qr/" + page.getUuid();
            return result;
        }
        if (page.getTemplate() == null) {
            result.tmpIconSource = R.drawable.ic_error_outline_24dp;
            result.stringToCode = page.getTitle() + "\n";
            for (int i = 0; i < page.getFieldsValues().size(); i++) {

                result.stringToCode += "\n" + page.getFieldsNames().get(i) + ": " + page.getFieldsValues().get(i);
            }
            return result;
        }
        Log.d("swich", page.getTemplate());
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
            case "html":
                result.tmpIconSource = R.drawable.ic_web_24dp;
                result.stringToCode = "https://velox-app.herokuapp.com/qr/" + page.getUuid();
                break;
            case "url":
                result.tmpIconSource = R.drawable.ic_insert_link_24dp;
                result.stringToCode = page.getFieldsValues().get(0);
                break;
            case "email":
                result.tmpIconSource = R.drawable.ic_email_24dp;
                result.stringToCode = "mailto:" + page.getFieldsValues().get(0) + "?subject=" + page.getFieldsValues().get(1) + "&body=" + page.getFieldsValues().get(2);
                break;
            case "whatsapp":
    //                    result.tmpIconSource = R.drawable.ic_email_24dp;
                result.stringToCode = "SMSTO:" + page.getFieldsValues().get(0) + ":" + page.getFieldsValues().get(1);
                break;
            case "contact":
                result.tmpIconSource = R.drawable.ic_person_24dp;
                result.stringToCode = "MECARD:N:" + page.getFieldsValues().get(0) + "," + page.getFieldsValues().get(1) +
                        ";NICKNAME:" + page.getFieldsValues().get(2) + ";TEL:" + page.getFieldsValues().get(3) +
                        ";TEL:" + page.getFieldsValues().get(4) + ";EMAIL:" + page.getFieldsValues().get(5) + ";BDAY:" + page.getFieldsValues().get(6) +
                        ";NOTE:" + page.getFieldsValues().get(7) + ";ADR:,," + page.getFieldsValues().get(8) + "," + page.getFieldsValues().get(9) +
                        "," + page.getFieldsValues().get(10) + "," + page.getFieldsValues().get(11) + "," + page.getFieldsValues().get(12) + ";;";
                Log.d("swich", result.stringToCode);
                break;
            case "push":
                result.tmpIconSource = R.drawable.ic_notifications_24dp;
                result.stringToCode = "https://velox-app.herokuapp.com/qr/" + page.getUuid();
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

