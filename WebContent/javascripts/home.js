$(document).ready(initPage);

function initPage() {
	listBaseURI();
	getCurrentUser();
}

function listBaseURI() {
	$.getJSON('./servlet/listBaseURI', function(data) {
		var container = $('#container');
		$('#nav-view').attr('href', 'view.html?bid=' + data[0]['bid']);
		for(var i=0; i<data.length; i++) {
			var entry = $('<div class="row-fluid">');
			var uriSpan = $('<span id="baseURI" class="span7">').html(decodeURIComponent(data[i]['baseURI']));
			var progress = $('<div id="progress" class="span3"><div class="row-fluid"><div class="progress span10"><div id="progressBar" class="bar" style="width:'+getPercent(data[i]['position'],data[i]['sum'])+'%"></div></div><span class="span2"><span id="curPos">'+data[i]['position']+'</span>/<span id="sumPos">'+data[i]['sum']+'</span></span></div></div>');
			var btnGroup = $('<div id="evaluatingButtons" class="span2 btn-group"><button id="viewBtn" class="btn" onClick="redirectToView('+data[i]['bid']+')">View</button><button id="markBtn" class="btn" onClick="redirectToMark('+data[i]['cid']+')">Mark</button></div>');
			entry.append(uriSpan);
			entry.append(progress);
			entry.append(btnGroup);
			container.append(entry);
		}
	});
}

function getCurrentUser() {
	$.get('./servlet/getCurrentUser', function(response) {
		if(response.trim() == '') {
			location.href = './index.html';
		}
		else {
			$('#userEmail').html(response);
		}
	});
}

function redirectToMark(cid) {
	location.href='mark.html?cid=' + cid;
}

function redirectToView(bid) {
	location.href='view.html?bid=' + bid;
}

function getPercent(pos,sum) {
	return pos / sum * 100;
}