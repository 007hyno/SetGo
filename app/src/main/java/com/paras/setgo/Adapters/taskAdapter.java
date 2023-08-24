package com.paras.setgo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.paras.setgo.Activities.TaskActivity;
import com.paras.setgo.Models.TaskItemModel;
import com.paras.setgo.R;

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
            holder.itemTime.setText("Total Time: "+data.getTotalDuration());
            holder.itemSets.setText("Sets: "+data.getSets());

            holder.moreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Wroking on it", Toast.LENGTH_SHORT).show();
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

    @Override
    public int getItemCount() {
        return taskItemData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayoutCompat linearLayoutCompat;
        private TextView itemName,itemSets,itemTime;
        private ImageButton moreBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayoutCompat = itemView.findViewById(R.id.taskItem);
            itemName = itemView.findViewById(R.id.itemTitle);
            itemTime = itemView.findViewById(R.id.itemTime);
            itemSets = itemView.findViewById(R.id.itemSets);
            moreBtn = itemView.findViewById(R.id.moreBtn);

        }
    }
}
