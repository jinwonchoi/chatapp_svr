<!-- =========================================================================================
    File Name: Email.vue
    Description: Email Application (Inbox)
    ----------------------------------------------------------------------------------------
    Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
      Author: Pixinvent
    Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->


<template>
  <div id="consult-app" class="border border-solid d-theme-border-grey-light rounded relative overflow-hidden">

      <vs-sidebar class="items-no-padding" parent="#consult-app" :click-not-close="clickNotClose" :hidden-background="clickNotClose" v-model="isConsultSidebarActive">
          <consult-sidebar :consultTypeTags="consultTypeTags" @closeSidebar="toggleConsultSidebar" :consultTypeFilter="consultTypeFilter" />
      </vs-sidebar>

      <div :class="{'sidebar-spacer': clickNotClose&&!isConsultViewSidebarActive,'sidebar-spacer-cover-all':clickNotClose&&isConsultViewSidebarActive}" 
      class="no-scroll-content border border-solid d-theme-border-grey-light border-r-0 border-t-0 border-b-0">

          <!-- SEARCH BAR -->
          <div class="flex border d-theme-dark-bg items-center">
              <feather-icon class="md:inline-flex lg:hidden ml-4 mr-4 cursor-pointer" icon="MenuIcon" @click.stop="toggleConsultSidebar(true)"/>
              <vs-input icon-no-border icon="icon-search" size="large" icon-pack="feather" placeholder="Search ..." v-model="searchQuery" class="vs-input-no-border vs-input-no-shdow-focus w-full" v-on:keyup.enter="doSearchQuery"/>
          </div>
          <!-- EMAILS LIST -->
          <component :is="scrollbarTag" class="consult-content-scroll-area" :settings="settings" ref="consultListPS" :key="$vs.rtl">
              <transition-group name="list-enter-up" class="consult__consults" tag="ul" appear>
                  <li class="cursor-pointer consult__consult-item" v-for="(consult, index) in consults" :key="String(consultTypeFilter) + String(consult.id)" @click="updateActiveConsult(consult)" :style="{transitionDelay: (index * 0.1) + 's'}">
                      <consult-item :consult="consult"/>
                  </li>
              </transition-group>
          </component>
          <template class="left">
            <vs-pagination v-model="pageNo" :total="totalPages" :max="pageSize" @input="handleChangePage"/>
          </template>
      </div>
      <!-- EMAIL VIEW SIDEBAR -->
      <consult-view
          :consult      = "selConsult"
          :isConsultViewSidebarActive = "isConsultViewSidebarActive"
          :consultTypeFilter      = "consultTypeFilter"
          @previousConsult    = "previousConsult"
          @nextConsult        = "nextConsult"
          @closeSidebar    = "closeConsultViewSidebar" v-if="selConsult">
      </consult-view>
  </div>
</template>

<script>
import ConsultSidebar        from './ConsultSidebar.vue'
import ConsultView            from './ConsultView.vue'
import ConsultItem            from './ConsultItem.vue'
import VuePerfectScrollbar from 'vue-perfect-scrollbar'
import codes from '@/common/codes.js'


export default {
  data () {
    return {
      selConsult : null,
      isConsultViewSidebarActive      : false,
      clickNotClose        : true,
      isConsultSidebarActive : true,
      settings             : {
        maxScrollbarLength : 60,
        wheelSpeed         : 0.30
      },
      pageSize: 7,
      pageNo: 1,
      totalPages: 0,
      // Filter Options
      searchQuery: '',
      sortDir: 'ASC',
      sortField: '',
    }
  },
  watch: {
    isConsultViewSidebarActive (value) {
      if (!value) this.showThread = false
    },
    consultTypeFilter () {
      const scroll_el = this.$refs.consultListPS.$el || this.$refs.consultListPS
      scroll_el.scrollTop = 0
      this.$store.commit('consult/UPDATE_CONSULT_TYPE_FILTER', this.$route.params.filter)
      this.listConsults()
      if (!this.clickNotClose) {
        this.toggleConsultSidebar()
      }
    },
    windowWidth () {
      this.setSidebarWidth()
    }
  },
  computed: {
    consultTypeFilter () {
      return this.$route.params.filter?this.$route.params.filter:""
    },
    consultTypeTags() {
      return codes.consultTypeTags
    },
    consults () {
      return this.$store.state.consult.consults
    },
    scrollbarTag () { return this.$store.getters.scrollbarTag },
    windowWidth () {
      return this.$store.state.windowWidth
    }
  },
  methods: {
    updateActiveConsult (consult) {
      this.toggleConsultSidebar(false)
      this.isConsultViewSidebarActive = true
      this.selConsult = consult
    },
    nextConsult () {
      // const currentConsultIndex = this.consults.findIndex((mail) => mail.id === this.openMailId)
      // if (this.consults[currentMailIndex + 1]) this.openMailId = this.consults[currentMailIndex + 1].id
    },
    previousConsult () {
      // const currentConsultIndex = this.consults.findIndex((mail) => mail.id === this.openMailId)
      // if (this.consults[currentMailIndex - 1]) this.openMailId = this.consults[currentMailIndex - 1].id
    },
    // updateLabels (label) {
    //   this.$store.dispatch('email/updateLabels', { mails: this.selectedMails, label })
    //   this.selectedMails = []
    // },
    //우측 상세 닫기
    closeConsultViewSidebar () {
      this.isConsultViewSidebarActive = false
      if (this.clickNotClose) {
        this.toggleConsultSidebar(true)
      }
    },
    setSidebarWidth () {
      if (this.windowWidth < 992) {
        this.isConsultSidebarActive = this.clickNotClose = false
      } else {
        this.isConsultSidebarActive = this.clickNotClose = true
      }
    },
    //좌측 메뉴 열기/닫기
    toggleConsultSidebar (value = false) {
        this.isConsultSidebarActive = value
    },
    doSearchQuery () {
      this.listConsults()
    },
    entered() {
      console.log("entered")
    },
    handleSearch(searching) {
      console.log("handleSearch")
      console.log("this.$store.state.noticeBoard.totalPage :"+this.$store.state.noticeBoard.totalPage )
      //let _print = `The user searched for: ${searching}\n`
      this.searchQuery= searching
      this.listConsults()
      // this.$store.commit('noticeBoard/TEST_NOTICES')
      // this.totalPages += 1
      // this.pageNo += 1
    },
    handleChangePage(page) {
      //let _print = `The user changed the page to: ${page}\n`
      console.log("handleChangePage:"+page)
      console.log(page)
      this.pageNo=page
      this.listConsults()
    },
    handleSort(key, active) {
      console.log('handleSort:'+key)
      //let _print = `the user ordered: ${key} ${active}\n`
      this.sortDir=active?'ASC':'DESC'
      this.sortField=key
      this.listConsults()
    },
    listConsults() {
      console.log('listConsults')
      this.$store.dispatch('consult/fetchConsults', { 
          pageNo: this.pageNo, 
          pageSize: this.pageSize, 
          sortDir:this.sortDir,
          sortField:this.sortField | 'id',
          searchByOrMap:this.searchQuery?{ "chatComment":this.searchQuery} :{},
        }).then(() => { 
          console.log("this.$store.state.consult.totalPage:"+this.$store.state.consult.totalPage)
          this.totalPages =this.$store.state.consult.totalPage        
        }).catch(err => { console.error(err) })
    }

  },
  components: {
    ConsultItem,
    ConsultSidebar,
    ConsultView,
    // ChatLog,
    // ChatConsultDetails,
    VuePerfectScrollbar
  },
  created () {
    this.setSidebarWidth()
    this.listConsults()
    this.$store.commit('consult/UPDATE_CONSULT_TYPE_FILTER', this.$route.params.filter)        // Update Mail Filter
    this.listConsults()
    // this.$store.dispatch('email/fetchMeta')                                          // Fetch Unread Mails
    // this.$store.dispatch('email/fetchTags')                                          // Fetch Mail Tags
  },
}

</script>


<style lang="scss">
@import "@/assets/scss/vuexy/apps/consult.scss";
</style>
