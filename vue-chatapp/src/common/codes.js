export default {
    findConsultType (code) {
        if (!code) return {label:'N/A', value:'',color:'dark'}
        return this.consultTypeTags.find(e => e.value === code.toUpperCase() )
    },
    findConsultStatus (code) {
        if (!code) return {label:'N/A', value:'',color:'dark'}
        return this.consultStatusTags.find(e => e.value === code.toUpperCase() )
    },
    consultTypeTags: [
        {label:'Questions', value:'B', color:'primary'},
        {label:'Complaints', value:'C',color:'success'},
        {label:'AS Request', value:'A',color:'warning'},
        {label:'N/A', value:'',color:'dark'},
      ],
    consultStatusTags: [
        {label:'Open', value:'O',color:'primary'},
        {label:'Close', value:'C',color:'success'},
        {label:'Reject', value:'R',color:'warning'},
        {label:'AssignTo', value:'P',color:'danger'},
        {label:'N/A', value:'',color:'dark'},
      ],
}