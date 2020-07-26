<!-- =========================================================================================
    File Name: EmailView.vue
    Description: Email Application - Single Email View (Inbox)
    ----------------------------------------------------------------------------------------
    Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
      Author: Pixinvent
    Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->


<template>
  <div>
    <vs-sidebar click-not-close parent="#consult-app" :hidden-background="true" class="full-vs-sidebar-cover-all consult-view-sidebar items-no-padding" v-model="isConsultViewSidebarActive" position-right>
      <div class="consult-sidebar-content px-0 sm:pb-6 h-full">
        <div class="flex flex-wrap items-center email-header justify-between md:px-8 px-6 sm:pb-2 sm: pt-2 d-theme-dark-bg">
          <div class="flex mb-1">
            <div class="flex items-center">
              <feather-icon :icon="$vs.rtl ? 'ArrowRightIcon' : 'ArrowLeftIcon'" @click="$emit('closeSidebar')" class="cursor-pointer mr-4" svg-classes="w-6 h-6"></feather-icon>
              <h6>Chat Details</h6>
            </div>
          </div>
        </div>
        <div class="ml-10 mb-4 mt-1">
          <chat :paramConsult="currentConsult"></chat>
        </div>
      </div>
    </vs-sidebar>
  </div>
</template>

<script>
import VuePerfectScrollbar from 'vue-perfect-scrollbar'
import Chat from '@/views/apps/chat/Chat.vue'

export default {
  props: {
    consultTypeFilter: {
      type: String,
      required: true
    },
    consult: {
      type: Object,
      required: true,
    },
    isConsultViewSidebarActive: {
      type: Boolean,
      required: true
    },
  },
  data () {
    return {
      showThread: false,
      settings: {
        maxScrollbarLength: 60,
        wheelSpeed: 0.50
      },
    }
  },
  watch: {
    isConsultViewSidebarActive (value) {
      if (!value) {
        this.$emit('closeSidebar')
        setTimeout(() => {
          this.showThread = false
        }, 500)
      }
    }
  },
  computed: {
    currentConsult () {
        this.$store.dispatch('chat/fetchChatConsultDetails', { 
          'customerId':this.consult.customerId
        }).then(() => { 
          console.log("chat.chatConsultDetails.length:"+this.$store.state.chat.chatConsultDetails.length)
        }).catch(err => { console.error(err) })
      return this.consult
    },
    // labelColor () {
    //   return (label) => {
    //     const tags = this.$store.state.consult.consultTypeTags
    //     return tags.find((tag) => {
    //       return tag.value === label
    //     }).color
    //   }
    // },
    // currentConsultLabels: {
    //   get () {
    //     return this.currentConsult.labels
    //   },
    //   set (value) {
    //     if (Array.isArray(value)) {
    //       const payload = { mailId: this.openMailId, labels: value }
    //       this.$store.dispatch('email/setLabels', payload)
    //     }
    //   }
    // },
    scrollbarTag () { return this.$store.getters.scrollbarTag }
  },
  methods: {
  },
  components: {
    VuePerfectScrollbar,
    Chat
  },
  // updated () {
  //   if (!this.currentConsult) return
  //   //if (this.currentMail.unread && this.isConsultViewSidebarActive) this.$store.dispatch('email/setUnread', { emailIds: [this.openMailId], unread: false })
  // }
}

</script>

