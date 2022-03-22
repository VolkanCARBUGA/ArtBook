package com.android.artbook;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.artbook.databinding.RecyclerviewRowBinding;

import java.util.ArrayList;

public class ArtAdapter extends RecyclerView.Adapter<ArtAdapter.ArtHolder> {
    ArrayList<Art> artArrayList;

    public ArtAdapter(ArrayList<Art> artArrayList) {
        this.artArrayList = artArrayList;
    }

    @NonNull
    @Override
    public ArtHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewRowBinding recyclerviewRowBinding = RecyclerviewRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ArtHolder(recyclerviewRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtHolder holder, int position) {
        holder.binding.recyclerviewTextView.setText(artArrayList.get(position).name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), ArtAdd.class);
                intent.putExtra("artId",artArrayList.get(holder.getAdapterPosition()).id);
                intent.putExtra("info","old");
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return artArrayList.size();
    }

    public class ArtHolder extends RecyclerView.ViewHolder {
        private RecyclerviewRowBinding binding;

        public ArtHolder(RecyclerviewRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}