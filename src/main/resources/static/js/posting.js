$(document).ready(function () {
	$('.answerPosting').click(function() {
		let postingId = $(this).attr('id');
		$('#postingId').val(postingId);
	});
	
	$('#newPosting').click(function() {
		$('#postingId').val('');
	});	
});