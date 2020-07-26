<!-- =========================================================================================
    File Name: ChatContact.vue
    Description: Chat contact - single component for chat
    ----------------------------------------------------------------------------------------
    Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
      Author: Pixinvent
    Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->

<template>
    <div class="chat__contact flex items-center px-2 pt-4 pb-2" :class="{'bg-primary-gradient text-white shadow-lg': isActiveChatSession}">
        <div v-if="chatSession.agentInfo" class="contact__avatar mr-1">
            <vs-avatar class="border-2 border-solid border-white" :src="userImg(chatSession.agentInfo)" size="42px" />
        </div>
        <div class="contact__container w-full flex items-center justify-between overflow-hidden">
            <div v-if="chatSession.agentInfo" class="contact__info flex flex-col truncate w-5/6">
                <h5 class="font-semibold" :class="{'text-white': isActiveChatSession}">{{ chatSession.agentInfo.userName }} with {{ chatSession.customerInfo.customerId }}</h5>
                <span class="truncate">{{ showLastMsg ? chatSession.lastMessage : ""}}</span>
            </div>
            <div v-else class="contact__info flex flex-col truncate w-5/6">
                <h5 class="font-semibold" :class="{'text-white': isActiveChatSession}">{{ chatSession.customerInfo.customerId }}</h5>
                <span class="truncate">{{ showLastMsg ? chatSession.lastMessage : ""}}</span>
            </div>

            <div class="chat__contact__meta flex self-start flex-col items-end w-1/6">
                <vs-chip class="number" color="primary" v-if="chatSession.unreadCnt">{{ chatSession.unreadCnt }}</vs-chip>
                <span class="whitespace-no-wrap">{{ lastMessaged | gapFromNow }}</span>
            </div>
        </div>
    </div>
</template>

<script>
export default {
  props: {
    chatSession          : { type: Object,  required: true },
    isActiveChatSession : { type: Boolean },
    lastMessaged     : { type: String,  default : '' },
    showLastMsg      : { type: Boolean, default: false },
    unseenMsg        : { type: Number,  default : 0 }
  },
  computed: {
    userImg() {
      return (userInfo)=>this.$utils.userImg(userInfo)
    }
  }
}
</script>

