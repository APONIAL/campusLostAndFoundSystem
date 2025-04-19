<template>
  <div class="feedback-container">
    <!-- 提交反馈表单 -->
    <div class="feedback-form">
      <h3 style="margin-bottom: 20px">请对本平台提出建议或者反馈</h3>
      <el-form :model="form" label-width="100px">
        <el-form-item label="反馈建议">
          <el-input
            type="textarea"
            v-model="form.content"
            placeholder="请输入您的建议或反馈"
            :rows="4"
          ></el-input>
        </el-form-item>
        <div style="display: flex">
          <el-form-item label="评分">
            <el-rate
              v-model="form.rate"
              :colors="colors"
              show-text
            ></el-rate>
          </el-form-item>
          <el-form-item style="justify-content: flex-start !important;">
            <el-button type="primary" @click="submitFeedback">Submit</el-button>
          </el-form-item>
        </div>
      </el-form>
    </div>

    <!-- 时间线展示所有反馈 -->
    <div class="feedback-timeline">
      <h3 style="margin-bottom: 20px">所有人的反馈</h3>
      <el-timeline>
        <el-timeline-item
          v-for="(item, index) in feedbackList"
          :key="index"
          :timestamp="item.createTime"
          placement="top"
        >
          <el-card>
            <div style="display: flex ">
              <img :src="item.avatarUrl" alt=""
              style="width: 35px;height: 35px;border-radius: 50%">
              <h4 style="margin-top: 10px;margin-left: 10px">用户: {{ item.userName }}</h4>
            </div>
            <p style="margin-top: 10px">{{ item.content }}</p>
            <div class="rating">
              <span>评分: </span>
              <el-rate
                v-model="item.rate"
                disabled
                :colors="colors"
                show-text
              ></el-rate>
            </div>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </div>
  </div>
</template>

<script>
export default {
  name: "Feedback",
  data() {
    return {
      // 表单数据
      form: {
        content: "",
        rate: null,
      },
      // 评分颜色
      colors: ["#99A9BF", "#F7BA2A", "#FF9900"],
      // 所有反馈列表
      feedbackList: [],
    };
  },
  mounted() {
    this.getData()
  },
  methods: {
    // 提交反馈
    submitFeedback() {
      this.$request.post("/feedback/save", this.form).then((res) => {
        if (res.code === "200") {
          this.$message.success("提交成功");
          // 清空表单
          this.form.content = "";
          this.form.rate = null;
        } else {
          this.$message.error(res.data.msg);
        }
      }).then(res => res);
    },
    getData() {
      this.$request.get('/feedback/getAllFeedback').then(res => {
        if (res.code === '200') {
          this.feedbackList = res.data
        } else {
          this.$message.error(res.data.msg)
        }
      }).catch(res => res)
    },
  },
};
</script>

<style scoped>
.feedback-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.feedback-form {
  margin-bottom: 40px;
}

.feedback-timeline {
  margin-top: 20px;
}

.rating {
  margin-top: 10px;
}

</style>
