$(function() {
	$("#submitBtn").click(function() {// 点击提交表单
		$(this).addClass("pub-disabled1").attr("disabled", "disabled");
		$("#sForm").submit();
	})

});