<template>
  <div>
    <div style="margin: 10px;font-size: 24px">{{ news.title }}</div>
    <div style="margin: 10px 0">{{news.author}}   <span>{{news.time}}</span></div>
    <div style="box-shadow: 0 2px 4px rgba(0,0,0,.12)">
      <div v-html="news.content" ></div>
    </div>
  </div>

</template>

<script>
export default {
  name: "NewsDetail",
  data() {
    return {
      news: {}
    }
  },
  created() {
    let id = this.$route.query.id;
    if (!id) {
      return;
    }
    this.$request.get('/news/' + id).then(res => {
      this.news = res.data
    })
  },
}
</script>

<style scoped>

</style>
