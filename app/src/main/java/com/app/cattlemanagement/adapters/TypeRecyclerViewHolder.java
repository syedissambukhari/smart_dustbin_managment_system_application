package com.app.cattlemanagement.adapters;

import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cattlemanagement.R;

public class TypeRecyclerViewHolder extends RecyclerView.ViewHolder {

    TextView tvTitle;
    TextView tvDesc;
    TextView tvAddress;

    TextView tvFirstName;
    TextView tvLastName;
    TextView tvStatus;
    TextView tvTime;

    Button btnAccept;
    Button btnReject;
    Spinner spnDriver;

    public TypeRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.tv_title);
        tvDesc = itemView.findViewById(R.id.tv_description);
        tvAddress = itemView.findViewById(R.id.tv_address);
        tvFirstName = itemView.findViewById(R.id.tv_first_name);
        tvLastName = itemView.findViewById(R.id.tv_last_name);
        tvStatus = itemView.findViewById(R.id.tv_status);
        tvTime = itemView.findViewById(R.id.tv_time);
        btnAccept = itemView.findViewById(R.id.btn_accept);
        btnReject = itemView.findViewById(R.id.btn_reject);
        spnDriver = itemView.findViewById(R.id.driver_spinner);
    }

}


