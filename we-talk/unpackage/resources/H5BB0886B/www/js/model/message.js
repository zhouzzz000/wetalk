class Message{
	
	constructor(data={})
	{
		this.sendUserId = data.sendUserId;
		this.acceptUserId = data.acceptUserId;
		this.sendUserAvator = data.sendUserAvator;
		this.acceptUserAvator = data.acceptUserAvator;
		this.content = data.content;
		this.id = data.id;
		this.type = data.type;
	}
}

export {
	Message
}