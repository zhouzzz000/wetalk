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
				})
			})
		},
		refreshUserInfo(){
			this.userInfo = User.getUserGlobalInfo();
		},
		exit:function(){
			mui.plusReady(()=>{
				plus.storage.setItem("userInfo","");
				plus.runtime.restart();
			})
		},
		tap2avator:function(){
			mui.plusReady(function () {
			    mui.openWindow("./me-pages/avator.html","avator");
			})
		}
	}
})
