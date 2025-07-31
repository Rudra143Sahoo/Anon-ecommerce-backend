package com.nt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.model.Address;
import com.nt.repo.AddressRepository;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepo;

    public Address saveAddress(Address address) {
        return addressRepo.save(address);
    }

    public List<Address> getAddressesByUserId(Long userId) {
        return addressRepo.findByUserId(userId);
    }

    public void deleteAddress(Long id) {
        addressRepo.deleteById(id);
    }

    public Address updateAddress(Address updated) {
        return addressRepo.save(updated);
    }
 // ✅ Add this method
    public Address findById(Long id) {
        return addressRepo.findById(id).orElse(null);
    }
}
