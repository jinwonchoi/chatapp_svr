<!-- =========================================================================================
  File Name: UserView.vue
  Description: User View page
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->

<template>
  <div id="page-user-view">

    <vs-alert color="danger" title="User Not Found" :active.sync="user_not_found">
      <span>User record with id: {{ $route.params.userId }} not found. </span>
      <span>
        <span>Check </span><router-link :to="{name:'page-user-list'}" class="text-inherit underline">All Users</router-link>
      </span>
    </vs-alert>

    <div id="user-data" v-if="user_data">

      <vx-card title="Account" class="mb-base">

        <!-- Avatar -->
        <div class="vx-row">

          <!-- Avatar Col -->
          <div class="vx-col" id="avatar-col">
            <div class="img-container mb-4">
              <img :src="userImg(user_data)" class="rounded w-full" />
            </div>
          </div>

          <!-- Information - Col 1 -->
          <div class="vx-col flex-1" id="account-info-col-1">
            <table>
              <tr>
                <td class="font-semibold">Username</td>
                <td>{{ user_data.userName }} ({{ user_data.agentId }})</td>
              </tr>
              <tr>
                <td class="font-semibold">Name</td>
                <td>{{ user_data.firstName }} {{ user_data.lastName }}</td>
              </tr>
              <tr>
                <td class="font-semibold">Email</td>
                <td>{{ user_data.userEmail }}</td>
              </tr>
            </table>
          </div>
          <!-- /Information - Col 1 -->


          <!-- Information - Col 2 -->
          
          <div class="vx-col flex-1" id="account-info-col-2">
            <table>
              <tr>
                <td class="font-semibold">confirmed</td>
                <td>{{ confirmed }}</td>
              </tr>
              <tr>
                <td class="font-semibold">Role</td>
                <td>{{ role }}</td>
              </tr>
              <tr>
                <td class="font-semibold">Org</td>
                <td>{{ user_data.groupId }} / {{ user_data.bizId }}</td>
              </tr>
            </table>
          </div>
          <!-- /Information - Col 2 -->
          <div class="vx-col w-full flex" id="account-manage-buttons">
            <vs-button icon-pack="feather" icon="icon-edit" class="mr-4" :to="{name: 'app-user-edit', params: { userId: $route.params.userId }}">Edit</vs-button>
            <vs-button type="border" color="danger" icon-pack="feather" icon="icon-trash" @click="confirmDeleteRecord">Delete</vs-button>
          </div>

        </div>

      </vx-card>


      <div class="vx-row">
        <div class="vx-col w-full">
          <vx-card title="Information" class="mb-base">
            <table>
              <tr>
                <td class="font-semibold">Country</td>
                <td>{{ user_data.country }}</td>
              </tr>
              <tr>
                <td class="font-semibold">Language</td>
                <td>{{ user_data.lang }}</td>
              </tr>
              <tr>
                <td class="font-semibold">Office Phone</td>
                <td>{{ user_data.officePhone }}</td>
              </tr>
              <tr>
                <td class="font-semibold">Cell Phone</td>
                <td>{{ user_data.cellPhone }}</td>
              </tr>
              <tr>
                <td class="font-semibold">Address</td>
                <td>{{ user_data.address }}</td>
              </tr>
            </table>
          </vx-card>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import moduleUserManagement from '@/store/user-management/moduleUserManagement.js'

export default {
  data () {
    return {
      user_data: null,
      user_not_found: false
    }
  },
  computed: {
    // userAddress () {
    //   let str = ''
    //   for (const field in this.user_data.location) {
    //     str += `${field  } `
    //   }
    //   return str
    // }
    confirmed() {
      return (this.user_data.confirmedYn==='Y')? "Yes":"No"
    },
    role() {
      if (this.user_data.role==='A') {
        return "Admin"
      } else if (this.user_data.role==='M') {
        return "Manager"
      } else if (this.user_data.role==='O') {
        return "Agent"
      } 
    },
    userImg() {
      return (userInfo)=>this.$utils.userImg(userInfo)
    }
  },
  methods: {
    confirmDeleteRecord () {
      this.$vs.dialog({
        type: 'confirm',
        color: 'danger',
        title: 'Confirm Delete',
        text: `You are about to delete "${this.user_data.username}"`,
        accept: this.deleteRecord,
        acceptText: 'Delete'
      })
    },
    deleteRecord () {
      /* Below two lines are just for demo purpose */
      // this.$router.push({name:'app-user-list'})
      // this.showDeleteSuccess()

      /* UnComment below lines for enabling true flow if deleting user */
      this.$store.dispatch("userManagement/deleteUser", this.user_data.id)
        .then(()   => { this.$router.push({name:'app-user-list'}); this.showDeleteSuccess() })
        .catch(err => { console.error(err)       })
    },
    showDeleteSuccess () {
      this.$vs.notify({
        color: 'success',
        title: 'User Deleted',
        text: 'The selected user was successfully deleted'
      })
    }
  },
  created () {
    // Register Module UserManagement Module
    if (!moduleUserManagement.isRegistered) {
      this.$store.registerModule('userManagement', moduleUserManagement)
      moduleUserManagement.isRegistered = true
    }

    const userId = this.$route.params.userId
    this.$store.dispatch('userManagement/fetchUser', userId)
      .then(res => { this.user_data = res.data.item })
      .catch(err => {
        if (err.response.status === 404) {
          this.user_not_found = true
          return
        }
        console.error(err) 
      })
  }
}

</script>

<style lang="scss">
#avatar-col {
  width: 10rem;
}

#page-user-view {
  table {
    td {
      vertical-align: top;
      min-width: 140px;
      padding-bottom: .8rem;
      word-break: break-all;
    }

    &:not(.permissions-table) {
      td {
        @media screen and (max-width:370px) {
          display: block;
        }
      }
    }
  }
}

// #account-info-col-1 {
//   // flex-grow: 1;
//   width: 30rem !important;
//   @media screen and (min-width:1200px) {
//     & {
//       flex-grow: unset !important;
//     }
//   }
// }


@media screen and (min-width:1201px) and (max-width:1211px),
only screen and (min-width:636px) and (max-width:991px) {
  #account-info-col-1 {
    width: calc(100% - 12rem) !important;
  }

  // #account-manage-buttons {
  //   width: 12rem !important;
  //   flex-direction: column;

  //   > button {
  //     margin-right: 0 !important;
  //     margin-bottom: 1rem;
  //   }
  // }

}

</style>
