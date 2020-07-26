<!-- =========================================================================================
  File Name: NoticeEdit.vue
  Description: Notice Edit Page
========================================================================================== -->
<template>
  <vs-prompt
    title="Assign this chat to.. "
    accept-text = "Ok"
    cancel-text = "Cancel"
    button-cancel="border"
    @accept="selectAgent"
    @close="init"
    :active.sync="activePrompt"
    class=".notice-dialog"
    >
    <div id="agent-list-table">
      <vs-table 
        :sst="true"
        :total="totalCount"
        max-items="pageSize"
        @change-page="handleChangePage"
        :data="users">
        <!-- <div slot="header"> -->
        <div  slot="header" class="flex flex-wrap items-center data-list-btn-container">
          <vs-input class="vs-table--search" v-model="searchQuery" placeholder="Search..." v-on:keyup.enter="doSearchQuery"/>
        </div>
        <template slot="thead">
          <vs-th class="header-title">Agents</vs-th>
        </template>
        <template slot-scope="{data}">
          <tbody>
            <vs-tr :data="tr" :key="indextr" v-for="(tr, indextr) in data">
              <vs-td class="mb-4"> 
                <div class="user-name" @click="selectAgent(data[indextr])">
                  <div class="mr-3"><vs-avatar class="m-0" :src="userImg(data[indextr])" size="35px" /></div>
                  <div class="leading-tight">
                      <p class="font-medium">{{ data[indextr].agentId }}</p>
                      <span class="text-xs">{{ data[indextr].userName }}</span>
                  </div>
                </div>
              </vs-td>
            </vs-tr>
          </tbody>
          <template class="center">
            <vs-pagination v-model="pageNo" :total="totalPages" :max="pageSize" @input="handleChangePage"/>
          </template>
        </template>
      </vs-table>
       <vs-popup title="Confirm Assignment" :active.sync="popupActive">
        <p>
          This chat will be assigned to {{selectedAgent}}.
        </p>
        <div class="button-align">
        <vs-button @click="confirmAssignment" color="primary"  type="border" >Ok</vs-button>
        <vs-button @click="cancelAssignment" type="filled" >Cancel</vs-button>
        </div>
      </vs-popup>
    </div>
  </vs-prompt>
</template>


<script>
import moduleUserManagement from '@/store/user-management/moduleUserManagement.js'

export default {
  props: {
    showAgentList: {
      type: Boolean,
      required: true
    }
  },
  data () {
    return {
      pageSize: 7,
      pageNo: 1,
      totalPages: 0,
      // Filter Options
      searchQuery: '',
      popupActive:false,
      selAgent: null
    }
  },
  // components: {
  //   utils,
  // },  
  computed: {
    activePrompt: {
      get () {
        console.log("list agent")
        this.listUsers()  
        return this.showAgentList
      },
      set (value) {
        this.$emit('hideAgentList', value)
      }
    },
    users() {
      return this.$store.state.userManagement.users
    },
    userImg() {
      return (userInfo)=>this.$utils.userImg(userInfo)
    },
    totalCount() {
      return this.$store.state.userManagement.totalCount
    },
    selectedAgent() {
      return this.selAgent?`${this.selAgent.userName}(${this.selAgent.agentId})`:""
    }
  },
  methods: {
    init () {
      console.log("init")
    },
    selectAgent (agent) {
      this.selAgent=agent
      this.popupActive = true
    },
    doSearchQuery () {
      this.listUsers()
    },
    handleChangePage(page) {
      this.pageNo=page
      this.listUsers()
    },
    listUsers() {
      //this.$store.dispatch('userManagement/fetchUsers', {"map":{"bizId": this.$store.state.AppActiveUser.bizId }}).catch(err => { console.error(err) })
      this.$store.dispatch('userManagement/fetchUsers', { 
          pageNo: this.pageNo, 
          pageSize: this.pageSize, 
          sortDir:this.sortDir,
          sortField:this.sortField | 'id',
          searchByOrMap:this.searchQuery?{ "userName":this.searchQuery, "agentId":this.searchQuery} :{},
          searchMap: {"bizId": this.$store.state.AppActiveUser.bizId }
        }).then(() => { 
          this.totalPages =this.$store.state.userManagement.totalPage        
        }).catch(err => { console.error(err) })
    },
    confirmAssignment() {
      this.popupActive = false
      this.$emit('selectAgent', this.selAgent)
    },
    cancelAssignment() {
      this.popupActive = false
    }
    // openConfirm() {
    //   this.$vs.dialog({
    //     type: 'confirm',
    //     color: 'danger',
    //     title: `Confirm`,
    //     text: 'Cake sesame snaps cupcake gingerbread danish I love gingerbread. Apple pie pie jujubes chupa chups.',
    //     accept: this.acceptAlert
    //   })
    // },
    // acceptAlert() {
    //   this.$vs.notify({
    //     color: 'danger',
    //     title: 'Deleted image',
    //     text: 'The selected image was successfully deleted'
    //   })
    // },
  },
  created() {
        if (!moduleUserManagement.isRegistered) {
      this.$store.registerModule('userManagement', moduleUserManagement)
      moduleUserManagement.isRegistered = true
    }

  }
}

</script>
<style lang="scss">
#agent-list-table {
  .vs-table--tbody-table {
    min-width: 50px;
  }
  .vs-con-table {

    /*
      Below media-queries is fix for responsiveness of action buttons
      Note: If you change action buttons or layout of this page, Please remove below style
    */
    .vs-table--search {
      width: 100%;
    }
    .header-title {
      // max-width: 10px;
      // width: 10px;
      font-size: 14px;
      font-weight: 700;
    }
    .agent-id {
      max-width: 10px;
      width: 10px;
    }
    .user-name {
      max-width: 100rem;
      width: 100%;
      display: flex;
      display: -webkit-flex;
      align-items: center;
      /* font-size: 20px;*/
    }

    .vs-table--header {
      display: flex;
      flex-wrap: wrap;
       margin-left: 0.1rem;
       margin-right: 0.5rem;
      > span {
        display: flex;
        flex-grow: 1;
      }

      .vs-table--search{
        // padding-top: 0;

        .vs-table--search-input {
          padding: 0.9rem 1.5rem;
          font-size: 1rem;

          &+i {
            left: 1rem;
          }

          &:focus+i {
            left: 1rem;
          }
        }
      }
    }

    .vs-table {
      border-collapse: separate;
      border-spacing: 0 0.3rem;
      padding: 0 1rem;

      tr{
          box-shadow: 0 4px 4px 0 rgba(0,0,0,.05);
          td{
            padding: 2px;
            &:first-child{
              border-top-left-radius: .5rem;
              border-bottom-left-radius: .5rem;
            }
            &:last-child{
              border-top-right-radius: .5rem;
              border-bottom-right-radius: .5rem;
            }
          }
      }
    }

    .vs-table--thead{
      th {
        padding-top: 0;
        padding-bottom: 0;

        .vs-table-text{
          font-weight: 600;
        }
      }
      th.td-check{
        padding: 0 15px !important;
      }
      tr{
        background: none;
        box-shadow: none;
      }
    }

    .vs-table--pagination {
      justify-content: left;
    }
  }
}
</style>