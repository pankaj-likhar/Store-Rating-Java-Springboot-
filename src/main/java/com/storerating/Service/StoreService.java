package com.storerating.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storerating.Entity.Store;
import com.storerating.Entity.User;
import com.storerating.Repository.StoreRepository;

@Service
public class StoreService {

	@Autowired
	private StoreRepository storeRepository;

	public Store save(Store store) {
		return storeRepository.save(store);
	}

	public List<Store> findAll() {
		return storeRepository.findAll();
	}

	public Store findById(Long id) {
		return storeRepository.findById(id).orElse(null);
	}

	public List<Store> findByOwner(User owner) {
		return storeRepository.findByOwner(owner);
	}

	public void delete(Long id) {
		storeRepository.deleteById(id);
	}

	public long getTotalStores() {
		return storeRepository.count();
	}

}
