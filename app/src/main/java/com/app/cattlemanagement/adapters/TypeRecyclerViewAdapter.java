package com.app.cattlemanagement.adapters;

import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cattlemanagement.R;
import com.app.cattlemanagement.activities.admin.DriverAssignmentActivity;
import com.app.cattlemanagement.info.Info;
import com.app.cattlemanagement.models.Super;
import com.app.cattlemanagement.models.TrashSource;
import com.app.cattlemanagement.models.User;
import com.app.cattlemanagement.utils.Utils;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TypeRecyclerViewAdapter extends RecyclerView.Adapter<TypeRecyclerViewHolder> implements Info {

    Context context;
    List<Super> listInstances;
    int type;

    public TypeRecyclerViewAdapter(Context context, List<Super> listInstances, int type) {
        this.context = context;
        this.listInstances = listInstances;
        this.type = type;
    }

    @NonNull
    @Override
    public TypeRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(context);
        View view;
        view = li.inflate(R.layout.drive_layout, parent, false);
        if (type == Info.RV_USER_REGISTRATIONS)
            view = li.inflate(R.layout.rv_user_registrations, parent, false);
        if (type == Info.RV_TRASH_SOURCES)
            view = li.inflate(R.layout.rv_trash_source, parent, false);
        if (type == Info.RV_DRIVER_ASSIGNMENTS)
            view = li.inflate(R.layout.rv_trash_source_assignment, parent, false);
        if (type == Info.RV_ASSIGNED_DRIVER)
            view = li.inflate(R.layout.rv_assigned_trash_sources, parent, false);
        return new TypeRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TypeRecyclerViewHolder holder, int position) {
        if (type == Info.RV_ASSIGNED_DRIVER) {
            initAssignedTrashSources(holder, position);
            return;
        }
        if (type == Info.RV_DRIVER_ASSIGNMENTS) {
            initTrashSourceAssignment(holder, position);
            return;
        }

        if (type == Info.RV_TRASH_SOURCES) {
            initTrashSource(holder, position);
            return;
        }

        if (type == Info.RV_USER_REGISTRATIONS) {
            initUsers(holder, position);
            return;
        }
        initDocuments(holder, position);
    }

    private void initAssignedTrashSources(TypeRecyclerViewHolder holder, int position) {
        TrashSource trashSource = (TrashSource) listInstances.get(position);
        holder.tvTitle.setText(trashSource.getTitle());
        holder.tvDesc.setText(trashSource.getDescription());
        holder.tvAddress.setText(trashSource.getAddress());
        holder.tvStatus.setText(trashSource.getDriverName());
        if (trashSource.getSchedule() != null)
            holder.tvTime.setText(trashSource.getSchedule());
        else
            holder.tvTime.setText("N/A");

    }

    private void initTrashSourceAssignment(TypeRecyclerViewHolder holder, int position) {
        TrashSource trashSource = (TrashSource) listInstances.get(position);
        holder.tvTitle.setText(trashSource.getTitle());
        holder.tvDesc.setText(trashSource.getDescription());
        holder.tvAddress.setText(trashSource.getAddress());
        if (trashSource.getSchedule() != null)
            holder.tvTime.setText(trashSource.getSchedule() + " | Click here to update schedule ");
        else
            holder.tvTime.setText("Click here to update schedule ");


        List<String> listDrivers = new ArrayList<>();
        listDrivers.add("Select Driver");
        for (User user : DriverAssignmentActivity.listDrivers)
            listDrivers.add(user.getStrEtFirstName() + " " + user.getStrEtLastName());
        String[] items = listDrivers.toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, items);
        holder.spnDriver.setAdapter(adapter);
        holder.spnDriver.setSelection(Utils.getIndex(items, trashSource.getDriverName()));
        holder.spnDriver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    trashSource.setDriverName("-");
                    trashSource.setDriverId("-");
                    FirebaseDatabase.getInstance().getReference().child(NODE_TRASH_SOURCE)
                            .child(trashSource.getUserId())
                            .setValue(trashSource);
                    return;
                }
                i = i - 1;
                trashSource.setDriverName(DriverAssignmentActivity.listDrivers.get(i).getStrEtFirstName() + " " + DriverAssignmentActivity.listDrivers.get(i).getStrEtLastName());
                trashSource.setDriverId(DriverAssignmentActivity.listDrivers.get(i).getId());
                FirebaseDatabase.getInstance().getReference().child(NODE_TRASH_SOURCE)
                        .child(trashSource.getUserId())
                        .setValue(trashSource);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                (timePicker, i, i1) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, i);
                    calendar.set(Calendar.MINUTE, i1);

                    SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
                    String timeString = sdf.format(calendar.getTime());
                    holder.tvTime.setText(timeString + "Click here to update schedule");
                    trashSource.setSchedule(timeString);
                    FirebaseDatabase.getInstance().getReference().child(NODE_TRASH_SOURCE)
                            .child(trashSource.getUserId())
                            .setValue(trashSource);
                }, 0, 0, false);
        Log.i(TAG, "initAssignedTrashSources: BEFORE CLICKING");

        holder.tvTime.setOnClickListener(view -> {
            Log.i(TAG, "initAssignedTrashSources: SHOULD SHOW TIME");
            timePickerDialog.show();
        });
    }

    private void initTrashSource(TypeRecyclerViewHolder holder, int position) {
        TrashSource trashSource = (TrashSource) listInstances.get(position);
        holder.tvTitle.setText(trashSource.getTitle());
        holder.tvDesc.setText(trashSource.getDescription());
        holder.tvAddress.setText(trashSource.getAddress());
        holder.tvStatus.setText(trashSource.getStatus());
        if (trashSource.getStatus().equals(Info.STATUS_PENDING))
            holder.tvStatus.setTextColor(context.getColor(R.color.yellow_orig));
        if (trashSource.getStatus().equals(Info.STATUS_REJECTED))
            holder.tvStatus.setTextColor(context.getColor(R.color.red));
        if (trashSource.getStatus().equals(Info.STATUS_APPROVED))
            holder.tvStatus.setTextColor(context.getColor(R.color.green));
        holder.btnAccept.setOnClickListener(view -> {
            trashSource.setStatus(Info.STATUS_APPROVED);
            FirebaseDatabase.getInstance().getReference().child(NODE_TRASH_SOURCE)
                    .child(trashSource.getUserId())
                    .setValue(trashSource);
        });
        holder.btnReject.setOnClickListener(view -> {
            trashSource.setStatus(Info.STATUS_REJECTED);
            FirebaseDatabase.getInstance().getReference().child(NODE_TRASH_SOURCE)
                    .child(trashSource.getUserId())
                    .setValue(trashSource);
        });
    }

    private void initUsers(TypeRecyclerViewHolder holder, int position) {
        User user = (User) listInstances.get(position);
        holder.tvFirstName.setText(user.getStrEtFirstName());
        holder.tvLastName.setText(user.getStrEtLastName());
        holder.tvStatus.setText(user.getVerStatus());
        if (user.getVerStatus().equals(Info.USER_ACTIVE))
            holder.tvStatus.setTextColor(context.getColor(R.color.yellow));
        else
            holder.tvStatus.setTextColor(context.getColor(R.color.red));

        holder.btnAccept.setOnClickListener(view -> {
            user.setVerStatus(Info.USER_ACTIVE);
            FirebaseDatabase.getInstance().getReference().child(NODE_USERS).child(user.getId())
                    .setValue(user);
        });

        holder.btnReject.setOnClickListener(view -> {
            user.setVerStatus(Info.USER_REJECT);
            FirebaseDatabase.getInstance().getReference().child(NODE_USERS).child(user.getId())
                    .setValue(user);
        });

    }


    private void initDocuments(TypeRecyclerViewHolder holder, int position) {
//        holder.tvDriveDesc.setText(drive.getDescription());
//        holder.tvDriveTitle.setText(drive.getTitle());
//        holder.tvDriveType.setText(drive.getType());
//        holder.et_etAddress.setText(drive.getAddress());
    }

    @Override
    public int getItemCount() {
        return listInstances.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }
}
