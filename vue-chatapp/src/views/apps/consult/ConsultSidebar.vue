<!-- =========================================================================================
    File Name: EmailSidebar.vue
    Description: Email Application Sidebar(Filter) (Inbox)
    ----------------------------------------------------------------------------------------
    Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
      Author: Pixinvent
    Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->


<template>
    <div class="consult__consult-sidebar h-full">
        <component :is="scrollbarTag" class="consult-filter-scroll-area" :settings="settings" :key="$vs.rtl">
            <div class="consult__labels px-6 py-4">
                <h5 class="mb-8">Labels</h5>
                <div class="consult__lables-list">
                    <router-link tag="span" class="consult__label flex items-center mb-4 cursor-pointer" v-for="(tag, index) in consultTypeTags" :key="index" :to="`${baseUrl}/${tag.value}`">
                        <div class="ml-1 h-3 w-3 rounded-full mr-4" :class="'border-2 border-solid border-' + tag.color"></div>
                        <span class="text-lg" :class="{'text-primary': consultTypeFilter == tag.value}">{{ tag.label }}</span>
                    </router-link>
                </div>
            </div>
        </component>
    </div>
</template>

<script>
import VuePerfectScrollbar from 'vue-perfect-scrollbar'
import codes from '@/common/codes.js'

export default {
  props: {
    consultTypeFilter: {
      type: String,
      required: true
    }
  },
  data () {
    return {
      settings: {
        maxScrollbarLength: 60,
        wheelSpeed: 0.30
      }
    }
  },
  computed: {
    baseUrl () {
      const path = this.$route.path
      return path.slice(0, path.lastIndexOf('/'))
    },
    scrollbarTag () { return this.$store.getters.scrollbarTag },
    consultTypeTags() {
      return codes.consultTypeTags
    }
  },
  methods: {
  },
  components: {
    VuePerfectScrollbar
  }
}

</script>

