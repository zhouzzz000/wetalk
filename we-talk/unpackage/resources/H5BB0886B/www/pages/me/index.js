import {User} from "../../js/model/user.js"
var app = new Vue({
	el: "#app-me",
	data: {
		userInfo:"",
	},

	created:function() {
		 this.initUserInfo()
	},
	methods: {
		initUserInfo() {
			mui.plusReady(()=>{
				// this.userInfo = User.getUserGlobalInfo("userInfo");
				var curwebview = plus.webview.currentWebview();
				curwebview.addEventListener("show",()=>{
					this.refreshUserInfo();
				});
				//自定义事件，刷新头像
				window.addEventListener("refresh",()=>{
					this.refreshUserInfo();
				});
			})
		},
		refreshUserInfo(){
			this.userInfo = User.getUserGlobalInfo();
		},
		exit:function(){
			mui.plusReady(()=>{
				User.userLogout();
			})
		},
		tap2avator:function(){
			mui.plusReady(function () {
			    mui.openWindow("./me-pages/avator.html","avator");
			})
		},
		tap2nickname:function(){
			mui.plusReady(function () {
			    mui.openWindow("./me-pages/nickname.html","nickname");
			})
		},
		tap2qrcode:function(){
			mui.plusReady(function () {
			    mui.openWindow("./me-pages/qrcode.html","qrcode");
			})
		}
	}
})
