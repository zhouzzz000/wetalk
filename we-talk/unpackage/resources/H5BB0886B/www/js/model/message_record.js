import {Http} from "../http.js";
import {config} from "../config.js";
class MessageRecord{
	/**
	 * @param {Object} data 聊天数据
	 * @param {Object} flag 判断发送方：1 我方 2 别人
	 */
	static saveUserMessageHistory(data,flag)
	{
		if(data == null || data == undefined)
		{
			return;
		}
		var myId;
		var friendId;
		if(flag === 1)
		{
			myId = data.sendUserId;
			friendId = data.acceptUserId;
		}else if(flag === 2)
		{
			myId = data.acceptUserId;
			friendId = data.sendUserId;
		}
		mui.plusReady(()=> {
		    var key = "chat-"+ myId + "-" + friendId;
		    // 聊天记录是否已有缓存
		    var chatHistoryListStr = plus.storage.getItem(key);
			var chatHistoryList = [];
			if(chatHistoryListStr != null && chatHistoryListStr != undefined && chatHistoryListStr.length > 0)
			{
				chatHistoryList = JSON.parse(chatHistoryListStr);
			}
			data.flag = flag;
			chatHistoryList.push(data);
			plus.storage.setItem(key,JSON.stringify(chatHistoryList));
		})
		
	}
	
	static getUserMessageHistory(myId, friendId)
	{
		    var key = "chat-"+ myId + "-" + friendId;
		    var chatHistoryListStr = plus.storage.getItem(key);
		    var chatHistoryList = [];
		    if(chatHistoryListStr != null && chatHistoryListStr != undefined && chatHistoryListStr.length > 0)
		    {
		    	chatHistoryList = JSON.parse(chatHistoryListStr);
		    }
		    return chatHistoryList;
	}
	/**
	 * @param {Object} data 快照数据
	 * @param {String} data.myId 用户id
	 * @param {String} data.chatUserId 聊条对象id
	 * @param {String} data.msg 和该用户最新的聊天内容
	 * @param {Integar} data.isRead 该消息是否被读取
	 * @param {Integar} data.unread 该聊天未读的消息数量
	 */
	static saveUserChatSnapshot(data={})
	{
		mui.plusReady( () => {
			var myId = data.myId;
			var chatUserId = data.chatUserId;
			var key = "chat-snapshot-"+myId;
		    var chatSnapshotListStr = plus.storage.getItem(key);
			var unread = !data.isRead;
			var chatSnapshotList = [];
			if(chatSnapshotListStr != null && chatSnapshotListStr != undefined && chatSnapshotListStr.length > 0)
			{
				chatSnapshotList = JSON.parse(chatSnapshotListStr);
				//循环快照列表，判断每个元素是否包含（匹配）friendId，匹配则删除
				for (var i = 0; i < chatSnapshotList.length; i++) {
					if(chatSnapshotList[i].chatUserId == chatUserId)
					{
						unread = chatSnapshotList[i].unread + unread;
						chatSnapshotList.splice(i,1);
						break;
					}
				}
			}
			data.unread = unread;
			chatSnapshotList.unshift(data);
			plus.storage.setItem(key,JSON.stringify(chatSnapshotList));
			var chatListWebview = plus.webview.getWebviewById("chat");
			mui.fire(chatListWebview, "refreshSnapshotList");
		})
	}
	
	static getUserChatSnapshot(myId)
	{
		var key = "chat-snapshot-" + myId;
		var chatSnapshotListStr = plus.storage.getItem(key);
		var chatSnapshotList = [];
		if(chatSnapshotListStr != null && chatSnapshotListStr != undefined && chatSnapshotListStr.length > 0)
		{
			chatSnapshotList = JSON.parse(chatSnapshotListStr);
		}
		return chatSnapshotList;
	}
	
	static clearChatSnapshotUnread(myId, chatUserId)
	{
		var key = "chat-snapshot-"+myId;
		var chatSnapshotListStr = plus.storage.getItem(key);
		var chatSnapshotList = [];
		if(chatSnapshotListStr != null && chatSnapshotListStr != undefined && chatSnapshotListStr.length > 0)
		{
			chatSnapshotList = JSON.parse(chatSnapshotListStr);
		}
		var i;
		for(i=0; i<chatSnapshotList.length; i++)
		{
			if(chatSnapshotList[i].chatUserId == chatUserId)
			{
				chatSnapshotList[i].unread = 0;
				break;
			}
		}
		if(i == chatSnapshotList.length)
		{
			return;
		}
		plus.storage.setItem(key,JSON.stringify(chatSnapshotList));
		var chatListWebview = plus.webview.getWebviewById("chat");
		mui.fire(chatListWebview, "refreshSnapshotList");
	}
	
	static async fetchUnreadMessageList(userId){
		var res = await Http.request({
			url:config.api_base_url + "user/getUnreadMsgList?userId="+userId,
		})
		return res.data;
	}
	
	static removeSnapshotById(myId, chatUserId)
	{
		var key = "chat-snapshot-"+myId;
		var chatSnapshotListStr = plus.storage.getItem(key);
		var chatSnapshotList = [];
		if(chatSnapshotListStr != null && chatSnapshotListStr != undefined && chatSnapshotListStr.length > 0)
		{
			chatSnapshotList = JSON.parse(chatSnapshotListStr);
		}
		var i;
		for(i=0; i<chatSnapshotList.length; i++)
		{
			if(chatSnapshotList[i].chatUserId == chatUserId)
			{
				chatSnapshotList.splice(i,1);
				break;
			}
		}
		plus.storage.setItem(key,JSON.stringify(chatSnapshotList));
		var chatListWebview = plus.webview.getWebviewById("chat");
		mui.fire(chatListWebview, "refreshSnapshotList");
	}
}

export{
	MessageRecord
}