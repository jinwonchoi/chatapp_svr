<!-- =========================================================================================
    File Name: Chat.vue
    Description: Chat Application - Stay connected
    ----------------------------------------------------------------------------------------
    Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
      Author: Pixinvent
    Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->


<template>
    <div id="chat-app" class="border border-solid d-theme-border-grey-light rounded relative overflow-hidden">
        <vs-sidebar class="items-no-padding" parent="#chat-app" :click-not-close="clickNotClose" :hidden-background="clickNotClose" v-model="isChatSidebarActive" id="chat-list-sidebar">
            <!-- USER PROFILE SIDEBAR -->
            <!-- 현재는 불필요 -->
            <!-- <user-profile :active="activeProfileSidebar" :userId="userProfileId" class="user-profile-container" :isLoggedInUser="isLoggedInUserProfileView" @closeProfileSidebar="closeProfileSidebar"></user-profile> -->
            <div class="chat__profile-search flex p-4">
                <!-- <div class="relative inline-flex">
                    <vs-avatar v-if="activeUser.profileUrl" class="m-0 border-2 border-solid border-white" :src="activeUser.profileUrl" size="40px" @click="showProfileSidebar(Number(activeUser.uid), true)" />
                    <div class="h-3 w-3 border-white border border-solid rounded-full absolute right-0 bottom-0" :class="'bg-' + getStatusColor(true)"></div>
                </div> -->
                <vs-input icon-no-border icon="icon-search" icon-pack="feather" class="w-full mx-5 input-rounded-full" placeholder="Search or start a new chat" v-model="searchQuery"/>

                <feather-icon class="md:inline-flex lg:hidden -ml-3 cursor-pointer" icon="XIcon" @click="toggleChatSidebar(false)" />
            </div>

            <vs-divider class="d-theme-border-grey-light m-0" />
            <component :is="scrollbarTag" class="chat-scroll-area pt-4" :settings="settings" :key="$vs.rtl">

                <!-- ACTIVE CHATS LIST -->
                <div class="chat__chats-list mb-8" v-if="!paramConsult">
                    <h3 class="text-primary mb-5 px-4">Chats</h3>
                    <ul class="chat__active-chats bordered-items">
                        <li class="cursor-pointer" v-for="(chatSession, index) in chatSessions" :key="index" @click="updateActiveChatSession(chatSession.id)">
                            <chat-session showLastMsg :chatSession="chatSession" :lastMessaged="chatSession.updatedDtm" 
                            :lastMessage="chatSession.lastMessage" 
                            :isActiveChatSession="isActiveChatSession(chatSession.id)"></chat-session>
                        </li>
                    </ul>
                </div>
                <!-- CONSULT LIST -->
                <div class="chat__consults" v-if="paramConsult">
                    <h3 class="text-primary mb-5 px-4">Customer's History</h3>
                    <ul class="chat__consults bordered-items">
                        <li class="cursor-pointer" v-for="(consult, index) in chatConsults" :key="index" @click="updateActiveChatConsult(consult)">
                            <chat-consult-item :chatConsult="consult" :isActiveChatConsult="isActiveChatConsult(consult.id)"></chat-consult-item>
                        </li>
                    </ul>
                </div>
            </component>
        </vs-sidebar>

        <!-- RIGHT COLUMN -->
        <div id="chat-log" class="chat__bg no-scroll-content chat-content-area border border-solid d-theme-border-grey-light border-t-0 border-r-0 border-b-0" 
          :class="{'sidebar-spacer--wide': clickNotClose && !isDetailsSidebarActive, 'sidebar-spacer--wide-double': clickNotClose && isDetailsSidebarActive, 'flex items-center justify-center': activeChatSession.id === 0}">
            <template v-if="(activeChatSession.id > 0)">
                <div class="chat__navbar">
                    <chat-navbar :isSidebarCollapsed="!clickNotClose" :chatSessionId="activeChatSession.id" :isPinnedProp="isChatPinned" @openContactsSidebar="toggleChatSidebar(true)" @showProfileSidebar="showProfileSidebar" @toggleIsChatPinned="toggleIsChatPinned"></chat-navbar>
                </div>
                <component :is="scrollbarTag" :class="{'chat-content-scroll-area-consult': paramConsult }" class="chat-content-scroll-area border border-solid d-theme-border-grey-light" :settings="settings" ref="chatLogPS" :key="$vs.rtl">
                    <div class="chat__log" ref="chatLog">
                        <chat-log :chatSession="activeChatSession" v-if="activeChatSession"></chat-log>
                    </div>
                </component>
                <div v-if="(chatAvailable && (activeChatSession.agentId === activeUser.agentId))" class="chat__input flex p-4 bg-white">
                    <vs-input class="flex-1" placeholder="Type Your Message" v-model="typedMessage" @keyup.enter="sendMsg" />
                    <vs-button class="bg-primary-gradient ml-4" type="filled" @click="sendMsg">Send</vs-button>
                </div>
                <div v-else-if="(chatAvailable && (activeChatSession.agentId !== activeUser.agentId))" class="chat__input flex p-4">
                </div>
                <div v-else class="chat__input flex p-4 bg-white">
                    <!-- <vs-input class="flex-1" placeholder="Type Your Message" v-model="typedMessage" @keyup.enter="sendMsg" /> -->
                    <vs-button class="bg-primary-gradient ml-4" type="filled" @click="takeChatSession">Pick this chat</vs-button>
                </div>
            </template>
            <template v-else>
                <div class="flex flex-col items-center">
                    <feather-icon icon="MessageSquareIcon" class="mb-4 bg-white p-8 shadow-md rounded-full" svgClasses="w-16 h-16"></feather-icon>
                    <h4 class=" py-2 px-4 bg-white shadow-md rounded-full cursor-pointer" @click.stop="toggleChatSidebar(true)">Check Chat List</h4>
                </div>
            </template>
        </div>
        <vs-sidebar position-right   class="items-no-padding" parent="#chat-app" :click-not-close="clickNotClose" :hidden-background="clickNotClose" 
            v-model="isDetailsSidebarActiveDivModel" id="chat-list-sidebar">
                <chat-consult-details :isSidebarActive="isDetailsSidebarActive" :data="getChatConsultDetails" 
                @closeSidebar="toggleChatConsultDetailsBar" @removeChatSession="removeActiveChatSession"></chat-consult-details>
        </vs-sidebar>
    </div>
</template>

<script>
import ChatContact         from './ChatContact.vue'
import ChatLog             from './ChatLog.vue'
import ChatNavbar          from './ChatNavbar.vue'
import UserProfile         from './UserProfile.vue'
import VuePerfectScrollbar from 'vue-perfect-scrollbar'
//import moduleChat          from '@/store/chat/moduleChat.js'
import ChatSession         from './ChatSession.vue'
import ChatConsultDetails  from './ChatConsultDetails.vue'
import ChatConsultItem     from './ChatConsultItem.vue'

export default {
  props: {
    paramConsult          : { type: Object },
    paramChatSession      : { type: Object },
  },
  data () {
    return {
      active               : true,
      isHidden             : false,
      searchContact        : '',
      activeProfileSidebar : false,
      activeChatSession    : {
        id : 0,
        chatConsultDetails: null
      },
      userProfileId        : -1,
      typedMessage         : '',
      isChatPinned         : false,
      settings             : {
        maxScrollbarLength : 60,
        wheelSpeed         : 0.70
      },
      clickNotClose        : true,
      isChatSidebarActive  : true,
      isDetailsSidebarActive  : false,
      isLoggedInUserProfileView: false,
    }
  },
  watch: {
    windowWidth () {
      this.setSidebarWidth()
    },
    activeChatSession() {
      this.isDetailsSidebarActive = (this.activeChatSession!==null && this.activeChatSession['chatConsultDetails']!==null)
    },
    paramConsult: {
      immediate: true,
      handler(newVal, oldVal) {
        this.updateActiveChatConsult (newVal)
      }
    }
  },
  computed: {
    activeUser () {
      return this.$store.state.AppActiveUser
    },
    getStatusColor () {
      return (isActiveUser) => {
        const userStatus = this.getUserStatus(isActiveUser)

        if (userStatus === 'online') {
          return 'success'
        } else if (userStatus === 'do not disturb') {
          return 'danger'
        } else if (userStatus === 'away') {
          return 'warning'
        } else {
          return 'grey'
        }
      }
    },
    // chatContacts () {
    //   return this.$store.getters['chat/chatContacts']
    // },
    // contacts () {
    //   return this.$store.getters['chat/contacts']
    // },
    searchQuery: {
      get () {
        return this.$store.state.chat.chatSearchQuery
      },
      set (val) {
        this.$store.dispatch('chat/setChatSearchQuery', val)
      }
    },
    scrollbarTag () {
      return this.$store.getters.scrollbarTag
    },
    isActiveChatSession () {
      return (chatSessionId) => this.activeChatSession && (chatSessionId === this.activeChatSession.id) 
    },
    isActiveChatConsult () {
      return (chatId) => this.activeChatSession 
      && (chatId === (this.activeChatSession.chatConsultDetails?this.activeChatSession.chatConsultDetails.id:0)) 
    },
    hasChatConsultDetails() {
      return this.activeChatSession!==null&&this.activeChatSession['chatConsultDetails']
    },
    chatAvailable() {
      return this.activeChatSession!==null&&this.activeChatSession['chatConsultDetails']&&(this.activeChatSession.id > 0)
    },
    windowWidth () {
      return this.$store.state.windowWidth
    },
    chatSessions () {
      return this.$store.state.chat.chatSessions
    },
    chatConsults() {
      return this.$store.state.chat.chatConsultDetails
    },
    isDetailsSidebarActiveDivModel () {
      return this.isDetailsSidebarActive && this.clickNotClose
    },
    // isDetailsSidebarActive () {
    //   let leftSidebar = (this.activeChatSession!==null && this.activeChatSession['chatConsultDetails']!==null)
    //   console.log("leftSidebar="+leftSidebar)
    //   return  leftSidebar
    // },
    getChatConsultDetails() {
      return this.hasChatConsultDetails? {
          ...this.activeChatSession.chatConsultDetails
          , "agentInfo":this.activeChatSession.agentInfo
          , "customerInfo":this.activeChatSession.customerInfo
        }:null
    }
  },
  methods: {
    getUserStatus (isActiveUser) {
      // return "active"
      return isActiveUser ? this.$store.state.AppActiveUser.status : this.contacts[this.activeChatSession].status
    },
    closeProfileSidebar (value) {
      this.activeProfileSidebar = value
    },
    // chatSession을 열기
    updateActiveChatSession (sessionId) {
      this.activeChatSession = this.$store.getters['chat/chatSession'](sessionId)
      this._updateActiveChat(this.activeChatSession['chatConsultDetails'], sessionId)
    },
    updateActiveChatConsult (consult) {
      const session = this.$store.getters['chat/chatSessionOfChatId'](consult.id)
      this.activeChatSession = session?session:{
        id:0,
        "chatId":consult.id,
        "chatConsultDetails":consult,
        "agentInfo":consult.agentInfo,
        "customerInfo":consult.customerInfo,
      }
      this._updateActiveChat(consult, consult.refSessionId)
    },
    _updateActiveChat (consult, sessionId) {
      const activeChatConsultDetails = consult
      const activeChatId = activeChatConsultDetails?activeChatConsultDetails.id:null
      if (activeChatId) {
        this.$store.dispatch('chat/getMessagesByChatConsultDetails', { 'chatId': activeChatId,'viewPrevChat':'y' })
        this.$store.dispatch('chat/setMessagesAlreadyRead', { 'chatId': activeChatId })
        const hasPrevChat = (activeChatConsultDetails && activeChatConsultDetails.prevChatId>0)
        if (hasPrevChat) {
          this.$store.dispatch('chat/fetchActiveChatConsultDetailsHist', { 'sessionId': sessionId })
        } else {
          this.$store.dispatch('chat/fetchActiveChatConsultDetails', { 'chatId': activeChatId })
        }
      } else {
        this.$store.dispatch('chat/fetchWaitingMessageQueue', { 'chatSession': this.activeChatSession })
      }
      const chatData = this.$store.getters['chat/chatMessagesOfChatId'](activeChatId)
      if (chatData) this.isChatPinned = chatData.isPinned
      else this.isChatPinned = false
      this.toggleChatSidebar()
      this.typedMessage = ''
    },
    showProfileSidebar (userId, openOnLeft = false) {
      this.userProfileId = userId
      this.isLoggedInUserProfileView = openOnLeft
      this.activeProfileSidebar = !this.activeProfileSidebar
    },
    sendMsg () {
      if (!this.typedMessage) return
      const chatConsultDetails = this.activeChatSession['chatConsultDetails']?this.activeChatSession.chatConsultDetails:null
      const payload = {
        //'isPinned': this.isChatPinned,
        'msg': this.typedMessage,
        'chatConsultDetails': chatConsultDetails
      }
      this.$store.dispatch('chat/sendMessageToNexmo', payload)
      this.typedMessage = ''

      const scroll_el = this.$refs.chatLogPS.$el || this.$refs.chatLogPS
      scroll_el.scrollTop = this.$refs.chatLog.scrollHeight
    },
    takeChatSession () {
      this.$store.dispatch('chat/takeWaitingSessionForChat', { 'chatSession': this.activeChatSession, 'agentId':this.$store.state.AppActiveUser.agentId } )
    },
    toggleIsChatPinned (value) {
      this.isChatPinned = value
    },
    setSidebarWidth () {
      if (this.windowWidth < 1200) {
        this.isChatSidebarActive = this.clickNotClose = false
      } else {
        this.isChatSidebarActive = this.clickNotClose = true
      }
    },
    toggleChatSidebar (value = false) {
      if (!value && this.clickNotClose) return
      this.isChatSidebarActive = value
    },
    toggleChatConsultDetailsBar() {
      this.isDetailsSidebarActive!=this.isDetailsSidebarActive
    },
    removeActiveChatSession() {
      this.activeChatSession = null
    }
  },
  components: {
    VuePerfectScrollbar,
    ChatContact,
    UserProfile,
    ChatNavbar,
    ChatLog,
    ChatSession,
    ChatConsultDetails,
    ChatConsultItem,
  },
  created () {
    // console.log("registerModule chat: "+moduleChat.isRegistered)
    // this.$store.dispatch('chat/fetchContacts')
    // this.$store.dispatch('chat/fetchChatContacts')
    // this.$store.dispatch('chat/fetchChats')
    ////////////////////////////////
    this.$store.dispatch('chat/fetchChatSessionStatus')
    this.setSidebarWidth()
  },
  beforeDestroy () {
    this.$stomp.disconnect()
    // console.log("unregisterModule chat")
  },
  mounted () {
    this.$store.dispatch('chat/setChatSearchQuery', '')
    this.$stomp.connect()
    if (this.paramConsult) {
      this.updateActiveChatConsult (this.paramConsult)
    }
  }
}

</script>


<style lang="scss">
@import "@/assets/scss/vuexy/apps/chat.scss";
</style>
