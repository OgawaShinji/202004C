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
public class ItemDetailFormTest {
	
	@Autowired
	Validator validator;
	
	private ItemDetailForm itemDetailForm = new ItemDetailForm();
	private BindingResult bindingResult = new BindException(itemDetailForm, "ItemDetailForm");

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		itemDetailForm.setId("1");
		itemDetailForm.setSize("M");
		itemDetailForm.setPrice("200");
		itemDetailForm.setQuantity("2");
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * エラーなし
	 */
	@Test
	public void noError() {
		validator.validate(itemDetailForm, bindingResult);
		assertThat(bindingResult.getFieldError(), is(nullValue()));
	}

	/**
	 * quantityがnull
	 */
	@Test
	public void quantityIsNull() {
		itemDetailForm.setQuantity(null);
		validator.validate(itemDetailForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("quantity"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("数量を選択してください"));
	}
	
	/**
	 * quantityが空文字
	 */
	@Test
	public void quantityIsBlank() {
		itemDetailForm.setQuantity("");
		validator.validate(itemDetailForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("quantity"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("数量を選択してください"));
	}
	
	/**
	 * quantityが半角スペースのみ
	 */
	@Test
	public void quantityIsOnlySpace() {
		itemDetailForm.setQuantity(" ");
		validator.validate(itemDetailForm, bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("quantity"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("数量を選択してください"));
	}

}
