<!-- =========================================================================================
  File Name: NoticeEdit.vue
  Description: Notice Edit Page
========================================================================================== -->
<template>
    <vs-prompt
        title="Edit Notice"
        accept-text = "Save"
        cancel-text = "Close"
        button-cancel="border"
        @accept="saveNotice"
        @close="init"
        :is-valid="validateForm"
        :active.sync="activePrompt"
        class=".notice-dialog"
        >
        <div>
            <form>
                <div class="vx-row">
                  <div class="vx-col flex">
                    <vs-dropdown class="cursor-pointer flex" vs-custom-content>
                      <a class="flex items-center" href="#">
                        <vs-chip>
                          <div class="h-2 w-2 rounded-full mr-1" :class="'bg-' + postTypeColor"></div>
                          <span>{{ postType }}</span>
                        </vs-chip>
                        <i class="material-icons">expand_more</i>
                      </a>
                      <!-- <vs-button class="btn-drop" type="filled" icon="expand_more"></vs-button> -->
                      <vs-dropdown-menu style="z-index: 200001">
                            <vs-dropdown-item v-for="(noticeType, index) in noticeTypes" :key="index" @click="updateNoticeType(noticeType.type)">
                                {{ noticeType.name }}
                            </vs-dropdown-item>
                      </vs-dropdown-menu>
                    </vs-dropdown>
                  </div>
                  <div class="vx-col ml-auto flex dropdown-button-container">
                    <p>{{ toLocale(noticeLocal.updatedDtm) }} <span><br> by {{ noticeLocal.registerUserInfo?noticeLocal.registerUserInfo.userName:"None" }}</span></p>
                  </div>
                </div>
                <div class="vx-row">
                  <div class="vx-col w-full notice-content">
                    <vs-input v-validate="'required'" name="title" class="w-full mb-4 mt-5" placeholder="Title" v-model="noticeLocal.title" />
                    <vs-textarea rows="5" label="Add description" v-model="noticeLocal.content" />
                  </div>
                </div>
            </form>
        </div>
    </vs-prompt>
</template>


<script>

export default {
  props: {
    showEdit: {
      type: Boolean,
      required: true
    },
    noticeData: {
      type: Object,
      required: true
    }
  },
  data () {
    return {
      noticeLocal: Object.assign({}, this.noticeData)
    }
  },
  computed: {
    activePrompt: {
      get () {
        return this.showEdit
      },
      set (value) {
        this.$emit('hideEdit', value)
      }
    },
    toLocale() {
      return (theTime) => new Date(theTime).toLocaleString()
    },
    noticeTypes() {
      return this.$store.state.noticeBoard.noticeTypes
    },
    postType: function() {
      //return this.localPostType
        const _postType = this.noticeLocal.postType
        if (_postType === 'S') return "Schedule"//calendar
        else if (_postType === 'U') return "Urgent" //
        else if (_postType === 'W') return "Biz"
        else //if (_postType === 'N') 
          return "Normal"
    },
    postTypeColor() {
      const _postType = this.noticeLocal.postType
        if (_postType === 'S') return "primary"
        else if (_postType === 'U') return "danger"
        else if (_postType === 'W') return "warning"
        else //if (_postType === 'N') 
          return "success"
    },
    validateForm () {
      return !this.errors.any() && this.noticeLocal.title !== ''
    }

  },
  methods: {
    removeNotice () {
      this.$store.dispatch('noticeBoard/deleteNotice', this.noticeLocal.id)
        .then(() => {
          // Fetch Tasks
          this.$store.dispatch('noticeBoard/fetchNotices', this.$store.state.noticeBoard.queryParam)
        })
        .catch((error) => { console.error(error) })
    },
    init () {
      console.log("init")
      //this.noticeData = Object.assign({}, this.$store.getters['noticeBoard/getNotice'](this.noticeData.id))
    },
    saveNotice () {
      this.$store.dispatch('noticeBoard/updateNotice', this.noticeLocal)
      .then(() => {
          // Fetch Tasks
          this.$store.dispatch('noticeBoard/fetchNotices', this.$store.state.noticeBoard.queryParam)
        })
        .catch((error) => { console.error(error) })
    },
    updateNoticeType (type) {
      this.noticeLocal.postType = type
    }
  },
  created () {
    // Register Module UserManagement Module
    // if (!moduleNoticeBoard.isRegistered) {
    //   this.$store.registerModule('noticeBoard', moduleNoticeBoard)
    //   moduleNoticeBoard.isRegistered = true
    // }
    //this.fetch_notice_data(this.$route.params.idd)
  }
}

</script>
