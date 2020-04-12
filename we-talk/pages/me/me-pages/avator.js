import {User} from "../../../js/model/user.js"
var app = new Vue({
	el: "#app-avator",
	data: {
		userAvator:"",
	},

	created:function() {
		this.initAvator();
	},
	methods: {
		initAvator(){
			mui.plusReady(()=> {
			    this.userAvator = User.getUserGlobalInfo().faceImageFull;
				if(this.userAvator === "")
				{
					this.userAvator = "../../../css/registLogin/login.jpg"; 
				}
			})
		},
		openSelectView:function()
		{
			mui("#sheet-avator").popover("toggle");
		},
		selectImage:function(){
			mui("#sheet-avator").popover("toggle");
			mui.openWindow("../../../plugin/v3.1.6/myface-uploader.html","myface-uploader");
		},
		downloadImage:function(){
			console.log(22)
		}
	}
})
