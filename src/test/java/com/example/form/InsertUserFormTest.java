package com.example.form;

import static org.hamcrest.CoreMatchers.*;

import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InsertUserFormTest {

	@Autowired
	Validator validator;

	private InsertUserForm insertUserForm = new InsertUserForm();
	private BindingResult bindingResult = new BindException(insertUserForm, "InsertUserForm");

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		insertUserForm.setName("峯田和伸");
		insertUserForm.setEmail("mineta@sample.com");
		insertUserForm.setPassword("minemineta");
		insertUserForm.setCheckpassword("minemineta");
		insertUserForm.setZipcode("171-0032");
		insertUserForm.setAddress("東京都豊島区雑司が谷");
		insertUserForm.setTelephone("0888-2222-3333");
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * エラーなし
	 */
	@Test
	public void noError() {
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError(), is(nullValue()));
	}

	/**
	 * nameがnull
	 */
	@Test
	public void nameIsNull() {
		insertUserForm.setName(null);
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("name"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("名前を入力して下さい"));
	}

	/**
	 * nameが空文字
	 */
	@Test
	public void nameIsBlank() {
		insertUserForm.setName("");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("name"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("名前を入力して下さい"));
	}

	/**
	 * nameが半角スペースのみ
	 */
	@Test
	public void nameIsOnlySpace() {
		insertUserForm.setName(" ");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("name"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("名前を入力して下さい"));
	}
	
	/**
	 * emailがnull
	 */
	@Test
	public void emailIsNull() {
		insertUserForm.setEmail(null);
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("email"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("メールアドレスを入力して下さい"));
	}
	
	/**
	 * emailが空文字
	 */
	@Test
	public void emailIsBlank() {
		insertUserForm.setEmail("");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("email"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("メールアドレスを入力して下さい"));
	}
	
	/**
	 * emailが半角スペースのみ
	 */
	@Test
	public void emailIsOnlySpace() {
		insertUserForm.setEmail(" ");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("email"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("メールアドレスを入力して下さい"));
	}
	
	/**
	 * emailが@無し
	 */
	@Test
	public void email_アットマークなし() {
		insertUserForm.setEmail("aaaa");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("email"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("メールアドレスの形式が不正です"));
	}
	
	/**
	 * passwordがnull
	 */
	@Test
	public void passwordIsNull() {
		insertUserForm.setPassword(null);
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("password"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("パスワードを入力して下さい"));
	}
	
	/**
	 * passwordが空文字
	 */
	@Test
	public void passwordIsBlank() {
		insertUserForm.setPassword("");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("password"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("パスワードを入力して下さい"));
	}
	
	/**
	 * passwordが半角スペースのみ
	 */
	@Test
	public void passwordIsOnlySpace() {
		insertUserForm.setPassword(" ");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("password"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("パスワードを入力して下さい"));
	}
	
	/**
	 * passwordが8文字未満
	 */
	@Test
	public void password_8文字未満() {
		insertUserForm.setPassword("aaaa");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("password"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("パスワードを入力して下さい"));
	}
	
	/**
	 * passwordが16文字より多い
	 */
	@Test
	public void password_16文字より多い() {
		insertUserForm.setPassword("bbbbbbbbbbbbbbbbbbbb");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("password"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("パスワードを入力して下さい"));
	}
	
	/**
	 * checkpasswordがnull
	 */
	@Test
	public void checkpasswordIsNull() {
		insertUserForm.setCheckpassword(null);
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("checkpassword"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("確認用パスワードを入力して下さい"));
	}
	
	/**
	 * checkpasswordが空文字
	 */
	@Test
	public void checkpasswordIsBlank() {
		insertUserForm.setCheckpassword("");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("checkpassword"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("確認用パスワードを入力して下さい"));
	}
	
	/**
	 * checkpasswordが半角スペースのみ
	 */
	@Test
	public void checkpasswordIsOnlySpace() {
		insertUserForm.setCheckpassword(" ");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("checkpassword"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("確認用パスワードを入力して下さい"));
	}
	
	/**
	 * zipcodeがnull
	 */
	@Test
	public void zipcodeIsNull() {
		insertUserForm.setZipcode(null);
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("zipcode"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("郵便番号を入力して下さい"));
	}
	
	/**
	 * zipcodeが空文字
	 */
	@Test
	public void zipcodeIsBlank() {
		insertUserForm.setZipcode("");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("zipcode"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("郵便番号を入力して下さい"));
	}
	
	/**
	 * zipcodeが半角スペースのみ
	 */
	@Test
	public void zipcodeIsOnlySpace() {
		insertUserForm.setZipcode(" ");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("zipcode"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("郵便番号を入力して下さい"));
	}
	
	/**
	 * zipcodeがXXX-XXXXの形式じゃない
	 */
	@Test
	public void zipcode_形式が不正(){
		insertUserForm.setZipcode("111111111");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("zipcode"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("郵便番号はXXX-XXXXの形式で入力して下さい"));
	}
	
	/**
	 * addressがnull
	 */
	@Test
	public void addressIsNull() {
		insertUserForm.setAddress(null);
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("address"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("住所を入力して下さい"));
	}
	
	/**
	 * addressが空文字
	 */
	@Test
	public void addressIsBlank() {
		insertUserForm.setAddress("");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("address"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("住所を入力して下さい"));
	}
	
	/**
	 * addressが半角スペースのみ
	 */
	@Test
	public void addressIsOnlySpace() {
		insertUserForm.setAddress(" ");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("address"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("住所を入力して下さい"));
	}
	
	/**
	 * telephoneがnull
	 */
	@Test
	public void telephoneIsNull() {
		insertUserForm.setTelephone(null);
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("telephone"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("電話番号を入力して下さい"));
	}
	
	/**
	 * telephoneが空文字
	 */
	@Test
	public void telephoneIsBlank() {
		insertUserForm.setTelephone("");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("telephone"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("電話番号を入力して下さい"));
	}
	
	/**
	 * telephoneが半角スペースのみ
	 */
	@Test
	public void telephoneIsOnlySpace() {
		insertUserForm.setTelephone(" ");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("telephone"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("電話番号を入力して下さい"));
	}
	
	/**
	 * telephoneがXXXX-XXXX-XXXXの形式じゃない
	 */
	@Test
	public void telephone_形式が不正() {
		insertUserForm.setTelephone(" ");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("telephone"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("電話番号はXXXX-XXXX-XXXXの形式で入力して下さい"));
	}

}
