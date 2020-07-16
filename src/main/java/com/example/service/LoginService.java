package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.User;
import com.example.repository.UsersRepository;

@Service
@Transactional
public class LoginService {

	@Autowired
	private UsersRepository usersRepository;

	public User findByMailAddressAndPassward(String email, String password) {
		User user = usersRepository.findByMailAddressAndPassward(email, password);
		return user;
	}

}
