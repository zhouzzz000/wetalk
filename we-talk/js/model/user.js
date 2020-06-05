import {Http} from "../http.js";
import {config} from "../config.js";
class User{
	static setUserGlobalInfo(user)
	{
		var userInfoStr = JSON.stringify(user);
		plus.storage.setItem("userInfo",userInfoStr);
		
	}
	
	static getUserGlobalInfo(){
		return JSON.parse(plus.storage.getItem("userInfo"));
	}
	static setUserFriendList(friendList)
	{
		var friendListStr = JSON.stringify(friendList);
		plus.storage.setItem("friendList",friendListStr);
		
	}
	
	static async getUserFriendList(){
		var friendListStr = plus.storage.getItem("friendList")
		if(friendListStr == null || friendListStr == undefined || friendListStr.length <1)
		{
			var res = await User.getFriendList(User.getUserGlobalInfo().id);
			User.setUserFriendList(res.data);
			return res.data;
		}
		var friendList = JSON.parse(friendListStr);
		return friendList;
	}
	static userLogout(){
		plus.storage.removeItem("userInfo");
		plus.storage.removeItem("friendList");
		plus.runtime.restart();
	}
	
	static async getFriendList(userId){
	  return await Http.request({
			url: config.api_base_url + "friends/all?userId="+userId,
		});
	}
	
	static getFriendListFromStorage(){
		var friendListStr = plus.storage.getItem("friendList")
		if(friendListStr == null || friendListStr == undefined || friendListStr.length <1)
		{
			return null;
		}
		var friendList = JSON.parse(friendListStr);
		return friendList;
	}
	
	static getFriendInfoFromFriendList(friendId)
	{
		var friendList = User.getFriendListFromStorage();
		for (var i = 0; i < friendList.length; i++) {
			if(friendList[i].id == friendId)
			{
				
				return friendList[i];
			}
		}
		return null;
	}
	
}

export{
	User
}