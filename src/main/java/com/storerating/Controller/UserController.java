package com.storerating.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.storerating.Entity.Rating;
import com.storerating.Entity.Store;
import com.storerating.Entity.User;
import com.storerating.Service.RatingService;
import com.storerating.Service.StoreService;
import com.storerating.Service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private StoreService storeService;

	@Autowired
	private RatingService ratingService;

	@Autowired
	private UserService userService;
	

	@GetMapping("/dashboard")
	public String dashboard(Model model, Authentication authentication) {
		User user = userService.findByEmail(authentication.getName()); // get user using email from auth
		model.addAttribute("userName", user != null ? user.getName() : "User"); // add userName to the model
		model.addAttribute("userId", user.getId());
		model.addAttribute("stores", storeService.findAll());
		return "user/dashboard";
	}


	@GetMapping("/rate-store/{storeId}")
	public String rateStore(@PathVariable Long storeId, Model model, Authentication authentication) {
		Store store = storeService.findById(storeId);
		User user = userService.findByEmail(authentication.getName());

		if (store == null) {
			return "redirect:/user/dashboard?storeNotFound";
		}

		Rating existingRating = ratingService.findByUserAndStore(user, store);

		model.addAttribute("store", store);
		model.addAttribute("rating", existingRating != null ? existingRating : new Rating());
		model.addAttribute("ratings", ratingService.findByStore(store));

		return "user/rate-store";
	}

	@PostMapping("/submit-rating")
	public String submitRating(@RequestParam Long storeId, @RequestParam int rating,
			@RequestParam(required = false) String comment, Authentication authentication) {
		User user = userService.findByEmail(authentication.getName());
		Store store = storeService.findById(storeId);

		if (store == null || user == null) {
			return "redirect:/user/dashboard?error";
		}

		Rating existingRating = ratingService.findByUserAndStore(user, store);

		if (existingRating != null) {
			existingRating.setRating(rating);
			existingRating.setComment(comment);
			ratingService.save(existingRating);
		} else {
			Rating newRating = new Rating(rating, comment, user, store);
			ratingService.save(newRating);
		}

		return "redirect:/user/dashboard" + "?success";
	}

	
    @GetMapping("/edit-user-profile/{id}")
    public String editUserProfile(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "user/edit-user-profile";
    }


	@PostMapping("/update-user-profile")
	public String updateUserProfile(@RequestParam Long userId,
	                                @RequestParam String name,
	                                @RequestParam String email,
	                                @RequestParam String address) {
	    userService.updateUserDetails(userId, name, email, address);
	    return "redirect:/user/dashboard?profileUpdated";
	}

	
}
