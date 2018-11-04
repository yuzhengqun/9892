$(function() {
	$('#searchBtn').click(function() {
		var searchVal = $('#searchVal').val();
		// alert('searchVal:' + searchVal);
		location.href = "/item/" + searchVal;
	});

});