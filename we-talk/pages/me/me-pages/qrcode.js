import {User} from "../../../js/model/user.js";
import {Utils} from "../../../js/utils.js";
import {Http} from "../../../js/http.js";
import {config} from "../../../js/config.js";
var app = new Vue({
	el: "#app-qrcode",
	data: {
		userInfo:""
	},

	created:function() {
		this.initUserInfo();
	},
	methods: {
		initUserInfo(){
			mui.plusReady(()=>{
			    this.userInfo = User.getUserGlobalInfo();
			})
		},
		downloadQrcode:function(){
			mui.plusReady(()=> {
			    plus.nativeUI.showWaiting("下载中");
				var qrcodeImage = this.userInfo.qrcode;
				var task = plus.downloader.createDownload(
					qrcodeImage,
					{},
					(downloadFile,status)=>{
						plus.nativeUI.closeWaiting();
						if(status == 200){
							var tempFile = downloadFile.filename;
							plus.gallery.save(tempFile,()=>{
								Utils.showToast("保存二维码到相册成功！","success");
							})
						}else{
							Utils.showToast("下载错误...","failure");
							console.log("下载错误");
						}
					},
				);
				task.start();//开始任务
			})
		}
	}
})
