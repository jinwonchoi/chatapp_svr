(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-1a49e8b2"],{"25b1":function(t,e,s){},"4faa":function(t,e,s){"use strict";var a={users:[]},n=(s("20d6"),{ADD_USER:function(t,e){t.users.push(e)},SET_USERS:function(t,e){t.users=e},REMOVE_USER:function(t,e){var s=t.users.findIndex((function(t){return t.id===e}));t.users.splice(s,1)},UPDATE_USER:function(t,e){var s=t.users.findIndex((function(t){return t.id===e.id}));Object.assign(t.users[s],e)}}),r=s("74f4"),i={fetchUsers:function(t){var e=t.commit;return new Promise((function(t,s){r["a"].get("/user/list").then((function(s){e("SET_USERS",s.data.item),t(s)})).catch((function(t){s(t)}))}))},fetchUser:function(t,e){return new Promise((function(t,s){r["a"].get("/user/".concat(e)).then((function(e){t(e)})).catch((function(t){s(t)}))}))},deleteUser:function(t,e){var s=t.commit;return new Promise((function(t,a){r["a"].delete("/user/".concat(e)).then((function(a){s("REMOVE_USER",e),t(a)})).catch((function(t){a(t)}))}))},addUser:function(t,e){var s=t.commit;return new Promise((function(t,a){r["a"].post("/user/add",{item:e}).then((function(a){s("ADD_USER",Object.assign(e,{id:a.data.id})),t(a)})).catch((function(t){a(t)}))}))},updateUser:function(t,e){var s=t.commit;return new Promise((function(t,a){r["a"].post("/user/update",{item:e}).then((function(a){s("UPDATE_USER",e),t(a)})).catch((function(t){a(t)}))}))}},o={};e["a"]={isRegistered:!1,namespaced:!0,state:a,mutations:n,actions:i,getters:o}},"7fab":function(t,e,s){"use strict";s.r(e);var a=function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",{attrs:{id:"page-user-view"}},[s("vs-alert",{attrs:{color:"danger",title:"User Not Found",active:t.user_not_found},on:{"update:active":function(e){t.user_not_found=e}}},[s("span",[t._v("User record with id: "+t._s(t.$route.params.userId)+" not found. ")]),s("span",[s("span",[t._v("Check ")]),s("router-link",{staticClass:"text-inherit underline",attrs:{to:{name:"page-user-list"}}},[t._v("All Users")])],1)]),t.user_data?s("div",{attrs:{id:"user-data"}},[s("vx-card",{staticClass:"mb-base",attrs:{title:"Account"}},[s("div",{staticClass:"vx-row"},[s("div",{staticClass:"vx-col",attrs:{id:"avatar-col"}},[s("div",{staticClass:"img-container mb-4"},[s("img",{staticClass:"rounded w-full",attrs:{src:t.user_data.profileUrl}})])]),s("div",{staticClass:"vx-col flex-1",attrs:{id:"account-info-col-1"}},[s("table",[s("tr",[s("td",{staticClass:"font-semibold"},[t._v("Username")]),s("td",[t._v(t._s(t.user_data.userName)+" ("+t._s(t.user_data.agentId)+")")])]),s("tr",[s("td",{staticClass:"font-semibold"},[t._v("Name")]),s("td",[t._v(t._s(t.user_data.firstName)+" "+t._s(t.user_data.lastName))])]),s("tr",[s("td",{staticClass:"font-semibold"},[t._v("Email")]),s("td",[t._v(t._s(t.user_data.userEmail))])])])]),s("div",{staticClass:"vx-col flex-1",attrs:{id:"account-info-col-2"}},[s("table",[s("tr",[s("td",{staticClass:"font-semibold"},[t._v("confirmed")]),s("td",[t._v(t._s(t.confirmed))])]),s("tr",[s("td",{staticClass:"font-semibold"},[t._v("Role")]),s("td",[t._v(t._s(t.role))])]),s("tr",[s("td",{staticClass:"font-semibold"},[t._v("Org")]),s("td",[t._v(t._s(t.user_data.groupId)+" / "+t._s(t.user_data.bizId))])])])]),s("div",{staticClass:"vx-col w-full flex",attrs:{id:"account-manage-buttons"}},[s("vs-button",{staticClass:"mr-4",attrs:{"icon-pack":"feather",icon:"icon-edit",to:{name:"app-user-edit",params:{userId:t.$route.params.userId}}}},[t._v("Edit")]),s("vs-button",{attrs:{type:"border",color:"danger","icon-pack":"feather",icon:"icon-trash"},on:{click:t.confirmDeleteRecord}},[t._v("Delete")])],1)])]),s("div",{staticClass:"vx-row"},[s("div",{staticClass:"vx-col w-full"},[s("vx-card",{staticClass:"mb-base",attrs:{title:"Information"}},[s("table",[s("tr",[s("td",{staticClass:"font-semibold"},[t._v("Country")]),s("td",[t._v(t._s(t.user_data.country))])]),s("tr",[s("td",{staticClass:"font-semibold"},[t._v("Language")]),s("td",[t._v(t._s(t.user_data.lang))])]),s("tr",[s("td",{staticClass:"font-semibold"},[t._v("Office Phone")]),s("td",[t._v(t._s(t.user_data.officePhone))])]),s("tr",[s("td",{staticClass:"font-semibold"},[t._v("Cell Phone")]),s("td",[t._v(t._s(t.user_data.cellPhone))])]),s("tr",[s("td",{staticClass:"font-semibold"},[t._v("Address")]),s("td",[t._v(t._s(t.user_data.address))])])])])],1)])],1):t._e()],1)},n=[],r=s("4faa"),i={data:function(){return{user_data:null,user_not_found:!1}},computed:{confirmed:function(){return"Y"===this.user_data.confirmedYn?"Yes":"No"},role:function(){return"A"===this.user_data.role?"Admin":"M"===this.user_data.role?"Manager":"O"===this.user_data.role?"Agent":void 0}},methods:{confirmDeleteRecord:function(){this.$vs.dialog({type:"confirm",color:"danger",title:"Confirm Delete",text:'You are about to delete "'.concat(this.user_data.username,'"'),accept:this.deleteRecord,acceptText:"Delete"})},deleteRecord:function(){var t=this;this.$store.dispatch("userManagement/deleteUser",this.user_data.id).then((function(){t.$router.push({name:"app-user-list"}),t.showDeleteSuccess()})).catch((function(t){console.error(t)}))},showDeleteSuccess:function(){this.$vs.notify({color:"success",title:"User Deleted",text:"The selected user was successfully deleted"})}},created:function(){var t=this;r["a"].isRegistered||(this.$store.registerModule("userManagement",r["a"]),r["a"].isRegistered=!0);var e=this.$route.params.userId;this.$store.dispatch("userManagement/fetchUser",e).then((function(e){t.user_data=e.data.item})).catch((function(e){404!==e.response.status?console.error(e):t.user_not_found=!0}))}},o=i,c=(s("d9cb"),s("2877")),d=Object(c["a"])(o,a,n,!1,null,null,null);e["default"]=d.exports},d9cb:function(t,e,s){"use strict";var a=s("25b1"),n=s.n(a);n.a}}]);
//# sourceMappingURL=chunk-1a49e8b2.630ae73d.js.map