package com.storerating.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.storerating.Entity.Store;
import com.storerating.Entity.User;
import com.storerating.Service.RatingService;
import com.storerating.Service.StoreService;
import com.storerating.Service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserService userService;

	@Autowired
	private StoreService storeService;

	@Autowired
	private RatingService ratingService;

	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		model.addAttribute("totalUsers", userService.getTotalUsers());
		model.addAttribute("totalStores", storeService.getTotalStores());
		model.addAttribute("totalRatings", ratingService.getTotalRatings());
		return "admin/dashboard";
	}

	@GetMapping("/add-user")
	public String addUser(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("roles", User.Role.values());
		return "admin/add-user";
	}

	@PostMapping("/add-user")
	public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("roles", User.Role.values());
			return "admin/add-user";
		}

		if (userService.existsByEmail(user.getEmail())) {
			model.addAttribute("emailError", "Email already exists");
			model.addAttribute("roles", User.Role.values());
			return "admin/add-user";
		}

		userService.save(user);
		return "redirect:/admin/dashboard?userAdded";
	}

	@GetMapping("/add-store")
	public String addStore(Model model) {
		model.addAttribute("store", new Store());
		model.addAttribute("users", userService.findAll());
		return "admin/add-store";
	}

	@PostMapping("/add-store")
	public String saveStore(@Valid @ModelAttribute("store") Store store, @RequestParam(required = false) Long ownerId,
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("users", userService.findAll());
			return "admin/add-store";
		}

		if (ownerId != null) {
			User owner = userService.findById(ownerId);
			if (owner != null) {
				store.setOwner(owner);
				owner.setRole(User.Role.STORE_OWNER);
				userService.save(owner);
			}
		}

		storeService.save(store);
		return "redirect:/admin/dashboard?storeAdded";
	}

	@GetMapping("/manage-stores")
	public String manageStores(Model model) {
		model.addAttribute("stores", storeService.findAll());
		return "admin/manage-stores";
	}

	@PostMapping("/delete-store/{id}")
	public String deleteStore(@PathVariable Long id) {
		storeService.delete(id);
		return "redirect:/admin/manage-stores?deleted";
	}

	@PostMapping("/update-user-role")
	public String updateUserRole(@RequestParam Long userId, @RequestParam User.Role role) {
		userService.updateUserRole(userId, role);
		return "redirect:/admin/dashboard?roleUpdated";
	}
	
	@GetMapping("/edit-store/{id}")
	public String editStoreForm(@PathVariable Long id, Model model) {
	    Store store = storeService.findById(id);
	    model.addAttribute("store", store);

	    if (store.getOwner() != null) {
	        // If the store has an assigned owner, only pass that owner in the list
	        model.addAttribute("users", java.util.List.of(store.getOwner()));
	    } else {
	        // If no owner is assigned, pass all users
	        model.addAttribute("users", userService.findAll());
	    }

	    return "admin/edit-store";
	}


	@PostMapping("/edit-store")
	public String updateStore(@Valid @ModelAttribute("store") Store store,
	                          BindingResult result,
	                          @RequestParam(required = false) Long ownerId,
	                          Model model) {
	    if (result.hasErrors()) {
	        model.addAttribute("users", userService.findAll());
	        return "admin/edit-store";
	    }

	    if (ownerId != null) {
	        User owner = userService.findById(ownerId);
	        if (owner != null) {
	            store.setOwner(owner);
	            owner.setRole(User.Role.STORE_OWNER);
	            userService.save(owner);
	        }
	    }

	    storeService.save(store);
	    return "redirect:/admin/manage-stores?updated";
	}
	
	@GetMapping("/show-all-users")
	public String findAllUsers(Model model) {
	    model.addAttribute("users", userService.findAllUsers());
	    return "admin/show-all-users";
	}
	
	@GetMapping("/show-all-stores")
	public String findAll(Model model) {
	    model.addAttribute("stores", storeService.findAll());
	    return "admin/show-all-stores";
	}
}
