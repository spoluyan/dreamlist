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
			button.removeClass('btn-success');
			button.addClass('btn-warning');
			button.html('<span class="glyphicon glyphicon-unchecked" aria-hidden="true"></span>');
			var text = p.text();
			p.html('<del>' + text + '</del>');
		} else {
			button.removeClass('btn-warning');
			button.addClass('btn-success');
			button.html('<span class="glyphicon glyphicon-check" aria-hidden="true"></span>');
			var text = p.find('del').text();
			p.html('');
			p.text(text);
		}
	});
}

function editDream(dreamId) {
	
}

function removeDream(dreamId) {
	if (confirm(confirmRemove)) {
		$.ajax({
			url : removeAction.url({
				id : dreamId
			}),
			type : removeAction.method
		}).success(function() {
			$('#dream-row-' + dreamId).remove();
		});
	}
}