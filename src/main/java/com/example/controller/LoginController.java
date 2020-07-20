package com.example.controller;

import java.security.Principal;
import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Order;
import com.example.domain.User;
import com.example.form.LoginForm;
import com.example.security.LoginUserDetails;
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
	@RequestMapping("/login/success")
	public String login(@AuthenticationPrincipal LoginUserDetails userDetails, Model model) {
		// security実装で不必要になったが一応残しておくことにしました by ogawa
		// User user = loginService.findByMailAddressAndPassward(loginForm.getEmail(),
		// loginForm.getPassword());
		// if (Objects.isNull(user)) {
		// model.addAttribute("failed", "メールアドレスまたはパスワードが違います");
		// return toLogin();
		// } else {

		// 未ログインでカートに追加し仮ユーザーIDがsessionにある場合
		if (Objects.nonNull(session.getAttribute("user"))) {
			User userAlreadyInSession = (User) session.getAttribute("user");
			// Loginしたユーザー情報をOrder型にset
			Order orderForLoginUser = new Order();
			orderForLoginUser.setStatus(0);
			orderForLoginUser.setUserId(userDetails.getId());
			Integer ordersIdForLoginUser = shoppingCartService.findIdByUserIdAndStatus(orderForLoginUser);
			// 未ログインでカートに追加していた場合の仮ユーザー情報をOrder型にset
			Order orderForNotLoginUser = new Order();
			orderForNotLoginUser.setStatus(0);
			orderForNotLoginUser.setUserId(userAlreadyInSession.getId());
			Integer ordersIdForNotLoginUser = shoppingCartService.findIdByUserIdAndStatus(orderForNotLoginUser);

			// 未ログイン時の買い物情報以前にログインしてカートに追加していないとき
			if (Objects.isNull(ordersIdForLoginUser)) {// Orderテーブルの仮ユーザーIDをLoginユーザーのIDに変更
				shoppingCartService.updateOrdersUserId(userAlreadyInSession.getId(), orderForLoginUser);
			} else {// 以前にカートに追加していたときそれらのOrder_itemと未ログイン状態で追加したOrder_itemのOrderIdを統一
				orderForLoginUser.setId(ordersIdForLoginUser);
				orderForNotLoginUser.setId(ordersIdForNotLoginUser);
				shoppingCartService.changeUserDuringShopping(orderForNotLoginUser, orderForLoginUser);
			}
		}
		session.setAttribute("user", userDetails.getUser());
		return "redirect:/item-list";
		// }
	}

	@RequestMapping("/logout/success")
	public String logout() {
		session.removeAttribute("user");
		return "redirect:/item-list";
	}
}
