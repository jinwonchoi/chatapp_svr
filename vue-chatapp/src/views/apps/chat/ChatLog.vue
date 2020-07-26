<!-- =========================================================================================
    File Name: ChatLog.vue
    Description: Chat Application - Log of chat
    ----------------------------------------------------------------------------------------
    Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
      Author: Pixinvent
    Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->

<template>
    <div id="component-chat-log" class="m-8" v-if="chatData">
        <div v-for="(msg, index) in chatData.msg" class="msg-grp-container" :key="index">

            <!-- If previous msg is older than current time -->
            <template v-if="chatData.msg[index-1]">
                <vs-divider v-if="!isSameDay(msg.createdDtm, chatData.msg[index-1].createdDtm)" class="msg-time">
                    <span>{{ toDate(msg.createdDtm) }}</span>
                </vs-divider>
                <div class="spacer mt-8" v-if="!hasSentPreviousMsg(chatData.msg[index-1].inbound, msg.inbound)"></div>
            </template>

            <div class="flex items-start" :class="[{'flex-row-reverse' : !msg.inbound}]">

                <template v-if="chatData.msg[index-1]">
                    <template v-if="(!hasSentPreviousMsg(chatData.msg[index-1].inbound, msg.inbound) || !isSameDay(msg.createdDtm, chatData.msg[index-1].createdDtm))">
                        <vs-avatar size="40px" class="border-2 shadow border-solid border-white m-0 flex-shrink-0" :class="msg.inbound ? 'sm:ml-5 ml-3' : 'sm:mr-5 mr-3'" 
                        :src="senderImg(msg)"></vs-avatar>
                    </template>
                </template>

                <template v-if="index == 0">
                    <vs-avatar size="40px" class="border-2 shadow border-solid border-white m-0 flex-shrink-0" :class="msg.inbound ? 'sm:ml-5 ml-3' : 'sm:mr-5 mr-3'" 
                    :src="senderImg(msg)"></vs-avatar>
                </template>

                <template v-if="chatData.msg[index-1]">
                    <div class="mr-16" v-if="!(!hasSentPreviousMsg(chatData.msg[index-1].inbound, msg.inbound) || !isSameDay(msg.createdDtm, chatData.msg[index-1].createdDtm))"></div>
                </template>

                <div class="msg break-words relative shadow-md rounded py-3 px-4 mb-2 rounded-lg max-w-sm" :class="{'bg-primary-gradient text-white'
                : msg.inbound, 'border border-solid border-transparent bg-white': !msg.inbound}">
                    <span>{{ msg.message }}</span>
                </div>
            </div>
            <template v-if="chatData.msg[index-1]&&needPrevChatDiv(msg)">
                <vs-divider class="msg-time items-center" position="right">
                    <feather-icon :icon="'ChevronsLeftIcon'" svgClasses="w-4 h-4"/><span>Previous chat</span>
                </vs-divider>
                <div class="spacer mt-8" v-if="!hasSentPreviousMsg(chatData.msg[index-1].inbound, msg.inbound)"></div>
            </template>
        </div>
    </div>
</template>

<script>
export default{
  props: {
    chatSession: {
      type: Object,
      required: true
    }
  },
  // data() {
  //   return {
  //     chatAgents: []
  //   } 
  // },
  computed: {
    chatData () {
      if (this.chatSession.chatConsultDetails) {
        return this.$store.getters['chat/chatMessagesOfChatId'](this.chatSession.chatConsultDetails.id)
      } else {
        return { "msg": this.$store.getters['chat/waitingMessages'] }
      }
    },
    activeUserImg () {
      const URL = process.env.NODE_ENV === 'production'?process.env.VUE_APP_PROD_URL: process.env.VUE_APP_DEV_URL
      return URL+this.$store.state.AppActiveUser.profileUrl
    },
    senderImg () {
      const URL = process.env.NODE_ENV === 'production'?process.env.VUE_APP_PROD_URL: process.env.VUE_APP_DEV_URL
      const defaultUrl = "/files/profile/default.jpg"
      return (msg) => {
        if (msg.inbound) return URL+process.env.VUE_APP_DEFAULT_AVATAR_URL //this.$store.state.AppActiveUser.profileUrl
        else {
          let chatAgent = this.$store.getters['chat/chatSessionAgent'](msg.chatId)
          return chatAgent?(URL+chatAgent.profileUrl):(URL+this.$store.state.AppActiveUser.photoUrl)
        }
      }
    },
    hasSentPreviousMsg () {
      return (last_sender, current_sender) => last_sender === current_sender
    },
    needPrevChatDiv () {
      return (msg) => this.chatSession.chatId!==msg.chatId
    }
  },
  methods: {
    isSameDay (time_to, time_from) {
      const date_time_to = new Date(Date.parse(time_to))
      const date_time_from = new Date(Date.parse(time_from))
      return date_time_to.getFullYear() === date_time_from.getFullYear() &&
                date_time_to.getMonth() === date_time_from.getMonth() &&
                date_time_to.getDate() === date_time_from.getDate()
    },
    toDate (time) {
      const locale = 'en-us'
      const date_obj = new Date(Date.parse(time))
      const monthName = date_obj.toLocaleString(locale, {
        month: 'short'
      })
      return `${date_obj.getDate()  } ${   monthName}`
    },
    scrollToBottom () {
      this.$nextTick(() => {
        this.$parent.$el.scrollTop = this.$parent.$el.scrollHeight
      })
    }
  },
  updated () {
    this.scrollToBottom()
  },
  mounted () {
    this.scrollToBottom()
  }
}
</script>
