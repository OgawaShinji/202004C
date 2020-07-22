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
public class PaymentFormTest {

	@Autowired
	Validator validator;

	private PaymentForm paymentForm = new PaymentForm();
	private BindingResult bindingResult = new BindException(paymentForm, "PaymentForm");

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		paymentForm.setName("峯田和伸");
		paymentForm.setEmail("mineta@sample.com");
		paymentForm.setPassword("minemineta");
		paymentForm.setCheckpassword("minemineta");
		paymentForm.setZipcode("171-0032");
		paymentForm.setAddress("東京都豊島区雑司が谷");
		paymentForm.setTelephone("0888-2222-3333");
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * エラーなし
	 */
	@Test
	public void noError() {
		validator.validate(paymentForm, bindingResult);
		assertThat(bindingResult.getFieldError(), is(nullValue()));
	}

	/**
	 * nameが空文字
	 */
	@Test
	public void nameIsBlank() {
		paymentForm.setName("");
		validator.validate(paymentForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("name"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("名前を入力してください"));
	}

	/**
	 * nameが半角スペースのみ
	 */
	@Test
	public void nameIsOnlySpace() {
		paymentForm.setName(" ");
		validator.validate(paymentForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("name"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("名前を入力してください"));
	}

	/**
	 * emailが空文字
	 */
	@Test
	public void emailIsBlank() {
		paymentForm.setEmail("");
		validator.validate(paymentForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("email"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("メールアドレスの形式が不正です"));
	}

	/**
	 * emailが半角スペースのみ
	 */
	@Test
	public void emailIsOnlySpace() {
		paymentForm.setEmail(" ");
		validator.validate(paymentForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("email"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("メールアドレスの形式が不正です"));
	}

	/**
	 * emailが@無し
	 */
	@Test
	public void email_アットマークなし() {
		paymentForm.setEmail("aaaa");
		validator.validate(paymentForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("email"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("メールアドレスの形式が不正です"));
	}

	/**
	 * passwordが空文字
	 */
	@Test
	public void passwordIsBlank() {
		paymentForm.setPassword("");
		validator.validate(paymentForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("password"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("パスワードは８文字以上１６文字以内で設定してください"));
	}

	/**
	 * passwordが半角スペースのみ
	 */
	@Test
	public void passwordIsOnlySpace() {
		paymentForm.setPassword(" ");
		validator.validate(paymentForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("password"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("パスワードは８文字以上１６文字以内で設定してください"));
	}

	/**
	 * passwordが8文字未満
	 */
	@Test
	public void password_8文字未満() {
		paymentForm.setPassword("aaaa");
		validator.validate(paymentForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("password"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("パスワードは８文字以上１６文字以内で設定してください"));
	}

	/**
	 * passwordが16文字より多い
	 */
	@Test
	public void password_16文字より多い() {
		paymentForm.setPassword("bbbbbbbbbbbbbbbbbbbb");
		validator.validate(paymentForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("password"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("パスワードは８文字以上１６文字以内で設定してください"));
	}

	/**
	 * checkpasswordが空文字
	 */
	@Test
	public void checkpasswordIsBlank() {
		paymentForm.setCheckpassword("");
		validator.validate(paymentForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("checkpassword"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("確認用パスワードを入力してください"));
	}

	/**
	 * checkpasswordが半角スペースのみ
	 */
	@Test
	public void checkpasswordIsOnlySpace() {
		paymentForm.setCheckpassword(" ");
		validator.validate(paymentForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("checkpassword"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("確認用パスワードを入力してください"));
	}

	/**
	 * zipcodeが空文字
	 */
	@Test
	public void zipcodeIsBlank() {
		paymentForm.setZipcode("");
		validator.validate(paymentForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("zipcode"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("郵便番号はXXX-XXXXの形式で入力してください"));
	}

	/**
	 * zipcodeが半角スペースのみ
	 */
	@Test
	public void zipcodeIsOnlySpace() {
		paymentForm.setZipcode(" ");
		validator.validate(paymentForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("zipcode"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("郵便番号はXXX-XXXXの形式で入力してください"));
	}

	/**
	 * zipcodeがXXX-XXXXの形式じゃない
	 */
	@Test
	public void zipcode_形式が不正() {
		paymentForm.setZipcode("111111111");
		validator.validate(paymentForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("zipcode"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("郵便番号はXXX-XXXXの形式で入力してください"));
	}

	/**
	 * addressが空文字
	 */
	@Test
	public void addressIsBlank() {
		paymentForm.setAddress("");
		validator.validate(paymentForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("address"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("住所を入力してください"));
	}

	/**
	 * addressが半角スペースのみ
	 */
	@Test
	public void addressIsOnlySpace() {
		paymentForm.setAddress(" ");
		validator.validate(paymentForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("address"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("住所を入力してください"));
	}

	/**
	 * telephoneが空文字
	 */
	@Test
	public void telephoneIsBlank() {
		paymentForm.setTelephone("");
		validator.validate(paymentForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("telephone"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("電話番号はXXX-XXXX-XXXXの形式で入力してください"));
	}

	/**
	 * telephoneが半角スペースのみ
	 */
	@Test
	public void telephoneIsOnlySpace() {
		paymentForm.setTelephone(" ");
		validator.validate(paymentForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("telephone"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("電話番号はXXX-XXXX-XXXXの形式で入力してください"));
	}

	/**
	 * telephoneがXXXX-XXXX-XXXXの形式じゃない
	 */
	@Test
	public void telephone_形式が不正() {
		paymentForm.setTelephone(" ");
		validator.validate(paymentForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("telephone"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("電話番号はXXX-XXXX-XXXXの形式で入力してください"));
	}

}
