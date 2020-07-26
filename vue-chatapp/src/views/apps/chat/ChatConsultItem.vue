<!-- =========================================================================================
    File Name: ChatContact.vue
    Description: Chat contact - single component for chat
    ----------------------------------------------------------------------------------------
    Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
      Author: Pixinvent
    Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->

<template>
    <div class="chat__contact flex items-center px-2 pt-4 pb-2" :class="{'bg-primary-gradient text-white shadow-lg': isActiveChatConsult}">
        <div v-if="chatConsult.agentInfo" class="contact__avatar mr-1">
            <vs-avatar class="border-2 border-solid border-white" :src="userImg(chatConsult.agentInfo)" size="42px" />
        </div>
        <div class="contact__container w-full flex items-center justify-between overflow-hidden">
            <div class="contact__info flex flex-col truncate w-5/6">
                <h5 class="font-semibold" :class="{'text-white': isActiveChatConsult}">{{ chatConsult.agentInfo.userName }} with {{ chatConsult.customerInfo.customerId }}</h5>
                <span class="truncate">{{ chatConsult.chatComment }}</span>
            </div>
            <div class="chat__contact__meta flex self-start flex-col items-end w-1/6">
                <span class="whitespace-no-wrap">{{ chatConsult.updatedDtm | date }}</span>
            </div>
        </div>
    </div>
</template>

<script>
export default {
  props: {
    chatConsult          : { type: Object,  required: true },
    isActiveChatConsult : { type: Boolean },
  },
  watch: {
    chatConsult() {
      console.log("aa")
    }
  },

  computed: {
    userImg() {
      return (userInfo)=>this.$utils.userImg(userInfo)
    },

  }
}
</script>

