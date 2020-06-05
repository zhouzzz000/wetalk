import {
	User
} from "../../../js/model/user.js";
import {
	Utils
} from "../../../js/utils.js";
import {
	Http
} from "../../../js/http.js";
import {
	config
} from "../../../js/config.js";
var app = new Vue({
	el: "#app-requestlist",
	data: {
		requestlist: [],
	},

	created: function() {
		this.initRequestList();
	},
	methods: {
		initRequestList() {
			mui.plusReady(() => {
				var curWebview = plus.webview.currentWebview();
				this.requestlist = curWebview.requestList;
			})
		},
		async tap2refuse(id,index)
		{
			var acceptUserId = User.getUserGlobalInfo().id;
			mui.plusReady(async () => {
			    var wait = Utils.showWait("请稍等...");
				var res = await Http.request({
					url:config.api_base_url + "friends/refuse",
					type:"post",
					data:{
						sendUserId:id,
						acceptUserId:acceptUserId
					}
				});
				console.log(res);
				wait.close();
				if(res.status === 200)
				{
					var sendUserNickname = this.requestlist[index].sendUserNickname;
					Utils.showToast("已拒绝","inform");
					this.requestlist.splice(index,1);
					var contactListWebview = plus.webview.getWebviewById("contact-list");
					mui.fire(contactListWebview, "refreshRequest");
				}else{
					Utils.showToast("请重试...","inform");
				}
			})
		},
		async tap2accept(id,index)
		{
			var acceptUserId = User.getUserGlobalInfo().id;
			mui.plusReady(async () => {
			    var wait = Utils.showWait("请稍等...");
				var res = await Http.request({
					url:config.api_base_url + "friends/accept",
					type:"post",
					data:{
						sendUserId:id,
						acceptUserId:acceptUserId
					}
				});
				wait.close();
				if(res.status === 200)
				{
					console.log(res);
					var sendUserNickname = this.requestlist[index].sendUserNickname;
					Utils.showToast("成功和"+sendUserNickname + "成为朋友","success");
					this.requestlist.splice(index,1);
					var contactListWebview = plus.webview.getWebviewById("contact-list");
					mui.fire(contactListWebview, "refreshRequest");
				}else{
					Utils.showToast("请重试...","inform");
				}
			})
		},
	}
})

mui.init({
	pullRefresh: {
		container: "#app-requestlist", //下拉刷新容器标识，querySelector能定位的css选择器均可，比如：id、.class等
		down: {
			style: "circle",
			callback: function() {
				 mui('#app-requestlist').pullRefresh().endPulldown();
			}
		},
	}
})
