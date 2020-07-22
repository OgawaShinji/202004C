package com.example.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.example.domain.Item;
import com.example.domain.Topping;
import com.example.service.ItemDetailService;
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
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemDetailControllerTest {

	public static final Operation DELETE_ALL = Operations.deleteAllFrom("items");

	public static final Operation INSERT = Operations.insertInto("items")
                                                .columns("id","name","description","price_m","price_l","image_path","categoryid")
                                                .values("1", "Gorgeous4サンド", "人気の定番具材「ハム」と「チキン」をセットにした食べごたえ抜群のサンドイッチです。", "480", "700", "1.jpg","2")
                                                .values("2", "エスプレッソフラペチーノ", "ひと口目に感じるエスプレッソは「リストレット」という方法で抽出した力強い香りと優しい苦味を、ふた口目は全体を混ぜて、こだわりのクリームから来るアフォガートのような味わいをお楽しみください。リフレッシュしたい時や、ほっとひと息つきたい時にも、何度でも飲みたくなるフラペチーノです。", "530", "650", "2.jpg","1")
                                                .values("3", "Specialキャラメルドーナッツ", "ドーナツ生地の中に、なめらかで風味豊かなキャラメルフィリングを入れ、仕上げにキャラメルのビター感と香ばしさが楽しめるキャラメルコーティングをかけました。", "250", "500", "3.jpg","2")
                                                .values("4", "チョコクッキー", "ソフトな食感のクッキー生地には、小麦の香ばしさが感じられるよう全粒粉を入れ、砂糖の一部にはブラウンシュガーを使い、コクのある甘さをプラスしています。風味豊かなスターバックスオリジナルのチョコレートチャンクがごろごろ入っていて、どこを食べてもチョコレートの味わいを存分に楽しめます。ショートサイズのマグカップの上に乗せられるくらいのサイズは、コーヒーと一緒に楽しむのにもぴったりです。", "190", "300", "4.jpg","2")
                                                .values("5", "カフェモカ", "エスプレッソにほろ苦いチョコレートシロップとミルクを加え、ホットビバレッジにはホイップクリームをトッピング。ちょっとした贅沢を楽しみたい方におすすめです。", "400", "520", "5.jpg","1")
                                                .values("6", "カフェラテ", "最も人気のあるエスプレッソ ビバレッジ。リッチなエスプレッソにスチームミルクを注ぎ、フォームミルクを丁寧にトッピングしました。ミルキーな味わいで気持ちを温かくしてくれます。", "340", "460", "6.jpg","1")
                                                .values("7", "カプチーノ", "リッチなエスプレッソに一気にミルクを注ぐことで、一口飲んだときからコーヒー感が感じられるビバレッジです。ベルベットのようにきめ細かいフォームミルクをお楽しみください。", "340", "460", "7.jpg","1")
                                                .values("8", "キャラメルマキアート", "バニラシロップとスチームミルクのコンビネーションになめらかなフォームミルクをたっぷりのせ、その上からエスプレッソを注いでアクセントを付けました。仕上げにオリジナルキャラメルソースをトッピングしています。", "390", "510", "8.jpg","1")
                                                .values("9", "キャラメルフラペチーノ", "コーヒー、ミルク、キャラメルシロップを氷とブレンドした、多くのお客様に愛されているフローズン ビバレッジです。トッピングしたホイップクリームとキャラメルソースと混ぜながら、豊かでほんのり香ばしいキャラメルの風味をお楽しみください。", "490", "570", "9.jpg","1")
                                                .values("10", "バニラ クリーム フラペチーノ", "ミルクとバニラシロップを氷とブレンドし、ホイップクリームをトッピングした、クリーミーな味わいのフローズン ビバレッジ。真っ白な見た目も爽やかです。ミルクとバニラシロップというシンプルな組み合わせはいろいろなカスタマイズとも好相性。", "490", "570", "10.jpg","1")
                                                .values("11", "ダークモカフラペチーノ", "コーヒー、ミルク、ダークチョコレートパウダー、そして人気のチョコレートチップを氷とブレンドした、チョコレートラバーズに人気のフローズンビバレッジ。コーヒーとダークチョコレートのほろ苦い味わいと、チョコレートチップの食感が織り成すハーモニーは、ブラックコーヒーファンにも支持されています。", "500", "580", "11.jpg","1")
                                                .values("12", "抹茶クリームフラペチーノ", "世界中で様々な形で飲用されている抹茶ですが、スターバックスではミルクと甘みを加えて氷でブレンドしたリフレッシングなフラペチーノに仕上げました。", "490", "570", "12.jpg","1")
                                                .values("13", "ドリップコーヒー", "世界中のコーヒー産地から厳選された高品質のアラビカ種コーヒー豆を使用したスターバックスの定番商品です。バラエティあふれるコーヒー豆を通して、スターバックスのコーヒージャーニーをお楽しみください。異なるローストレベルのコーヒーを日替わりでご用意していますので、お気に入りの1杯を見つけてみませんか。", "290", "410", "13.jpg","1")
                                                .values("14", "アイスコーヒー", "熱を加えずに14時間かけてゆっくりと水で抽出したコールドブリュー コーヒー。香り高い風味が引き出されるよう、特別にブレンド、ローストしたコーヒー豆を使用しています。豊かな味わいとなめらかな口あたりをお楽しみください。", "330", "450", "14.jpg","1")
                                                .values("15", "アメリカン", "エスプレッソに熱いお湯を注いだ、すっきりとしたのどごしのコーヒーです。ドリップ コーヒーのお好きな方にもおすすめです。", "310", "430", "15.jpg","1")
                                                .values("16", "エスプレッソ", "キャラメルのような甘く力強い味とナッツを感じさせる後味。スターバックスのすべてのエスプレッソ ビバレッジに使われているエスプレッソです。どうぞ、お早めにお召し上がりください。", "310", "350", "16.jpg","1")
                                                .values("17", "ナッティホワイトモカ", "ホワイトチョコレートとヘーゼルナッツに香り高いエスプレッソを加えた風味豊かなホワイト モカ。ホイップクリームをツリーに見立て、ナッツ&ホワイトチョコレートソースのリボンと、3色のチョコレート、シルバーのアラザンをイルミネーションのようにちりばめました。ホリデーシーズンにぴったりのあたたかな一杯で、特別なひと時をお楽しみください。", "450", "570", "17.jpg","1")
                                                .values("18", "ジンジャーブレッドラテ", "スターバックスのホリデーといえばやっぱりジンジャーブレッド ラテ、という人も多いのではないでしょうか。ジンジャーブレッドクッキーをイメージした、ほんのり甘くてスパイシーな風味は、この時期にしか味わえない特別なビバレッジです。体の中からじんわりとあたためてくれる一杯で、ほっとリラックスしたひと時をお過ごしください。", "450", "570", "18.jpg","1")
                                                .build();



	@Autowired
	private ItemDetailController itemDetailController;
	@Autowired
	private ItemDetailService itemDetailService;

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
		this.mockMvc = MockMvcBuilders.standaloneSetup(itemDetailController).build();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void index初期表示() throws Exception {
		String itemId ="4";
		Item item=itemDetailService.load(itemId);
		List<Topping> toppingList = itemDetailService.showAll();

		Map<Integer, Topping> toppingMap = new HashMap<Integer, Topping>();

        for (int i = 0; i < toppingList.size(); i++) {
            Topping topping = toppingList.get(i);
            toppingMap.put(topping.getId(), topping);
		}
		// 
		mockMvc.perform(get("/item-detail/showDetail?itemId=4")).andExpect(status().isOk()).andExpect(view().name("item/item_detail"));
		
		MvcResult mvcResult=mockMvc.perform(get("/item-detail/showDetail?itemId=4")).andExpect(status().isOk()).andExpect(view().name("item/item_detail"))
				.andExpect(model().attributeExists("item"))
				.andExpect(model().attributeExists("toppingMap"))
				.andReturn();

		Item actual= (Item) mvcResult.getModelAndView().getModel().get("item");
		assertEquals("error", actual.getId(),item.getId());
		assertEquals("error", actual.getName(),item.getName());
		assertEquals("error", actual.getDescription(),item.getDescription());
		assertEquals("error", actual.getPriceM(),item.getPriceM());
		assertEquals("error", actual.getPriceL(),item.getPriceL());
		assertEquals("error", actual.getImagePass(),item.getImagePass());
		assertEquals("error", actual.getCategoryId(),item.getCategoryId());

		Map<Integer,Topping> actualToppingMap= (Map<Integer, Topping>) mvcResult.getModelAndView().getModel().get("toppingMap");
		Topping expected=actualToppingMap.get(13);
		assertEquals("error",expected.getId() ,toppingList.get(0).getId());
		assertEquals("error",expected.getName() ,toppingList.get(0).getName());
		assertEquals("error",expected.getPriceM() ,toppingList.get(0).getPriceM());
		assertEquals("error",expected.getPriceL() ,toppingList.get(0).getPriceL());

	}

}