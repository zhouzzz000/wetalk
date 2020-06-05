import {Http} from "../http.js";
import {config} from "../config.js";
import {Utils} from "../utils.js";
class Moment{
	static async uploadMoment(data={})
	{
	   var res = await Http.request({
			url:config.api_base_url + "moment/upload",
			type:'post',
			data:{
				userId:data.userId,
				content:data.content,
				imagesBase64Data:data.imagesBase64Data,
				timeout:-1,
			}
		})
		if(res == null || res == undefined)
		{
			return null;
		}
		if(res.status != 200)
		{
			mui.plusReady( () => {
			    Utils.showToast(res.data,"inform");
			})
			return null;
		}else{
			return res;
		}
	}
	static async getMomentsById(userId)
	{
		var res = await Http.request({
			url:config.api_base_url+"moment/all?userId="+userId,
		})
		return res;
	}
	
	static async getMomentsByIdOnlyOwn(userId)
	{
		var res = await Http.request({
			url:config.api_base_url+"moment/friend?userId="+userId,
		})
		return res;
	}
}

export {
	Moment
}