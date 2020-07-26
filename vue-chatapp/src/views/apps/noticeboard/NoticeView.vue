<!-- =========================================================================================
    File Name: TodoItem.vue
    Description: Single todo item component
    ----------------------------------------------------------------------------------------
    Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
      Author: Pixinvent
    Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->


<template>
    <vs-prompt
        :title=noticeLocal.title
        cancel-text = "Close"
        :buttons-hidden=true
        @close="init"
        :active.sync="activePrompt"
        class=".notice-dialog"
        >
        <div>
            <form>
                <div class="vx-row">
                    <div class="vx-col flex">
                      <vs-chip>
                        <div class="h-2 w-2 rounded-full mr-1" :class="'bg-' + postTypeColor(noticeLocal.postType)"></div>
                        <span>{{ postType(noticeLocal.postType) | capitalize }}</span>
                      </vs-chip>
                    </div>
                    <div class="vx-col ml-auto flex">
                      <p>{{ toLocale(noticeLocal.updatedDtm) }} <br><span> by {{ noticeLocal.registerUserInfo?noticeLocal.registerUserInfo.userName:"None" }} </span></p>
                    </div>

                </div>

                <div class="vx-row">
                    <div class="vx-col w-full notice-content">
                      <vs-divider/>
                      <vs-textarea :rows="noticeLocal.content.split('\n').length" v-model="noticeLocal.content" readonly />
                    </div>
                </div>

            </form>
        </div>
    </vs-prompt>
</template>

<script>
export default {
  props: {
    showView: {
      type: Boolean,
      required: true
    },
    noticeId: {
      type: Number,
      required: true
    }
  },
  data () {
    return {
      noticeLocal: Object.assign({}, 
        this.$store.getters['noticeBoard/getNotice'](this.noticeId))
    }
  },
  computed: {
    activePrompt: {
      get () {
        return this.showView
      },
      set (value) {
        this.$emit('hideView', value)
      }
    },
    toLocale() {
      return (theTime) => new Date(theTime).toLocaleString()
    },
    postType() {
      return (_postType) => {
        if (_postType === 'S') return "Schedule" //calendar
        else if (_postType === 'U') return "Urgent" //
        else if (_postType === 'W') return "Biz"
        else //if (_postType === 'N') 
          return "Normal"
      }
    },
    postTypeColor() {
      return (_postType) => {
        if (_postType === 'S') return "primary"
        else if (_postType === 'U') return "danger"
        else if (_postType === 'W') return "warning"
        else //if (_postType === 'N') 
          return "success"
      }
    },

  },
  methods: {
    init () {
      this.noticeLocal = Object.assign({}, this.$store.getters['noticeBoard/getNotice'](this.noticeId))
    },
  }
}
</script>
<style>
  .notice-content {
    min-height: 200px;
  }
  .notice-dialog {
    min-width: 700px;
  }
</style>