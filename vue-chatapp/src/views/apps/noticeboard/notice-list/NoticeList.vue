<!-- =========================================================================================
  File Name: UserList.vue
  Description: User List page
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->

<template>

  <div id="data-list-list-view" class="data-list-container">

    <!-- <data-view-sidebar :isSidebarActive="addNewDataSidebar" @closeSidebar="toggleDataSidebar" :data="popupData" /> -->
      <!--  -->

    <vs-table 
      :sst="true"
      :total="totalCount"
      max-items="pageSize"
      @change-page="handleChangePage"
      @sort="handleSort"
      :data="notices">

      <!-- <div slot="header"> -->
      <div slot="header"  class="flex-grow">
        <!-- flex-grow justify-between -->
        <!-- ITEMS PER PAGE -->
        <vs-dropdown vs-trigger-click class="cursor-pointer">
          <div class="p-4 border border-solid d-theme-border-grey-light rounded-full d-theme-dark-bg cursor-pointer flex items-center justify-between font-medium">
            <span class="mr-2">{{ pageNo * pageSize - (pageSize - 1) }} - {{ totalCount - pageNo * pageSize > 0 ? pageNo * pageSize : totalCount }} of {{ totalCount }}</span>
            <feather-icon icon="ChevronDownIcon" svgClasses="h-4 w-4" />
          </div>
          <!-- <vs-button class="btn-drop" type="line" color="primary" icon-pack="feather" icon="icon-chevron-down"></vs-button> -->
          <vs-dropdown-menu>

            <vs-dropdown-item @click="gridApi.paginationSetPageSize(10)">
              <span>10</span>
            </vs-dropdown-item>
            <vs-dropdown-item @click="gridApi.paginationSetPageSize(20)">
              <span>20</span>
            </vs-dropdown-item>
            <vs-dropdown-item @click="gridApi.paginationSetPageSize(25)">
              <span>25</span>
            </vs-dropdown-item>
            <vs-dropdown-item @click="gridApi.paginationSetPageSize(30)">
              <span>30</span>
            </vs-dropdown-item>
          </vs-dropdown-menu>
        </vs-dropdown>

      </div>
        <div  slot="header" class="flex flex-wrap items-center data-list-btn-container">
          <vs-input class="sm:mr-4 mr-0 sm:w-auto w-full sm:order-normal order-3 sm:mt-0 mt-4" v-model="searchQuery" placeholder="Search..." v-on:keyup.enter="doSearchQuery"/>

          <!-- ACTION - DROPDOWN -->
          <!-- <vs-dropdown vs-trigger-click class="cursor-pointer">
            <div class="p-3 shadow-drop rounded-lg d-theme-dark-light-bg cursor-pointer flex items-end justify-center text-lg font-medium w-32">
              <span class="mr-2 leading-none">Actions</span>
              <feather-icon icon="ChevronDownIcon" svgClasses="h-4 w-4" />
            </div>

            <vs-dropdown-menu>

              <vs-dropdown-item>
                <span class="flex items-center">
                  <feather-icon icon="TrashIcon" svgClasses="h-4 w-4" class="mr-2" />
                  <span>Delete</span>
                </span>
              </vs-dropdown-item>

              <vs-dropdown-item>
                <span class="flex items-center">
                  <feather-icon icon="ArchiveIcon" svgClasses="h-4 w-4" class="mr-2" />
                  <span>Archive</span>
                </span>
              </vs-dropdown-item>

              <vs-dropdown-item>
                <span class="flex items-center">
                  <feather-icon icon="FileIcon" svgClasses="h-4 w-4" class="mr-2" />
                  <span>Print</span>
                </span>
              </vs-dropdown-item>
            </vs-dropdown-menu>
          </vs-dropdown>         -->



          <!-- ADD NEW -->
            <!-- <div class="p-3 shadow-drop rounded-lg d-theme-dark-light-bg cursor-pointer flex items-end justify-center text-lg font-medium w-32"> -->

          <div class="btn-add-new p-2 ml-4 mr-4 rounded-lg cursor-pointer flex items-center justify-center text-lg font-medium text-base text-primary border border-solid border-primary" @click="addNewData">
              <feather-icon icon="PlusIcon" svgClasses="h-4 w-4" />
              <span class="ml-2 text-base text-primary">Add New</span>
          </div>
        </div>
      
      
      <template slot="thead">
        <vs-th sort-key="updatedDtm">Updated</vs-th>
        <!-- <vs-th sort-key="postType">post type</vs-th>
        <vs-th>Action</vs-th> -->
      </template>

        <template slot-scope="{data}">
          <tbody>
            <vs-tr :data="tr" :key="indextr" v-for="(tr, indextr) in data">

              <vs-td :data="data[indextr].updatedDtm" class="main-title">  
                <!-- @showDisplayPrompt="enableShowView($event)" -->
                <div @click="viewData(data[indextr].id)">
                  <p class="updated-dtm font-light truncate">{{ toLocale(data[indextr].updatedDtm) }} <span>by {{ data[indextr].registerUserInfo?data[indextr].registerUserInfo.userName:"None" }} </span></p><br>
                  <vs-chip>
                    <div class="h-2 w-2 rounded-full mr-1" :class="'bg-' + postTypeColor(data[indextr].postType)"></div>
                    <span>{{ postType(data[indextr].postType) | capitalize }}</span>
                  </vs-chip>
                  <p class="notice-title font-medium truncate">{{ data[indextr].title }}</p>
                </div>
              </vs-td>

              <!-- <vs-td>
                <vs-chip :color="getOrderStatusColor(tr.order_status)" class="product-order-status">{{ tr.order_status | title }}</vs-chip>
                추후 공지종류 => 긴급, 정보, 등
              </vs-td> -->


              <vs-td class="whitespace-no-wrap">
                <feather-icon icon="EditIcon" svgClasses="w-5 h-5 hover:text-primary stroke-current" @click.stop="editData(tr)" />
                <feather-icon icon="TrashIcon" svgClasses="w-5 h-5 hover:text-danger stroke-current" class="ml-2" @click.stop="deleteData(tr.id)"/> 
              </vs-td>

            </vs-tr>
          </tbody>
          <template class="left">
            <vs-pagination v-model="pageNo" :total="totalPages" :max="pageSize" @input="handleChangePage"/>
          </template>
        </template>
    </vs-table>
            <!-- EDIT TODO DIALOG -->
    <notice-view :showView="showView" :noticeId="noticeIdToView" @hideView="hideView" v-if="showView"/>
    <notice-edit :showEdit="showEdit" :noticeData="popupData" @hideEdit="hideEdit" v-if="showEdit"/>
    <notice-add :showAdd="showAdd" @hideAdd="hideAdd" v-if="showAdd"/>
  </div>

</template>
<script>
//import vSelect from 'vue-select'
// Store Module
//import moduleNoticeBoard from '@/store/notice-board/moduleNoticeBoard.js'
// Cell Renderer
import NoticeView       from '../NoticeView.vue'
import NoticeEdit       from '../NoticeEdit.vue'
import NoticeAdd       from '../NoticeAdd.vue'
//pagination
//https://github.com/lusaxweb/vuesax/issues/545
//참고 서버연동 테이블처리
//https://lusaxweb.github.io/vuesax/components/table.html#filter-and-sorter
export default {
  components: {
    NoticeView,
    NoticeEdit,
    NoticeAdd,
  },
  data () {
    return {
      selected:[],
      pageSize: 7,
      pageNo: 1,
      totalPages: 0,
      // Filter Options
      searchQuery: '',
      sortDir: 'ASC',
      sortField: '',
      currentx: 5,
      // popup noticeView
      showView : false,
      showEdit : false,
      showAdd  : false,
      noticeIdToView  : 0,
      popupData : {},
      // Cell Renderer Components
      components: {
        
      }
    }
  },
  watch: {
  },
  computed: {
    notices () {
      return this.$store.state.noticeBoard.notices
    },
    totalCount() {
      return this.$store.state.noticeBoard.totalCount
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
    // enableShowView (itemId) {
    //   this.noticeIdToView  = itemId
    //   this.showView = true
    // },
    addNewData () {
      this.showAdd = true
    },
    hideAdd () {
      this.showAdd = false
    },
    deleteData (id) {
      console.log("deleteData")
      this.$store.dispatch('noticeBoard/deleteNotice', id)
        .then(() => {
          // Fetch Tasks
          this.$store.dispatch('noticeBoard/fetchNotices', this.$store.state.noticeBoard.queryParam)
        })
        .catch(err => { console.error(err) })

    },
    editData (data) {
      //this.popupData = JSON.parse(JSON.stringify(this.blankData))
      this.popupData = data
      this.showEdit = true
    },
    hideEdit () {
      this.showEdit = false
    },
    viewData (id) {
      this.noticeIdToView  = id
      this.showView = true
    },
    hideView () {
      this.showView = false
    },
    getOrderStatusColor (status) {
      if (status === 'on_hold')   return 'warning'
      if (status === 'delivered') return 'success'
      if (status === 'canceled')  return 'danger'
      return 'primary'
    },
    getPopularityColor (num) {
      if (num > 90)  return 'success'
      if (num > 70)  return 'primary'
      if (num >= 50) return 'warning'
      if (num < 50)  return 'danger'
      return 'primary'
    },
    doSearchQuery () {
      this.listNotices()
    },
    entered() {
      console.log("entered")
    },
    handleSearch(searching) {
      console.log("handleSearch")
      console.log("this.$store.state.noticeBoard.totalPage :"+this.$store.state.noticeBoard.totalPage )
      //let _print = `The user searched for: ${searching}\n`
      this.searchQuery= searching
      this.listNotices()
      // this.$store.commit('noticeBoard/TEST_NOTICES')
      // this.totalPages += 1
      // this.pageNo += 1
    },
    handleChangePage(page) {
      //let _print = `The user changed the page to: ${page}\n`
      console.log("handleChangePage")
      console.log(page)
      this.pageNo=page
      this.listNotices()
    },
    handleSort(key, active) {
      //let _print = `the user ordered: ${key} ${active}\n`
      this.sortDir=active?'ASC':'DESC'
      this.sortField=key
      this.listNotices()
    },
    listNotices() {
      console.log('listNotices')
      this.$store.dispatch('noticeBoard/fetchNotices', { 
          pageNo: this.pageNo, 
          pageSize: this.pageSize, 
          sortDir:this.sortDir,
          sortField:this.sortField | 'id',
          searchByOrMap:this.searchQuery?{ "content":this.searchQuery, "title":this.searchQuery} :{}
        }).then(() => { 
          console.log("this.$store.state.noticeBoard.totalPage:"+this.$store.state.noticeBoard.totalPage)
          this.totalPages =this.$store.state.noticeBoard.totalPage        
        }).catch(err => { console.error(err) })
    }
  },
  mounted () {
    this.isMounted = true
  },
  created () {
    console.log('created')
    this.listNotices() 
  }
}

</script>

<style lang="scss">
#data-list-list-view {
  .vs-con-table {

    /*
      Below media-queries is fix for responsiveness of action buttons
      Note: If you change action buttons or layout of this page, Please remove below style
    */
    @media (max-width: 689px) {
      .vs-table--search {
        margin-left: 0;
        max-width: unset;
        width: 100%;

        .vs-table--search-input {
          width: 100%;
        }
      }
    }

    @media (max-width: 461px) {
      .items-per-page-handler {
        display: none;
      }
    }

    @media (max-width: 341px) {
      .data-list-btn-container {
        width: 100%;

        .dd-actions,
        .btn-add-new {
          width: 100%;
          margin-right: 0 !important;
        }
      }
    }

    .updated-dtm {
      max-width: 20rem;
      font-size: 10px;
    }
    .notice-title {
      font-size: 14px;
    }
    .main-title {
      width: 95%;
    }

    .vs-table--header {
      display: flex;
      flex-wrap: wrap;
      margin-left: 1.5rem;
      margin-right: 1.5rem;
      > span {
        display: flex;
        flex-grow: 1;
      }

      .vs-table--search{
        padding-top: 0;

        .vs-table--search-input {
          padding: 0.9rem 2.5rem;
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
      border-spacing: 0 1.3rem;
      padding: 0 1rem;

      tr{
          box-shadow: 0 4px 20px 0 rgba(0,0,0,.05);
          td{
            padding: 20px;
            &:first-child{
              border-top-left-radius: .5rem;
              border-bottom-left-radius: .5rem;
            }
            &:last-child{
              border-top-right-radius: .5rem;
              border-bottom-right-radius: .5rem;
            }
          }
          td.td-check{
            padding: 20px !important;
          }
      }
    }

    .vs-table--thead{
      th {
        padding-top: 0;
        padding-bottom: 0;

        .vs-table-text{
          text-transform: uppercase;
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
      justify-content: center;
    }
  }
}
</style>