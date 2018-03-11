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
    <div class="List_Content">
      <h1>
        <span>11</span>
      </h1>
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
      wd: ''
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
    /*height: 42px;*/
    border-bottom: 1px solid #efeeee;
    position: relative;
  }

  .List_Content {

  }
</style>
