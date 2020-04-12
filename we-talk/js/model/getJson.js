import {Http} from "../http.js";
import {config} from "../config.js";

class GetJson{
	constructor() {
	}
	static async tabbar(){
		if(GetJson.tabbar1)
		{
			return GetJson.tabbar1;
		}
		GetJson.tabbar1 = await Http.request({
			url:config.json_base_url+'tabbar.json',
			dataType:"json",
			type:"get"
		});
		return GetJson.tabbar1;
	}
}
GetJson.tabbar1="";
export {
	GetJson,
}