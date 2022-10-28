package br.com.devleo.tcc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.com.devleo.tcc.model.Locale;

public class LocaleAdapter extends RecyclerView.Adapter<LocaleAdapter.MyViewHolder> {

    Context context;
    ArrayList<Locale> locals;

    public LocaleAdapter(Context context, ArrayList<Locale> locals) {
        this.context = context;
        this.locals = locals;
    }

    @NonNull
    @Override
    public LocaleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.items_locals, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LocaleAdapter.MyViewHolder holder, int position) {
        Locale local = locals.get(position);
        holder.name.setText(local.getNome());
        holder.address.setText(local.getAddress());
    }

    @Override
    public int getItemCount() {
        return locals.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, address;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.listName);
            address = itemView.findViewById(R.id.listAddress);
        }
    }
}
