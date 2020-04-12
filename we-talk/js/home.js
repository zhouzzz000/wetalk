import {
	GetJson
} from "./model/getJson.js";
// var wetalkArray = [
// 	{
// 		pageId:"chat",
// 		pageUrl:"pages/chat/index.html"
// 	},
// 	{
// 		pageId:"contact-list",
// 		pageUrl:"pages/contact-list/index.html"
// 	},
// 	{
// 		pageId:"friends",
// 		pageUrl:"pages/friends/index.html"
// 	},
// 	{
// 		pageId:"me",
// 		pageUrl:"pages/me/index.html"
// 	}
// ];



var app1 = new Vue({
	el: "#app1",
	data: {
		tabbar:"",
	},

	created:function() {
		 this.initTabbar();
	},
	methods: {
		async initTabbar() {
			// 初始化界面
			let tabbar1 = await GetJson.tabbar();
			let tabbar = tabbar1.tabbar;
			this.tabbar = tabbar;
			mui.plusReady(() => {
				mui.back = function(){
					return false;
				}
				var curWebview = plus.webview.currentWebview();
				var pageStype = {
					top: "44px",
					bottom: "50px"
				}
				// 将别的界面加载进来
				for (var i = 0; i < tabbar.length; i++) {
					var wePage = plus.webview.create(tabbar[i].pageUrl, tabbar[i].pageId,
						pageStype);
					wePage.hide();
					// 追加到当前主界面
					curWebview.append(wePage);
				}
				plus.webview.show(tabbar[0].pageId);
				mui(".mui-bar-tab").on("tap", "a", function() {
					var index = this.getAttribute("index");
					plus.webview.show(tabbar[index].pageId, "fade-in", 500);
					for (var i = 0; i < tabbar.length; i++) {
						if (index != i) {
							plus.webview.hide(tabbar[i].pageId, "fade-out", 500);
						}
					}
				})
			});
		},
	}
})
