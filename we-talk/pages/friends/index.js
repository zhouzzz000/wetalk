import {User} from "../../js/model/user.js";
import {Utils} from "../../js/utils.js";
import {Http} from "../../js/http.js";
import {config} from "../../js/config.js";
var app = new Vue({
	el: "#app-friends",
	data: {
		
	},

	created:function() {
		
	},
	methods: {
		tap2search:function(){
			mui.openWindow("./friends-pages/searchfriend.html","searchfriend");
		},
		tap2scan:function(){
			mui.openWindow("./friends-pages/scan.html","scan");
		},
		tap2moment:function(){
			mui.openWindow("../moment/index.html","moment");
		}
	}
})
