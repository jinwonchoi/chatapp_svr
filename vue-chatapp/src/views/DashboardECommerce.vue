<!-- =========================================================================================
    File Name: DashboardEcommerce.vue
    Description: Dashboard - Ecommerce
    ----------------------------------------------------------------------------------------
    Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
      Author: Pixinvent
    Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->

<template>
    <div>
        <div class="vx-row">
            <div class="vx-col w-full sm:w-1/2 md:w-1/2 lg:w-1/4 xl:w-1/4 mb-base">
                <statistics-card-line
                  v-if="customerInboundInfo"
                  icon="PhoneIncomingIcon"
                  :statistic="customerInboundInfo.analyticsData.callCount | k_formatter"
                  statisticTitle="Customers Inbound.No"
                  :chartData="customerInboundInfo.series"
                  type="area" />
            </div>

            <div class="vx-col w-full sm:w-1/2 md:w-1/2 lg:w-1/4 xl:w-1/4 mb-base">
                <statistics-card-line
                  v-if="responseCallsInfo.analyticsData"
                  icon="PhoneOutgoingIcon"
                  :statistic="responseCallsInfo.analyticsData.callCount | k_formatter"
                  statisticTitle="Response.No"
                  :chartData="responseCallsInfo.series"
                  color="success"
                  type="area" />
            </div>

            <div class="vx-col w-full sm:w-1/2 md:w-1/2 lg:w-1/4 xl:w-1/4 mb-base">
                <statistics-card-line
                  v-if="resolvedCallsInfo.analyticsData"
                  icon="PhoneOffIcon"
                  :statistic="resolvedCallsInfo.analyticsData.callCount"
                  statisticTitle="Resolved.No"
                  :chartData="resolvedCallsInfo.series"
                  color="danger"
                  type="area" />
            </div>
            <!-- <div class="vx-col w-full sm:w-1/2 md:w-1/2 lg:w-1/4 xl:w-1/4 mb-base">
                <statistics-card-line
                  v-if="ordersRecevied.analyticsData"
                  icon="ShoppingBagIcon"
                  :statistic="ordersRecevied.analyticsData.orders | k_formatter"
                  statisticTitle="Orders Received"
                  :chartData="ordersRecevied.series"
                  color="warning"
                  type="area" />
            </div> -->
            <!-- RADIAL CHART -->
            <!-- <div class="vx-col w-full sm:w-1/2 md:w-1/2 lg:w-1/2 xl:w-1/2 mb-base"> -->
            <div class="vx-col w-full sm:w-1/2 md:w-1/2 lg:w-1/4 xl:w-1/4 mb-base">
            <!-- <div class="vx-col w-full mb-base"> -->
                <vx-card title="Goal Overview">
                    <template slot="actions">
                        <feather-icon icon="HelpCircleIcon" svgClasses="w-6 h-6 text-grey"></feather-icon>
                    </template>

                    <!-- CHART -->
                    <template slot="no-body">
                        <div class="mt-10">
                            <vue-apex-charts type="radialBar" height="240" :options="analyticsData.goalOverviewRadialBar.chartOptions" :series="goalOverviewInfo.series" />
                        </div>
                    </template>

                    <!-- DATA -->
                    <div class="flex justify-between text-center mt-6" slot="no-body-bottom">
                        <div class="w-1/2 border border-solid d-theme-border-grey-light border-r-0 border-b-0 border-l-0">
                            <p class="mt-4">Resolved</p>
                            <p class="mb-4 text-3xl font-semibold">{{ goalOverviewInfo.analyticsData.resolvedCount }}</p>
                        </div>
                        <div class="w-1/2 border border-solid d-theme-border-grey-light border-r-0 border-b-0">
                            <p class="mt-4">Total</p>
                            <p class="mb-4 text-3xl font-semibold">{{ goalOverviewInfo.analyticsData.totalCount }}</p>
                        </div>
                    </div>
                </vx-card>
            </div>
        </div>
        <div class="vx-row">

            <!-- CHAT CARD -->
            <div id="chat-card" class="vx-col w-full lg:w-1/2 lg:mt-0 mt-base">
                <vx-card title="Chat" class="overflow-hidden">
                    <template slot="no-body">
                        <!-- <div class="chat-card-log">
                            <component :is="scrollbarTag" ref="chatLogPS" class="scroll-area pt-6 px-6" :settings="settings" :key="$vs.rtl">
                                <ul ref="chatLog">
                                        <li class="flex items-start" :class="{'flex-row-reverse': msg.isSent, 'mt-4': index}" v-for="(msg, index) in chatLog" :key="index">
                                            <vs-avatar size="40px" class="m-0 flex-shrink-0" :class="msg.isSent ? 'ml-5' : 'mr-5'" :src="msg.senderImg"></vs-avatar>
                                            <div class="msg relative bg-white shadow-md py-3 px-4 mb-2 rounded-lg max-w-md" :class="{'chat-sent-msg bg-primary-gradient text-white': msg.isSent, 'border border-solid d-theme-border-grey-light': !msg.isSent}">
                                                <span>{{ msg.msg }}</span>
                                            </div>
                                        </li>
                                </ul>
                            </component>
                        </div>
                        <div class="flex bg-white chat-input-container p-6">
                            <vs-input class="mr-3 w-full" v-model="chatMsgInput" @keyup.enter="chatMsgInput = ''" placeholder="Type Your Message" ></vs-input>
                            <vs-button icon-pack="feather" icon="icon-send" @click="chatMsgInput = ''"></vs-button>
                        </div> -->
                        <component :is="scrollbarTag" class="chat-scroll-area pt-4" :settings="settings" :key="$vs.rtl">
                            <div class="chat-card-log chat__chats-list mb-8">
                                <ul class="chat__active-chats bordered-items">
                                    <li class="cursor-pointer" v-for="(chatSession, index) in chatSessions" :key="index" >
                                        <router-link :to="{name:'chat'}">
                                            <chat-session showLastMsg :chatSession="chatSession" :lastMessaged="chatSession.updatedDtm" 
                                        :lastMessage="chatSession.lastMessage" 
                                        :isActiveChatSession="isActiveChatSession(chatSession.id)"></chat-session>
                                        </router-link>                                        
                                    </li>
                                </ul>
                                <div class="chat__chats-list-bottom"></div>
                            </div>
                        </component>
                    </template>
                </vx-card>
            </div>

            <!-- NOTICE BOARD CARD -->
            <div class="vx-col w-full lg:w-1/2 lg:mt-0 mt-base">
                <vx-card title="Notice Updates" class="overflow-hidden">
                    <template slot="no-body">
                        <!-- <div class="chat-card-log">
                            <component :is="scrollbarTag" ref="chatLogPS" class="scroll-area pt-6 px-6" :settings="settings" :key="$vs.rtl">
                                <ul ref="chatLog">
                                        <li class="flex items-start" :class="{'flex-row-reverse': msg.isSent, 'mt-4': index}" v-for="(msg, index) in chatLog" :key="index">
                                            <vs-avatar size="40px" class="m-0 flex-shrink-0" :class="msg.isSent ? 'ml-5' : 'mr-5'" :src="msg.senderImg"></vs-avatar>
                                            <div class="msg relative bg-white shadow-md py-3 px-4 mb-2 rounded-lg max-w-md" :class="{'chat-sent-msg bg-primary-gradient text-white': msg.isSent, 'border border-solid d-theme-border-grey-light': !msg.isSent}">
                                                <span>{{ msg.msg }}</span>
                                            </div>
                                        </li>
                                </ul>
                            </component>
                        </div>
                        <div class="flex bg-white chat-input-container p-6">
                            <vs-input class="mr-3 w-full" v-model="chatMsgInput" @keyup.enter="chatMsgInput = ''" placeholder="Type Your Message" ></vs-input>
                            <vs-button icon-pack="feather" icon="icon-send" @click="chatMsgInput = ''"></vs-button>
                        </div> -->
                        <component :is="scrollbarTag" class="chat-scroll-area pt-4" :settings="settings" :key="$vs.rtl">
                            <notice-list class="chat-card-log mb-8"></notice-list>
                        </component>

                    </template>
                </vx-card>
            </div>

        </div>
       


   
    </div>
</template>

<script>
import VuePerfectScrollbar from 'vue-perfect-scrollbar'
import VueApexCharts from 'vue-apexcharts'
import StatisticsCardLine from '@/components/statistics-cards/StatisticsCardLine.vue'
import analyticsData from './ui-elements/card/analyticsData.js'
import ChangeTimeDurationDropdown from '@/components/ChangeTimeDurationDropdown.vue'
import ChatSession         from '@/views/apps/chat/ChatSession.vue'
import NoticeList         from '@/views/apps/noticeboard/notice-list/NoticeList.vue'

export default{
  components: {
    VueApexCharts,
    StatisticsCardLine,
    VuePerfectScrollbar,
    ChangeTimeDurationDropdown,
    ChatSession,
    NoticeList,
  },
  data () {
    return {
      customerInBound: {},
      responseCalls: {},
      resolvedCalls: {},
      resolvedGoal: {},

      revenueComparisonLine: {},
      goalOverview: {},

      browserStatistics: [],
      clientRetentionBar: {},

      sessionsData: {},
      chatLog: [],
      chatMsgInput: '',
      customersData: {},

      analyticsData,
      settings: { // perfectscrollbar settings
        maxScrollbarLength: 60,
        wheelSpeed: .60
      },
      activeChatSession    : null,
    }
  },
  computed: {
    scrollbarTag () { return this.$store.getters.scrollbarTag },
    customerInboundInfo () { 
        return this.$store.state.dashboard.customerInBound 
    },
    responseCallsInfo () {
        return this.$store.state.dashboard.responseCalls
    },
    resolvedCallsInfo () {
        return this.$store.state.dashboard.resolvedCalls
    },
    goalOverviewInfo () {
        return this.$store.state.dashboard.resolvedGoal
    },
    chatSessions () {
      return this.$store.state.chat.chatSessions
    },
    isActiveChatSession () {
      return (chatSessionId) => this.activeChatSession && (chatSessionId === this.activeChatSession.id) 
    },

  },
  methods: {
    updateActiveChatSession() {
        console.log("do nothing")

    }
  },
  mounted () {
    const scroll_el = this.$refs.chatLogPS.$el || this.$refs.chatLogPS
    scroll_el.scrollTop = this.$refs.chatLog.scrollHeight
  },
  created () {
    console.log('Dashboard Created')

    this.$store.dispatch('dashboard/getCustomerInboundCount', { 
          bizId: this.$store.state.AppActiveUser.bizId
    }).then(() => { 
        console.log('ok')
    }).catch(err => { console.error(err) })

    this.$store.dispatch('dashboard/getResponseCount', { 
          bizId: this.$store.state.AppActiveUser.bizId
    }).then(() => { 
        console.log('ok')
    }).catch(err => { console.error(err) })

    this.$store.dispatch('dashboard/getResolvedCount', { 
          bizId: this.$store.state.AppActiveUser.bizId
    }).then(() => { 
        console.log('ok')
    }).catch(err => { console.error(err) })

    this.$store.dispatch('dashboard/getResolvedGoal', { 
          bizId: this.$store.state.AppActiveUser.bizId
    }).then(() => { 
        console.log('ok')
    }).catch(err => { console.error(err) })

    this.$store.dispatch('chat/fetchChatSessionStatus')
    .then(() => { 
        console.log('ok')
    }).catch(err => { console.error(err) })

  }
}
</script>

<style lang="scss">
.chat-card-log {
    height: 400px;

    .chat-sent-msg {
        background-color: #f2f4f7 !important;
    }
}
</style>
<style lang="scss">
@import "@/assets/scss/vuexy/apps/chat.scss";
</style>
