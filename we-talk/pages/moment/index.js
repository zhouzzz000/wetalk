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
	Moment
} from "../../js/model/moment.js";


var app = new Vue({
	el: "#app-moment",
	data: {
		userInfo: null,
		curPage: 0,
		moments: [],
		momentBackgroundImg: "",
	},

	created: function() {
		mui.plusReady(() => {
			this.userInfo = User.getUserGlobalInfo();
			this.momentBackgroundImg = this.userInfo.momentBackgroundImg;
		});
		window.addEventListener("addMoments", (data) => {
			this.addMoments(data);
		})
	},
	mounted: function() {
		mui.plusReady(() => {
			this.initMoments();
		});
	},
	methods: {
		async initMoments() {
			var curwebView = plus.webview.currentWebview();
			var userId = curwebView.userId;
			var res;
			if(!userId)
			{
				res = await Moment.getMomentsById(this.userInfo.id);
				console.log(res);
			}
			else{
				res = await Moment.getMomentsByIdOnlyOwn(userId);
			}
			if (res.status === 200) {
				var momentArr = res.data;
				for (var i = 0; i < momentArr.length; i++) {
					var imageStr = momentArr[i].images;
					imageStr = imageStr.substr(0, imageStr.length - 1);
					if (imageStr != null || imageStr != undefined) {
						momentArr[i].images = imageStr.split(",");
					}
				}
				this.moments = res.data;
			}
		},
		tap2edit() {
			mui.openWindow("./moment-edit/index.html", "moment-edit");
		},
		tap2uploadImage() {
			mui.plusReady(() => {
				mui("#sheet-avator").popover("toggle");
			})
		},
		selectImage() {
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
				var img = new Image();
				img.crossOrigin = ""; //跨域
				img.src = imgPath;
				img.onload = async () => {
					var base64 = this.getBase64Image(img);
					var res = await Http.request({
						url: config.api_base_url + "file/image/upload_moment_img",
						data: {
							userId: this.userInfo.id,
							faceBase64Data: base64,
						},
						type: 'post'
					})
					if (res.status != 200) {
						Utils.showToast("上传失败...", "imform");
						return;
					}
					this.userInfo = res.data;
					User.setUserGlobalInfo(this.userInfo);
				}

			}, function(e) {
				console.log("取消选择图片");
			}, {
				filter: "image"
			});
		},
		downloadImage() {
			mui("#sheet-avator").popover("toggle");
			mui.plusReady(() => {
				plus.nativeUI.showWaiting("下载中");
				var avatorImage = this.$data.userInfo.momentBackgroundImg;
				if (avatorImage != null) {
					var task = plus.downloader.createDownload(
						avatorImage, {},
						(downloadFile, status) => {
							plus.nativeUI.closeWaiting();
							if (status == 200) {
								var tempFile = downloadFile.filename;
								plus.gallery.save(tempFile, () => {
									Utils.showToast("保存照片到相册成功！", "success");
								})
							} else {
								Utils.showToast("下载错误...", "failure");
								console.log("下载错误");
							}
						},
					);
					task.start(); //开始任务
				}
			})
		},
		downloadImageFromMoment(imgUrl){
			var binArr = ['下载','取消'];
			mui.confirm("是否下载图片到相册？","下载图片",binArr,(e)=>{
				if(e.index == 0)
				{
					mui.plusReady(() => {
						plus.nativeUI.showWaiting("下载中");
						var avatorImage = imgUrl;
						if (avatorImage != null) {
							var task = plus.downloader.createDownload(
								avatorImage, {},
								(downloadFile, status) => {
									plus.nativeUI.closeWaiting();
									if (status == 200) {
										var tempFile = downloadFile.filename;
										plus.gallery.save(tempFile, () => {
											Utils.showToast("保存照片到相册成功！", "success");
										})
									} else {
										Utils.showToast("下载错误...", "failure");
										console.log("下载错误");
									}
								},
							);
							task.start(); //开始任务
						}
					})
				}
			})
		},
		getBase64Image(img) {
			var canvas = document.createElement("canvas"); // 创建一个canvas
			canvas.width = img.width; // 设置对应的宽高
			canvas.height = img.height;
			var ctx = canvas.getContext("2d"); // 二维绘图环境
			ctx.drawImage(img, 0, 0, img.width, img.height); // 将图片画在画布上
			var ext = img.src.substring(img.src.lastIndexOf(".") + 1).toLowerCase(); // 获取到图片的格式
			var dataURL = canvas.toDataURL("image/" + ext); // 得到base64 编码的 dataURL
			console.log(dataURL)
			return dataURL;

		},
		upfresh() {
			this.curPage++;
			console.log(this.curPage);
		},
		addMoments(e) {
			var data = e.detail;
			this.moments.unshift(data);
		},
		tap2OwnMoment(){
			// console.log(111);
			mui.openWindow({
				url:"./own.html",
				id:"own",
				style:{},
				extras:{
					userId:this.userInfo.id,
				}
			});
		},
		tap2friendMoment(id)
		{
			mui.openWindow({
				url:"./own.html",
				id:"own",
				style:{},
				extras:{
					userId:id,
				}
			});
		}
	}
})



function xx() {
	mui.plusReady(function() {
		var curWebView = plus.webview.currentWebview();
		mui.fire(curWebView, "refreshMoment")
	})
}
