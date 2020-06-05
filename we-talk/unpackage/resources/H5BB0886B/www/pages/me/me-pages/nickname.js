import {User} from "../../../js/model/user.js";
import {Utils} from "../../../js/utils.js";
import {Http} from "../../../js/http.js";
import {config} from "../../../js/config.js";
var app = new Vue({
	el: "#app-nickname",
	data: {
		nickname:"",
	},

	created:function() {
		this.initNickname();
	},
	methods: {
		initNickname(){
			mui.plusReady(()=> {
			    this.nickname = User.getUserGlobalInfo().nickname;
			})
		},
		updateNickname:function(){
			mui.plusReady(()=> {
			   if(this.nickname == "")
			   {
					Utils.showToast("昵称不能为空...","inform");
			   }else if(this.nickname.length > 20){
				    Utils.showToast("昵称长度不能超过20...","inform");
			   }else{
				   var wait = Utils.showWait("修改中...");
					var user = User.getUserGlobalInfo();
					Http.request({
						url: config.api_base_url + "user/update/nickname",
						data: {
							"id": user.id,
							"nickname": this.nickname
						},
						type:"post"
					}).then((data)=>{
						wait.close();
						console.log(data);
						if(data.status == 200)
						{
							Utils.showToast("修改成功","success");
							var userInfo = data.data;
							User.setUserGlobalInfo(userInfo);
							var meWebView = plus.webview.getWebviewById("me");
							mui.fire(meWebView, "refresh");
							mui.openWindow("index.html", "index");
						}else{
							console.log(data.msg)
							Utils.showToast("修改失败","failure");
						}
					},
					(err)=>{
						wait.close();
						console.log(err.msg)
						Utils.showToast("修改失败","failure");
					});
				}
			});
		}
	}
})
