<template>
  <div class="main">
    <!-- 导航 -->
    <div id="Header_head">
      <div>
        <div>
          <div class="SearchForm_form_style_other" id="SearchForm_form">
            <div id="Header_logo">
              <a href="/">
                <img src="../assets/logo.png">
              </a>
            </div>
            <span id="SearchForm_search_input_wapper">
              <input type="text" name="wd" placeholder="啥都可以搜..." v-model="wd"/>
            </span>
            <span id="SearchForm_submit_btn_wrapper">
              <button id="SearchForm_submit_btn">搜索</button>
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
          <div class="Search_result_title" >
            <span>{{item.title}}</span>
          </div>
          <div class="Search_result_information">
            <div class="Information_detail_information" >
              <p class="Information_detail_information_Item">
                <span>文件名:</span>
                <span>{{item.fileName}}</span>
              </p>
              <p class="Information_detail_information_Item" >
                <span>文件数目：</span>
                <span>{{item.fileCount}}</span>
              </p>
              <p class="Information_detail_information_Item" >
                <span>文件大小：</span>
                <span>{{item.size}}</span>
              </p>
              <p class="Information_detail_information_Item" >
                <span>收录时间：</span>
                <span>{{ item.time }}</span>
              </p>
            </div>
          </div>
        </div>
        <!-- 底部翻页-->
        <div id="Page_page_container">
          <div id="Page_page_list">
            <span id="Page_up_page" class="hidden">上一页</span>
            <strong class="Page_current_page">1</strong>
            <span>2</span><span>3</span><span>4</span><span>5</span><span>6</span><span>7</span><span>8</span><span>9</span><span>10</span>
            <span id="Page_next_page" class="">下一页</span>
          </div>
        </div>
      </div>
      <div class="Search_content_right">
        <p>111</p>
        <p>111</p>
        <p>111</p>
        <p>111</p>
        <p>111</p>
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
import ajax from 'axios'
export default {
  name: 'ListSearch',
  data () {
    return {
      wd: '',
      searchResult: [
        {
          id: 1,
          title: 'test1',
          size: '12.4G',
          fileName: 'testFile.mp4',
          fileCount: 25,
          time: '2017-09-06'
        },
        {
          id: 2,
          title: 'test2',
          size: '20.7G',
          fileName: 'test.rmvb',
          fileCount: 3,
          time: '2017-09-06'
        },
        {
          id: 3,
          title: 'test3',
          size: '20.7G',
          fileName: 'test.rmvb',
          fileCount: 3,
          time: '2017-09-06'
        },
        {
          id: 4,
          title: 'test4',
          size: '20.7G',
          fileName: 'test.rmvb',
          fileCount: 3,
          time: '2017-09-06'
        },
        {
          id: 5,
          title: 'test5',
          size: '20.7G',
          fileName: 'test.rmvb',
          fileCount: 3,
          time: '2017-09-06'
        },
        {
          id: 6,
          title: 'test6',
          size: '20.7G',
          fileName: 'test.rmvb',
          fileCount: 3,
          time: '2017-09-06'
        },
        {
          id: 7,
          title: 'test7',
          size: '20.7G',
          fileName: 'test.rmvb',
          fileCount: 3,
          time: '2017-09-06'
        },
        {
          id: 8,
          title: 'test8',
          size: '20.7G',
          fileName: 'test.rmvb',
          fileCount: 3,
          time: '2017-09-06'
        }
      ]
    }
  },
  mounted: function () {
    // 进行赋值
    this.wd = this.$route.params.wd
    // 网络请求
    ajax.get('/index.html')
      .then(function (data) {
        console.log(data)
      })
      .catch(function (error) {
        console.log(error)
      })
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

  #Bottom_bottom_wrapper {
    margin: 0 auto;
    text-align: center;
    font-size: 13px;
    width: 100%;
    position: relative;
    z-index: 10;
  }

  #Page_page_container #Page_page_list span#Page_next_page, #Page_page_container #Page_page_list span#Page_up_page {
    width: auto;
    padding: 0 18px;
  }

  #Page_page_container #Page_page_list span, #Page_page_container #Page_page_list strong {
    font: 14px arial;
    display: inline-block;
    text-align: center;
    height: 34px;
    line-height: 34px;
    width: 34px;
    border: 1px solid #e1e2e3;
    margin-right: 9px;
    color: #5a5a58;
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
    padding-bottom: 20px;
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
</style>
