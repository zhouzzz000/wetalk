import {User} from "../../../js/model/user.js";
import {Utils} from "../../../js/utils.js";
import {Http} from "../../../js/http.js";
import {config} from "../../../js/config.js";
var app = new Vue({
	el: "#app-scan",
	data: {
		scan:null,
	},

	created:function() {
		this.initComponent();
	},
	methods: {
		initComponent(){
			mui.plusReady(()=>{
				setTimeout(this.startScan(),'500');
			})
		},
		startScan(){
			var styles = {
				frameColor: "#92fb65",
				scanbarColor: "#c5e3fb",
				background: "",
			}
			
			this.scan = new plus.barcode.Barcode('scanComponent',null, styles);
			this.scan.onmarked = this.onmarked;
			this.scan.start();
		},
		onmarked:function(type, result){
			if(type === 0)
			{
				var content = result.split(":");
				if(content.length != 2)
				{
					mui.alert(result);
				}else{
					var friendUserId = content[1];
					this.searchFriend(friendUserId);
				}
			}
		},
		searchFriend(friendId){
			mui.plusReady(async ()=> {
				var wait = plus.nativeUI.showWaiting("搜索中...");
				var my = User.getUserGlobalInfo();
				var res = await Http.request({
					url:config.api_base_url + "friends/search_id",
					data:{
						myUserId:my.id,
						friendUserId: friendId ,
					},
					type:"post"
				}).catch(()=>{Utils.showToast("网络错误...","inform");
					wait.close();
					this.scan.close();
					mui.back();
				});
					wait.close();
					
				if(res.status !== 200)
				{
					Utils.showToast(res.msg,"inform");
					this.scan.close();
					mui.back();
				}
				else{
					// Utils.showToast("搜索成功","success");
					// console.log(JSON.stringify(data));
					mui.openWindow({
						url:"./searchresult.html",
						id:"searchresult",
						style:{},
						extras:{
							friendInfo:res.data,
						}
					});
					wait.close();
					this.scan.close();
				}
			})
		}
	}
})
