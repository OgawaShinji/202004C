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
import com.example.form.InsertUserForm;
import com.example.service.InsertUserService;

@Controller
@RequestMapping("/toInsert")
public class InsertUserController {

	@Autowired
	private InsertUserService insertUserService;
	
	@Autowired
	private HttpSession session;
	
	@ModelAttribute
	public InsertUserForm setUpInsertUserForm() {
		return new InsertUserForm();
	}
	 /////////////////////////////////////////////////////
	  // ユースケース：管理者を登録する
	  /////////////////////////////////////////////////////
	  /**
	  * 管理者登録画面を出力します.
	  * 
	  * @return 管理者登録画面
	  */
	  @RequestMapping("")
	  public String toInsert() {
	  return "user/register_user";
	  }
	  /**
	   * 登録情報を入力します
	   * @param insertUserForm
	   * @return item/item_listにフォワード
	   */
	  @RequestMapping("/insert")
	  public String insert(@Validated InsertUserForm insertUserForm, BindingResult result, Model model) {
		  if (result.hasErrors()) {
			return toInsert();
		}
		  User user = new User();
		  user.setName(insertUserForm.getName());
		  user.setEmail(insertUserForm.getEmail());
		  user.setPassword(insertUserForm.getPassword());
		  StringBuilder br = new StringBuilder(insertUserForm.getZipcode());
			br.deleteCharAt(3);
		  user.setZipcode(br.toString());
		  user.setAddress(insertUserForm.getAddress());
		  user.setTelephone(insertUserForm.getTelephone());
			
		  //メールアドレスの重複確認
		  if (Objects.isNull(insertUserService.findByMailAddress(insertUserForm.getEmail()))) {
			model.addAttribute("emailError", "そのメールアドレスはすでに使われています");
			return toInsert();
		}
		  //確認用パスワード
		  if (insertUserForm.getPassword().equals(insertUserForm.getCheckpassword())) {
			  insertUserService.insert(user);
		}else {
			model.addAttribute("passwordError","パスワードと確認用パスワードが不一致です");
			return toInsert();
		}
		  return "forward:user/login";
	  }
}
