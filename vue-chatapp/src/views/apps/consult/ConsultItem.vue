<!-- =========================================================================================
    File Name: MailItem.vue
    Description: Mail Item - Displays mail item
    ----------------------------------------------------------------------------------------
    Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
      Author: Pixinvent
    Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->

<template>
    <div class="consult__consult-item sm:px-4 px-2 py-4">

        <!-- MAIL ROW 1 : META -->
        <div class="flex w-full">
            <vs-avatar class="sender__avatar flex-shrink-0 mr-3 border-2 border-solid border-white" :src="userImg(consult.agentInfo)" size="40px"></vs-avatar>

            <div class="flex w-full justify-between items-start">
                <div class="consult__details">
                    <h5 class="mb-1 font-semibold">{{ consult.agentInfo?consult.agentInfo.userName:"" }}</h5>
                    <span>{{ consult.customerId }}</span>
                </div>

                <div class="consult-item__meta flex items-center">
                    <div class="consult__labels hidden sm:flex items-center">
                            <!-- <vs-chip color="primary">Basic Chip</vs-chip> -->
                        <div class="h-2 w-2 rounded-full mr-2" :class="'bg-' + consultStatus(consult).color"></div>
                    </div>
                    <span>{{ consult.updatedDtm | fulltime }}</span>
                </div>
            </div>
        </div>

        <!-- /MAIL ROW 1 -->

        <!-- MAIL ROW 2 : MSG & ACTIONS -->
        <div class="flex w-full">
            <div class="flex items-center ml-1">
                <!-- <vs-checkbox v-model="isSelectedMail" @click.stop class="vs-checkbox-small ml-0 mr-1"></vs-checkbox>
                <feather-icon icon="StarIcon" class="cursor-pointer" :svgClasses="[{'text-warning fill-current stroke-current': mail.isStarred}, 'w-5', 'h-5']" @click.stop="toggleIsStarred"></feather-icon> -->
            </div>
            <div class="consult__message truncate ml-3">
                <span>{{ consult.chatComment | filter_tags }}</span>
            </div>
        </div>
        <!-- /MAIL ROW 2 -->
    </div>
</template>

<script>
export default {
  props: {
    consult: {
      type: Object,
      required: true
    }
  },
  data () {
    return {
      consult_type_choices: [
        {label:'Questions', value:'B', color:'primary'},
        {label:'Complaints', value:'C', color:'warning'},
        {label:'AS Request', value:'A', color:'danger'},
      ],
      consult_status_choices: [
        {label:'Open', value:'O', color:'primary'},
        {label:'Close', value:'C', color:'success'},
        {label:'Reject', value:'R', color:'danger'},
        {label:'AssignTo', value:'P', color:'warning'}
      ],
    }
  },
  computed: {
    labelColor () {
      return (label) => {
        const tags = this.$store.state.email.mailTags
        return tags.find((tag) => {
          return tag.value === label
        }).color
      }
    },
    userImg() {
      return (userInfo)=>this.$utils.userImg(userInfo)
    },
    consultType() {
      return (value) => {
        let _consult_type = this.consult_type_choices.find( e => e.value === value.consultType)
        return _consult_type?_consult_type.label:""
      }
    },
    consultStatus() {
      return (value) => {
        let _consult_status = this.consult_status_choices.find( e => e.value === value.consultStatus)
        return _consult_status?_consult_status:{label:'', color:'dark'}
      }
    },    
  },
  methods: {
    toggleIsStarred () {
      const payload = { mailId: this.mail.id, value: !this.mail.isStarred }
      this.$store.dispatch('email/toggleIsMailStarred', payload)
    }
  }
}

</script>

