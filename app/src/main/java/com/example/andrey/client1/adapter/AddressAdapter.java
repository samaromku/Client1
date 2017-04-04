package com.example.andrey.client1.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.andrey.client1.R;
import com.example.andrey.client1.entities.Address;
import com.example.andrey.client1.storage.OnListItemClickListener;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder>{
    private List<Address> addresses;
    private OnListItemClickListener clickListener;

    public AddressAdapter(List<Address> addresses, OnListItemClickListener clickListener) {
        this.addresses = addresses;
        this.clickListener = clickListener;
    }

    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item, parent, false);
        return new AddressAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AddressAdapter.ViewHolder holder, int position) {
        holder.bind(addresses.get(position));
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView addressId;
        TextView orgName;
        TextView addressName;


        public ViewHolder(View itemView) {
            super(itemView);
            addressId = (TextView) itemView.findViewById(R.id.address_id);
            orgName = (TextView) itemView.findViewById(R.id.org_name);
            addressName = (TextView) itemView.findViewById(R.id.address);
        }

        public void bind(Address address) {
            addressId.setText(String.valueOf(address.getId()));
            orgName.setText(address.getName());
            addressName.setText(address.getAddress());
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition());
        }
    }

}
