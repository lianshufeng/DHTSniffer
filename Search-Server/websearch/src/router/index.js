import Vue from 'vue'
import Router from 'vue-router'
import MainSearch from '@/components/MainSearch'
import ListSearch from '@/components/ListSearch'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'MainSearch',
      component: MainSearch
    },
    {
      path: '/s/:wd/:page',
      name: 'ListSearch',
      component: ListSearch
    },
    {
      path: '/s/:wd',
      name: 'ListSearch',
      component: ListSearch
    }
  ]
})
