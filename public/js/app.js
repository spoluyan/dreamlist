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

function limitDreamLength(id) {
	var textarea = $(id);
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

function copyFromFriend(dreamId) {
	$.ajax({
		url : copyAction.url({
			dreamId : dreamId
		}),
		type : copyAction.method
	}).success(function() {
		$('#added-modal').modal('show');
		setTimeout(function() {
			$('#added-modal').modal('hide');
		}, 1000);
	});
}

function addFriend(login) {
	$.ajax({
		url : addFriendAction.url(),
		type : addFriendAction.method,
		data: 'login=' + login
	}).success(function() {
		$('#added-modal').modal('show');
		setTimeout(function() {
			$('#added-modal').modal('hide');
		}, 1000);
		if ($('#search').length != 0) {
			search();
		}
	});
}

function deleteFriend(login) {
	$.ajax({
		url : deleteFriendAction.url(),
		type : deleteFriendAction.method,
		data: 'login=' + login
	}).success(function() {
		$('#panel-' + login).remove();
	});
}

function search() {
	var q = $('#search').val();
	$.ajax({
		url : searchAction.url(),
		type : searchAction.method,
		data: 'query=' + q
	}).success(function(data) {
		var result = $('#search-result');
		result.empty();
		for (var i = 0; i < data.length; i++) {
			result.append('<tr><td>' + data[i] + '</td><td><button type="button" class="btn btn-default btn-xs" onclick="addFriend(\'' + data[i] + '\')">' + addButtonText + '</button></td></tr>');
		}
	});
}