package com.example.controller;

import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.User;
import com.example.form.LoginForm;
import com.example.service.LoginService;

@Controller
@RequestMapping("/")
public class LoginController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private HttpSession session;
	

	@ModelAttribute
	public LoginForm setUpLoginForm() {
		return new LoginForm();
	}
/////////////////////////////////////////////////////
// ユースケース：ログインをする
/////////////////////////////////////////////////////
/**
* ログイン画面を出力します.
* 
* @return ログイン画面
*/
  @RequestMapping("/toLogin")
  public String toLogin() {
	  return "user/login";
  }
  @RequestMapping("/login")
  public String login(LoginForm loginForm, Model model) {
	 User user = loginService.findByMailAddressAndPassward(loginForm.getMailAddress(), loginForm.getPassword());
	 System.out.println(user);
	 if (Objects.isNull(user)) {
		model.addAttribute("failed", "メールアドレスまたはパスワードが違います");
		return toLogin();
	}else{
		session.setAttribute("user",user);
		//IndexControllerに処理移行をお願いします。
		return "item/item_list";
	}
  }
  
}
