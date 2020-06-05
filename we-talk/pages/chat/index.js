import {config} from "../../js/config.js";
import {User} from "../../js/model/user.js";
import {Utils} from "../../js/utils.js";
import {Http} from "../../js/http.js";
import {DataContent} from "../../js/model/data_content.js";
import {Message} from "../../js/model/message.js";
import {action} from "../../js/enum.js";
import {MessageRecord} from "../../js/model/message_record.js";
	//构建聊天业务
	window.CHAT = {
		socket:null,
		init:function(){
			console.log("init...");
			if(window.WebSocket)
			{
				if(CHAT.socket != null && CHAT.socket != undefined && CHAT.socket.readyState == WebSocket.OPEN){
					return false;
				}
				CHAT.socket = new WebSocket(config.wssocket_url);
				CHAT.socket.onopen = CHAT.wsopen;
				CHAT.socket.onclose = CHAT.wsclose;
				CHAT.socket.onerror = CHAT.wserror;
				CHAT.socket.onmessage = CHAT.wsmessage;
			}else{
				alter("设备不支持websocket协议。。。");
			}
		},
		wsopen:()=>{
			console.log("websocket连接已建立...")
			mui.plusReady(async () =>{
			    var me = User.getUserGlobalInfo();
			    var newMessage = new Message({
			    	sendUserId:me.id,
			    });
			    var dataContent = new DataContent({
			    	action:action.CONNECT,
			    	message:newMessage,
			    })
			    CHAT.chat(JSON.stringify(dataContent));
				var unreadIds = "";
				var unreadList = await MessageRecord.fetchUnreadMessageList(me.id);
				for (var i = 0; i < unreadList.length; i++) {
					var msgObj = unreadList[i];
					unreadIds = unreadIds + msgObj.id + ",";
					MessageRecord.saveUserMessageHistory(msgObj,2);
					MessageRecord.saveUserChatSnapshot({
						myId:msgObj.acceptUserId,
						chatUserId: msgObj.sendUserId,
						idRead:0,
						msg:msgObj.content,
					})
				}
				CHAT.signedMsgList(unreadIds);
			})
			// 设置定时发送心跳
			setInterval("CHAT.keepalive()",10000);
		},
		wsclose:()=>{
			console.log("websocket连接已关闭...")
		},
		wserror:(err)=>{
			console.log("连接发生错误：" + err.data);
			// CHAT.init();
		},
		wsmessage:(e)=>{
			mui.plusReady(()=> {
				// 转换为对象
				var chatData = JSON.parse(e.data);
				if(chatData.action == action.CHAT){
					var message = chatData.message;
					var chatUserId = message.sendUserId;
					var chatWebview = plus.webview.getWebviewById("chatting-"+chatUserId);
					var isRead=0;
					if(chatWebview != null){
						chatWebview.evalJS("app.accept('"+JSON.stringify(message)+"')")
						isRead = 1;
					}else{
						app.playAcceptMsgSound();
					}
					//签收消息
					var dataContentSign = new DataContent({
						action:action.SIGNED,
						extend:message.id
					});
					CHAT.chat(JSON.stringify(dataContentSign));
					//保存聊天历史记录到缓存
					MessageRecord.saveUserMessageHistory(message,2);
					// 保存快照到本地
					MessageRecord.saveUserChatSnapshot({
						myId:message.acceptUserId,
						chatUserId:message.sendUserId,
						msg:message.content,
						isRead:isRead,
						type:message.type
					})
				}else if(chatData.action == action.REFRESH_FRIEND)
				{
					var contactListWebview = plus.webview.getWebviewById("contact-list");
					mui.fire(contactListWebview, "refreshRequest");
				}
			})
		},
		chat:(msg)=>{
			if(CHAT.socket != null && CHAT.socket != undefined && CHAT.socket.readyState == WebSocket.OPEN){
				CHAT.socket.send(msg);
			}else{
				console.log("消息重新发送");
				CHAT.init();
				if(CHAT.socket.readyState == WebSocket.OPEN)
				{
					CHAT.socket.send(msg);
				}
			}
		},
		signedMsgList:(ids)=>{
			//签收消息
			var dataContentSign = new DataContent({
				action:action.SIGNED,
				extend:ids
			});
			CHAT.chat(JSON.stringify(dataContentSign));
		},
		keepalive:async ()=>{
			// 构建心跳对象
			var dataContentSign = new DataContent({
				action:action.KEEPALIVE,
			});
			CHAT.chat(JSON.stringify(dataContentSign));
			// 定时获取未读聊天记录
			var me = User.getUserGlobalInfo();
			var unreadIds = "";
			var unreadList = await MessageRecord.fetchUnreadMessageList(me.id);
			for (var i = 0; i < unreadList.length; i++) {
				var msgObj = unreadList[i];
				unreadIds = unreadIds + msgObj.id + ",";
				MessageRecord.saveUserMessageHistory(msgObj,2);
				MessageRecord.saveUserChatSnapshot({
					myId:msgObj.acceptUserId,
					chatUserId: msgObj.sendUserId,
					idRead:0,
					msg:msgObj.content,
					type:msgObj.type,
				})
			}
			if(unreadIds != ""){
				CHAT.signedMsgList(unreadIds);
			}
			// 定时获取最新的好友列表信息
			var contactListWebview = plus.webview.getWebviewById("contact-list");
			mui.fire(contactListWebview, "refreshRequest");
		}
	}
	
	// CHAT.init();
	window.addEventListener("initChat",()=>{
		CHAT.init();
	})
	mui.plusReady( ()=> {
	    var curWebview = plus.webview.currentWebview();
		curWebview.addEventListener("show",()=>{
			CHAT.init();
		})
	})
	
	// CHAT.init();
	var app = new Vue({
		el: "#app-chat",
		data: {
			loginUserInfo:{},
			chatSnapshotList:[],
			friendList:null,
			TEXT: 1,
			IMAGE: 2,
			AUDIO: 3,
			VEDIO: 4,
		},
		watch:{
		
		},
		created: function() {
			this.initUserInfo();
			window.addEventListener("refreshSnapshotList",()=>{
				this.initChatSnapshot();
			})
		},
		methods: {
			// 播放接受消息的铃声
			playAcceptMsgSound(){
				var audioPlayer = plus.audio.createPlayer("../../mp3/9478.mp3");
				audioPlayer.play();
			},
			initUserInfo(){
				mui.plusReady(() => {
				    this.loginUserInfo = User.getUserGlobalInfo();
					this.initChatSnapshot();
				})
			},
			initChatSnapshot()
			{
				mui.plusReady(() => {
					if(this.friendList == null)
					{
						this.friendList = User.getFriendListFromStorage();
					}
					
					var snapshotList = MessageRecord.getUserChatSnapshot(this.loginUserInfo.id);
					for (var i = 0; i < snapshotList.length; i++) {
						var friendInfo = this.getFriendInfoFromFriendList(snapshotList[i].chatUserId);
						if(friendInfo != null)
						{
							snapshotList[i].avator = friendInfo.faceImage;
							snapshotList[i].nickname = friendInfo.nickname;
						}
					}
					this.chatSnapshotList = snapshotList;
				})
			},
			getFriendInfoFromFriendList(friendId){
				for (var i = 0; i < this.friendList.length; i++) {
					if(this.friendList[i].id == friendId)
					{
						
						return this.friendList[i];
					}
				}
				return null;
			},
			tap2chatting(chatUserId)
			{
				mui.openWindow({
					url:'./chatting.html',
					id:'chatting-'+chatUserId,
					extras:{
						chatUserId:chatUserId
					}
				});
			},
			removeSnapshotById(chatUserId)
			{
				MessageRecord.removeSnapshotById(this.loginUserInfo.id, chatUserId);
			}
		}
	})
	