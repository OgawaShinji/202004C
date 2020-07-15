package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.User;
import com.example.repository.UsersRepository;

@Service
@Transactional
public class InsertUserService {

	@Autowired
	private UsersRepository usersRepository;
	
	public void insert(User user) {
		StringBuilder br = new StringBuilder(user.getZipcode());
		br.deleteCharAt(3);

		usersRepository.insert(user);
	}
	
	public User findByMailAddress(String email) {
		return usersRepository.findByMailAddress(email);
	}
}
