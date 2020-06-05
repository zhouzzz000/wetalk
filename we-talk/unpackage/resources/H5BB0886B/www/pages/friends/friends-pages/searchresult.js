import {User} from "../../../js/model/user.js";
import {Utils} from "../../../js/utils.js";
import {Http} from "../../../js/http.js";
import {config} from "../../../js/config.js";
var app = new Vue({
	el: "#app-searchresult",
	data: {
		friendInfo:"",
		meInfo:"",
	},

	created:function() {
		this.initFriendInfo();
	},
	methods: {
		tap2add:function(){
			mui.plusReady(async ()=> {
				var myid = this.meInfo.id;
				var friendId = this.friendInfo.id;
				if(myid === friendId)
				{
					Utils.showToast("不能添加自己...","inform");
					return;
				}
				if(myid.length < 1 || friendId.length<1)
				{
					Utils.showToast("好友请求发送失败，请重新发送...","inform");
					return;
				}
				plus.nativeUI.showWaiting("好友请求发送...");
				var res = await Http.request({
					url: config.api_base_url + "friends/add",
					type:"post",
					data:{
						sendUserId:myid,
						acceptUserId:friendId
					}
				});
				console.log(res);
				plus.nativeUI.closeWaiting();
				if(res.status !== 200)
				{
					Utils.showToast(res.msg,"inform");
					mui.openWindow("index.html","index");
					return ;
				}
				Utils.showToast(res.data,"success");
				mui.openWindow("index.html","index");
			 });
		},
		initFriendInfo(){
			mui.plusReady(()=>{
				this.meInfo = User.getUserGlobalInfo();
				var currentWebview = plus.webview.currentWebview();
				this.friendInfo = currentWebview.friendInfo;
			});
		},
		tap2back:function(){
			mui.back();
		}
	}
})
