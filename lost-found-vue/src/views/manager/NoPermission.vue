<template>
    <div id="sec-1" class="section">
      <div id="ctn">
        <div class="marquee">
          <div class="marquee-text"></div><div class="marquee-text"></div><div class="marquee-text"></div>
        </div>

        <div class="text-ctn">ERROR</div>

        <div id="forbidden">FORBIDDEN</div>

        <div class="text-ctn">HTTP<br>403</div>

        <div class="marquee">
          <div class="marquee-text"></div><div class="marquee-text"></div><div class="marquee-text"></div>
        </div>
      </div>
        <el-button style="margin-left: 20px" type="danger" @click="goHome">返回首页</el-button>
    </div>
</template>

<script>
export default {
  name: 'noPermission',
  methods:{
    goHome(){
      this.$router.push('/')
    }
  }
}
</script>

<style scoped>
body {
  margin: 0;
  font-family: Helvetica;
}

.section {
  /*width: 100vw;*/
  height: 85vh;
}

.text-ctn {
  margin: 18px;
}

.marquee {
  width: 360px;
  border-bottom: 3px solid #000000;
  background-color: #000000;
  white-space: nowrap;
}

.marquee-text {
  display: inline-block;
  width: 260px;
  font-size: 24px;
  font-weight: bold;
  color: #ff0000;
  animation: scroll 3s linear infinite;
}

.marquee-text:before {
  content: "ACCESS DENIED";
}

@keyframes scroll {
  100% {
    transform: translateX(-100%);
  }
}

#sec-1 {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #e80d0d;
}

#ctn {
  margin-top: 40px;
  width: 360px;
  height: 360px;
  font-size: 30px;
  font-weight: bold;
  text-align: center;
  overflow: hidden;
}

#forbidden {
  display: inline-block;
  padding: 5px 10px 5px 10px;
  border: 2px solid #000000;
  border-radius: 8px;
  font-size: 50px;
  animation: flash 1s linear infinite;
}

@keyframes flash {
  50% {
    opacity: 0;
  }
}

</style>
