<!-- =========================================================================================
  File Name: AddNewDataSidebar.vue
  Description: Add New Data - Sidebar component
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->


<template>
  <!-- <vs-sidebar position-right   class="items-no-padding" parent="#chat-app" :click-not-close="clickNotClose" :hidden-background="clickNotClose" v-model="isDetailsSidebarActive" id="chat-list-sidebar">
  <vs-sidebar click-not-close position-right parent="body" default-index="1" color="primary" class="add-new-data-sidebar items-no-padding" spacer v-model="isSidebarActiveLocal"> -->
    <div class="add-new-data-sidebar items-no-padding">
    <div class="mt-6 flex items-center justify-between px-6">
        <h4>Chat Consult Details</h4>
    </div>
                    
      <div class="p-4">
        <div class="mt-4">
          <p><span class="font-medium mr-2">Started:</span> {{ toLocale(chatConsultDetails['createdDtm']) }}</p>
        </div>
        <div class="mt-4">
          <p><span class="font-medium mr-2">Updted:</span> {{ toLocale(chatConsultDetails.updatedDtm) }}</p>
        </div>
        <div class="mt-4">
          <p><span class="font-medium mr-2">Customer Id:</span> {{ chatConsultDetails.customerId }}</p>
        </div>
        <div class="mt-4">
          <p><span class="font-medium mr-2">Language / Country:</span> {{ language }} / {{ country }}</p>
        </div>
        <div class="mt-4">
          <p><span class="font-medium mr-2">Consult Status:</span> {{ consult_status?consult_status.label:"" }}</p>
        </div>
        <div class="mt-4" v-if="chatConsultDetails.prevChatId">
          <p><span class="font-medium mr-2">Assigned By :</span> {{ prevAgentName }}</p>
        </div>
        <!-- AGENT ID -->
        <div class="mt-4" v-if="!isOwner">
          <p><span class="font-medium mr-2">Owned By :</span> {{ agentName }}</p>
        </div>
        <!-- <div v-if="!isOwner" class="flex items-center">
          <p><span class="font-medium mr-2">Owned By :</span></p>
          <vs-avatar :src="userImg(chatConsultDetails.agentInfo)" class="flex-shrink-0 mr-2" size="30px" @click="$router.push(url)" />
          <vs-avatar :src="userImg(chatConsultDetails.agentInfo)" class="flex-shrink-0 mr-2" size="30px" />
          <router-link :to="url" @click.stop.prevent class="text-inherit hover:text-primary">{{ params.value }}</router-link>
          {{ agentName }}
        </div> -->
    
        <vs-divider class="mb-0"></vs-divider>
        <!-- consult type -->
        <div class="mt-4">
          <label class="vs-input--label">Consult Type</label>
          <v-select v-model="consult_type" :clearable="false" :options="consult_type_choices" name="consult-type" 
            v-validate="'required'" :dir="$vs.rtl ? 'rtl' : 'ltr'"/>
          <span class="text-danger text-sm" v-show="errors.has('consult-type')">{{ errors.first('consult-type') }}</span>
        </div>

        <div class="mt-4">
          <!-- <label class="vs-input--label">Comment</label> -->
          <vs-textarea label="Comment" v-model="chatConsultDetails.chatComment" placeholder="Customer's response..." rows="10" />
        </div>

      </div>
    
    <div class="flex flex-wrap items-center p-6" slot="footer" v-if="isOwner">
      <vs-button class="mr-6" @click="saveData" :disabled="!isFormValid">Save</vs-button>
      <!-- ACTION - DROPDOWN -->
      <vs-dropdown vs-trigger-click class="cursor-pointer" v-if="isProcessing">
            <div class="p-3 mr-6 vs-button vs-button-primary vs-button-filled shadow-drop rounded-lg d-theme-dark-light-bg cursor-pointer flex items-end justify-center text-lg font-small w-32">
              <span class="mr-2 leading-none">Actions</span>
              <feather-icon icon="ChevronDownIcon" svgClasses="h-4 w-4" />
            </div>

            <vs-dropdown-menu>

              <vs-dropdown-item @click="completeConsult">
                <span class="flex items-center">
                  <feather-icon icon="CheckIcon" svgClasses="h-4 w-4" class="mr-2" />
                  <span>Close the chat</span>
                </span>
              </vs-dropdown-item>

              <vs-dropdown-item @click="popupAgentList">
                <span class="flex items-center">
                  <feather-icon icon="ChevronsRightIcon" svgClasses="h-4 w-4" class="mr-2" />
                  <span>AssignTo</span>
                </span>
              </vs-dropdown-item>
            </vs-dropdown-menu>
          </vs-dropdown> 
    </div>

  <!-- </vs-sidebar> -->
    <agent-list :showAgentList="showAgentList" @hideAgentList="hideAgentList" @selectAgent="selectAgent" v-if="showAgentList"/>

  </div>
</template>

<script>
import VuePerfectScrollbar from 'vue-perfect-scrollbar'
import vSelect from 'vue-select'
import lang from '@/common/lang.js'
import country from '@/common/country.js'
import AgentList           from './AgentList.vue'

export default {
  props: {
    isSidebarActive: {
      type: Boolean,
      required: true
    },
    data: {
      type: Object,
      default: () => {}
    }
  },
  components: {
    VuePerfectScrollbar,
    'v-select': vSelect,
    AgentList,
  },
  data () {
    return {
      chatConsultDetails: null,
      consult_type_choices: [
        {label:'Questions', value:'B'},
        {label:'Complaints', value:'C'},
        {label:'AS Request', value:'A'},
      ],
      consult_status_choices: [
        {label:'Open', value:'O'},
        {label:'Close', value:'C'},
        {label:'Reject', value:'R'},
        {label:'AssignTo', value:'P'}
      ],
      settings: { // perfectscrollbar settings
        maxScrollbarLength: 60,
        wheelSpeed: .60
      },
      showAgentList        : false,
    }
  },
  watch: {
    isSidebarActive (val) {
      if (!val) return
      if (Object.entries(this.data).length === 0) {
        this.initValues()
        this.$validator.reset()
      } else {
        this.chatConsultDetails = this.data
      }
    },
    data () {
      if (!this.data) return
      if (Object.entries(this.data).length === 0) {
        this.initValues()
        this.$validator.reset()
      } else {
        this.chatConsultDetails = this.data
      }
    },
  },
  computed: {
    isSidebarActiveLocal: {
      get () {
        return this.isSidebarActive
      },
      set (val) {
        if (!val) {
          this.$emit('closeSidebar')
        }
      }
    },
    consult_type: {
      get () {
        let _consult_type = this.consult_type_choices.find( e => e.value === this.chatConsultDetails.consultType)
        return { label: _consult_type?_consult_type.label:"", value: this.chatConsultDetails.consultType }
      },
      set (obj) {
        this.chatConsultDetails.consultType = obj.value
      }
    },
    consult_status: {
      get () {
        let _consult_status = this.consult_status_choices.find( e => e.value === this.chatConsultDetails.consultStatus)
        return { label: _consult_status?_consult_status.label:"", value: this.chatConsultDetails.consultStatus }
      },
      set (obj) {
        this.chatConsultDetails.consultStatus = obj.value
      }
    },
    isProcessing() {
      return (this.chatConsultDetails.consultStatus === 'O' || this.chatConsultDetails.consultStatus === 'P')
    },
    isFormValid () {
      return !this.errors.any() // && this.dataName && this.dataCategory && this.dataPrice > 0
    },
    scrollbarTag () { return this.$store.getters.scrollbarTag },
    isOwner() {
      return this.chatConsultDetails.agentId===this.$store.state.AppActiveUser.agentId
    },
    userImg() {
      return (userInfo)=>this.$utils.userImg(userInfo)
    },
    language() {
      return lang.find(this.chatConsultDetails.lang)
    },
    country() {
      return country.find(this.chatConsultDetails.country)
    },
    toLocale() {
      return (theTime) => {
        return this.$utils.toLocale(theTime)
        //return theTime?new Date(theTime).toLocaleString():""
      }
    },
    agentName() {
      return (this.chatConsultDetails&&this.chatConsultDetails.agentInfo)?
      `${this.chatConsultDetails.agentInfo.userName} ( ${ this.chatConsultDetails.agentInfo.agentId} )`:""
    },
    prevAgentName() {
      const prevAgent=this.$store.getters['chat/chatSessionAgent'](this.chatConsultDetails.prevChatId)
      return `${prevAgent.userName} ( ${ prevAgent.agentId} )`
    },

  },
  methods: {
    initValues () {
      // if (this.data.id) return
      // this.chatConsultDetails = null
      // this.customerInfo = null
      // this.agentInfo = null
      this.chatConsultDetails = {
        id: 0,
        customerId: null,
        agentId : null,
        consultType: null,
        consultStatus: null,
        country: null,
        lang : null,
        comment: null,
        createdDtm: null,
        updatedDtm: null
      }
    },
    saveData () {
      this.$validator.validateAll().then(result => {
        if (result) {
          const obj = {
            chatConsultDetails: this.chatConsultDetails,
          }
          this.$store.dispatch('chat/updateChatConsultDetails', obj)
          .then(
              this.$vs.notify({
              title:'Changes Saved',
              text:`Successfully saved your consult with ${this.chatConsultDetails.customerId}.`,
              color:'success'})
          )
          .catch(
            err => { console.error(err) 
            this.$vs.notify({
            title:'Changes failed',
            text:`Your consult with ${this.chatConsultDetails.customerId} failed to save.`,
            color:'danger'})
          })
          
          // this.$emit('closeSidebar')
          // this.initValues()
        }
      })
    },
    completeConsult () {
      this.$validator.validateAll().then(result => {
        if (result) {
          const obj = {
            chatConsultDetails: this.chatConsultDetails,
          }
          this.$store.dispatch('chat/completeChatConsultDetails', obj)
          .then( result => {
              this.$vs.notify({
              title:'Chat closed',
              text:`Closed your consult with ${this.chatConsultDetails.customerId}.`,
              color:'success'})
              this.$emit('removeChatSession')
          })
          .catch(
            err => { console.error(err) 
            this.$vs.notify({
            title:'Close failed',
            text:`Your consult with ${this.chatConsultDetails.customerId} failed to close.`,
            color:'danger'})
          })
          
          // this.$emit('closeSidebar')
          // this.initValues()
        }
      })
    },
    popupAgentList() {
      this.showAgentList = true
    },
    passoverConsult (agent) {
      this.$validator.validateAll().then(result => {
        if (result) {
          const obj = {
            chatConsultDetails: this.chatConsultDetails,
            passoverAgentId: agent.agentId
          }
          this.$store.dispatch('chat/passOverChatConsultDetails', obj)
          .then( result => {
            this.$vs.notify({
            title:'Chat assigned',
            text:`Assigned your consult with ${this.chatConsultDetails.customerId} to ${agent.agentId}.`,
            color:'success'})
            this.$emit('removeChatSession')
          })
          .catch(
            err => { console.error(err) 
            this.$vs.notify({
            title:'Assignment failed',
            text:`Your consult with ${this.chatConsultDetails.customerId} failed to assign.`,
            color:'danger'})
          })

          // this.$emit('closeSidebar')
          // this.initValues()
        }
      })
    },
    selectAgent(agent) {
      console.log("selectAgent")
      this.showAgentList = false
      this.passoverConsult (agent)
    },
    hideAgentList() {
      this.showAgentList = false
    },

  },
  created() {
    this.initValues()
  }
}
</script>

<style lang="scss" scoped>
.add-new-data-sidebar {
  ::v-deep .vs-sidebar--background {
    z-index: 52010;
  }

  ::v-deep .vs-sidebar {
    z-index: 52010;
    width: 400px;
    max-width: 90vw;

    .img-upload {
      margin-top: 2rem;

      .con-img-upload {
        padding: 0;
      }

      .con-input-upload {
        width: 100%;
        margin: 0;
      }
    }
  }
}

.scroll-area--data-list-add-new {
    // height: calc(var(--vh, 1vh) * 100 - 4.3rem);
    height: calc(var(--vh, 1vh) * 100 - 16px - 45px - 82px);

    &:not(.ps) {
      overflow-y: auto;
    }
}
</style>
