package com.storerating.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.storerating.Entity.Rating;
import com.storerating.Entity.Store;
import com.storerating.Entity.User;

public interface RatingRepository extends JpaRepository<Rating, Long> {

	List<Rating> findByStore(Store store);

	List<Rating> findByUser(User user);

	Optional<Rating> findByUserAndStore(User user, Store store);

	List<Rating> findByStoreIn(List<Store> stores);

}
