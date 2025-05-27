package com.storerating.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.storerating.Entity.Store;
import com.storerating.Entity.User;

public interface StoreRepository extends JpaRepository<Store, Long>{

	List<Store> findByOwner(User owner);
}
