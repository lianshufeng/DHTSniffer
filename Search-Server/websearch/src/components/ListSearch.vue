<template>

  <div class="main">
    <!-- 导航 -->
    <div id="Header_head">
      <div>
        <div>
          <div class="SearchForm_form_style_other" id="SearchForm_form">
            <div id="Header_logo">
              <a href="#/">
                <img src="../assets/logo.png">
              </a>
            </div>
            <span id="SearchForm_search_input_wapper">
              <input type="text" name="wd" placeholder="啥都可以搜..." v-model="wd" @keydown="enterKeyDown($event)" />
            </span>
            <span id="SearchForm_submit_btn_wrapper">
              <button id="SearchForm_submit_btn" v-on:click="search()" >搜索</button>
            </span>
          </div>
        </div>
      </div>
    </div>
    <!-- 内容页面 -->
    <div class="Search_container">
      <div class="Search_content_left">

        <div class="Search_result" v-for="item in searchResult" :key="item.id" >
          <!--  标题 -->
          <div class="Search_result_title"  v-on:click="openPage(item.id)" >
            <span>{{item.title}}</span>
          </div>

          <div class="Search_result_information">
            <div class="Information_detail_information" >
              <p class="Information_detail_information_Item" >
                <span class="Page_Item_title" >文档类型：</span>
                <span>{{ item.type }}</span>
                <template v-for="type in item.types">
                  <span :key="type">
                    {{type}}
                  </span>
                </template>
              </p>
              <p class="Information_detail_information_Item" >
                <span class="Page_Item_title">文件数量：</span>
                <span>{{item.fileCount}}</span>
              </p>
              <p class="Information_detail_information_Item" >
                <span class="Page_Item_title">文件大小：</span>
                <span>{{item.size}}</span>
              </p>
              <p class="Information_detail_information_Item" >
                <span class="Page_Item_title" >收录时间：</span>
                <span>{{ item.time }}</span>
              </p>
              <p style="text-align:center;cursor: pointer;">
                <span v-on:click="openPage(item.id)" >浏览详情</span>
              </p>
              </div>
          </div>
        </div>

        <!-- 底部翻页-->
        <div id="Page_page_container">
          <div id="Page_page_list">
            <!-- 上一页渲染 -->
            <template>
              <span class="Page_up_page" v-show="page.current > 1"  v-on:click="skipPage(page.current-1)" >上一页</span>
            </template>
            <!-- 页码的渲染 -->
            <template v-for="n in page.total"  >
              <span v-if="n == page.current" :key="n" class="Page_current_page"  >
                {{n}}
              </span>
              <span v-else :key="n" v-on:click="skipPage(n)">
                {{n}}
              </span>
            </template>
            <!-- 下一页的渲染 -->
            <template>
              <span class="Page_next_page" v-show="page.current < page.total" v-on:click="skipPage(page.current+1)">下一页</span>
            </template>
          </div>
        </div>
      </div>
      <div class="Search_content_right">
      </div>
      <div class="clearfix" ></div>
    </div>
    <!-- 底部信息-->
    <div id="Bottom_bottom_wrapper" style="color:#999;"  >
      <div   >
        <p>
          ©2018 金牌找你妹
        </p>
      </div>
    </div>
  </div>
</template>

<script>
// 引用ajax
// import ajax from 'axios'

import Vue from 'vue'
import {Tabs, Tab} from 'vue-tabs-component'
Vue.component('tabs', Tabs)
Vue.component('tab', Tab)

export default {
  name: 'ListSearch',
  methods: {
    search: function () {
      this.requestSearch(this.wd, 1)
    },
    enterKeyDown (ev) {
      if (ev.keyCode === 13) {
        this.search()
      }
    },
    openPage: function (pageId) {
      let path = '/info/' + pageId + '.html'
      window.open(path)
    },
    skipPage: function (newPage) {
      this.requestSearch(this.wd, newPage)
    },
    requestSearch: function (keyWord, pageNum) {
      console.log('keyWord : ' + keyWord + ' page : ' + pageNum)
      // 需要请求成功后更改
      this.page.current = pageNum
      // 回到顶部
      document.body.scrollTop = document.documentElement.scrollTop = 0
    }
  },
  data () {
    return {
      wd: '',
      page: {
        total: 5,
        current: 1
      },
      searchResult: [
        {
          id: 1,
          types: ['压缩', '视频'],
          title: 'test1',
          size: '12.4G',
          fileCount: 25,
          time: '2017-09-06'
        },
        {
          id: 2,
          types: ['文档', '应用'],
          title: 'test1',
          size: '12.4G',
          fileCount: 25,
          time: '2017-09-06'
        }
      ]
    }
  },
  mounted: function () {
    // 进行赋值
    this.wd = this.$route.params.wd
    // 首次载入进行搜索
    this.requestSearch(this.wd, 1)
  }
}
</script>

<style scoped>
  .main {
    margin: 0px
  }
  .clearfix{
    height: 0;
    clear: both;
  }
  .Page_Item_title{
    float: left;
    width: 80px;
  }
  #Bottom_bottom_wrapper {
    margin: 0 auto;
    text-align: center;
    font-size: 13px;
    width: 100%;
    position: relative;
    z-index: 10;
  }

  #Page_page_container #Page_page_list span.Page_next_page, #Page_page_container #Page_page_list span.Page_up_page {
    width: auto;
    padding: 0 6px;
  }

  #Page_page_container #Page_page_list span, #Page_page_container #Page_page_list strong {
    font: 14px arial;
    display: inline-block;
    text-align: center;
    height: 34px;
    line-height: 34px;
    width: 34px;
    border: 1px solid #e1e2e3;
    margin-right: 4px;
    color: #5a5a58;
    cursor: pointer;
  }

  #Page_page_container {
    margin-top: 60px;
    font-family: arial,Microsoft YaHei,\\5FAE\8F6F\96C5\9ED1,Hiragino Sans GB,tahoma,simsun,\\5B8B\4F53;
  }
  .Search_container {
    padding-top: 20px;
    padding-bottom: 120px;
    position: relative;
    min-height: 350px;
  }
  .Information_detail_information_Item{
    margin:0 auto;
  }
  .Search_result {
    width: 100%;
    border: 1px solid #efeeee;
    margin-top: 30px;
  }
  .Search_content_right {
    width: 35%;
    float: right;
    vertical-align: top;
    border-left: 1px solid #e1e1e1;
    padding-left: 30px;
    /*padding-bottom: 20px;*/
  }
  .Search_content_left {
    width: 45%;
    padding-left: 100px;
    float: left;
  }
  .Search_result_title {
    min-height: 25px;
    background-color: #fafafa;
    padding: 10px 15px;
    color: -webkit-link;
    cursor: pointer;
    word-wrap: break-word;
    display: block;
  }
  #SearchForm_form #SearchForm_submit_btn_wrapper #SearchForm_submit_btn {
    background-color: #f8d305;
    text-align: center;
    font-size: 17px;
    outline: none;
    cursor: pointer;
    border: 1px solid #eac704;
  }
  .SearchForm_form_style_other #SearchForm_submit_btn_wrapper, .SearchForm_form_style_other #SearchForm_submit_btn_wrapper #SearchForm_submit_btn {
    width: 80px;
    height: 48px;
  }
  #Header_head #Header_logo {
    float: left;
  }
  #Header_logo img {
    width: 48px;
    height: 48px;
    border: none;
    text-decoration: none;
    padding: 0px 10px 0px 0px;
  }
  .SearchForm_form_style_other {
    vertical-align: top;
    display: inline-block;
    padding-left: 20px;
  }
  #SearchForm_form #SearchForm_search_input_wapper {
    border: 1px solid #d0d0d0;
    display: inline-block;
    vertical-align: top;
  }
  .SearchForm_form_style_other #SearchForm_search_input_wapper {
    width: 400px;
    height: 46px;
    position: relative;
  }
  #SearchForm_form #SearchForm_search_input_wapper input {
    outline: none;
    border: none;
    font-size: 16px;
    color: #333;
    display: inline-block;
  }
  .SearchForm_form_style_other #SearchForm_search_input_wapper input {
    width: 390px;
    height: 30px;
    margin: 9px 0 0 8px;
    background-color: #fafafa;
  }
  input {
    font-family: Microsoft YaHei, Helvetica Neue Light, Lucida Grande, Calibri, Arial, sans-serif;
  }
  .SearchForm_suggestion_wrapper {
    width: 402px;
    top: 48px;
    left: -1px;
  }
  .SearchForm_suggestion_base {
    position: absolute;
    z-index: 1000;
  }
  #Header_head {
    background-color: #fafafa;
    padding: 20px 10px;
    border-bottom: 1px solid #efeeee;
    position: relative;
  }
  .Information_detail_information{
    line-height: 24px;
    font-size: 13px;
    padding: 10px 15px;
    color: #333;
  }
  .Page_current_page{
    color: #333 !important;
    font-weight: 700!important;
    height: 36px!important;
    width: 36px!important;
    border: none!important;
    cursor: default!important;
    }
  .hidden {
    display: none!important;
  }

</style>

<!--tab-->
<!--<style>-->
  <!--.tabs-component {-->
    <!--margin: 4em 0;-->
  <!--}-->
  <!--.tabs-component-tabs {-->
    <!--border: solid 1px #ddd;-->
    <!--border-radius: 6px;-->
    <!--margin-bottom: 5px;-->
  <!--}-->
  <!--@media (min-width: 700px) {-->
    <!--.tabs-component-tabs {-->
      <!--border: 0;-->
      <!--align-items: stretch;-->
      <!--display: flex;-->
      <!--justify-content: flex-start;-->
      <!--margin-bottom: -1px;-->
    <!--}-->
  <!--}-->
  <!--.tabs-component-tab {-->
    <!--color: #999;-->
    <!--font-size: 14px;-->
    <!--font-weight: 600;-->
    <!--margin-right: 0;-->
    <!--list-style: none;-->
  <!--}-->
  <!--.tabs-component-tab:not(:last-child) {-->
    <!--border-bottom: dotted 1px #ddd;-->
  <!--}-->
  <!--.tabs-component-tab:hover {-->
    <!--color: #666;-->
  <!--}-->
  <!--.tabs-component-tab.is-active {-->
    <!--color: #000;-->
  <!--}-->
  <!--.tabs-component-tab.is-disabled * {-->
    <!--color: #cdcdcd;-->
    <!--cursor: not-allowed !important;-->
  <!--}-->
  <!--@media (min-width: 700px) {-->
    <!--.tabs-component-tab {-->
      <!--background-color: #fff;-->
      <!--border: solid 1px #ddd;-->
      <!--border-radius: 3px 3px 0 0;-->
      <!--margin-right: .5em;-->
      <!--transform: translateY(2px);-->
      <!--transition: transform .3s ease;-->
    <!--}-->
    <!--.tabs-component-tab.is-active {-->
      <!--border-bottom: solid 1px #fff;-->
      <!--z-index: 2;-->
      <!--transform: translateY(0);-->
    <!--}-->
  <!--}-->
  <!--.tabs-component-tab-a {-->
    <!--align-items: center;-->
    <!--color: inherit;-->
    <!--display: flex;-->
    <!--padding: .75em 1em;-->
    <!--text-decoration: none;-->
  <!--}-->
  <!--.tabs-component-panels {-->
    <!--padding: 4em 0;-->
  <!--}-->
  <!--@media (min-width: 700px) {-->
    <!--.tabs-component-panels {-->
      <!--border-top-left-radius: 0;-->
      <!--background-color: #fff;-->
      <!--border: solid 1px #ddd;-->
      <!--border-radius: 0 6px 6px 6px;-->
      <!--box-shadow: 0 0 10px rgba(0, 0, 0, .05);-->
      <!--padding: 4em 2em;-->
    <!--}-->
  <!--}-->
<!--</style>-->
