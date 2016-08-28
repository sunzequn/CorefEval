$(document).ready(initPage);
var cid = -1;
//var sviewAccessPoint = 'http://ws.nju.edu.cn/explorer/entity.jsp?q=';
var sviewAccessPoint = 'http://ws.nju.edu.cn/sview/views/eview.jsp?lang=en&query=';
function initPage() {
	cid = getParam('cid');
	getCurrentURI();
	bindButtons();
	getCurrentUser();
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

function getCurrentURI() {
	$.get('servlet/getCurrentURI', {'cid':cid}, function (data){
		parseJSON(data);
	});
}

function parseJSON(jsonStr) {
	var data = $.parseJSON(jsonStr);
	$('#baseURI').html(data['baseURI']);
	$('#nav-view').attr('href', 'view.html?bid=' + data['bid']);
	$('#currentURI').html(data['currentURI']);
	$('#sumMarked').html(data['sumMarked']);
	$('#cid').html(data['cid']);
	$('#viewerA').attr('src',sviewAccessPoint + data['currentDESURI']);
	$('#viewerB').attr('src',data['currentURI']);
	$('#viewerC').attr('src',sviewAccessPoint + data['baseDESURI']);
	$('#viewerD').attr('src',data['baseURI']);
	cid = data['cid'];
	setProgress(data['position'],data['sumURI']);
	setCorefedStatus(data['isCorefed']);
	if(!data['hasNext']) {
		$('#nextBtn').addClass('disabled');
	}			
	if(!data['hasPrev']) {
		$('#prevBtn').addClass('disabled');
	}
}

function setCorefedStatus(isCorefed) {
	switch(isCorefed) {
		case 1:			
			$('#currentURI').addClass('label label-success');
			$('#correctBtn').addClass('active');
		break;
		case 0:
			$('#currentURI').addClass('label');
			$('#notSureBtn').addClass('active');
		break;
		case -1:
			$('#currentURI').addClass('label label-important');
			$('#wrongBtn').addClass('active');
		break;
		default:
			$('#currentURI').addClass('label');
//			$('#notSureBtn').addClass('active');
		break;
	}
}

function markURI(isCorefed) {
	location.href='servlet/markURI?cid=' + cid + '&isCorefed=' + isCorefed;
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

function setProgress(cur, sum) {
	$('#curPos').html(cur);
	$('#sumPos').html(sum);
	var pos = cur / sum * 100;
	$('#progressBar').css('width', pos + '%');
}

function btnListener() {
	if($(this).hasClass("disabled") || $(this).hasClass("active")) {
		return;
	}
	var btnId = this.id;
	switch(btnId) {
		case 'correctBtn':
			markURI(1);
		break;
		case 'wrongBtn':
			markURI(-1);
		break;
		case 'notSureBtn':
			markURI(0);
		break;
		case 'prevBtn':
			cid--;
			location.href='mark.html?cid='+cid;
		break;
		case 'nextBtn':
//			markURI(0);
			cid++;
			location.href='mark.html?cid='+cid;
		break;
		default:
		break;
	}
}