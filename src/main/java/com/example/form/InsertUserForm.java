package com.example.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class InsertUserForm {

	/**
	 * 名前
	 */
	@NotBlank(message = "名前を入力して下さい")
	private String name;
	/**
	 * メールアドレス
	 */
	@NotBlank(message = "メールアドレスを入力して下さい")
	@Email(message = "メールアドレスの形式が不正です")
	private String email;
	/**
	 * パスワード
	 */
	@NotBlank(message = "パスワードを入力して下さい")
	@Size(min=8,max=16, message = "パスワードは８文字以上１６文字以内で設定してください")
	private String password;
	/**
	 * 確認用パスワード
	 */
	@NotBlank(message = "確認用パスワードを入力して下さい")
	private String checkpassword;
	/**
	 * 郵便番号
	 * 
	 */
	@NotBlank(message = "郵便番号を入力して下さい")
	@Pattern(message = "郵便番号はXXX-XXXXの形式で入力してください" ,regexp="^[0-9]{3}-[0-9]{4}$")
	private String zipcode;
	/**
	 * 住所
	 */
	@NotBlank(message = "住所を入力して下さい")
	private String address;
	/**
	 * 電話番号
	 */
	@NotBlank(message = "電話番号を入力して下さい")
	@Pattern(message = "電話番号はXXXX-XXXX-XXXXの形式で入力してください" ,regexp="^0\\d{2,3}-\\d{1,4}-\\d{4}$")
	private String telephone;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCheckpassword() {
		return checkpassword;
	}

	public void setCheckpassword(String checkpassword) {
		this.checkpassword = checkpassword;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}


}
