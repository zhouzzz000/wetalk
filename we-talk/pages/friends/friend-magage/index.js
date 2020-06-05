import {User} from "../../../js/model/user.js";
import {Utils} from "../../../js/utils.js";
import {Http} from "../../../js/http.js";
import {config} from "../../../js/config.js";
import {MessageRecord} from "../../../js/model/message_record.js";
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
		tap2chat:function(){
			var chatUserId = this.friendInfo.id;
			mui.openWindow({
				url:'../../chat/chatting.html',
				id:'chatting-'+chatUserId,
				extras:{
					chatUserId:chatUserId
				}
			});
		},
		tap2moment:function(){
			mui.openWindow({
				url:"../../moment/index.html",
				id:"moment",
				style:{},
				extras:{
					userId:this.friendInfo.id,
				}
			});
		},
		tap2clearHistTory:function(){
			var friendInfo = this.friendInfo;
			mui.confirm("是否清空与"+friendInfo.nickname + "的消息记录?","清空消息",['是','否'],(res)=>{
				var ix = res.index;
				if(ix === 0)
				{
					MessageRecord.clearUserMessageHistoryAll(User.getUserGlobalInfo().id, friendInfo.id);
					MessageRecord.removeSnapshotById(User.getUserGlobalInfo().id, friendInfo.id);
					mui.plusReady(function () {
					    Utils.showToast("清空成功...","success");
					})
					var chattingWebview = plus.webview.getWebviewById("chatting-"+friendInfo.id);
					mui.fire(chattingWebview,"clearRecord");
				}
			})
		},
		tap2deleteFriend:function(){
			var friendInfo = this.friendInfo;
			mui.confirm("是否删除"+friendInfo.nickname + "?","删除好友",['是','否'],(res)=>{
				var ix = res.index;
				if(ix === 0)
				{
					// MessageRecord.clearUserMessageHistoryAll(User.getUserGlobalInfo().id, friendInfo.id);
					// MessageRecord.removeSnapshotById(User.getUserGlobalInfo().id, friendInfo.id);
					// mui.plusReady(function () {
					//     Utils.showToast("删除成功...","success");
					// })
					// var chattingWebview = plus.webview.getWebviewById("chatting-"+friendInfo.id);
					// mui.fire(chattingWebview,"clearRecord");
					mui.plusReady( ()=> {
						var chattingWebview = plus.webview.getWebviewById("chatting-"+friendInfo.id);
						chattingWebview.close();
					    plus.webview.currentWebview().close();
						plus.webview.show("chat");
					})
				}
			})
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
