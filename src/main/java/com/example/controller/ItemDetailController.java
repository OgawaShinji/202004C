package com.example.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.Item;
import com.example.domain.Topping;
import com.example.form.ItemDetailForm;
import com.example.service.IndexService;
import com.example.service.ItemDetailService;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassResult;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImage;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifierResult;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;

@Controller
@RequestMapping("/item-detail")
public class ItemDetailController {

	/**
	 * @return ItemDetailForm
	 */
	@ModelAttribute
	public ItemDetailForm setUpForm() {
		return new ItemDetailForm();
	}

	@Autowired
	private ItemDetailService itemDetailService;
	@Autowired
	private IndexService indexService;

	/**
	 * アイテムの詳細画面を表示する処理
	 * 
	 * @param model
	 * @param itemId
	 * @return String
	 */
	@RequestMapping("/showDetail")
	public String index(Model model, String itemId) {
		Item item = itemDetailService.load(itemId);
		if (Objects.isNull(item)) {
			return "forward:/item-list";
		}
		List<Topping> toppingList = itemDetailService.showAll();

		Map<Integer, Topping> toppingMap = new HashMap<Integer, Topping>();

		// toppingListには(toppingTableのid，toppingオブジェクト)を詰める
		for (int i = 0; i < toppingList.size(); i++) {
			Topping topping = toppingList.get(i);
			toppingMap.put(topping.getId(), topping);
		}

		model.addAttribute("item", item);
		model.addAttribute("toppingMap", toppingMap);
		return "item/item_detail";
	}

	@RequestMapping("/visualrecognition")
	public String serchByImage(Model model, RedirectAttributes redirectAttributes, MultipartFile imgFile) {

		IamOptions options = new IamOptions.Builder().apiKey("YnFvyX5Qxuzt_myekiJJK2ahpEPb9XCeVaijLJ7uO8St").build();
		VisualRecognition service = new VisualRecognition("2018-03-19", options);
		try {
			InputStream imagesStream = imgFile.getInputStream();
			ClassifyOptions classifyOptions = new ClassifyOptions.Builder().imagesFile(imagesStream)
					.imagesFilename(imgFile.getName()).threshold((float) 0.6)
					.classifierIds(Arrays.asList("DrinkCustomModel_2095645901")).build();

			ClassifiedImages result = service.classify(classifyOptions).execute();
			// System.out.println(result);
			List<ClassifiedImage> images = result.getImages();
			for (ClassifiedImage image : images) {
				List<ClassifierResult> cr = image.getClassifiers();
				for (ClassifierResult r : cr) {
					// System.err.println(r.getName());
					List<ClassResult> cc = r.getClasses();
					for (ClassResult c : cc) {
						// System.err.println("ClassResult : " + c);
						System.err.println("ClassName : " + c.getClassName());
						System.err.println("Score : " + c.getScore());
						System.err.println("TypeHierarchy : " + c.getTypeHierarchy());
						System.err.println("---");
						if (c.getScore() <= 0.4 || Objects.isNull(c.getScore())) {
							redirectAttributes.addFlashAttribute("findByImageMessage", "検索された画像に該当する商品は見つかりませんでした");
							return "redirect:/item-list";
						}

						String drinkName = c.getClassName().replace(".zip", "");
						if (c.getClassName().equals("cal.zip")) {
							drinkName = "カルピスウォーター";
						}
						String listType = "arrival_date desc,categoryid";
						List<Item> itemListBysearchName = null;
						itemListBysearchName = indexService.findByLikeName(drinkName, listType);
						return index(model, itemListBysearchName.get(0).getId().toString());

					}
				}
			}

			return "";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("findByImageMessage", "検索された画像に該当する商品は見つかりませんでした");
			return "redirect:/item-list";
		} catch (IOException e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("findByImageMessage", "検索された画像に該当する商品は見つかりませんでした");
			return "redirect:/item-list";
		}
	}

}
