package com.paras.setgo.Adapters;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.paras.setgo.Activities.CreateActivity;
import com.paras.setgo.Activities.TaskActivity;
import com.paras.setgo.Models.TaskItemModel;
import com.paras.setgo.R;
import com.paras.setgo.dbHelper;

import java.util.ArrayList;

public class taskAdapter extends RecyclerView.Adapter<taskAdapter.ViewHolder>{
    private Context context;
    private ArrayList<TaskItemModel> taskItemData;

    public taskAdapter(Context context,ArrayList<TaskItemModel> taskItemData){
        this.taskItemData = taskItemData;
    this.context = context;
    }

    @NonNull
    @Override
    public taskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull taskAdapter.ViewHolder holder, int position) {
        TaskItemModel data =taskItemData.get(position);
            holder.itemName.setText(data.getTaskName());
            holder.itemTime.setText("Set: "+data.getTotalDuration());
            holder.itemSets.setText("Sets: "+data.getSets());
            holder.itemRest.setText("Rest: "+data.getRest());

        Log.e("TAG: "+data.getTaskName(), "- Reps : Sets "+ data.getReps()+" : "+ data.getSets());

        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(view,data);

//                    Toast.makeText(context, "Wroking on it", Toast.LENGTH_SHORT).show();
                }
            });
            holder.linearLayoutCompat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, TaskActivity.class);
                    intent.putExtra("Name",data.getTaskName());
                    intent.putExtra("Sets",data.getSets());
                    intent.putExtra("Reps",data.getReps());
                    intent.putExtra("TotalTime",data.getTotalDuration());
                    intent.putExtra("Rest",data.getRest());
                    context.startActivity(intent);
                }
            });

    }


    private void showPopupMenu(View view,TaskItemModel data) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_popup_edit:
                        Intent intent = new Intent(context, CreateActivity.class);
                        intent.putExtra("isUpdate",true);
                        intent.putExtra("Name",data.getTaskName());
                        intent.putExtra("Sets",data.getSets());
                        intent.putExtra("TotalTime",data.getTotalDuration());
                        intent.putExtra("Rest",data.getRest());
                        context.startActivity(intent);

                        return true;
                    case R.id.action_popup_delete:
                        dbHelper db = new dbHelper(context);
                        db.deleteTask(data.getTaskName());
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }


    @Override
    public int getItemCount() {
        return taskItemData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayoutCompat linearLayoutCompat;
        private TextView itemName,itemSets,itemTime,itemRest;
        private ImageButton moreBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayoutCompat = itemView.findViewById(R.id.taskItem);
            itemName = itemView.findViewById(R.id.itemTitle);
            itemTime = itemView.findViewById(R.id.itemTime);
            itemSets = itemView.findViewById(R.id.itemSets);
            itemRest = itemView.findViewById(R.id.itemRest);
            moreBtn = itemView.findViewById(R.id.moreBtn);

        }
    }
}
