import {
	User
} from "../../js/model/user.js";
import {
	Utils
} from "../../js/utils.js";
import {
	Http
} from "../../js/http.js";
import {
	config
} from "../../js/config.js";
import {
	Message
} from "../../js/model/message.js";
import {
	msgType,
	action
} from "../../js/enum.js";
import {
	DataContent
} from "../../js/model/data_content.js";
import {
	MessageRecord
} from "../../js/model/message_record.js";
window.app = new Vue({
	el: "#app-chatting",
	data: {
		loginUserInfo: null,
		chatUserInfo: null,
		sendTextContent: '',
		isInputing: false,
		record: [],
		speech: 1,
		rec: null,
		isRecord: 0,
		TEXT: 1,
		IMAGE: 2,
		AUDIO: 3,
		VEDIO: 4,

	},
	watch: {

	},
	updated:function(){
		this.scrollToLastest();
	},
	// mounted: function () {
	//  // this.$nextTick(function() {
	//  	var msgListArea = document.getElementById('msg');
	// 	console.log(msgListArea);
	//  	msgListArea.scrollTop = msgListArea.scrollHeight + msg.offsetHeight;
	//  // s})
	// },
	created: function() {
		this.initChattingUserInfo();
		this.resizeWindowHeight();
		window.addEventListener('show', () => {
			MessageRecord.clearChatSnapshotUnread(this.loginUserInfo.id, this.chatUserInfo.id);
		});
		window.addEventListener('clearRecord', () => {
			console.log(1111);
			console.log(this.record);
			this.record=[];
			console.log(this.record);
		});
		this.rec = Recorder({
			type: "mp3",
			sampleRate: 16000,
			bitRate: 16
		});
	},
	methods: {
		initChattingUserInfo() {
			mui.plusReady(async () => {
				var curwebView = plus.webview.currentWebview();
				this.loginUserInfo = User.getUserGlobalInfo();
				var res = await Http.request({
					url: config.api_base_url + "user/seach_id?userId=" + curwebView.chatUserId,
				});
				this.chatUserInfo = res.data;
				this.initChatRecord();
				this.scrollToLastest();
				MessageRecord.clearChatSnapshotUnread(this.loginUserInfo.id, this.chatUserInfo.id);
			})

		},
		initChatRecord() {
			this.record = MessageRecord.getUserMessageHistory(this.loginUserInfo.id, this.chatUserInfo.id);
			this.scrollToLastest();
		},
		scrollToLastest() {
			this.$nextTick(() => {
				var msgListArea = this.$refs.msg;
				msgListArea.scrollTop = msgListArea.scrollHeight + msgListArea.offsetHeight;
			})
		},
		resizeWindowHeight() {
			window.addEventListener('resize', () => {
				this.scrollToLastest();
			})
		},
		tap2send: function() {
			mui.plusReady(() => {
				var networkStatus = plus.networkinfo.getCurrentType();
				if (networkStatus < 2) {
					Utils.showToast("请连接网络...", "inform");
					return;
				}
				if (this.sendTextContent.length === 0) {
					return;
				}
				this.send({
					sendUserId: this.loginUserInfo.id,
					acceptUserId: this.chatUserInfo.id,
					sendUserAvator: this.loginUserInfo.faceImage,
					acceptUserAvator: this.chatUserInfo.faceImage,
					content: this.sendTextContent,
					type: msgType.TEXT,
				});
			})
		},
		tap2focusInput: function() {
			document.getElementById('msg-text').focus();
		},
		send(data = {}) {
			data.flag = 1;
			this.record.push(data);
			// 保存到本地
			MessageRecord.saveUserMessageHistory(data, 1);
			// 保存快照
			MessageRecord.saveUserChatSnapshot({
				myId: data.sendUserId,
				chatUserId: data.acceptUserId,
				msg: data.content,
				isRead: 1,
				type: data.type
			})
			this.playSendMsgSound();
			var newMsg = new Message(data);
			var dataContent = new DataContent({
				action: action.CHAT,
				message: newMsg,
				extend: null,
			})
			this.chat(JSON.stringify(dataContent));
			this.sendTextContent = "";
			this.scrollToLastest();
		},
		accept(data) {
			var msg = JSON.parse(data);
			this.record.push(msg);
			this.playAcceptMsgSound();
			this.scrollToLastest();
		},
		// 播放发送消息的铃声
		playSendMsgSound() {
			var audioPlayer = plus.audio.createPlayer("../../mp3/send.mp3");
			audioPlayer.play();
		},
		// 播放接受消息的铃声
		playAcceptMsgSound() {
			var audioPlayer = plus.audio.createPlayer("../../mp3/9478.mp3");
			audioPlayer.play();
		},
		chat(msg) {
			var wsWebview = plus.webview.getWebviewById("chat");
			wsWebview.evalJS("CHAT.chat('" + msg + "')");
		},
		tap2input() {
			this.speech = 0;
		},
		tap2speech() {
			this.speech = 1;
		},
		hold2record() {
			this.rec.open(() => {
				this.isRecord = 1;
				console.log("record.start。。。");
				this.rec.start();
			}, function(msg, isUserNotAllow) { //用户拒绝未授权或不支持
				//dialog&&dialog.Cancel(); 如果开启了弹框，此处需要取消
				console.log((isUserNotAllow ? "UserNotAllow，" : "") + "无法录音:" + msg);
				plus.android.requestPermissions(["Audio"], (res) => {}, (err) => {});
			});
		},
		release2record() {
			this.isRecord = 0;
			console.log("record end...")
			var me = this;
			this.rec.stop((blob, duration) => {
				/*
				blob文件对象，可以用FileReader读取出内容
				，或者用FormData上传，本例直接上传二进制文件
				，对于普通application/x-www-form-urlencoded表单上传
				，请参考github里面的例子
				*/
				if (duration < 500) {
					mui.plusReady(() => {
						Utils.showToast("时间太短...", "inform");
					})
					me.rec.close();
					return;
				}
				// console.log(blob,(window.URL||webkitURL).createObjectURL(blob),"时长:"+duration+"ms");
				me.rec.close();
				var form = new window.FormData();
				form.append("audioFile", blob, "record.mp3");

				console.log(form);
				var url = config.api_base_url + "file/audio/upload";
				mui.ajax({
					url: url,
					dataType: 'json',
					type: "POST",
					data: form,
					processData: false, //必须加
					contentType: false, //必须加
					success: (res) => {
						var audioUrl = res.data;
						this.send({
							sendUserId: this.loginUserInfo.id,
							acceptUserId: this.chatUserInfo.id,
							sendUserAvator: this.loginUserInfo.faceImage,
							acceptUserAvator: this.chatUserInfo.faceImage,
							content: audioUrl + "%" + duration / 1000,
							type: msgType.AUDIO,
						});
						this.scrollToLastest();
					},
					error: (err) => {
						console.log(err);
					},
				});
			}, function(msg) {
				console.log("录音失败:" + msg);
				me.rec.close(); //可以通过stop方法的第3个参数来自动调用close
				// me.rec=null;
			});
		},
		swipe2cancel() {
			this.isRecord = 0;
			console.log("record cancel...");
		},
		tap2playAudio(content) {
			var url = content.split("%")[0];
			var audioPlayer = plus.audio.createPlayer(url);
			audioPlayer.play();
		},
		tap2selectImage() {
			mui("#sheet-avator").popover("toggle");
		},
		select4photo() {
			mui("#sheet-avator").popover("toggle");
			mui.plusReady(() => {
				plus.android.requestPermissions(["Camera"], (res) => {}, (err) => {
					console.log(err);
				});
				var cmr = plus.camera.getCamera();
				var reso = cmr.supportedImageResolutions[13];
				var fmt = cmr.supportedImageFormats[0];
				cmr.captureImage((res) => {
						console.log(res);
						var userInfo = this.loginUserInfo;
						userInfo.faceImage = res;
						this.loginUserInfo = userInfo;
					},
					(err) => {
						console.log(err);
					}, {
						resolution: reso,
						format: 'png'
					})

			})
		},
		select4gallery() {
			mui("#sheet-avator").popover("toggle");
			var imgPath;
			plus.gallery.pick((path) => {
				console.log(path);
				// 生成base64编码
				imgPath = path;
				if (imgPath == null || imgPath == undefined) {
					Utils.showToast("选取图片失败...", "inform")
					return;
				}
				mui.plusReady( () =>{
					Utils.showWait("发送中...");
				})
				var img = new Image();
				img.crossOrigin = ""; //跨域
				img.src = imgPath;
				img.onload = async () => {
					var base64 = this.getBase64Image(img);
					var blob = this.dataURLtoBlob(base64);
					console.log(blob);
					this.uploadFile(blob,'file/image/upload',"image.jpg",this.sendImage,img.height,img.width);
				}

			}, (e)=> {
				mui.plusReady( () =>{
					plus.nativeUI.closeWaiting();
					Utils.showToast("选取失败...","inform");
				})
				console.log("取消选择图片");
			}, {
				filter: "image"
			});
		},
		sendImage:function(res,height,width){
			this.send({
				sendUserId: this.loginUserInfo.id,
				acceptUserId: this.chatUserInfo.id,
				sendUserAvator: this.loginUserInfo.faceImage,
				acceptUserAvator: this.chatUserInfo.faceImage,
				content: res + "?" + 'height='+height + '&width=' + width ,
				type: msgType.IMAGE,
			});
		},
		getBase64Image(img) {
			var canvas = document.createElement("canvas"); // 创建一个canvas
			canvas.width = img.width; // 设置对应的宽高
			canvas.height = img.height;
			var ctx = canvas.getContext("2d"); // 二维绘图环境
			ctx.drawImage(img, 0, 0, img.width, img.height); // 将图片画在画布上
			var ext = img.src.substring(img.src.lastIndexOf(".") + 1).toLowerCase(); // 获取到图片的格式
			var dataURL = canvas.toDataURL("image/" + ext); // 得到base64 编码的 dataURL
			return dataURL;
		
		},
		dataURLtoBlob(dataurl) {
			 var arr = dataurl.split(',');
			  //注意base64的最后面中括号和引号是不转译的   
			  var _arr = arr[1].substring(0,arr[1].length-2);
			  var mime = arr[0].match(/:(.*?);/)[1],
				  bstr =atob(_arr),
				  n = bstr.length,
				  u8arr = new Uint8Array(n);
			  while (n--) {
				  u8arr[n] = bstr.charCodeAt(n);
			  }
			  return new Blob([u8arr], {
				  type: mime
			  });
		  },
		uploadFile(blob,url,name,cb,height,width)
		{
			if(url == null || name == null)
			{
				return;
			}
			var form = new window.FormData();
			form.append("file", blob, name);
			var url = config.api_base_url + url;
			mui.ajax({
				url: url,
				dataType: 'json',
				type: "POST",
				data: form,
				processData: false, //必须加
				contentType: false, //必须加
				success: (res) => {
					console.log(res);
					mui.plusReady( () =>{
						plus.nativeUI.closeWaiting();
					})
					if(res.status != 200)
					{
						mui.plusReady( () =>{
							Utils.showToast("发送失败","inform");
						})
					}
					console.log(res);
					cb(res.data,height,width);
				},
				error: (err) => {
					console.log(err);
					mui.plusReady( () =>{
						plus.nativeUI.closeWaiting();
						Utils.showToast("发送...","inform");
					})
				},
			});
		},
		longtap2remove(index,id){
			console.log(id + " " + index);
			mui.confirm("是否删除该条消息?","消息",['是','否'],(res)=>{
				var ix = res.index;
				if(ix === 0)
				{
					this.record.splice(index,1);
					MessageRecord.saveUserMessageHistoryAll(this.record,this.loginUserInfo.id,this.chatUserInfo.id);
				}
			})
		},
		tap2friendManage(sendUserId){
			if(sendUserId ==  this.loginUserInfo.id)
			{
				// mui.openWindow("../me/index.html","me");
			}else{
				mui.openWindow(
				{
					url:"../friends/friend-magage/index.html",
					id:"friend-manage",
					style:{},
					extras:{
						friendInfo:this.chatUserInfo,
					}
				});
			}
		}
	}
})
