package com.storerating.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.storerating.Entity.Rating;
import com.storerating.Entity.Store;
import com.storerating.Entity.User;
import com.storerating.Service.RatingService;
import com.storerating.Service.StoreService;
import com.storerating.Service.UserService;

@Controller
@RequestMapping("/store-owner")
public class StoreOwnerController {

	@Autowired
	private StoreService storeService;

	@Autowired
	private RatingService ratingService;

	@Autowired
	private UserService userService;

	@GetMapping("/dashboard")
	public String dashboard(Model model, Authentication authentication) {
		User owner = userService.findByEmail(authentication.getName());
		List<Store> ownedStores = storeService.findByOwner(owner);
		List<Rating> allRatings = ratingService.findByStores(ownedStores);

		 if (!ownedStores.isEmpty()) {
	            model.addAttribute("store", ownedStores.get(0));
	        }
		
		double averageRating = 0.0;
		if (!allRatings.isEmpty()) {
			averageRating = allRatings.stream().mapToInt(Rating::getRating).average().orElse(0.0);
		}

		model.addAttribute("ownedStores", ownedStores);
		model.addAttribute("ratings", allRatings);
		model.addAttribute("averageRating", Math.round(averageRating * 100.0) / 100.0);
		model.addAttribute("totalRatings", allRatings.size());

		return "store-owner/dashboard";
	}
}
