function markDream(dreamId) {
	$.ajax({
		url : markAction.url({
			id : dreamId
		}),
		type : markAction.method
	}).success(function() {
		var button = $('#mark-' + dreamId);
		var p = $('#dream-' + dreamId);
		var isDone = p.find('del').length == 0;
		if (isDone) {
			button.html('<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>');
			var text = p.text();
			p.html('<del>' + text + '</del>');
		} else {
			button.html('<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>');
			var text = p.find('del').text();
			p.html('');
			p.text(text);
		}
	});
}