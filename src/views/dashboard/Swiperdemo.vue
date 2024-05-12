<template>
  <div class="Swiper-demo">
    <div class="swiper-container">
      <div class="swiper-wrapper">
        <div class="swiper-slide"><img height="300px" src="https://gss0.baidu.com/9fo3dSag_xI4khGko9WTAnF6hhy/zhidao/wh%3D600%2C800/sign=738bb875b13533faf5e39b2898e3d127/96dda144ad345982d8e4d4e90cf431adcbef845a.jpg"></div>
        <div class="swiper-slide"><img height="300px" src="../../assets/404_images/404.png"></div>
        <div class="swiper-slide"><img height="300px" src="../../assets/404_images/404.png"></div>
      </div>
      <!-- 如果需要分页器 -->
      <!-- <div class="swiper-pagination" /> -->
      <!-- 如果需要导航按钮 -->
      <div class="swiper-button-prev" />
      <div class="swiper-button-next" />
      <!-- 如果需要滚动条 -->
      <div class="swiper-scrollbar" />
    </div>
  </div>
</template>
<script>
import Swiper from 'swiper'
import 'swiper/swiper.min.css'
export default {
  mounted() {
    new Swiper('.swiper-container', {
      // direction: 'vertical', // 垂直切换选项
      loop: true, // 循环模式选项
      // 如果需要分页器
      pagination: {
        el: '.swiper-pagination'
      },

      // 如果需要前进后退按钮
      navigation: {
        nextEl: '.swiper-button-next',
        prevEl: '.swiper-button-prev'
      },
      // 如果需要滚动条
      scrollbar: {
        el: '.swiper-scrollbar'
      }
    })
  }
}
</script>
<style scoped>
.swiper-container {
    width: auto;
    height: 300px;
    margin: 0,auto;
}
</style>
