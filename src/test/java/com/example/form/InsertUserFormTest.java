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
	 * nameが空文字
	 */
	@Test
	public void nameIsBlank() {
		insertUserForm.setName("");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("name"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("名前を入力してください"));
	}

	/**
	 * nameが半角スペースのみ
	 */
	@Test
	public void nameIsOnlySpace() {
		insertUserForm.setName(" ");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("name"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("名前を入力してください"));
	}

	/**
	 * emailが空文字
	 */
	@Test
	public void emailIsBlank() {
		insertUserForm.setEmail("");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("email"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("メールアドレスの形式が不正です"));
	}

	/**
	 * emailが半角スペースのみ
	 */
	@Test
	public void emailIsOnlySpace() {
		insertUserForm.setEmail(" ");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("email"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("メールアドレスの形式が不正です"));
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
	 * passwordが空文字
	 */
	@Test
	public void passwordIsBlank() {
		insertUserForm.setPassword("");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("password"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("パスワードは８文字以上１６文字以内で設定してください"));
	}

	/**
	 * passwordが半角スペースのみ
	 */
	@Test
	public void passwordIsOnlySpace() {
		insertUserForm.setPassword(" ");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("password"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("パスワードは８文字以上１６文字以内で設定してください"));
	}

	/**
	 * passwordが8文字未満
	 */
	@Test
	public void password_8文字未満() {
		insertUserForm.setPassword("aaaa");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("password"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("パスワードは８文字以上１６文字以内で設定してください"));
	}

	/**
	 * passwordが16文字より多い
	 */
	@Test
	public void password_16文字より多い() {
		insertUserForm.setPassword("bbbbbbbbbbbbbbbbbbbb");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("password"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("パスワードは８文字以上１６文字以内で設定してください"));
	}

	/**
	 * checkpasswordが空文字
	 */
	@Test
	public void checkpasswordIsBlank() {
		insertUserForm.setCheckpassword("");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("checkpassword"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("確認用パスワードを入力してください"));
	}

	/**
	 * checkpasswordが半角スペースのみ
	 */
	@Test
	public void checkpasswordIsOnlySpace() {
		insertUserForm.setCheckpassword(" ");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("checkpassword"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("確認用パスワードを入力してください"));
	}

	/**
	 * zipcodeが空文字
	 */
	@Test
	public void zipcodeIsBlank() {
		insertUserForm.setZipcode("");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("zipcode"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("郵便番号はXXX-XXXXの形式で入力してください"));
	}

	/**
	 * zipcodeが半角スペースのみ
	 */
	@Test
	public void zipcodeIsOnlySpace() {
		insertUserForm.setZipcode(" ");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("zipcode"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("郵便番号はXXX-XXXXの形式で入力してください"));
	}

	/**
	 * zipcodeがXXX-XXXXの形式じゃない
	 */
	@Test
	public void zipcode_形式が不正() {
		insertUserForm.setZipcode("111111111");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("zipcode"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("郵便番号はXXX-XXXXの形式で入力してください"));
	}

	/**
	 * addressが空文字
	 */
	@Test
	public void addressIsBlank() {
		insertUserForm.setAddress("");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("address"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("住所を入力してください"));
	}

	/**
	 * addressが半角スペースのみ
	 */
	@Test
	public void addressIsOnlySpace() {
		insertUserForm.setAddress(" ");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("address"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("住所を入力してください"));
	}

	/**
	 * telephoneが空文字
	 */
	@Test
	public void telephoneIsBlank() {
		insertUserForm.setTelephone("");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("telephone"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("電話番号はXXXX-XXXX-XXXXの形式で入力してください"));
	}

	/**
	 * telephoneが半角スペースのみ
	 */
	@Test
	public void telephoneIsOnlySpace() {
		insertUserForm.setTelephone(" ");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("telephone"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("電話番号はXXXX-XXXX-XXXXの形式で入力してください"));
	}

	/**
	 * telephoneがXXXX-XXXX-XXXXの形式じゃない
	 */
	@Test
	public void telephone_形式が不正() {
		insertUserForm.setTelephone(" ");
		validator.validate(insertUserForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("telephone"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("電話番号はXXXX-XXXX-XXXXの形式で入力してください"));
	}

}
