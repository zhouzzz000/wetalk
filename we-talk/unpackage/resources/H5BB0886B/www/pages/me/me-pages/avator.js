import {User} from "../../../js/model/user.js";
import {Utils} from "../../../js/utils.js";
var app = new Vue({
	el: "#app-avator",
	data: {
		userAvator:"",
	},

	created:function() {
		this.initAvator();
	},
	methods: {
		initAvator(){
			mui.plusReady(()=> {
			    this.userAvator = User.getUserGlobalInfo().faceImageFull;
				if(this.userAvator === "")
				{
					this.userAvator = "../../../css/registLogin/login.jpg"; 
				}
				//自定义事件，刷新头像
				window.addEventListener("refresh",()=>{
					this.userAvator = User.getUserGlobalInfo().faceImageFull;
				});
			})
		},
		openSelectView:function()
		{
			mui("#sheet-avator").popover("toggle");
		},
		selectImage:function(){
			mui("#sheet-avator").popover("toggle");
			mui.openWindow({
				url:"../../../plugin/v3.1.6/myface-uploader.html",
				id:"myface-uploader",
				createNew:true
				});
		},
		downloadImage:function(){
			mui.plusReady(()=> {
			    plus.nativeUI.showWaiting("下载中");
				var avatorImage = this.$data.userAvator;
				var task = plus.downloader.createDownload(
					avatorImage,
					{},
					(downloadFile,status)=>{
						plus.nativeUI.closeWaiting();
						if(status == 200){
							var tempFile = downloadFile.filename;
							plus.gallery.save(tempFile,()=>{
								Utils.showToast("保存照片到相册成功！","success");
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
