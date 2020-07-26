<template>	
	<div>
		<h4>You are in home.</h4>
		<chat-stomp></chat-stomp>
		<h4>You are in home.</h4>
	</div>
</template>
<script>
import ChatStomp from "../http/requests/ChatStomp.vue"
export default {
	name: "home",
	components: {
		ChatStomp
	}

}
</script>