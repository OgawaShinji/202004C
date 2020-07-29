$(function(){

	$("#btn").on("click",function(){
		var usePoint = Number($("#usePoint").val());
		var total_price = Number($(".total_price").text().split(',')) - usePoint;
		$(".total_price").val(total_price);
		$("#usePoint").val(0);
	});
})