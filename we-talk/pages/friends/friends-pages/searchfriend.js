import {User} from "../../../js/model/user.js";
import {Utils} from "../../../js/utils.js";
import {Http} from "../../../js/http.js";
import {config} from "../../../js/config.js";
var app = new Vue({
	el: "#app-searchfriend",
	data: {
		username:"",
	},

	created:function() {
		
	},
	methods: {
		searchFriend:function(){
			mui.plusReady(async ()=> {
			    if(this.username == "" || this.username.length < 1)
			    {
			    	Utils.showToast("weTalk号不能为空","inform");
			    	return;
			    }
			    if(this.username.length > 20)
			    {
			    	Utils.showToast("weTalk号长度不能超过20位","inform");
			    	return;
			    }
				var wait = plus.nativeUI.showWaiting("搜索中...");
				var my = User.getUserGlobalInfo();
				var res = await Http.request({
					url:config.api_base_url + "friends/search",
					data:{
						myUserId:my.id,
						friendUserName:this.username,
					},
					type:"post"
				}).catch(()=>{Utils.showToast("网络错误...","inform")});
				wait.close();
				if(res.status !== 200)
				{
					Utils.showToast(res.msg,"inform");
				}
				else{
					// Utils.showToast("搜索成功","success");
					mui.openWindow({
						url:"./searchresult.html",
						id:"searchresult",
						style:{},
						extras:{
							friendInfo:res.data,
						}
					});
				}
			})
		}
	}
})
