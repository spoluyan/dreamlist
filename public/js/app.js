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
	var p = $('#dream-' + dreamId);
	var isDone = p.find('del').length == 0;
	
	var text;
	if (isDone) {
		text = p.text();
	} else {
		text = p.find('del').text();
	}
	$('#dreamId').val(dreamId);
	$('#dream-text').val(text);
}

function limitDreamLength() {
	var textarea = $('#dream-text');
	if (textarea.val().length > 255) {
        var text = textarea.val();
        textarea.val(text.substr(0, 255));   
    }
}

function updateDream() {
	var text = $('#dream-text').val();
	var dreamId = $('#dreamId').val();
	$.ajax({
		url : updateAction.url({
			id : dreamId
		}),
		type : updateAction.method,
		data: 'text=' + text
	}).success(function() {
		var p = $('#dream-' + dreamId);
		var isDone = p.find('del').length == 0;
		if (isDone) {
			p.text(text);
		} else {
			p.find('del').text(text);
		}
		$('#edit-modal').modal('hide');
	});
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