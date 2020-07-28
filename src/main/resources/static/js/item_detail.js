$(function() {

	// ページ遷移時にデフォルトで表示する金額
	// デフォルト
	// サイズ＝M
	// トッピング＝なし
	// 数量＝1
	$(function() {
		var item_price_total = Number($(".item-priceM").text());
		var tax_price = item_price_total * 0.1;
		var total_price = item_price_total + tax_price;

		$("#item-price-total").text(item_price_total);
		$("#tax-price").text(tax_price);
		$("#total-price").text(total_price);
	})

	// formの値を変更する事に合計金額を自動反映させる機能
	$("input:radio,input:checkbox,select")
			.change(
					function() {

						var item_size = $("input:radio:checked").val();
						var item_price = 0;
						var item_quantity = $(".item-quantity").val();
						;
						var topping_price = 0;
						var topping_quantity = $("input:checkbox:checked").length;

						if (item_size == "M") {
							var str = $(".item-priceM").text();
							item_price = Number(str);
							topping_price = 200;
						} else if (item_size == "L") {
							var str = $(".item-priceL").text();
							item_price = Number(str);
							topping_price = 300;
						}

						var item_price_total = 0
						if (topping_quantity == 0) {
							item_price_total = item_price * item_quantity;
						} else if (topping_quantity >= 1) {
							item_price_total = (item_price + (topping_price * topping_quantity))
									* item_quantity;
						}
						var tax_price = item_price_total * 0.1;
						var total_price = item_price_total + tax_price;

						$("#item-price-total").text(item_price_total);
						$("#tax-price").text(tax_price);
						$("#total-price").text(total_price);

					});

	$('[id^=toppingList]').closest("span").mouseover(
			function() {
				var id = $(this).find("input[id^=toppingList]").attr("id");
				var topping = {
					1 : [ "コーヒー風味の生クリーム。きめの細かさとほろ苦さがやみつきに！" ],
					2 : [ "牛乳よりもカロリーが抑えられ、栄養価も高い！" ],
					3 : ["生乳本来のおいしさを生かしたさらりとした飲み心地！"],
					4 : ["飲みやすく優しい味わいに調整され栄養満点！"],
					5 : ["ミルクをお湯で割らずにたくさん注いだ濃厚な味わい！"]
					
				}
				var input = document.getElementById(id);
				var value = input.getAttribute('value');
				for ( var key in topping) {
					if (key == value) {
						$(this).append(
								'<span class="tooltips">' + topping[key]
										+ '</span>');
					}
				}
			});
	$('.checkbox-inline').closest("span").mouseout(function() {
		$("span[class=tooltips]").remove();
	})
})