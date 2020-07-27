package com.example.controller;

import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.User;
import com.example.form.MyPageForm;
import com.example.service.InsertUserService;

@Controller
@RequestMapping("/mypage")
public class UserMypageController {

	@Autowired
	private HttpSession session;

	@Autowired
	private InsertUserService insertUserService;

	@ModelAttribute
	public MyPageForm setMyPageForm() {
		return new MyPageForm();
	}

	/**
	 * マイページ画面を表示します.
	 * 
	 * @return マイページ画面
	 */
	@RequestMapping("")
	public String index() {
		return "user/mypage";
	}

	/**
	 * マイページ編集画面を表示します.
	 * 
	 * @param myPageForm
	 * @param model
	 * @return マイページ編集画面
	 */
	@RequestMapping("/edit")
	public String edit(MyPageForm myPageForm, Model model) {
		// 編集フォームに初期値を表示するためのユーザー情報をsessionから取得
		User firstUser = (User) session.getAttribute("user");
		
		myPageForm.setName(firstUser.getName());
		myPageForm.setEmail(firstUser.getEmail());
		StringBuilder br = new StringBuilder(firstUser.getZipcode());
		br.insert(3, '-');
		myPageForm.setZipcode(br.toString());
		myPageForm.setAddress(firstUser.getAddress());
		myPageForm.setTelephone(firstUser.getTelephone());
		model.addAttribute("myPageForm", myPageForm);
		return "user/edit_mypage";
	}

	/**
	 * マイページ編集画面で更新したユーザー情報をもとにマイページ画面を表示します.
	 * 
	 * @param myPageForm
	 * @param result
	 * @param model
	 * @return マイページ画面
	 */
	@RequestMapping("/update")
	public String update(@Validated MyPageForm myPageForm, BindingResult result, Model model) {
		// sessionに格納されているユーザー情報を取得
		User user = (User) session.getAttribute("user");
		if (result.hasErrors()) {
			return edit(myPageForm, model);
		}
		// メールアドレスの重複確認
		if (!(Objects.isNull(insertUserService.findByMailAddress(myPageForm.getEmail())))) {
			// 変更を加えてる場合のみ重複確認
			if (!(user.getEmail().equals(myPageForm.getEmail()))) {
				model.addAttribute("emailError", "そのメールアドレスはすでに使われています");
				return edit(myPageForm, model);
			}
		}
		// sessionに格納されているユーザ情報をフォームに入力された情報で更新する
		user.setName(myPageForm.getName());
		user.setEmail(myPageForm.getEmail());
		StringBuilder br = new StringBuilder(myPageForm.getZipcode());
		br.deleteCharAt(3);
		user.setZipcode(br.toString());
		user.setAddress(myPageForm.getAddress());
		user.setTelephone(myPageForm.getTelephone());
		session.setAttribute("user", user);
		// usersテーブルを更新
		insertUserService.update(user);
		return "user/mypage";
	}
}
