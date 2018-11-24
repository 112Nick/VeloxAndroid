package ru.mail.park.velox.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.mail.park.velox.OnItemClickListener;
import ru.mail.park.velox.R;
import ru.mail.park.velox.model.Page;


public class MenuRecyclerAdapter extends RecyclerView.Adapter<MenuRecyclerAdapter.BtnRecyclerViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<Page> data;
    private final Boolean isContainer;

    private final OnItemClickListener<Page> onItemClickListener;


    public MenuRecyclerAdapter(Context context, OnItemClickListener<Page> onItemClickListener, Boolean isContainer) {
        layoutInflater = LayoutInflater.from(context);
        this.data = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
        this.isContainer = isContainer;
    }

    @NonNull
    @Override
    public BtnRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isContainer) {
            return new BtnRecyclerViewHolder(layoutInflater.inflate(R.layout.btn, parent, false), isContainer);
        } else {
            return new BtnRecyclerViewHolder(layoutInflater.inflate(R.layout.list_item, parent, false), isContainer);
        }
    }

    @Override
    public void onBindViewHolder(BtnRecyclerViewHolder holder, int position) {
        holder.bind(data.get(position), this.onItemClickListener, isContainer);
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


    final static class BtnRecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView pageName = null;
        private TextView fieldName = null;
        private TextView fieldValue = null;


        BtnRecyclerViewHolder(View itemView, Boolean isContainer) {
            super(itemView);
            if (isContainer) {
                pageName = itemView.findViewById(R.id.Title);
            } else {
                fieldName = itemView.findViewById(R.id.field_name);
                fieldValue = itemView.findViewById(R.id.field_value);

            }
        }

        void bind(final Page page, OnItemClickListener onItemClickListener, Boolean isContainer) {
            if (isContainer) {

                pageName.setText(page.getTitle());
            } else {
                fieldName.setText(page.getFieldsNames().get(0));
                fieldValue.setText(page.getFieldsValues().get(0));

            }
            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(page));
        }



    }
}
