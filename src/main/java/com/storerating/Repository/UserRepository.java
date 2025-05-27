package com.storerating.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.storerating.Entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByEmail(String email);

	List<User> findByRole(User.Role role);

	boolean existsByEmail(String email);

}
