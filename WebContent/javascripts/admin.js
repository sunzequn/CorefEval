$(document).ready(initPage);
var uid = -1;
function initPage() {
	uid = getParam('uid');
	listUser();	
	getCurrentUser();
	bindBatchBtns();
}

function bindBatchBtns() {
	$('#multiGrantBtn').click(function () {
		var range = getBatchRange();
		if(range) {
			multiGrant(range.from, range.to);
		}
	});

	$('#multiRevokeBtn').click(function () {
		var range = getBatchRange();
		if(range) {
			multiRevoke(range.from, range.to);
		}
	})
}

function getBatchRange() {
	var range;
	var from = parseInt($('#rangeFrom').val().trim());
	var to = parseInt($('#rangeTo').val().trim());
	if(from <= to) {
		range = {'from': from,
				'to': to}
	}
	return range;
}

function getCurrentUser() {
	$.get('./servlet/getCurrentUser', function(response) {
		if(response.trim() == 'admin') {
			$('#userEmail').html(response);			
		}
		else {
			location.href = './index.html';
		}
	});
}

function listUser() {
	$.getJSON('./servlet/listUser',function(json) {
		constructUserList(json);
	});
}

function getParam(paramName) {
	var params = location.href.split('?')[1];
	if(!params) {
		return -1;
	}
	var paramsArray = params.split('&');
	for(var i = 0; i < paramsArray.length; i++) {
		if(paramsArray[i].split('=')[0] == paramName) {
			return paramsArray[i].split('=')[1];
		}
	}
	return null;
}

function constructUserList(json) {
	var ul = $('#userList');
	if(uid == -1) {
		uid = json[0]['uid'];
	}
	$.each(json, function(index, val) {
		var li = $('<li>');		
		if(val['uid'] == uid) {
			li.attr('class','active');
		}
		var a = $('<a>', {
						text: val['email'],
						href: '?uid=' + val['uid']
						});
		var i = $('<i class="icon-chevron-right"></i>');
		a.append(i);
		li.append(a);
		ul.prepend(li);
	});
	listUserProcess(uid);
}

function listUserProcess(uid) {
	$.getJSON('./servlet/getProcessOfUser', {uid:uid}, function(json) {
		constructUserProcess(json);
	});
}

function constructUserProcess(json) {
	var tbody = $('#userProcessTbody');
	$.each(json, function(index, val) {
		var bid = val['bid'];
		var tr = $('<tr>');
		var td0 = $('<td>').html(index);
		var td1 = $('<td>').html(decodeURIComponent(val['baseURI']));
		var td2 = $('<td>').html(val['position'] + '/' + val['sum']);
		var td3 = $('<td>');
		if(val['position'] == 0) {
			var grantBtn = $('<button>',{
								text: ' Grant ',
								class: 'btn btn-success btn-grant',
								bid: bid
							});
			grantBtn.click(btnListener);
			td3.append(grantBtn);			
		}
		else {
			var revokeBtn = $('<button>',{
								text: 'Revoke',
								class: 'btn btn-danger btn-revoke',
								bid: bid
							});
			revokeBtn.click(btnListener);
			td3.append(revokeBtn);
			tr.attr('class', 'info');
		}
		tr.attr('bid', bid);
		tr.append(td0);		
		tr.append(td1);
		tr.append(td2);
		tr.append(td3);
		tbody.append(tr);
	});
	console.log(json);
}

function btnListener() {
	if($(this).hasClass('btn-grant')) {
		grant($(this).attr('bid'));
	}
	else if($(this).hasClass('btn-revoke')) {
		revoke($(this).attr('bid'));
	}
}

function multiGrant(from, to) {
	var bids = '';
	for(var i = from; i < to; i++) {
		bids += getBidByIndex(i) + ',';
	}
	bids += getBidByIndex(to);
	grant(bids);
}

function multiRevoke(from, to) {
	var bids = '';
	for(var i = from; i < to; i++) {
		bids += getBidByIndex(i) + ',';
	}
	bids += getBidByIndex(to);
	revoke(bids);
}

function getBidByIndex(index) {
	var row = $('#userProcessTbody>tr').eq(index);
	var bid = '';
	if(row.attr('bid')) {
		bid = row.attr('bid');
	}
	return bid;
}

function grant(bid) {
	$.post('./servlet/manageUser',{op:'grant', uid:uid, bid:bid}, function(resp) {
		location.href='?uid=' + uid;
	});
}

function revoke(bid) {
	$.post('./servlet/manageUser',{op:'revoke', uid:uid, bid:bid}, function(resp) {
		location.href='?uid=' + uid;
	});
}
