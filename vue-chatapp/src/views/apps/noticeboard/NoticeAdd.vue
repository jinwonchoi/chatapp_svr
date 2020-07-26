<!-- =========================================================================================
  File Name: NoticeEdit.vue
  Description: Notice Edit Page
========================================================================================== -->
<template>
    <vs-prompt
        title="Add Notice"
        accept-text = "Save"
        cancel-text = "Close"
        button-cancel="border"
        @cancel="clearFields"
        @accept="saveNotice"
        @close="clearFields"
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
    showAdd: {
      type: Boolean,
      required: true
    },
  },
  data () {
    return {
      noticeLocal: {
        title: "",
        content: "",
        registerId: this.$store.state.AppActiveUser.uid,
        postType: "N",
        postLevel: 0,
        refId: 0
      }
    }
  },
  computed: {
    activePrompt: {
      get () {
        return this.showAdd
      },
      set (value) {
        this.$emit('hideAdd', value)
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
    clearFields () {
      Object.assign(this.noticeLocal, {
        title: "",
        content: "",
        registerId: this.$store.AppActiveUser.uid,
        postType: "N",
        postLevel: 0,
        refId: 0
      })
    },
    saveNotice () {
      this.$store.dispatch('noticeBoard/addNotice', this.noticeLocal)
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
}

</script>
