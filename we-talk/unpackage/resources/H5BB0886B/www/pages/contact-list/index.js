import {User} from "../../js/model/user.js";
import {Utils} from "../../js/utils.js";
import {Http} from "../../js/http.js";
import {MessageRecord} from "../../js/model/message_record.js";
import {config} from "../../js/config.js";
var app = new Vue({
	el: "#app-contact",
	data: {
		requestCount:0,
		userInfo:"",
		requestList:{},
		friendsList:null,
	},

	created:function() {
		// this.clearFriendListCache();
		this.initRequestList();
		this.initContactList();
		mui.plusReady(()=> {
			var curwebview = plus.webview.currentWebview();
		    curwebview.addEventListener("show",()=>{
		    	this.initRequestList();
		    });
		})
		window.addEventListener("refreshRequest",()=>{
			this.clearFriendListCache();
			this.initRequestList();
			
		})
	},
	methods: {
		initRequestList(){
			mui.plusReady(async ()=> {
				this.userInfo = User.getUserGlobalInfo();
				var userId = this.userInfo.id;
			    var res = await Http.request({
			    	url:config.api_base_url + "user/friend_request_list?acceptUserId="+userId
			    })
				if(res.data != null && res.data.length > 0)
				{
					this.requestCount = res.data.length;
				}else{
					this.requestCount = 0;
				}
				this.requestList = res.data;
			})
		},
		tap2requestlist:function(){
			mui.plusReady(()=> {
			    mui.openWindow({
			    	url:"./contact-pages/request-list.html",
			    	id:"request-list",
			    	style:{},
			    	extras:{
			    		requestList:this.requestList
			    	}
			    });
			})
		},
		refreshRequest()
		{
			if(this.requestCount > 0)
			{
				this.requestCount = this.requestCount-1;
			}
		},
		initContactList(){
			mui.plusReady(async ()=>{
				var res = await User.getUserFriendList();
				var friendsList = res;
				// 循环好友列表，转换为拼音
				if(!friendsList || friendsList.length < 1)
				{
					var list = document.getElementById('list');
					list.style.height = document.body.offsetHeight-44 + "px";
					window.IndexedList = new mui.IndexedList(list);
					return ;
				}
				var friends = {
					'A':[],'B':[],'C':[],'D':[],'E':[],'F':[],
					'G':[],'H':[],'I':[],'J':[],'K':[],'L':[],
					'M':[],'N':[],'O':[],'P':[],'Q':[],'R':[],
					'S':[],'T':[],'X':[],'Y':[],'Z':[],'W':[],
					'#':[]
				}
				for(var i=0; i<friendsList.length; i++)
				{
					var friend = friendsList[i];
					// 转换拼音
					var spell = words.convertPinyin(friend.nickname);
					// 截取拼音首字母
					var firstWord = spell.substr(0,1).toUpperCase();
					if(isNaN(firstWord))
					{
						friends[firstWord].push(friend);
					}else{
						friends['#'].push(friend);
					}
				}
				this.friendsList = friends;
				var list = document.getElementById('list');
				list.style.height = document.body.offsetHeight-44 + "px";
				window.IndexedList = new mui.IndexedList(list);
			})	
		},
		clearFriendListCache(){
			mui.plusReady(()=>{
				plus.storage.removeItem("friendList");
				this.initContactList();
			})
		},
		tap2chat(chatUserId){
			mui.openWindow({
				url:'../chat/chatting.html',
				id:'chatting-'+chatUserId,
				extras:{
					chatUserId:chatUserId
				}
			});
		}
	}
})
