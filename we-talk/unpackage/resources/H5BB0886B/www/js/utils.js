import {config} from "./config.js";

class Utils{
	static isNotNull(str)
	{
		if(str != null && str !="" && str!=undefined && str.trim()!="")
		{
			return true;
		}
		return false;
	}
	
	static showToast(msg,type){
		plus.nativeUI.toast(msg,{
			icon:"/resources/imgs/"+type+".png",
			verticalAlign:"center"
		})
	}
	
	static showWait(msg)
	{
		return plus.nativeUI.showWaiting(msg,{
			verticalAlign:"center",
		})
	}
}

export{
	Utils
}