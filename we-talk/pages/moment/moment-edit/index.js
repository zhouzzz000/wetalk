import {User} from "../../../js/model/user.js";
import {Utils} from "../../../js/utils.js";
import {Http} from "../../../js/http.js";
import {config} from "../../../js/config.js";
import {Moment} from "../../../js/model/moment.js";
var app = new Vue({
	el: "#app-moment-edit",
	data: {
		content:"",
		word:0,
		images:[],
		imagesBase64Data:[],
		selectIndex:-1,
		userInfo:null,
	},
	watch:{
		content:function(val,oldVal)
		{
			this.$data.word = val.length;
		}
	},
	created:function() {
		mui.plusReady( () => {
		    this.userInfo = User.getUserGlobalInfo();
		})
		
	},
	methods: {
		tap2selectImage(){
			var imgPath;
			plus.gallery.pick((path) => {
				imgPath = path;
				if (imgPath == null || imgPath == undefined) {
					Utils.showToast("选取图片失败...", "inform")
					return;
				}
				this.images.push(imgPath);
				var img = new Image();
				img.crossOrigin = ""; //跨域
				img.src = imgPath;
				img.onload = () => {
					var base64 = this.getBase64Image(img);
					this.imagesBase64Data.push(base64);
				}
			
			}, function(e) {
				console.log("取消选择图片");
			}, {
				filter: "image"
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
		longtap2remove(index){
			mui.plusReady(() => {
				this.selectIndex = index;
				mui("#sheet-avator").popover("toggle");
			})
		},
		replace(){
			mui.plusReady(() => {
				mui("#sheet-avator").popover("toggle");
				if(this.selectIndex == -1)
				{
					return;
				}
				plus.gallery.pick((path) => {
					var imgPath = path;
					if (imgPath == null || imgPath == undefined) {
						Utils.showToast("选取图片失败...", "inform")
						return;
					}
					this.$set(this.images,this.selectIndex,imgPath);
					var img = new Image();
					img.crossOrigin = ""; //跨域
					img.src = imgPath;
					img.onload = () => {
						var base64 = this.getBase64Image(img);
						this.imagesBase64Data[this.selectIndex] = base64;
						this.selectIndex = -1;
					}
				}, function(e) {
					console.log("取消选择图片");
				}, {
					filter: "image"
				});
			})
		},
		remove(){
			mui.plusReady(() => {
				mui("#sheet-avator").popover("toggle");
				if(this.selectIndex == -1)
				{
					return;
				}
				this.images.splice(this.selectIndex,1);
				this.imagesBase64Data.splice(this.selectIndex,1);
				this.selectIndex = -1;
			})
		},
		tap2upload(){
			mui.plusReady( async ()=> {
			    if(this.word <=0 && this.images.length < 1)
			    {
			    	Utils.showToast("内容不能为空", "inform");
			    	return;
			    }
			    var res = Moment.uploadMoment({
			    	userId:this.userInfo.id,
			    	content:this.content,
			    	imagesBase64Data:this.imagesBase64Data
			    })
				console.log(res);
				var momentsWebview = plus.webview.getWebviewById("moment");
				console.log(momentsWebview);
				mui.fire(momentsWebview,"addMoments",{
					userId:this.userInfo.id,
					userAvator:this.userInfo.faceImage,
					userNickname:this.userInfo.nickname,
					content:this.content,
					images:this.images,
					
				})
				mui.openWindow("../index.html","moment");
				this.images.splice(0,this.images.length)
				this.imagesBase64Data.splice(0,this.imagesBase64Data.length)
				this.content = "";
				this.word = 0;
				this.selectIndex = -1;
				
			})
		}
	}
})
