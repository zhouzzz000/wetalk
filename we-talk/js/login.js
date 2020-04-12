import {config} from "./config.js";
import {Http} from "./http.js";
import { Utils } from "./utils.js";
import { User }  from "./model/user.js";

mui.plusReady(function() {
	//判断用户是否登陆过
	var userInfo = User.getUserGlobalInfo();
	if(userInfo)
	{
		mui.openWindow("./index.html","index");
	}
	var userform = document.getElementById("userform");
	var username = document.getElementById("username");
	var txt_password = document.getElementById("txt_password");
	var flag=false;
	userform.addEventListener("submit", function(e) {
		if(flag) return;
		flag=true;
		// 阻止默认时间，阻止默认表单提交
		e.preventDefault();
		var wait = Utils.showWait("登陆中...");
		// 判断用户名是否为空，如果为空则让其获得焦点
		if (!Utils.isNotNull(username.value)) {
			// 获取焦点
			wait.close();
			username.focus();
		} else if (!Utils.isNotNull(txt_password.value)) {
			// 获取焦点
			txt_password.focus();
			wait.close();
		} else {
			// 判断用户名和密码的长度，进行限制
			if (username.value.length > 12) {
				wait.close();
				Utils.showToast("用户名不能超过12位","failure");
				return false;
			} else if (txt_password.value.length > 12) {
				wait.close();
				Utils.showToast("密码不能超过12位","failure");
				return false;
			}
			username.blur();
			txt_password.blur();
			// 获取每台手机的唯一cid
			var cid = plus.push.getClientInfo().clientid;
			// 与后端联调
			var requestData = {
				url: config.api_base_url + "user/login",
				data: {
					"username": username.value,
					"password": txt_password.value,
					"cid": cid
				},
				dataType: "json",
				type: "post",
				timeout: 5000,
				header: {
					'Content-Type': 'application/json'
				}
			};
			
			Http.request(requestData).
				then(
					(e)=>{
						wait.close();
						if(e.status=="200"){
							Utils.showToast("登陆成功","success");
							var userGlobalInfo= e.data;
							User.setUserGlobalInfo(userGlobalInfo);
							mui.openWindow({
								url:"./index.html",
								id:"index",
								});
							flag=false;
						}else{
							Utils.showToast(e.msg,"failure");
							flag=false;
						}
					},
					(err)=>{
						wait.close();
						Utils.showToast("网络错误...","failure");
						flag=false;
					}
				);
		}
	});
})
