$(function () {

    // ページ遷移時にデフォルトで表示する金額
    // デフォルト
    // サイズ＝M
    // トッピング＝なし
    // 数量＝1
    $(function(){
        var item_price_total = Number($(".item-priceM").text());
        var tax_price = item_price_total * 0.1;
        var total_price = item_price_total + tax_price;

        $("#item-price-total").text(item_price_total);
        $("#tax-price").text(tax_price);
        $("#total-price").text(total_price);
    })


    // formの値を変更する事に合計金額を自動反映させる機能
    $("input:radio,input:checkbox,select").change(function () {

        var item_size = $("input:radio:checked").val();
        var item_price = 0;
        var item_quantity = $(".item-quantity").val();;
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
            item_price_total = (item_price + (topping_price * topping_quantity)) * item_quantity;
        }
        var tax_price = item_price_total * 0.1;
        var total_price = item_price_total + tax_price;

        $("#item-price-total").text(item_price_total);
        $("#tax-price").text(tax_price );
        $("#total-price").text(total_price);

    });
})