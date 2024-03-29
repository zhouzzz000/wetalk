import {Utils} from "./utils.js";
class Http {
	   static request(data1 = {}) {
		const res = new Promise(function(resolve, reject) {
			var url = data1.url;
			var type = data1.type ? data1.type : "get";
			var dataType = data1.dataType;
			var headers = data1.headers?data1.headers:{'Content-Type':'application/json'};
			var timeout = data1.timeout ? data1.timeout : 5000;
			var data = data1.data;
			var processData = data1.processData ? data1.processData:true;
			// var contentType = data1.contentType;
			mui.ajax(url, {
				type,
				data,
				dataType, //服务器返回json格式数据
				headers,
				timeout,
				processData,
				// contentType,
				success: function(data) {
					resolve(data);
				},
				error: function(data) {
					reject(data);
				}
			})
		}).catch((err)=>{console.log(err);Utils.showToast("网络错误","failure");plus.nativeUI.closeWaiting();});
		return res;
	}
}

export {
	Http
}
