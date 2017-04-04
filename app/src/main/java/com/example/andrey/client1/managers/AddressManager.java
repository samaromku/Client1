package com.example.andrey.client1.managers;

import com.example.andrey.client1.entities.Address;

import java.util.ArrayList;
import java.util.List;

public class AddressManager {
    private Address address;
    private List<Address>addresses;

    public static final AddressManager INSTANCE = new AddressManager();
    private AddressManager(){
        addresses = new ArrayList<>();
    }

    public void addAll(List<Address> addressList){
        addresses.addAll(addressList);
    }

    public void removeAll(){
        if(addresses.size()>0){
            addresses.clear();
        }
    }

    public Address getAddressByName(String name){
        for(Address a:addresses){
            if(a.getName().equals(name)){
                return a;
            }
        }
        return null;
    }

    public Address getAddressById(int id){
        for(Address a:addresses){
            if(a.getId()==id){
                return a;
            }
        }
        return null;
    }

    public Address getAddress() {
        return address;
    }

    public List<Address> getAddresses() {
        return addresses;
    }
}
