import {
	Utils
} from "../../js/utils.js";
import{
	config
} from "../../js/config.js";
import {
	User
}from "../../js/model/user.js"
mui.init();

var $image = $('#image');
mui.plusReady(function() {

	var user = User.getUserGlobalInfo();
	var faceImage = user.faceImageBig;
	// 获取屏幕宽度，设置图片
	$image.attr('src', faceImage);

	/** ======================== 从相册选择图片 ======================== **/
	// 从相册中选择图片
	console.log("从相册中选择图片:");
	plus.gallery.pick(function(path) {

		$image.attr('src', path);

		faceCutter();
	}, function(e) {
		mui.openWindow("index.html", "index.html");
	}, {
		filter: "image"
	});

	/** ======================== 图片裁剪 ======================== **/

	$("#cutter").on('tap', function() {

		plus.nativeUI.showWaiting("上传中...");

		var cropper = $image.data('cropper');
		var result = $image.cropper("getCroppedCanvas");
		if (result) {
			var base64Url = result.toDataURL();

			// 上传头像
			var user = User.getUserGlobalInfo();
			// 与后端联调
			mui.ajax(config.api_base_url + "file/image/uploadFaceBase64", {
				data: {
					userId: user.id,
					faceBase64Data: base64Url
				},
				dataType: 'json', //服务器返回json格式数据
				type: 'post', //HTTP请求类型
				timeout: 10000, //超时时间设置为10秒；
				headers: {
					'Content-Type': 'application/json'
				},
				success: function(data) {

					// 关闭等待框
					plus.nativeUI.closeWaiting();

					if (data.status == 200) {
						var userInfo = data.data;
						User.setUserGlobalInfo(userInfo);

						// 触发另外一个webview的自定义事件，可以使用 mui.fire()
						var meWebview = plus.webview.getWebviewById("me");
						mui.fire(meWebview, "refresh");

						// 触发另外一个webview的自定义事件，可以使用 mui.fire()
						var myfaceWebview = plus.webview.getWebviewById("avator");
						mui.fire(myfaceWebview, "refresh");
						
						// 页面跳转
						mui.openWindow("index.html", "index");
					} else {
						Utils.showToast(data.msg, "failure");
					}
				},
				error:function(err){
					// 关闭等待框
					console.log(err);
					plus.nativeUI.closeWaiting();
					Utils.showToast(err.msg, "failure");
				}
			});
		}
	});
});

function faceCutter() {

	plus.nativeUI.showWaiting("请稍等...");

	//				var $image = $('#image');
	var options = {
		aspectRatio: 1 / 1,
		crop: function(e) {}
	};

	// Cropper
	$image.cropper(options);

	plus.nativeUI.closeWaiting();
}
