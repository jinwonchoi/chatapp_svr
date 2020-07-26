import Vue from 'vue'

Vue.filter('capitalize', function (value) {
  if (!value) return ''
  value = value.toString()
  const arr = value.split(' ')
  const capitalized_array = []
  arr.forEach((word) => {
    const capitalized = word.charAt(0).toUpperCase() + word.slice(1)
    capitalized_array.push(capitalized)
  })
  return capitalized_array.join(' ')
})

Vue.filter('title', function (value, replacer = '_') {
  if (!value) return ''
  value = value.toString()

  const arr = value.split(replacer)
  const capitalized_array = []
  arr.forEach((word) => {
    const capitalized = word.charAt(0).toUpperCase() + word.slice(1)
    capitalized_array.push(capitalized)
  })
  return capitalized_array.join(' ')
})

Vue.filter('truncate', function (value, limit) {
  return value.substring(0, limit)
})

Vue.filter('tailing', function (value, tail) {
  return value + tail
})

Vue.filter('time', function (value, is24HrFormat = false) {
  if (value) {
    const date = new Date(Date.parse(value))
    let hours = date.getHours()
    const min = (date.getMinutes() < 10 ? '0' : '') + date.getMinutes()
    if (!is24HrFormat) {
      const time = hours > 12 ? 'AM' : 'PM'
      hours = hours % 12 || 12
      return `${hours}:${min} ${time}`
    }
    return `${hours}:${min}`
  }
})

Vue.filter('date', function (value, fullDate = false) {
  value = String(value)
  const date = value.slice(8, 10).trim()
  const month = value.slice(4, 7).trim()
  const year = value.slice(11, 15)

  if (!fullDate) return `${date} ${month}`
  else return `${date} ${month} ${year}`
})

Vue.filter('fulltime', function (value) {
  return new Date(value).toLocaleString()
})

Vue.filter('gapFromNow', function (value) {
  const gap = Math.round((new Date() - new Date(value))/1000)
  let sec = gap % 60
  let min =  (gap-sec) / 60
  let hr = Math.round(min/60)
  min = (gap-sec) % 60
  let days = Math.round(hr / 24)
  hr = hr % 24
  if (days > 0) {
      let str = (days>0)?`${days}d` :""
      str += (hr>0)?` ${hr}h`:""
      return (str+" ago")
      // str += (days===0 && min>0)?` ${min}m`:""
      // str += (str==="")?` ${sec}s`:""
      // return (str+" ago")
  } else {
    return new Date(value).toLocaleTimeString()
  }
})

Vue.filter('month', function (val, showYear = true) {
  val = String(val)

  const regx = /\w+\s(\w+)\s\d+\s(\d+)./
  if (!showYear) {
    return regx.exec(val)[1]
  } else {
    return `${regx.exec(val)[1]} ${regx.exec(val)[2]}`
  }

})

Vue.filter('csv', function (value) {
  return value.join(', ')
})

Vue.filter('filter_tags', function (value) {
  if (!value) return ''
  return value.replace(/<\/?[^>]+(>|$)/g, '')
})

Vue.filter('k_formatter', function (num) {
  return num > 999 ? `${(num / 1000).toFixed(1)}k` : num
})
