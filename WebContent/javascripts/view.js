$(document).ready(initPage);
var bid = 1;
function initPage() {
	bid = getParam('bid');
	if(bid == -1) {
		bid = 1;
	}
	getCurrentUser();
	listURI();
}

function listURI() {
	$.get('./servlet/listURIWithSource',{bid:bid},function(json) {
		parseJSON(decodeURIComponent(json));
	});
}

function parseJSON(jsonStr) {
	json = $.parseJSON(jsonStr);
	var sources = json['sources'];
	var URIs = json['URIs'];
	$.each(sources, function(index, value) {		
		constructURIBySource(value,URIs[value]);
	});
}

function constructURIBySource(source, URIList) {
	var markPrefix = './mark.html?cid=';
	$('#container').append('<h1>'+ source + '</h1>');
	var ul = $('<ul>');
	for(var i = 0; i < URIList.length; i ++) {
		var cid = URIList[i]['cid'];
		var URI = URIList[i]['URI'];
		var isCorefed = URIList[i]['isCorefed'];
		var labelClass = 'label uriLabel';
		if(isCorefed == 1) {
			labelClass += ' label-success';
		}
		else if (isCorefed == -1) {
			labelClass += ' label-important';
		}
		
		var li = $('<li>');
		var label = $('<a></a>', {
						class: labelClass,
						text: URI,
						href: markPrefix + cid
					});
		li.html(label);
		ul.append(li);
			
	}
	$('#container').append(ul);	
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
function bindButtons() {
	$('.btn').bind('click', btnListener);
	
}