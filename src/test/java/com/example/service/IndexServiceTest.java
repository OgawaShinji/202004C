package com.example.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.sql.DataSource;

import com.example.domain.Item;
import com.example.repository.ItemsRepository;
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
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexServiceTest {

	public static final Operation DELETE_ALL = Operations.deleteAllFrom("items");

    // カラムのdeletedの設定の仕方がよくない
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


										

	private static final RowMapper<Item> ITEM_ROW_MAPPER 
	=(rs,i)->{
		Item item = new Item();
		item.setId(rs.getInt("id"));
		item.setName(rs.getString("name"));
		item.setDescription(rs.getString("description"));
		item.setPriceM(rs.getInt("price_m"));
		item.setPriceL(rs.getInt("price_l"));
		item.setImagePass(rs.getString("image_path"));
		item.setCategoryId(rs.getInt("categoryid"));
		return item;
	};											

	
												
    @BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Mock
	private ItemsRepository itemsRepository;

	@InjectMocks
	private IndexService service;

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

    @Autowired
	private NamedParameterJdbcTemplate template;


    @Test
    public void IndexRepositoryのfindAllメソッドの引数がnameの時の動作確認テスト() {

		String sql = "SELECT * FROM items"
				+ " WHERE deleted != true ORDER BY name";

		List<Item> itemList = template.query(sql, ITEM_ROW_MAPPER);

		when(this.itemsRepository.findAll("name")).thenReturn(itemList);
		Item actual=this.service.findAll("name").get(0);

		assertEquals("error",actual,itemList.get(0));
		
	}
	
	@Test
	public void getItemListForAutocompleteメソッドの引数にnameで並び替えたitemListを詰めた時の動作確認(){
		
		String sql = "SELECT * FROM items"
		+ " WHERE deleted != true ORDER BY name";
		
		List<Item> itemList=template.query(sql,ITEM_ROW_MAPPER);
		
		StringBuilder str = service.getItemListForAutocomplete(itemList);
		String actual=str.toString();
		String expected = "'Gorgeous4サンド','Specialキャラメルドーナッツ','アイスコーヒー','アメリカン','エスプレッソ','エスプレッソフラペチーノ','カフェモカ','カフェラテ','カプチーノ','キャラメルフラペチーノ','キャラメルマキアート','ジンジャーブレッドラテ','ダークモカフラペチーノ','チョコクッキー','ドリップコーヒー','ナッティホワイトモカ','バニラ クリーム フラペチーノ','抹茶クリームフラペチーノ'";
		assertEquals("error",actual,expected);
	}
	
	
	// showListPaging()の引数がpage=1,size=6,itemListがfindall("name")の時の動作確認
	@Test
	public void showListPagingメソッドの正常系テスト1(){

		String sql = "SELECT * FROM items"
				+ " WHERE deleted != true ORDER BY name";

		final Integer page=1;
		final Integer size=6;
		List<Item> itemList=template.query(sql, ITEM_ROW_MAPPER);
		Page<Item> actual=service.showListPaging(page, size, itemList);
		assertEquals("error", actual.getContent().get(0), itemList.get(0));
		assertEquals("error", actual.getContent().get(1), itemList.get(1));
		assertEquals("error", actual.getContent().get(5), itemList.get(5));
		assertEquals("error", actual.getSize(), 6);
		assertEquals("error", actual.getTotalElements(), 18);
	}
	
	// showListPaging()の引数がpage=1,size=6,itemListがfindByLikeName("","name")の時の動作確認
	@Test
	public void showListPagingメソッドの正常系テスト2(){
		String sql = "SELECT * FROM items"
		+ " WHERE name LIKE '%%' AND deleted != true ORDER BY name";
		
		final Integer page=1;
		final Integer size=6;
		List<Item> itemList=template.query(sql, ITEM_ROW_MAPPER);
		Page<Item> actual=service.showListPaging(page, size, itemList);
		assertEquals("error", itemList.get(0), actual.getContent().get(0));
		assertEquals("error", itemList.get(1), actual.getContent().get(1));
		assertEquals("error", itemList.get(5), actual.getContent().get(5));
		assertEquals("error", actual.getSize(), 6);
		assertEquals("error", actual.getTotalElements(), 18);
	}
	
	// showListPaging()の引数がpage=1,size=6,itemListがfindByLikeName("コーヒー","name")の時の動作確認
	@Test
	public void showListPagingメソッドの正常系テスト3(){
		String sql = "SELECT * FROM items"
		+ " WHERE name LIKE '%コーヒー%' AND deleted != true ORDER BY name";

		final Integer page=1;
		final Integer size=6;
		List<Item> itemList=template.query(sql, ITEM_ROW_MAPPER);
		Page<Item> actual=service.showListPaging(page, size, itemList);
		assertEquals("error", itemList.get(0), actual.getContent().get(0));
		assertEquals("error", itemList.get(1), actual.getContent().get(1));
		assertEquals("error", actual.getSize(), 6);
		assertEquals("error", actual.getTotalElements(), 2);
	}
	
	// showListPaging()の引数がpage=1,size=6,itemListがfindByLikeName("'% --","name")の時の動作確認
	@Test
	public void showListPagingメソッドの異常系テスト1_SQLインジェクション対策の動作確認(){
		String name="'%--";
		String listType="name";
		String sql = "SELECT * FROM items"
		+ " WHERE name LIKE :name AND deleted != true ORDER BY " + listType;
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%");
		List<Item> itemList = template.query(sql, param, ITEM_ROW_MAPPER);
		
		final Integer page=1;
		final Integer size=6;
		Page<Item> actual=service.showListPaging(page, size, itemList);
		assertEquals("error",itemList,actual.getContent());
		assertEquals("error", actual.getSize(), 6);
		assertEquals("error", actual.getTotalElements(), 0);
		
	}
	

}
