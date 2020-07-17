package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.User;
import com.example.repository.UsersRepository;

@Service
@Transactional
public class InsertUserService {

	@Autowired
	private UsersRepository usersRepository;

	/**
	 * <pre>
	 * bcryptアルゴリズムでハッシュ化する実装を返します.
	 * これを指定することでパスワード暗号化やマッチ確認する際に
	 * &#64;Autowired
	 * private PasswordEncoder passwordEncoder;
	 * と記載するとDIされるようになります。
	 * </pre>
	 * 
	 * @return bcryptアルゴリズムで暗号化する実装オブジェクト
	 */
	// @Autowired
	// PasswordEncoder passwordEncoder;

	// @Bean
	// PasswordEncoder passwordEncoder() {
	// 	return new BCryptPasswordEncoder();
	// }

	/**
	 *ユーザー情報を登録します. 
	 * パスワードはここでハッシュ化されます
	 * @param administrator 管理者情報
	 */
	// public void insert(User user) {
	// 	user.setPassword(passwordEncoder.encode(user.getPassword()));
	// 	usersRepository.insert(user);
	// }

	public User findByMailAddress(String email) {
		return usersRepository.findByMailAddress(email);
	}
}
