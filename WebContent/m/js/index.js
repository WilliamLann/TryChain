(function(){
	//获取url栏参数
	function getParam(p){
		var params = window.location.search.substr(1).split('&');
		for(var i=0;i<params.length;i++)
			if(p==params[i].split("=")[0]) return decodeURIComponent(params[i].split("=")[1] || '');
		return '';
	}
	function getCookie(p){
		var cookies = document.cookie.split("; ");
		for(var i=0;i<cookies.length;i++)
			if(p==cookies[i].split("=")[0]) return decodeURIComponent(cookies[i].split("=")[1] || '');
		return '';
	}
	//初始化vue、mui、viewApi、scroll
	mui.init();
	var vm = new Vue({
		el: '#vue',
		data: {
			wechat: mui.os.wechat, //是否在微信环境
			userimg: '', //头像
			username: '', //昵称
			address: '', //账户地址
			value: 0, //余额
			addressRand: [], //随机地址
			txsInter: {}, //交易记录
			url: '', //邀请url
			num: 0, //邀请人数
			reward: 0, //邀请奖励
			records: [], //邀请记录
			inviterName: '', //邀请者昵称
			inviterId: getParam('id') || '', //邀请者id
			inviterUrl: '' //邀请者url
		}
	})
	var viewApi = mui('#app').view({
		defaultPage: '#home'
	});
	mui('.mui-scroll-wrapper').scroll();
	//收款码画布
	var cAddr = document.createElement('canvas'), ctxAddr = cAddr.getContext('2d');
	cAddr.width = 220;
	cAddr.height = 220;
	ctxAddr.fillStyle = '#fff';
	ctxAddr.fillRect(0,0,cAddr.width,cAddr.height);
	//获取TBC信息，同时判断登录状态
	getTbc();
	function getTbc(){
		vm.userimg = getCookie('ethUserimg');
		vm.username = getCookie('ethUsername');
		mui.getJSON('/tbc/getTbc',{},function(data){
			if(data){
				vm.address = data.address || '';
				vm.value = data.value || 0;
				vm.txsInter = data.txsInter || {};
				$('#addrQrcode').qrcode({
					render: 'canvas',
					width: 200,
					height: 200,
					text: vm.address
				});
				ctxAddr.drawImage($('#addrQrcode canvas')[0],10,10);
				$('#addrImg').attr('src', cAddr.toDataURL("image/png"));
			}else if(vm.inviterId){
				viewApi.go('#register');
			}else{
				viewApi.go('#login');
			}
		});
	}
	//邀请二维码画布
	var c = document.createElement('canvas'), ctx = c.getContext('2d');
	c.width = 220;
	c.height = 220;
	ctx.fillStyle = '#fff';
	ctx.fillRect(0,0,c.width,c.height);
//	var imgInit = $('#urlImg')[0];
//	imgInit.onload = function(){
//		ctx.drawImage(imgInit,0,0,c.width,c.height);
//	}
	//返回事件、view事件监听
	var oldBack = mui.back;
	mui.back = function() {
		if (viewApi.canBack()) {
			viewApi.back();
		} else {
			oldBack();
		}
	};
	window.history.pushState && window.history.pushState(null, null, "#");
	window.addEventListener("popstate", function(){
		if (viewApi.canBack()){
			window.history.pushState && window.history.pushState(null, null, "#");
		}
		mui.back();
	}, false)
	var view = viewApi.view;
	view.addEventListener('pageBeforeShow', function(e) {
		switch(e.detail.page.id){
		case 'login':
			if(!vm.inviterUrl){
				mui.getJSON('/user/getInviteUrl',{},function(data){
					vm.inviterUrl = data.msg || '';
					vm.inviterUrl.endsWith('=') && (vm.inviterUrl += vm.inviterId)
				})
			}
			break;
		case 'register':
			if(!vm.inviterName && vm.inviterId){
				mui.getJSON('/user/getUsername',{id: vm.inviterId}, function(data){
					if(data.success){
						vm.inviterName = data.msg;
					}else {
						vm.inviterId = '';
					}
				})
			}
			break;
		case 'home':
			if(!vm.address){
				getTbc();
			}
			break;
		case 'trade':
			if(!vm.addressRand.length){
				mui.getJSON('/tbc/getAddressRand',{},function(data){
					vm.addressRand = data || [];
				});
			}
			break;
		case 'invite':
			if(!vm.url){
				mui.getJSON('/tbc/getInviteCount',{},function(data){
					vm.num = data.count;
					vm.reward = data.sum || 0;
				});
				mui.getJSON('/user/getInviteUrl',{},function(data){
					vm.url = data.msg;
					$('#urlQrcode').qrcode({
						render: 'canvas',
						width: 200,
						height: 200,
						text: data.msg
					});
//					ctx.drawImage($('#urlQrcode canvas')[0],90,162,128,128);
					ctx.drawImage($('#urlQrcode canvas')[0],10,10);
					$('#urlImg').attr('src', c.toDataURL("image/jpeg"));
				});
			}
			break;
		case 'record':
			if(vm.num && !vm.records.length){
				mui.getJSON('/tbc/getInviteRecord',{},function(data){
					vm.records = data || [];
				});
			}
			break;
		}
	});
	view.addEventListener('pageShow', function(e) {
		switch(e.detail.page.id){
		case 'login':
			viewApi.history = [];
			if(viewApi.previousPage && viewApi.previousPage.id=="home"){
				viewApi._removePage(viewApi.previousPage, viewApi.previousNavbar);
			}
			break;
		case 'register':
			viewApi.history = [];
			if(viewApi.previousPage && viewApi.previousPage.id=="home"){
				viewApi._removePage(viewApi.previousPage, viewApi.previousNavbar);
			}
			break;
		case 'home':
			if(viewApi.previousPage && (viewApi.previousPage.id=="login" || viewApi.previousPage.id=="register")){
				viewApi.history = [];
				viewApi._removePage(viewApi.previousPage, viewApi.previousNavbar);
			}
			break;
		}
	});
	//复制邀请链接、地址
	var $urlCopy = $('#urlCopy');
	var $url = $('#url');
	$urlCopy.click(function(){
		$urlCopy.attr('disabled', true)
		$url.show().select();
		document.execCommand('Copy');
		$url.hide();
		mui.toast('复制成功');
		setTimeout(function(){
			$urlCopy.attr('disabled', false);
		}, 2000);
	});
	var $addrCopy = $('#addrCopy');
	var $addr = $('#addr');
	$addrCopy.click(function(){
		$addrCopy.attr('disabled', true)
		$addr.show().select();
		document.execCommand('Copy');
		$addr.hide();
		mui.toast('复制成功');
		setTimeout(function(){
			$addrCopy.attr('disabled', false);
		}, 2000);
	});
	//转账
	var $tradeBtn = document.getElementById('tradeBtn');
	var $to = document.getElementById('to');
	var $value = document.getElementById('value');
	$tradeBtn.addEventListener('tap', function(event) {
		if (!/^0x[0-9a-f]{40}$/.test($to.value)) return mui.toast('地址输入有误');
		if (vm.address==$to.value) return mui.toast('不能给自己转账');
		if ($value.value<=0 || $value.value>vm.value || !/^\d+$/.test($value.value)) return mui.toast('金额必须大于0且小于可用金额');
		$tradeBtn.setAttribute('disabled', true);
		mui.getJSON('/tbc/trade',{
			to: $to.value,
			value: $value.value
		},function(data){
			mui.toast(data.msg);
			if(data.success){
				mui.getJSON('/tbc/getTbc',{},function(data){
					vm.value = data.value || 0;
					vm.txsInter = data.txsInter || {};
				});
				mui.back();
			}
			setTimeout(function(){
				$tradeBtn.removeAttribute('disabled');						
			}, 2000)
		})
	});
	//随机地址
	mui('#recommend').on('tap', 'a', function(){
		if(this.innerText=='换一批'){
			mui.getJSON('/tbc/getAddressRand',{},function(data){
				vm.addressRand = data || [];
			});	
		}else{
			$to.value = this.innerText;
			mui('#recommend').popover('hide');
		}
	});
	//微信分享及扫码初始化
	mui.os.wechat && mui.getJSON('/user/getWeixinJSConfig',{url: location.href.split('#')[0]},function(data){
		var config = data.config;
		config.debug = false;
		config.jsApiList = ['checkJsApi','onMenuShareTimeline','onMenuShareAppMessage', 'scanQRCode'];
		wx.config(config);
		wx.ready(function(){
			wx.onMenuShareAppMessage(data.content);
			wx.onMenuShareTimeline(data.content);
			document.getElementById('scanBtn').addEventListener('tap', function(){
				wx.scanQRCode({
					needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
					scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
					success: function (res) {
						var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
						if(/^0x[0-9a-f]{40}$/.test(result)){
							$to.value = result;
						}else{
							mui.toast("扫描结果不是合法地址");
						}
					}
				});
			});
		})
	});
	//微信登录按钮事件
	document.getElementById('wxBtn').addEventListener('tap', function(){
		if(mui.os.wechat){
			location.replace(vm.inviterUrl);
		}else{
			mui.confirm(vm.inviterUrl, '复制以下链接在微信中打开', ['取消', '复制'], function(e) {
				if(e.index==1){
					$('#inviterUrl').show().select();
					document.execCommand('Copy');
					$('#inviterUrl').hide();
					mui.toast('复制成功');
				}
			})
		}
	})
	//登录、注册、发送验证码
	var $loginBtn = document.getElementById('loginBtn');
	var $loginName = document.getElementById('loginName');
	var $loginPassword = document.getElementById('loginPassword');
	var $mobile = document.getElementById('mobile');
	var $loginCode = document.getElementById('loginCode');
	var $loginSendBtn = document.getElementById('loginSendBtn');
	var $item = document.getElementById('item1');
	$loginBtn.addEventListener('tap', function(event) {
		var isCode = $item.classList.contains('mui-active');
		if(isCode){
			if (!/1\d{10}/.test($mobile.value)) return mui.toast('手机号输入错误');
			if (!/\d{6}/.test($loginCode.value)) return mui.toast('验证码输入错误');
		}else {
			if (!/1\d{10}/.test($loginName.value)) return mui.toast('账号输入错误');
			if ($loginPassword.value.length < 6) return mui.toast('密码输入有误');
		}
		var url = isCode?'/user/mobileLoginSubmit':'/user/loginSubmit';
		$loginBtn.setAttribute('disabled', true);
		mui.getJSON(url,{
			username: $loginName.value,
			password: $loginPassword.value,
			mobile: $mobile.value,
			code: $loginCode.value
		},function(data){
			mui.toast(data.msg);
			if(data.success){
				viewApi.go('#home');
			}
			setTimeout(function(){
				$loginBtn.removeAttribute('disabled');						
			}, 2000)
		})
	});
	var lt, lcd = 60, ls = false;
	$loginSendBtn.addEventListener('tap', function(event) {
		if(ls) return;
		if (!/1\d{10}/.test($mobile.value)) return mui.toast('手机号输入错误');
		ls = true;
		lst();
		lt = setInterval(function(){lst()},1000);
		mui.getJSON('/user/sendSmsCode',{
			mobile: $mobile.value
		},function(data){
			mui.toast(data.msg);
		})
	}, false);
	function lst() {
		if (lcd <= 0) {
			clearInterval(lt);
			ls = false;
			$loginSendBtn.innerText = "重发动态码";
			lcd = 60; 
		} else {
			$loginSendBtn.innerText = lcd + "秒后重发"; 
			lcd--;
		}
	};
	var $registerBtn = document.getElementById('registerBtn');
	var $registerName = document.getElementById('registerName');
	var $registerPassword = document.getElementById('registerPassword');
	var $repassword = document.getElementById('repassword');
	var $registerCode = document.getElementById('registerCode');
	var $inviterId = document.getElementById('inviterId');
	var $registerSendBtn = document.getElementById('registerSendBtn');
	$registerBtn.addEventListener('tap', function(event) {
		if (!/1\d{10}/.test($registerName.value)) return mui.toast('手机号输入错误');
		if ($registerPassword.value.length < 6) return mui.toast('密码最少为6位');
		if ($registerPassword.value!=$repassword.value) return mui.toast('两次密码必须一致');
		if (!/\d{6}/.test($registerCode.value)) return mui.toast('验证码输入错误');
		$registerBtn.setAttribute('disabled', true);
		mui.getJSON('/user/registerSubmit',{
			username: $registerName.value,
			password: $registerPassword.value,
			repassword: $repassword.value,
			code: $registerCode.value,
			id: $inviterId.value
		},function(data){
			mui.toast(data.msg);
			if(data.success){
				viewApi.go('#home');
			}
			setTimeout(function(){
				$registerBtn.removeAttribute('disabled');						
			}, 2000)
		})
	});
	var st, scd = 60, ss = false;
	$registerSendBtn.addEventListener('tap', function(event) {
		if(ss) return;
		if (!/1\d{10}/.test($registerName.value)) return mui.toast('手机号输入错误');
		ss = true;
		sst();
		st = setInterval(function(){sst()},1000);
		mui.getJSON('/user/sendSmsCode',{
			mobile: $registerName.value
		},function(data){
			mui.toast(data.msg);
		})
	}, false);
	function sst() {
		if (scd <= 0) {
			clearInterval(st);
			ss = false;
			$registerSendBtn.innerText = "重发动态码";
			scd = 60; 
		} else {
			$registerSendBtn.innerText = scd + "秒后重发"; 
			scd--;
		}
	};
	document.cookie='ignore=true;path=/'
})();