package ru.mail.park.velox.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ru.mail.park.velox.R;
import ru.mail.park.velox.model.InitItem;
import ru.mail.park.velox.model.Page;
import ru.mail.park.velox.utils.AppComponent;

public class PagesRecyclerAdapter extends RecyclerView.Adapter<PagesRecyclerAdapter.PagesRecyclerViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<Page> data;

    public PagesRecyclerAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.data = new ArrayList<>();
    }

    @NonNull
    @Override
    public PagesRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PagesRecyclerViewHolder(layoutInflater.inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(PagesRecyclerViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void add(Page newData) {
        data.add(0, newData);
        notifyItemInserted(0);
    }

    public void remove(Page w) {
        notifyItemRemoved(data.indexOf(w));
        data.remove(w);
    }


    final static class PagesRecyclerViewHolder extends RecyclerView.ViewHolder {
        private final TextView pageName;
        private final TextView date;
        private final ImageView preview;
        private final ImageView tmpIcon;
        private final Button moreBtn;


        PagesRecyclerViewHolder(View itemView) {
            super(itemView);
            tmpIcon = itemView.findViewById(R.id.tmp_icon);
            pageName = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            preview = itemView.findViewById(R.id.preview);
            moreBtn = itemView.findViewById(R.id.more_btn);

        }

        void bind(Page page) {
            InitItem initItem = initItem(page);
            tmpIcon.setImageResource(initItem.tmpIconSource);
            preview.setImageBitmap(AppComponent.generateQR(initItem.stringToCode));
            if (page.getTitle().length() > 8) {
                pageName.setText(page.getTitle().substring(0, 8) + "...");
            } else {
                pageName.setText(page.getTitle());
            }
            date.setText(page.getDate().substring(0, 10));

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
}
