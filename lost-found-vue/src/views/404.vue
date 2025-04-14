<template>
  <div class="bgImg" >
      <div style="margin-left: 49.5vw;padding-top: 20vh;">
        <router-link to="/">
          <el-button type="success" >返回首页</el-button>
        </router-link>
      </div>
  </div>
</template>

<script>
export default {
  name: "404"
}
</script>

<style scoped>
.bgImg{
  background: url("@/assets/imgs/404.png") no-repeat;
  background-size: 100% 100vh;
  height: 100vh;
}
</style>
