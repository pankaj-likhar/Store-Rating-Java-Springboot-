package com.storerating.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storerating.Entity.Rating;
import com.storerating.Entity.Store;
import com.storerating.Entity.User;
import com.storerating.Repository.RatingRepository;

@Service
public class RatingService {

	@Autowired
	private RatingRepository ratingRepository;

	public Rating save(Rating rating) {
		return ratingRepository.save(rating);
	}

	public List<Rating> findByStore(Store store) {
		return ratingRepository.findByStore(store);
	}

	public List<Rating> findByUser(User user) {
		return ratingRepository.findByUser(user);
	}

	public Rating findByUserAndStore(User user, Store store) {
		return ratingRepository.findByUserAndStore(user, store).orElse(null);
	}

	public List<Rating> findByStores(List<Store> stores) {
		return ratingRepository.findByStoreIn(stores);
	}

	public long getTotalRatings() {
		return ratingRepository.count();
	}

}
