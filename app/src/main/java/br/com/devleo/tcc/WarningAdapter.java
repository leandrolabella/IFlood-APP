package br.com.devleo.tcc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.com.devleo.tcc.model.Warning;

public class WarningAdapter extends RecyclerView.Adapter<WarningAdapter.MyViewHolder> {

    Context context;
    ArrayList<Warning> warnings;

    public WarningAdapter(Context context, ArrayList<Warning> warnings) {
        this.context = context;
        this.warnings = warnings;
    }

    @NonNull
    @Override
    public WarningAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.items_warnings, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WarningAdapter.MyViewHolder holder, int position) {
        Warning warning = warnings.get(position);
        holder.localName.setText(warning.getId());
        holder.message.setText(warning.getMsg());
        if (warning.isConfirm()) {
            holder.confirm.setText("Sim");
        } else {
            holder.confirm.setText("Não");
        }
    }

    @Override
    public int getItemCount() {
        return warnings.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView localName, message, confirm;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            localName = itemView.findViewById(R.id.listLocation);
            message = itemView.findViewById(R.id.listMessage);
            confirm = itemView.findViewById(R.id.listVerify);
        }
    }
}
