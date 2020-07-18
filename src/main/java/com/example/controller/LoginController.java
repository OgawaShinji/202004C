package com.example.controller;

import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Order;
import com.example.domain.User;
import com.example.form.LoginForm;
import com.example.service.LoginService;
import com.example.service.ShoppingCartService;

@Controller
@RequestMapping("")
public class LoginController {

	@Autowired
	private ShoppingCartService shoppingCartService;

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

	/**
	 * ログインします
	 * 
	 * @param loginForm
	 * @param model
	 * @return ログイン後小一覧画面
	 */
	@RequestMapping("/login")
	public String login(LoginForm loginForm, Model model) {
		User user = loginService.findByMailAddressAndPassward(loginForm.getMailAddress(), loginForm.getPassword());
		System.out.println(user);
		if (Objects.isNull(user)) {
			model.addAttribute("failed", "メールアドレスまたはパスワードが違います");
			return toLogin();
		} else {
			if (Objects.nonNull(session.getAttribute("user"))) {
				User userAlreadyInSession = (User) session.getAttribute("user");

				Order orderForLoginUser = new Order();
				orderForLoginUser.setStatus(0);
				orderForLoginUser.setUserId(user.getId());
				Integer ordersIdForLoginUser = shoppingCartService.findIdByUserIdAndStatus(orderForLoginUser);

				Order orderForNotLoginUser = new Order();
				orderForNotLoginUser.setStatus(0);
				orderForNotLoginUser.setUserId(userAlreadyInSession.getId());
				Integer ordersIdForNotLoginUser = shoppingCartService.findIdByUserIdAndStatus(orderForLoginUser);

				if (Objects.isNull(ordersIdForLoginUser)) {
					shoppingCartService.updateOrdersUserId(userAlreadyInSession.getId(), orderForLoginUser);
				} else {
					orderForLoginUser.setId(ordersIdForLoginUser);
					orderForNotLoginUser.setId(ordersIdForNotLoginUser);
					shoppingCartService.changeUserDuringShopping(orderForNotLoginUser, orderForLoginUser);
				}
			}
			session.setAttribute("user", user);
			return "redirect:/item-list";
		}
	}

}
