import {User} from "../../js/model/user.js";
import {Utils} from "../../js/utils.js";
import {Http} from "../../js/http.js";
import {config} from "../../js/config.js";
import {Message} from "../../js/model/message.js";
import {msgType, action} from "../../js/enum.js";
import {DataContent} from "../../js/model/data_content.js";
import {MessageRecord} from "../../js/model/message_record.js";
window.app = new Vue({
	el: "#app-chatting",
	data: {
		loginUserInfo:null,
		chatUserInfo:null,
		sendTextContent:'',
		isInputing:false,
		record:[
		]
	},
	watch:{
	
	},
	created: function() {
		this.initChattingUserInfo();
		this.resizeWindowHeight();
		window.addEventListener('show',()=>{
			MessageRecord.clearChatSnapshotUnread(this.loginUserInfo.id, this.chatUserInfo.id);
		})
	},
	methods: {
		initChattingUserInfo(){
			mui.plusReady(async ()=>{
				var curwebView = plus.webview.currentWebview();
				this.loginUserInfo = User.getUserGlobalInfo();
				var res = await Http.request({
					url:config.api_base_url + "user/seach_id?userId="+curwebView.chatUserId,
				});
				this.chatUserInfo = res.data;
				this.initChatRecord();
				this.scrollToLastest();
				MessageRecord.clearChatSnapshotUnread(this.loginUserInfo.id, this.chatUserInfo.id);
			})
			
		},
		initChatRecord(){
			this.record = MessageRecord.getUserMessageHistory(this.loginUserInfo.id, this.chatUserInfo.id);
		},
		scrollToLastest(){
			this.$nextTick(() => {
				var msgListArea = this.$refs.msg;
				msgListArea.scrollTop = msgListArea.scrollHeight + msg.offsetHeight;
			})
		},
		resizeWindowHeight(){
			window.addEventListener('resize',()=>{
				this.scrollToLastest();
			})
		},
		tap2send:function(){
			mui.plusReady(()=>{
				var networkStatus = plus.networkinfo.getCurrentType();
				if(networkStatus < 2)
				{
					Utils.showToast("请连接网络...","inform");
					return;
				}
				if(this.sendTextContent === 0)
				{
					return;
				}
				this.send({
					sendUserId:this.loginUserInfo.id,
					acceptUserId: this.chatUserInfo.id,
					sendUserAvator: this.loginUserInfo.faceImage,
					acceptUserAvator: this.chatUserInfo.faceImage,
					content:this.sendTextContent,
					type:msgType.TEXT,
				});
			})
		},
		tap2focusInput:function(){
			document.getElementById('msg-text').focus();
		},
		send(data={})
		{
			data.flag = 1;
			this.record.push(data);
			// 保存到本地
			MessageRecord.saveUserMessageHistory(data,1);
			// 保存快照
			MessageRecord.saveUserChatSnapshot({
				myId:data.sendUserId,
				chatUserId:data.acceptUserId,
				msg:data.content,
				isRead:1
			})
			this.playSendMsgSound();
			var newMsg = new Message(data);
			var dataContent = new DataContent({
				action:action.CHAT,
				message: newMsg,
				extend:null,
			})
			this.chat(JSON.stringify(dataContent));
			this.sendTextContent = "";
		},
		accept(data)
		{
			var msg = JSON.parse(data);
			this.record.push(msg);
			this.playAcceptMsgSound();
		},
		// 播放发送消息的铃声
		playSendMsgSound(){
			var audioPlayer = plus.audio.createPlayer("../../mp3/send.mp3");
			audioPlayer.play();
		},
		// 播放接受消息的铃声
		playAcceptMsgSound(){
			var audioPlayer = plus.audio.createPlayer("../../mp3/9478.mp3");
			audioPlayer.play();
		},
		chat(msg){
			var wsWebview = plus.webview.getWebviewById("chat");
			wsWebview.evalJS("CHAT.chat('"+msg+"')");
		}
	}
})
