$(document).ready(initPage);

function initPage() {
	bindButtons();
	bindEnterKey();
}

function bindEnterKey() {
	$(document).keypress(function(e) {		//监听回车
		if (e.which == 13) {
			var activeId = document.activeElement.id;
			if(activeId == 'emailInput' || activeId == 'passwdInput') {
				login();
			}
			else if(activeId == 'newEmailInput' || activeId == 'newPasswdInput' || activeId == 'repasswdInput') {
				signUp();
			}
		}
	});
}

function bindButtons() {
	$('.btn').bind('click', btnListener);
}

function btnListener() {
	var id = this.id;
	switch(id) {
		case 'loginBtn':
			login();
		break;
		case 'signUpBtn':
			signUp();
		break;
		case 'resetBtn':
			reset();
		break;
		default:
		break;
	}
}

function login() {
	var email = $('#emailInput').val().trim();
	var passwd = $('#passwdInput').val().trim();
	if(email != '' && passwd != '') {
		passwd = $.md5(passwd);
		$.get('servlet/login',{'email':email, 'passwd':passwd},function(resp) {
			if(resp == '-1') {
				alert('用户名或密码错误');
			}
			else if(resp == 'admin') {
				location.href = 'admin.html';
			}
			else {				
				location.href = 'home.html';	
			}
		});
	}
	

}

function signUp() {
	var email = $('#newEmailInput').val().trim();
	var passwd = $('#newPasswdInput').val().trim();
	var repasswd = $('#repasswdInput').val().trim();
	if(email != '' && passwd != '' && repasswd != '') {
		if(!validateEmail(email)) {
			alert('邮箱格式有误！');
		}
		else if(passwd != repasswd) {
			alert('两次密码不一致！');
		}
		else {
			passwd = $.md5(passwd);
			$.get('servlet/signUp',{'email':email, 'passwd':passwd},function(resp) {
				if(resp == '-1') {
					alert('注册失败');
				}
				else {
					location.href = 'home.html';
				}
			});
		}
	}

}

function validateEmail(email) { 
    var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
} 

function reset() {
	$('#newEmailInput').val('');
	$('#newPasswdInput').val('');
	$('#repasswdInput').val('');
}

