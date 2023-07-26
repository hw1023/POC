package com.bezkoder.springjwt.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.repository.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
	@Autowired
	UserRepository repository;	
	
  @GetMapping("/get/all")
  public List<User> allAccess() {
    return repository.findAll();
  }
  
  @GetMapping("/get/{id}")
  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
  public User idAccess(@PathVariable("id") long id) {
	  return repository.findById(id).get();
  }
  
  @GetMapping("/user")
  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
  public String userAccess() {
    return "User Content.";
  }

  @GetMapping("/mod")
  @PreAuthorize("hasRole('ROLE_MODERATOR')")
  public String moderatorAccess() {
    return "Moderator Board.";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public String adminAccess() {
    return "Admin Board.";
  }
  
  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
  @CachePut(value="user-cache", key="#id")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
  
	public User updateUser(@RequestBody User user, @PathVariable long id) {
		return repository.save(user);
	}
  
  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
  @CacheEvict("user-cache")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public void deleteUser(@PathVariable("id") long id) {
		repository.deleteById(id);
	}
}
