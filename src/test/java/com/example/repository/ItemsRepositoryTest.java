package com.example.repository;

import static org.junit.Assert.*;

import java.util.List;

import javax.sql.DataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.domain.Item;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemsRepositoryTest {
	
	public static final Operation DELETE_ALL =
	        Operations.deleteAllFrom("items");
	
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
	private ItemsRepository itemsRepository;
	
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
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * FindAllメソッドのテスト
	 */
	@Test
	public void サイズMの価格が安い順の並びが0番目() {
		Item item = itemsRepository.findAll("price_m").get(0);
		Integer id = 4;
		Integer priceM=190;
		Integer priceL = 300;
		Integer categoryId = 2;
//		Boolean deleted = false;
		
		assertEquals("idが違います", id, item.getId());
		assertEquals("商品名が違います", "チョコクッキー", item.getName());
		assertEquals("解説が違います", "ソフトな食感のクッキー生地には、小麦の香ばしさが感じられるよう全粒粉を入れ、砂糖の一部にはブラウンシュガーを使い、コクのある甘さをプラスしています。風味豊かなスターバックスオリジナルのチョコレートチャンクがごろごろ入っていて、どこを食べてもチョコレートの味わいを存分に楽しめます。ショートサイズのマグカップの上に乗せられるくらいのサイズは、コーヒーと一緒に楽しむのにもぴったりです。",item.getDescription() );
		assertEquals("Mの価格が違います", priceM, item.getPriceM());
		assertEquals("Lの価格が違います", priceL, item.getPriceL());
		assertEquals("写真が一致しません", "4.jpg", item.getImagePass());
		assertEquals("カテゴリーIDが違います", categoryId, item.getCategoryId());
//		assertEquals("deletedが一致しません", item.getDeleted(), deleted);
	}
	@Test
	public void サイズMの価格が安い順の並びが1番目() {
		Item item = itemsRepository.findAll("price_m").get(1);
		Integer id = 3;
		Integer priceM=250;
		Integer priceL = 500;
		Integer categoryId = 2;
		Boolean deleted = false;
		assertEquals("idが違います", item.getId(), id);
		assertEquals("商品名が違います", item.getName(), "Specialキャラメルドーナッツ");
		assertEquals("解説が違います", item.getDescription(), "ドーナツ生地の中に、なめらかで風味豊かなキャラメルフィリングを入れ、仕上げにキャラメルのビター感と香ばしさが楽しめるキャラメルコーティングをかけました。");
		assertEquals("Mの価格が違います", item.getPriceM(), priceM);
		assertEquals("Lの価格が違います", item.getPriceL(), priceL);
		assertEquals("写真が一致しません", item.getImagePass(), "3.jpg");
		assertEquals("カテゴリーIDが違います", item.getCategoryId(), categoryId);
//		assertEquals("deletedが一致しません", item.getDeleted(), deleted);
	}
	
	@Test
	public void サイズMの価格が高い順の並びが0番目() {
		Item item = itemsRepository.findAll("price_m DESC").get(0);
		Integer id = 2;
		Integer priceM=530;
		Integer priceL = 650;
		Integer categoryId = 1;
//		Boolean deleted = false;
		
		assertEquals("idが違います", id, item.getId());
		assertEquals("商品名が違います", "エスプレッソフラペチーノ", item.getName());
		assertEquals("解説が違います", "ひと口目に感じるエスプレッソは「リストレット」という方法で抽出した力強い香りと優しい苦味を、ふた口目は全体を混ぜて、こだわりのクリームから来るアフォガートのような味わいをお楽しみください。リフレッシュしたい時や、ほっとひと息つきたい時にも、何度でも飲みたくなるフラペチーノです。", item.getDescription());
		assertEquals("Mの価格が違います", priceM, item.getPriceM());
		assertEquals("Lの価格が違います", priceL,item.getPriceL() );
		assertEquals("写真が一致しません", "2.jpg", item.getImagePass());
		assertEquals("カテゴリーIDが違います", categoryId, item.getCategoryId());
//		assertEquals("deletedが一致しません", deleted, item.getDeleted());
	}
	@Test
	public void サイズMの価格が高い順の並びが1番目() {
		Item item = itemsRepository.findAll("price_m DESC").get(1);
		Integer id = 11;
		Integer priceM=500;
		Integer priceL = 580;
		Integer categoryId = 1;
//		Boolean deleted = false;
		
		assertEquals("idが違います", id, item.getId());
		assertEquals("商品名が違います", "ダークモカフラペチーノ", item.getName());
		assertEquals("解説が違います", "コーヒー、ミルク、ダークチョコレートパウダー、そして人気のチョコレートチップを氷とブレンドした、チョコレートラバーズに人気のフローズンビバレッジ。コーヒーとダークチョコレートのほろ苦い味わいと、チョコレートチップの食感が織り成すハーモニーは、ブラックコーヒーファンにも支持されています。", item.getDescription());
		assertEquals("Mの価格が違います", priceM, item.getPriceM());
		assertEquals("Lの価格が違います", priceL,item.getPriceL() );
		assertEquals("写真が一致しません", "11.jpg", item.getImagePass());
		assertEquals("カテゴリーIDが違います", categoryId, item.getCategoryId());
//		assertEquals("deletedが一致しません", deleted, item.getDeleted());
	}
	
	@Test
	public void サイズLの価格が安い順の並びが0番目() {

		Item item = itemsRepository.findAll("price_l").get(0);
		Integer id = 4;
		Integer priceM=190;
		Integer priceL = 300;
		Integer categoryId = 2;
		Boolean deleted = false;
		assertEquals("idが違います", item.getId(), id);
		assertEquals("商品名が違います", item.getName(), "チョコクッキー");
		assertEquals("解説が違います", item.getDescription(), "ソフトな食感のクッキー生地には、小麦の香ばしさが感じられるよう全粒粉を入れ、砂糖の一部にはブラウンシュガーを使い、コクのある甘さをプラスしています。風味豊かなスターバックスオリジナルのチョコレートチャンクがごろごろ入っていて、どこを食べてもチョコレートの味わいを存分に楽しめます。ショートサイズのマグカップの上に乗せられるくらいのサイズは、コーヒーと一緒に楽しむのにもぴったりです。");
		assertEquals("Mの価格が違います", item.getPriceM(), priceM);
		assertEquals("Lの価格が違います", item.getPriceL(), priceL);
		assertEquals("写真が一致しません", item.getImagePass(), "4.jpg");
		assertEquals("カテゴリーIDが違います", item.getCategoryId(), categoryId);
//		assertEquals("deletedが一致しません", item.getDeleted(), deleted);
	}
	@Test
	public void サイズLの価格が安い順の並びが1番目() {
		Item item = itemsRepository.findAll("price_l").get(1);
		Integer id = 16;
		Integer priceM=310;
		Integer priceL = 350;
		Integer categoryId = 1;
		Boolean deleted = false;
		assertEquals("idが違います", item.getId(), id);
		assertEquals("商品名が違います", item.getName(), "エスプレッソ");
		assertEquals("解説が違います", item.getDescription(), "キャラメルのような甘く力強い味とナッツを感じさせる後味。スターバックスのすべてのエスプレッソ ビバレッジに使われているエスプレッソです。どうぞ、お早めにお召し上がりください。");
		assertEquals("Mの価格が違います", item.getPriceM(), priceM);
		assertEquals("Lの価格が違います", item.getPriceL(), priceL);
		assertEquals("写真が一致しません", item.getImagePass(), "16.jpg");
		assertEquals("カテゴリーIDが違います", item.getCategoryId(), categoryId);
//		assertEquals("deletedが一致しません", item.getDeleted(), deleted);
	}

	@Test
	public void サイズLの価格が高い順の並びが0番目() {
		Item item = itemsRepository.findAll("price_l DESC").get(0);
		Integer id = 1;
		Integer priceM=480;
		Integer priceL = 700;
		Integer categoryId = 2;
//		Boolean deleted = false;
		assertEquals("idが違います", item.getId(), id);
		assertEquals("商品名が違います", item.getName(), "Gorgeous4サンド");
		assertEquals("解説が違います", item.getDescription(), "人気の定番具材「ハム」と「チキン」をセットにした食べごたえ抜群のサンドイッチです。");
		assertEquals("Mの価格が違います", item.getPriceM(), priceM);
		assertEquals("Lの価格が違います", item.getPriceL(), priceL);
		assertEquals("写真が一致しません", item.getImagePass(), "1.jpg");
		assertEquals("カテゴリーIDが違います", item.getCategoryId(), categoryId);
//		assertEquals("deletedが一致しません", item.getDeleted(), deleted);
	}
	@Test
	public void サイズLの価格が高い順の並びが1番目() {
		Item item = itemsRepository.findAll("price_l DESC").get(1);
		Integer id = 2;
		Integer priceM=530;
		Integer priceL = 650;
		Integer categoryId = 1;
//		Boolean deleted = false;
		
		assertEquals("idが違います", id, item.getId());
		assertEquals("商品名が違います", "エスプレッソフラペチーノ", item.getName());
		assertEquals("解説が違います", "ひと口目に感じるエスプレッソは「リストレット」という方法で抽出した力強い香りと優しい苦味を、ふた口目は全体を混ぜて、こだわりのクリームから来るアフォガートのような味わいをお楽しみください。リフレッシュしたい時や、ほっとひと息つきたい時にも、何度でも飲みたくなるフラペチーノです。", item.getDescription());
		assertEquals("Mの価格が違います", priceM, item.getPriceM());
		assertEquals("Lの価格が違います", priceL,item.getPriceL() );
		assertEquals("写真が一致しません", "2.jpg", item.getImagePass());
		assertEquals("カテゴリーIDが違います", categoryId, item.getCategoryId());
//		assertEquals("deletedが一致しません", deleted, item.getDeleted());
	}

	/**
	 * loadメソッドのテスト
	 */
	@Test
	public void idが1の時のテスト() {
		Item item = itemsRepository.load(1);
		Integer id = 1;
		Integer priceM=480;
		Integer priceL = 700;
		Integer categoryId = 2;
//		Boolean deleted = false;
		assertEquals("idが違います", item.getId(), id);
		assertEquals("商品名が違います", item.getName(), "Gorgeous4サンド");
		assertEquals("解説が違います", item.getDescription(), "人気の定番具材「ハム」と「チキン」をセットにした食べごたえ抜群のサンドイッチです。");
		assertEquals("Mの価格が違います", item.getPriceM(), priceM);
		assertEquals("Lの価格が違います", item.getPriceL(), priceL);
		assertEquals("写真が一致しません", item.getImagePass(), "1.jpg");
		assertEquals("カテゴリーIDが違います", item.getCategoryId(), categoryId);
//		assertEquals("deletedが一致しません", item.getDeleted(), deleted);
	}
	@Test
	public void idが2の時のテスト() {
		Item item = itemsRepository.load(2);
		Integer id = 2;
		Integer priceM=530;
		Integer priceL = 650;
		Integer categoryId = 1;
//		Boolean deleted = false;
		assertEquals("idが違います", item.getId(), id);
		assertEquals("商品名が違います", item.getName(), "エスプレッソフラペチーノ");
		assertEquals("解説が違います", item.getDescription(), "ひと口目に感じるエスプレッソは「リストレット」という方法で抽出した力強い香りと優しい苦味を、ふた口目は全体を混ぜて、こだわりのクリームから来るアフォガートのような味わいをお楽しみください。リフレッシュしたい時や、ほっとひと息つきたい時にも、何度でも飲みたくなるフラペチーノです。");
		assertEquals("Mの価格が違います", item.getPriceM(), priceM);
		assertEquals("Lの価格が違います", item.getPriceL(), priceL);
		assertEquals("写真が一致しません", item.getImagePass(), "2.jpg");
		assertEquals("カテゴリーIDが違います", item.getCategoryId(), categoryId);
//		assertEquals("deletedが一致しません", item.getDeleted(), deleted);
	}
	@Test
	public void loadに0が入った時() {
		Item item = itemsRepository.load(0);
		assertEquals("nullではありません", null, item);
	}
	
	@Test
	public void 曖昧検索にコーヒーと入力し並び替えサイズMの安い順の時の0番目() {
		List<Item> itemList = itemsRepository.findByLikeName("コーヒー", "price_m");
		Integer id = 13;
		Integer priceM=290;
		Integer priceL = 410;
		Integer categoryId = 1;
//		Boolean deleted = false;
		assertEquals("idが違います", id,itemList.get(0).getId());
		assertEquals("商品名が違います", "ドリップコーヒー",  itemList.get(0).getName());
		assertEquals("解説が違います", "世界中のコーヒー産地から厳選された高品質のアラビカ種コーヒー豆を使用したスターバックスの定番商品です。バラエティあふれるコーヒー豆を通して、スターバックスのコーヒージャーニーをお楽しみください。異なるローストレベルのコーヒーを日替わりでご用意していますので、お気に入りの1杯を見つけてみませんか。", itemList.get(0).getDescription());
		assertEquals("Mの価格が違います", priceM, itemList.get(0).getPriceM());
		assertEquals("Lの価格が違います", priceL, itemList.get(0).getPriceL());
		assertEquals("写真が一致しません", "13.jpg", itemList.get(0).getImagePass());
		assertEquals("カテゴリーIDが違います", categoryId, itemList.get(0).getCategoryId());
//		assertEquals("deletedが一致しません", item.getDeleted(), deleted);
	}
	@Test
	public void 曖昧検索にコーヒーと入力し並び替えサイズMの安い順の時の1番目() {
		List<Item> itemList = itemsRepository.findByLikeName("コーヒー", "price_m");
		Integer id = 14;
		Integer priceM=330;
		Integer priceL = 450;
		Integer categoryId = 1;
//		Boolean deleted = false;
		assertEquals("idが違います", id,itemList.get(1).getId());
		assertEquals("商品名が違います", "アイスコーヒー",  itemList.get(1).getName());
		assertEquals("解説が違います", "熱を加えずに14時間かけてゆっくりと水で抽出したコールドブリュー コーヒー。香り高い風味が引き出されるよう、特別にブレンド、ローストしたコーヒー豆を使用しています。豊かな味わいとなめらかな口あたりをお楽しみください。", itemList.get(1).getDescription());
		assertEquals("Mの価格が違います", priceM, itemList.get(1).getPriceM());
		assertEquals("Lの価格が違います", priceL, itemList.get(1).getPriceL());
		assertEquals("写真が一致しません", "14.jpg", itemList.get(1).getImagePass());
		assertEquals("カテゴリーIDが違います", categoryId, itemList.get(1).getCategoryId());
//		assertEquals("deletedが一致しません", item.getDeleted(), deleted);
	}
	
	@Test
	public void 曖昧検索にコーヒーと入力し並び替えサイズMの高い順の時の0番目() {
		List<Item> itemList = itemsRepository.findByLikeName("コーヒー", "price_m DESC");
		Integer id = 14;
		Integer priceM=330;
		Integer priceL = 450;
		Integer categoryId = 1;
//		Boolean deleted = false;
		assertEquals("idが違います", id,itemList.get(0).getId());
		assertEquals("商品名が違います", "アイスコーヒー",  itemList.get(0).getName());
		assertEquals("解説が違います", "熱を加えずに14時間かけてゆっくりと水で抽出したコールドブリュー コーヒー。香り高い風味が引き出されるよう、特別にブレンド、ローストしたコーヒー豆を使用しています。豊かな味わいとなめらかな口あたりをお楽しみください。", itemList.get(0).getDescription());
		assertEquals("Mの価格が違います", priceM, itemList.get(0).getPriceM());
		assertEquals("Lの価格が違います", priceL, itemList.get(0).getPriceL());
		assertEquals("写真が一致しません", "14.jpg", itemList.get(0).getImagePass());
		assertEquals("カテゴリーIDが違います", categoryId, itemList.get(0).getCategoryId());
//		assertEquals("deletedが一致しません", item.getDeleted(), deleted);
	}
	@Test
	public void 曖昧検索にコーヒーと入力し並び替えサイズMの高い順の時の1番目() {
		List<Item> itemList = itemsRepository.findByLikeName("コーヒー", "price_m DESC");
		Integer id = 13;
		Integer priceM=290;
		Integer priceL = 410;
		Integer categoryId = 1;
//		Boolean deleted = false;
		assertEquals("idが違います", id,itemList.get(1).getId());
		assertEquals("商品名が違います", "ドリップコーヒー",  itemList.get(1).getName());
		assertEquals("解説が違います", "世界中のコーヒー産地から厳選された高品質のアラビカ種コーヒー豆を使用したスターバックスの定番商品です。バラエティあふれるコーヒー豆を通して、スターバックスのコーヒージャーニーをお楽しみください。異なるローストレベルのコーヒーを日替わりでご用意していますので、お気に入りの1杯を見つけてみませんか。", itemList.get(1).getDescription());
		assertEquals("Mの価格が違います", priceM, itemList.get(1).getPriceM());
		assertEquals("Lの価格が違います", priceL, itemList.get(1).getPriceL());
		assertEquals("写真が一致しません", "13.jpg", itemList.get(1).getImagePass());
		assertEquals("カテゴリーIDが違います", categoryId, itemList.get(1).getCategoryId());
//		assertEquals("deletedが一致しません", item.getDeleted(), deleted);
	}
	
	@Test
	public void 曖昧検索にコーヒーと入力し並び替えサイズLの安い順の時の0番目() {
		List<Item> itemList = itemsRepository.findByLikeName("コーヒー", "price_l");
		Integer id = 13;
		Integer priceM=290;
		Integer priceL = 410;
		Integer categoryId = 1;
//		Boolean deleted = false;
		assertEquals("idが違います", id,itemList.get(0).getId());
		assertEquals("商品名が違います", "ドリップコーヒー",  itemList.get(0).getName());
		assertEquals("解説が違います", "世界中のコーヒー産地から厳選された高品質のアラビカ種コーヒー豆を使用したスターバックスの定番商品です。バラエティあふれるコーヒー豆を通して、スターバックスのコーヒージャーニーをお楽しみください。異なるローストレベルのコーヒーを日替わりでご用意していますので、お気に入りの1杯を見つけてみませんか。", itemList.get(0).getDescription());
		assertEquals("Mの価格が違います", priceM, itemList.get(0).getPriceM());
		assertEquals("Lの価格が違います", priceL, itemList.get(0).getPriceL());
		assertEquals("写真が一致しません", "13.jpg", itemList.get(0).getImagePass());
		assertEquals("カテゴリーIDが違います", categoryId, itemList.get(0).getCategoryId());
//		assertEquals("deletedが一致しません", item.getDeleted(), deleted);
	}
	
	@Test
	public void 曖昧検索にコーヒーと入力し並び替えサイズLの安い順の時の1番目() {
		List<Item> itemList = itemsRepository.findByLikeName("コーヒー", "price_l");
		Integer id = 14;
		Integer priceM=330;
		Integer priceL = 450;
		Integer categoryId = 1;
//		Boolean deleted = false;
		assertEquals("idが違います", id,itemList.get(1).getId());
		assertEquals("商品名が違います", "アイスコーヒー",  itemList.get(1).getName());
		assertEquals("解説が違います", "熱を加えずに14時間かけてゆっくりと水で抽出したコールドブリュー コーヒー。香り高い風味が引き出されるよう、特別にブレンド、ローストしたコーヒー豆を使用しています。豊かな味わいとなめらかな口あたりをお楽しみください。", itemList.get(1).getDescription());
		assertEquals("Mの価格が違います", priceM, itemList.get(1).getPriceM());
		assertEquals("Lの価格が違います", priceL, itemList.get(1).getPriceL());
		assertEquals("写真が一致しません", "14.jpg", itemList.get(1).getImagePass());
		assertEquals("カテゴリーIDが違います", categoryId, itemList.get(1).getCategoryId());
//		assertEquals("deletedが一致しません", item.getDeleted(), deleted);
	}
	
	@Test
	public void 曖昧検索にコーヒーと入力し並び替えサイズLの高い順の時の0番目() {
		List<Item> itemList = itemsRepository.findByLikeName("コーヒー", "price_m DESC");
		Integer id = 14;
		Integer priceM=330;
		Integer priceL = 450;
		Integer categoryId = 1;
//		Boolean deleted = false;
		assertEquals("idが違います", id,itemList.get(0).getId());
		assertEquals("商品名が違います", "アイスコーヒー",  itemList.get(0).getName());
		assertEquals("解説が違います", "熱を加えずに14時間かけてゆっくりと水で抽出したコールドブリュー コーヒー。香り高い風味が引き出されるよう、特別にブレンド、ローストしたコーヒー豆を使用しています。豊かな味わいとなめらかな口あたりをお楽しみください。", itemList.get(0).getDescription());
		assertEquals("Mの価格が違います", priceM, itemList.get(0).getPriceM());
		assertEquals("Lの価格が違います", priceL, itemList.get(0).getPriceL());
		assertEquals("写真が一致しません", "14.jpg", itemList.get(0).getImagePass());
		assertEquals("カテゴリーIDが違います", categoryId, itemList.get(0).getCategoryId());
//		assertEquals("deletedが一致しません", item.getDeleted(), deleted);
	}
	@Test
	public void 曖昧検索にコーヒーと入力し並び替えサイズLの高い順の時の1番目() {
		List<Item> itemList = itemsRepository.findByLikeName("コーヒー", "price_m DESC");
		Integer id = 13;
		Integer priceM=290;
		Integer priceL = 410;
		Integer categoryId = 1;
//		Boolean deleted = false;
		assertEquals("idが違います", id,itemList.get(1).getId());
		assertEquals("商品名が違います", "ドリップコーヒー",  itemList.get(1).getName());
		assertEquals("解説が違います", "世界中のコーヒー産地から厳選された高品質のアラビカ種コーヒー豆を使用したスターバックスの定番商品です。バラエティあふれるコーヒー豆を通して、スターバックスのコーヒージャーニーをお楽しみください。異なるローストレベルのコーヒーを日替わりでご用意していますので、お気に入りの1杯を見つけてみませんか。", itemList.get(1).getDescription());
		assertEquals("Mの価格が違います", priceM, itemList.get(1).getPriceM());
		assertEquals("Lの価格が違います", priceL, itemList.get(1).getPriceL());
		assertEquals("写真が一致しません", "13.jpg", itemList.get(1).getImagePass());
		assertEquals("カテゴリーIDが違います", categoryId, itemList.get(1).getCategoryId());
//		assertEquals("deletedが一致しません", item.getDeleted(), deleted);
	}
	
	@Test
	public void 曖昧検索を空欄にし並び替えサイズMの安い順の時の0番目() {
		List<Item> itemList = itemsRepository.findByLikeName("", "price_m");
		Integer id = 4;
		Integer priceM=190;
		Integer priceL = 300;
		Integer categoryId = 2;
//		Boolean deleted = false;
		
		assertEquals("idが違います", id, itemList.get(0).getId());
		assertEquals("商品名が違います", "チョコクッキー", itemList.get(0).getName());
		assertEquals("解説が違います", "ソフトな食感のクッキー生地には、小麦の香ばしさが感じられるよう全粒粉を入れ、砂糖の一部にはブラウンシュガーを使い、コクのある甘さをプラスしています。風味豊かなスターバックスオリジナルのチョコレートチャンクがごろごろ入っていて、どこを食べてもチョコレートの味わいを存分に楽しめます。ショートサイズのマグカップの上に乗せられるくらいのサイズは、コーヒーと一緒に楽しむのにもぴったりです。",itemList.get(0).getDescription() );
		assertEquals("Mの価格が違います", priceM, itemList.get(0).getPriceM());
		assertEquals("Lの価格が違います", priceL, itemList.get(0).getPriceL());
		assertEquals("写真が一致しません", "4.jpg", itemList.get(0).getImagePass());
		assertEquals("カテゴリーIDが違います", categoryId, itemList.get(0).getCategoryId());
//		assertEquals("deletedが一致しません", item.getDeleted(), deleted);
	}
	
	@Test
	public void 曖昧検索を空欄にし並び替えサイズMの安い順の時の1番目() {
		List<Item> itemList = itemsRepository.findByLikeName("", "price_m");
		Integer id = 3;
		Integer priceM=250;
		Integer priceL = 500;
		Integer categoryId = 2;
//		Boolean deleted = false;
		assertEquals("idが違います", id, itemList.get(1).getId());
		assertEquals("商品名が違います", "Specialキャラメルドーナッツ", itemList.get(1).getName());
		assertEquals("解説が違います", "ドーナツ生地の中に、なめらかで風味豊かなキャラメルフィリングを入れ、仕上げにキャラメルのビター感と香ばしさが楽しめるキャラメルコーティングをかけました。", itemList.get(1).getDescription());
		assertEquals("Mの価格が違います", priceM, itemList.get(1).getPriceM());
		assertEquals("Lの価格が違います",priceL , itemList.get(1).getPriceL());
		assertEquals("写真が一致しません", "3.jpg", itemList.get(1).getImagePass());
		assertEquals("カテゴリーIDが違います", categoryId, itemList.get(1).getCategoryId());
//		assertEquals("deletedが一致しません", item.getDeleted(), deleted);
	}
	
	@Test
	public void 曖昧検索を空欄にし並び替えサイズMの高い順の時の0番目() {
		List<Item> itemList = itemsRepository.findByLikeName("", "price_m DESC");
		Integer id = 2;
		Integer priceM=530;
		Integer priceL = 650;
		Integer categoryId = 1;
//		Boolean deleted = false;
		
		assertEquals("idが違います", id, itemList.get(0).getId());
		assertEquals("商品名が違います", "エスプレッソフラペチーノ", itemList.get(0).getName());
		assertEquals("解説が違います", "ひと口目に感じるエスプレッソは「リストレット」という方法で抽出した力強い香りと優しい苦味を、ふた口目は全体を混ぜて、こだわりのクリームから来るアフォガートのような味わいをお楽しみください。リフレッシュしたい時や、ほっとひと息つきたい時にも、何度でも飲みたくなるフラペチーノです。", itemList.get(0).getDescription());
		assertEquals("Mの価格が違います", priceM, itemList.get(0).getPriceM());
		assertEquals("Lの価格が違います", priceL,itemList.get(0).getPriceL() );
		assertEquals("写真が一致しません", "2.jpg", itemList.get(0).getImagePass());
		assertEquals("カテゴリーIDが違います", categoryId, itemList.get(0).getCategoryId());
//		assertEquals("deletedが一致しません", deleted, item.getDeleted());
	}
	
	@Test
	public void 曖昧検索を空欄にし並び替えサイズMの高い順の時の1番目() {
		List<Item> itemList = itemsRepository.findByLikeName("", "price_m DESC");
		Integer id = 11;
		Integer priceM=500;
		Integer priceL = 580;
		Integer categoryId = 1;
//		Boolean deleted = false;
		
		assertEquals("idが違います", id, itemList.get(1).getId());
		assertEquals("商品名が違います", "ダークモカフラペチーノ", itemList.get(1).getName());
		assertEquals("解説が違います", "コーヒー、ミルク、ダークチョコレートパウダー、そして人気のチョコレートチップを氷とブレンドした、チョコレートラバーズに人気のフローズンビバレッジ。コーヒーとダークチョコレートのほろ苦い味わいと、チョコレートチップの食感が織り成すハーモニーは、ブラックコーヒーファンにも支持されています。", itemList.get(1).getDescription());
		assertEquals("Mの価格が違います", priceM, itemList.get(1).getPriceM());
		assertEquals("Lの価格が違います", priceL,itemList.get(1).getPriceL() );
		assertEquals("写真が一致しません", "11.jpg", itemList.get(1).getImagePass());
		assertEquals("カテゴリーIDが違います", categoryId, itemList.get(1).getCategoryId());
//		assertEquals("deletedが一致しません", deleted, item.getDeleted());
	}
	
	@Test
	public void 曖昧検索を空欄にし並び替えサイズLの安い順の時の0番目() {
		List<Item> itemList = itemsRepository.findByLikeName("", "price_l");
		Integer id = 4;
		Integer priceM=190;
		Integer priceL = 300;
		Integer categoryId = 2;
//		Boolean deleted = false;
		
		assertEquals("idが違います", id, itemList.get(0).getId());
		assertEquals("商品名が違います", "チョコクッキー", itemList.get(0).getName());
		assertEquals("解説が違います", "ソフトな食感のクッキー生地には、小麦の香ばしさが感じられるよう全粒粉を入れ、砂糖の一部にはブラウンシュガーを使い、コクのある甘さをプラスしています。風味豊かなスターバックスオリジナルのチョコレートチャンクがごろごろ入っていて、どこを食べてもチョコレートの味わいを存分に楽しめます。ショートサイズのマグカップの上に乗せられるくらいのサイズは、コーヒーと一緒に楽しむのにもぴったりです。", itemList.get(0).getDescription());
		assertEquals("Mの価格が違います", priceM, itemList.get(0).getPriceM());
		assertEquals("Lの価格が違います", priceL, itemList.get(0).getPriceL() );
		assertEquals("写真が一致しません", "4.jpg", itemList.get(0).getImagePass());
		assertEquals("カテゴリーIDが違います", categoryId, itemList.get(0).getCategoryId());
//		assertEquals("deletedが一致しません", deleted, item.getDeleted());
	}
	@Test
	public void 曖昧検索を空欄にし並び替えサイズLの安い順の時の1番目() {
		List<Item> itemList = itemsRepository.findByLikeName("", "price_l");
		Integer id = 16;
		Integer priceM=310;
		Integer priceL = 350;
		Integer categoryId = 1;
//		Boolean deleted = false;
		
		assertEquals("idが違います", id, itemList.get(1).getId());
		assertEquals("商品名が違います", "エスプレッソ", itemList.get(1).getName());
		assertEquals("解説が違います", "キャラメルのような甘く力強い味とナッツを感じさせる後味。スターバックスのすべてのエスプレッソ ビバレッジに使われているエスプレッソです。どうぞ、お早めにお召し上がりください。", itemList.get(1).getDescription());
		assertEquals("Mの価格が違います", priceM, itemList.get(1).getPriceM());
		assertEquals("Lの価格が違います", priceL,itemList.get(1).getPriceL() );
		assertEquals("写真が一致しません", "16.jpg", itemList.get(1).getImagePass());
		assertEquals("カテゴリーIDが違います", categoryId, itemList.get(1).getCategoryId());
//		assertEquals("deletedが一致しません", deleted, item.getDeleted());
	}
	
	@Test
	public void 曖昧検索を空欄にし並び替えサイズLの高い順の時の0番目() {
		List<Item> itemList = itemsRepository.findByLikeName("", "price_l DESC");
		Integer id = 1;
		Integer priceM=480;
		Integer priceL = 700;
		Integer categoryId = 2;
//		Boolean deleted = false;
		
		assertEquals("idが違います", id, itemList.get(0).getId());
		assertEquals("商品名が違います", "Gorgeous4サンド", itemList.get(0).getName());
		assertEquals("解説が違います", "人気の定番具材「ハム」と「チキン」をセットにした食べごたえ抜群のサンドイッチです。", itemList.get(0).getDescription());
		assertEquals("Mの価格が違います", priceM, itemList.get(0).getPriceM());
		assertEquals("Lの価格が違います", priceL, itemList.get(0).getPriceL() );
		assertEquals("写真が一致しません", "1.jpg", itemList.get(0).getImagePass());
		assertEquals("カテゴリーIDが違います", categoryId, itemList.get(0).getCategoryId());
//		assertEquals("deletedが一致しません", deleted, item.getDeleted());
	}
	@Test
	public void 曖昧検索を空欄にし並び替えサイズLの高い順の時の1番目() {
		List<Item> itemList = itemsRepository.findByLikeName("", "price_l DESC");
		Integer id = 2;
		Integer priceM=530;
		Integer priceL = 650;
		Integer categoryId = 1;
//		Boolean deleted = false;
		
		assertEquals("idが違います", id, itemList.get(1).getId());
		assertEquals("商品名が違います", "エスプレッソフラペチーノ", itemList.get(1).getName());
		assertEquals("解説が違います", "ひと口目に感じるエスプレッソは「リストレット」という方法で抽出した力強い香りと優しい苦味を、ふた口目は全体を混ぜて、こだわりのクリームから来るアフォガートのような味わいをお楽しみください。リフレッシュしたい時や、ほっとひと息つきたい時にも、何度でも飲みたくなるフラペチーノです。", itemList.get(1).getDescription());
		assertEquals("Mの価格が違います", priceM, itemList.get(1).getPriceM());
		assertEquals("Lの価格が違います", priceL,itemList.get(1).getPriceL() );
		assertEquals("写真が一致しません", "2.jpg", itemList.get(1).getImagePass());
		assertEquals("カテゴリーIDが違います", categoryId, itemList.get(1).getCategoryId());
//		assertEquals("deletedが一致しません", deleted, item.getDeleted());
	}
	@Test
	public void 曖昧検索欄に4を入れた場合() {
		List<Item> itemList = itemsRepository.findByLikeName("4", "price_m");
		Integer id = 1;
		Integer priceM=480;
		Integer priceL = 700;
		Integer categoryId = 2;
//		Boolean deleted = false;
		
		assertEquals("idが違います", id, itemList.get(0).getId());
		assertEquals("商品名が違います", "Gorgeous4サンド", itemList.get(0).getName());
		assertEquals("解説が違います", "人気の定番具材「ハム」と「チキン」をセットにした食べごたえ抜群のサンドイッチです。", itemList.get(0).getDescription());
		assertEquals("Mの価格が違います", priceM, itemList.get(0).getPriceM());
		assertEquals("Lの価格が違います", priceL, itemList.get(0).getPriceL() );
		assertEquals("写真が一致しません", "1.jpg", itemList.get(0).getImagePass());
		assertEquals("カテゴリーIDが違います", categoryId, itemList.get(0).getCategoryId());
//		assertEquals("deletedが一致しません", deleted, item.getDeleted());
	}
	@Test
	public void 曖昧検索欄に全角の４を入れた場合() {
		List<Item> actual = itemsRepository.findByLikeName("４", "price_m");
		assertEquals("該当する商品はありません",actual.size(),0 );
	}
	@Test
	public void 曖昧検索欄におにぎりを入れた場合() {
		List<Item> actual = itemsRepository.findByLikeName("おにぎり", "price_m");
		assertEquals("該当する商品はありません",actual.size(),0 );
	}
	
}
