package com.example.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.nullable;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.example.domain.Item;
import com.example.service.IndexService;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ModelAndView;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexControllerTest {

	public static final Operation INSERT = Operations.insertInto("items")
			.columns("id", "name", "description", "price_m", "price_l", "image_path", "categoryid")
			.values("1", "Gorgeous4サンド", "人気の定番具材「ハム」と「チキン」をセットにした食べごたえ抜群のサンドイッチです。", "480", "700", "1.jpg", "2")
			.values("2", "エスプレッソフラペチーノ",
					"ひと口目に感じるエスプレッソは「リストレット」という方法で抽出した力強い香りと優しい苦味を、ふた口目は全体を混ぜて、こだわりのクリームから来るアフォガートのような味わいをお楽しみください。リフレッシュしたい時や、ほっとひと息つきたい時にも、何度でも飲みたくなるフラペチーノです。",
					"530", "650", "2.jpg", "1")
			.values("3", "Specialキャラメルドーナッツ",
					"ドーナツ生地の中に、なめらかで風味豊かなキャラメルフィリングを入れ、仕上げにキャラメルのビター感と香ばしさが楽しめるキャラメルコーティングをかけました。", "250", "500",
					"3.jpg", "2")
			.values("4", "チョコクッキー",
					"ソフトな食感のクッキー生地には、小麦の香ばしさが感じられるよう全粒粉を入れ、砂糖の一部にはブラウンシュガーを使い、コクのある甘さをプラスしています。風味豊かなスターバックスオリジナルのチョコレートチャンクがごろごろ入っていて、どこを食べてもチョコレートの味わいを存分に楽しめます。ショートサイズのマグカップの上に乗せられるくらいのサイズは、コーヒーと一緒に楽しむのにもぴったりです。",
					"190", "300", "4.jpg", "2")
			.values("5", "カフェモカ", "エスプレッソにほろ苦いチョコレートシロップとミルクを加え、ホットビバレッジにはホイップクリームをトッピング。ちょっとした贅沢を楽しみたい方におすすめです。",
					"400", "520", "5.jpg", "1")
			.values("6", "カフェラテ",
					"最も人気のあるエスプレッソ ビバレッジ。リッチなエスプレッソにスチームミルクを注ぎ、フォームミルクを丁寧にトッピングしました。ミルキーな味わいで気持ちを温かくしてくれます。", "340",
					"460", "6.jpg", "1")
			.values("7", "カプチーノ", "リッチなエスプレッソに一気にミルクを注ぐことで、一口飲んだときからコーヒー感が感じられるビバレッジです。ベルベットのようにきめ細かいフォームミルクをお楽しみください。",
					"340", "460", "7.jpg", "1")
			.values("8", "キャラメルマキアート",
					"バニラシロップとスチームミルクのコンビネーションになめらかなフォームミルクをたっぷりのせ、その上からエスプレッソを注いでアクセントを付けました。仕上げにオリジナルキャラメルソースをトッピングしています。",
					"390", "510", "8.jpg", "1")
			.values("9", "キャラメルフラペチーノ",
					"コーヒー、ミルク、キャラメルシロップを氷とブレンドした、多くのお客様に愛されているフローズン ビバレッジです。トッピングしたホイップクリームとキャラメルソースと混ぜながら、豊かでほんのり香ばしいキャラメルの風味をお楽しみください。",
					"490", "570", "9.jpg", "1")
			.values("10", "バニラ クリーム フラペチーノ",
					"ミルクとバニラシロップを氷とブレンドし、ホイップクリームをトッピングした、クリーミーな味わいのフローズン ビバレッジ。真っ白な見た目も爽やかです。ミルクとバニラシロップというシンプルな組み合わせはいろいろなカスタマイズとも好相性。",
					"490", "570", "10.jpg", "1")
			.values("11", "ダークモカフラペチーノ",
					"コーヒー、ミルク、ダークチョコレートパウダー、そして人気のチョコレートチップを氷とブレンドした、チョコレートラバーズに人気のフローズンビバレッジ。コーヒーとダークチョコレートのほろ苦い味わいと、チョコレートチップの食感が織り成すハーモニーは、ブラックコーヒーファンにも支持されています。",
					"500", "580", "11.jpg", "1")
			.values("12", "抹茶クリームフラペチーノ", "世界中で様々な形で飲用されている抹茶ですが、スターバックスではミルクと甘みを加えて氷でブレンドしたリフレッシングなフラペチーノに仕上げました。",
					"490", "570", "12.jpg", "1")
			.values("13", "ドリップコーヒー",
					"世界中のコーヒー産地から厳選された高品質のアラビカ種コーヒー豆を使用したスターバックスの定番商品です。バラエティあふれるコーヒー豆を通して、スターバックスのコーヒージャーニーをお楽しみください。異なるローストレベルのコーヒーを日替わりでご用意していますので、お気に入りの1杯を見つけてみませんか。",
					"290", "410", "13.jpg", "1")
			.values("14", "アイスコーヒー",
					"熱を加えずに14時間かけてゆっくりと水で抽出したコールドブリュー コーヒー。香り高い風味が引き出されるよう、特別にブレンド、ローストしたコーヒー豆を使用しています。豊かな味わいとなめらかな口あたりをお楽しみください。",
					"330", "450", "14.jpg", "1")
			.values("15", "アメリカン", "エスプレッソに熱いお湯を注いだ、すっきりとしたのどごしのコーヒーです。ドリップ コーヒーのお好きな方にもおすすめです。", "310", "430",
					"15.jpg", "1")
			.values("16", "エスプレッソ",
					"キャラメルのような甘く力強い味とナッツを感じさせる後味。スターバックスのすべてのエスプレッソ ビバレッジに使われているエスプレッソです。どうぞ、お早めにお召し上がりください。", "310",
					"350", "16.jpg", "1")
			.values("17", "ナッティホワイトモカ",
					"ホワイトチョコレートとヘーゼルナッツに香り高いエスプレッソを加えた風味豊かなホワイト モカ。ホイップクリームをツリーに見立て、ナッツ&ホワイトチョコレートソースのリボンと、3色のチョコレート、シルバーのアラザンをイルミネーションのようにちりばめました。ホリデーシーズンにぴったりのあたたかな一杯で、特別なひと時をお楽しみください。",
					"450", "570", "17.jpg", "1")
			.values("18", "ジンジャーブレッドラテ",
					"スターバックスのホリデーといえばやっぱりジンジャーブレッド ラテ、という人も多いのではないでしょうか。ジンジャーブレッドクッキーをイメージした、ほんのり甘くてスパイシーな風味は、この時期にしか味わえない特別なビバレッジです。体の中からじんわりとあたためてくれる一杯で、ほっとリラックスしたひと時をお過ごしください。",
					"450", "570", "18.jpg", "1")
			.build();
	public static final Operation DELETE_ALL = Operations.deleteAllFrom("items");

	@Autowired
	IndexController indexController;
	@Autowired
	IndexService indexService;

	private MockMvc mockMvc;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Autowired
	private DataSource dataSource;

	@Before
	public void setUp() throws Exception {
		Destination dest = new DataSourceDestination(dataSource);
		Operation ops = Operations.sequenceOf(DELETE_ALL, INSERT);
		DbSetup dbSetup = new DbSetup(dest, ops);
		dbSetup.launch();
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void index初期表示() throws Exception {
		Item item = new Item();
		item.setName("チョコクッキー");
		Map<String, String> selectMap = new LinkedHashMap<>();
		selectMap.put("価格安い順（Mサイズ）", "price_m");
		selectMap.put("価格安い順（Lサイズ）", "price_l");
		selectMap.put("価格高い順（Mサイズ）", "price_m DESC");
		selectMap.put("価格高い順（Lサイズ）", "price_l DESC");
		String listType = "price_m";
		List<Item> itemList = indexService.findAll(listType);
		Integer page = 1;
		Page<Item> itemPage = indexService.showListPaging(page, 6, itemList);
		// calcPageNumbersメソッド
		int totalPages = itemPage.getTotalPages();
		List<Integer> pageNumbers = null;
		if (totalPages > 0) {
			pageNumbers = new ArrayList<>();
			for (int i = 1; i <= totalPages; i++) {
				pageNumbers.add(i);
			}
		}
		//
		List<Item> autocompleteList = indexService.findAll("price_m");
		StringBuilder itemListForAutocomplete = indexService.getItemListForAutocomplete(autocompleteList);
		mockMvc.perform(get("/item-list")).andExpect(status().isOk()).andExpect(view().name("item/item_list"))
				.andExpect(model().attribute("selectMap", selectMap))
				.andExpect(model().attribute("pageNumbers", pageNumbers))
				.andExpect(model().attribute("name", nullable(String.class)))
				.andExpect(model().attribute("listType", "price_m"));

		MvcResult mvcResult = mockMvc.perform(get("/item-list")).andExpect(model().attributeExists("itemPage"))
				.andExpect(model().attributeExists("itemListForAutocomplete")).andReturn();
		Page<Item> actualPageItems = ((Page<Item>) mvcResult.getModelAndView().getModel().get("itemPage"));
		assertEquals(item.getName(), actualPageItems.getContent().get(0).getName());
		char firstChar = 'チ';
		assertEquals(firstChar, itemListForAutocomplete.charAt(1));

	}

}