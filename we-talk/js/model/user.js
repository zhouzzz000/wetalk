class User{
	static setUserGlobalInfo(user)
	{
		var userInfoStr = JSON.stringify(user);
		plus.storage.setItem("userInfo",userInfoStr);
		
	}
	
	static getUserGlobalInfo(){
		return JSON.parse(plus.storage.getItem("userInfo"));
	}
}

export{
	User
}