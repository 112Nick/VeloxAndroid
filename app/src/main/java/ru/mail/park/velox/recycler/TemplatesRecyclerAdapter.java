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

import ru.mail.park.velox.R;
import ru.mail.park.velox.model.Template;

public class TemplatesRecyclerAdapter extends RecyclerView.Adapter<TemplatesRecyclerAdapter.TemplatesRecyclerViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<Template> data;

    public TemplatesRecyclerAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.data = new ArrayList<>();
    }

    @NonNull
    @Override
    public TemplatesRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TemplatesRecyclerViewHolder(layoutInflater.inflate(R.layout.template, parent, false));
    }

    @Override
    public void onBindViewHolder(TemplatesRecyclerViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void add(Template newData) {
        data.add(0, newData);
        notifyItemInserted(0);
    }

    public void remove(Template w) {
        notifyItemRemoved(data.indexOf(w));
        data.remove(w);
    }


    final static class TemplatesRecyclerViewHolder extends RecyclerView.ViewHolder {
        private final ImageView tmpIcon;
        private final TextView tmpName;
//        private final TextView tmpname;


        TemplatesRecyclerViewHolder(View itemView) {
            super(itemView);
            tmpName = itemView.findViewById(R.id.template_name);
            tmpIcon = itemView.findViewById(R.id.tmp_icon);
        }

        void bind(Template template) {
//            preview.setImageResource(R.drawable.ic_launcher_background);
            tmpName.setText(template.getTmpname());
            tmpIcon.setImageResource(template.getDrawable());

        }
    }
}
