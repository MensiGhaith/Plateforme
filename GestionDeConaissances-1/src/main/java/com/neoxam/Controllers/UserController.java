package com.neoxam.Controllers;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neoxam.SendingEmail.SmtpEmailSender;
import com.neoxam.exception.ResourceNotFoundException;
import com.neoxam.models.*;
import com.neoxam.repository.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value="/gp")
public class UserController {
	
	
	
		@Autowired
		private UserRepository userrep;
		@Autowired
		private SmtpEmailSender smtpMailSender;
		@GetMapping(value="/employees")
		public List<User> getAllUsers() {
			return userrep.findAll();
		}

		@GetMapping(value="/employees/{id}")
		public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long idU)
				throws ResourceNotFoundException {
			User user = userrep.findById(idU)
					.orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + idU));
			return ResponseEntity.ok().body(user);
		}

		@PostMapping(value="/employees")
		public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
			if (userrep.existsByEmail(user.getEmail())) {
			return new ResponseEntity<>(new ResponseMessage("Fail -> Email is already in use!"),
						HttpStatus.BAD_REQUEST);
			}
			 userrep.save(user);
			  return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
		}

		@PutMapping("/employees/{id}")
		public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long id ,
				@Valid @RequestBody User userDetails) throws ResourceNotFoundException {
			User user = userrep.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + id));
			if(!(userDetails.getEmail()== null)) {
				user.setEmail(userDetails.getEmail());
			}
			if(!(userDetails.getPrenom()== null)) {
				user.setPrenom(userDetails.getPrenom());
			}
			if(!(userDetails.getRole()== null)) {
				user.setRole(userDetails.getRole());
			}
			if(!(userDetails.getPassword()== null)) {
				user.setPassword(userDetails.getPassword());
			}
			if(!(userDetails.getNom()== null)) {
				user.setNom(userDetails.getNom());
			}
			if(userDetails.isActive() != user.isActive()) {
				user.setActive(userDetails.isActive());
			}
			
			
			
			
			
			final User updatedUser = userrep.save(user);
			return ResponseEntity.ok(updatedUser);
		}
		@PutMapping("/employees/accepter/{id}")
		public ResponseEntity<User> Accept(@PathVariable(value = "id") Long id ) throws ResourceNotFoundException {
			User user = userrep.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + id));
			user.setActive(true);
	 User updatedUser = userrep.save(user);
	 final  String username=updatedUser.getNom();
	 try {
		smtpMailSender.send(updatedUser.getEmail(), "Neoxam Account", "Bienvenue sur notre plateforme , vous pouvez maintenant vous connecter ");
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
			return ResponseEntity.ok(updatedUser);
		}
		

		@DeleteMapping("/employees/{id}")
		public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long id)
				throws ResourceNotFoundException {
			User user = userrep.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + id));

			userrep.delete(user);
			Map<String, Boolean> response = new HashMap<>();
			response.put("deleted", Boolean.TRUE);
			return response;
		}
	}
	
