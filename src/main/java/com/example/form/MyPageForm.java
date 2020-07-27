package com.example.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class MyPageForm {

	/**
	 * 名前
	 */
	@NotBlank(message = "名前を入力してください")
	private String name;
	/**
	 * メールアドレス
	 */
	@Pattern(regexp = "^([\\w])+([\\w\\._-])*\\@([\\w])+([\\w\\._-])*\\.([a-zA-Z])+$", message = "メールアドレスの形式が不正です")
	private String email;
	/**
	 * 郵便番号
	 * 
	 */
	@Pattern(message = "郵便番号はXXX-XXXXの形式で入力してください", regexp = "^[0-9]{3}-[0-9]{4}$")
	private String zipcode;
	/**
	 * 住所
	 */
	@NotBlank(message = "住所を入力してください")
	private String address;
	/**
	 * 電話番号
	 */
	@Pattern(message = "電話番号はXXXX-XXXX-XXXXの形式で入力してください", regexp = "^0\\d{2,3}-\\d{1,4}-\\d{4}$")
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
