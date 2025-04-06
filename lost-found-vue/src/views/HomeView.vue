<template>
  <div>
    <el-container>

      <!--侧边栏-->
      <el-aside :width="asideWidth" style="min-height: 100vh;background-color: #001529">
        <!--侧边栏logo-->
        <div style="height: 60px;color: white; display: flex; align-items: center;justify-content: center">
          <img src="@/assets/logo.png" style="width: 50px;height: 50px">
          <span class="logo-title" v-show="!isCollapse">失物招领后台</span>
        </div>
        <!--侧边栏菜单-->
        <!--default-active 选中后的菜单项变为激活状态-->
        <el-menu :collapse="isCollapse" :collapse-transition="false" router background-color="#001529"
                 text-color="rgba(255,255,255,0.65)" active-text-color="#fff"
                 style="border: none" :default-active="$route.path">
          <!--一级菜单-->
          <el-menu-item index="/">
            <!--i里面放着图标-->
            <i class="el-icon-house"></i>
            <span slot="title">系统首页</span>
          </el-menu-item>
          <el-menu-item index="/1">
            <!--i里面放着图标-->
            <i class="el-icon-house"></i>
            <span slot="title">系统首页</span>
          </el-menu-item>
          <el-menu-item index="/2">
            <!--i里面放着图标-->
            <i class="el-icon-house"></i>
            <span slot="title">系统首页</span>
          </el-menu-item>
          <el-menu-item index="element">
            <i class="el-icon-house"></i>
            <span slot="title">Element页面</span>
          </el-menu-item>
          <!--二级菜单-->
          <el-submenu>
            <template slot="title">
              <i class="el-icon-menu"></i><span>系统管理</span>
            </template>
            <el-menu-item>用户信息</el-menu-item>
            <el-menu-item>管理员信息</el-menu-item>
            <el-menu-item index="/">系统首页</el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>

      <el-container>
        <!--头部区域-->
        <el-header>
          <i :class="collapseIcon" style="font-size: 26px;" @click="handleCollapse"></i>
          <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-left: 20px">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: '/user' }">用户管理</el-breadcrumb-item>
          </el-breadcrumb>
          <div style="flex: 1;width: 0;display: flex;align-items: center;justify-content: flex-end">
            <i class="el-icon-quanping" style="font-size: 26px" @click="handleFulls"></i>
            <el-dropdown placement="bottom">
              <div style="display: flex;align-items: center;cursor: default">
                <img src="@/assets/logo.png" style="width: 40px;height: 40px">
                <span>管理员</span>
              </div>
              <!--头像下拉框-->
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>个人信息</el-dropdown-item>
                <el-dropdown-item>修改密码</el-dropdown-item>
                <el-dropdown-item @click.native="handleLogoOut">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </el-header>

        <!--  主体区域-->
        <el-main>
          <div style="box-shadow: 0 0 10px rgba(0,0,0,.1);padding: 10px 20px;
          border-radius: 5px;margin-bottom: 10px">
            欢迎使用校园失物招领系统
          </div>
          <div style="display: flex">
            <el-card style="width: 30%">
              <div slot="header" class="clearfix"><span>系统公告</span></div>
              <div>
                2025毕设
                <div style="margin-top: 20px">
                  <div style="margin: 10px 0"><strong>主题色</strong></div>
                  <el-button type="primary">按钮</el-button>
                  <el-button type="success">按钮</el-button>
                  <el-button type="warning">按钮</el-button>
                  <el-button type="danger">按钮</el-button>
                  <el-button type="info">按钮</el-button>
                </div>
              </div>
            </el-card>
            <el-card style="width: 70%;margin-left: 10px">
              <div slot="header" class="clearfix">
                <span>渲染用户数据</span>
              </div>
              <div>
                <el-table :data="users" stripe style="width: 100%">
                  <el-table-column label="ID" prop="id"></el-table-column>
                  <el-table-column label="用户名" prop="username"></el-table-column>
                  <el-table-column label="姓名" prop="name"></el-table-column>
                  <el-table-column label="邮箱" prop="email"></el-table-column>
                  <el-table-column label="文件上传">
                    <template v-slot="scope">
                      <el-upload
                        class="upload-demo"
                        action="http://localhost:9090/file/upload"
                        :headers="{token:loginUser.token}"
                        :show-file-list="false"
                        :on-success="(row,res,file,fileList)=>handleTableUpload(scope.row,res,file,fileList)"
                      >
                        <el-button size="mini" type="primary">点击上传</el-button>
                      </el-upload>
                    </template>
                  </el-table-column>
                  <el-table-column label="头像预览">
                    <template v-slot="scope">
                      <el-image v-if="scope.row.avatar" :src="scope.row.avatar" style="width: 50px;height: 50px"></el-image>
                      <div>
                        <el-button size="mini" @click="preview(scope.row.avatar)">预览</el-button>
                      </div>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-card>
          </div>

          <!--  上传下载功能-->
          <div style="display: flex;margin: 10px 0">
            <el-card style="width: 50%;margin-right: 10px">
              <div slot="header" class="clearfix">
                <span>文件上传功能</span>
              </div>
              <div>
                <el-upload
                  class="upload-demo"
                  action="http://localhost:9090/file/upload"
                  :headers="{token:loginUser.token}"
                  :file-list="fileList"
                  :on-success="handleUploadSuccess"
                  list-type="picture"
                >
                  <el-button size="small" type="primary">单文件上传</el-button>
                </el-upload>
              </div>
              <div >
                <el-upload
                  action="http://localhost:9090/file/upload"
                  :headers="{token:loginUser.token}"
                  :on-success="handleMultipleFileUploadSuccess"
                  multiple
                >
                  <el-button style="margin: 10px 0" size="small" type="success">多文件上传</el-button>
                </el-upload>
                <el-button style="margin: 10px 0" type="success" @click="showUrls">显示上传的链接</el-button>
              </div>
            </el-card>
          </div>

        </el-main>

      </el-container>

    </el-container>
  </div>
</template>

<script>
export default {
  name: 'HomeView',
  data () {
    return {
      isCollapse: false, //不收缩
      asideWidth: '200px',
      collapseIcon: 'el-icon-s-fold',
      fileList:[],
      users: [],
      loginUser: JSON.parse(localStorage.getItem('user') || '{}'),
      url:'',
      urls:[]
    }
  },
  methods: {

    //预览图片
    preview(url){
      //默认图片是预览的
      window.open(url)
    },

    showUrls(){
      console.log(this.urls)
    },

    //多文件上传成功的callbackFn
    handleMultipleFileUploadSuccess(res, file, fileList){
       this.urls = fileList.map(v=>v.response?.data)
    },

    //要拿到当前表格行对象
    handleTableUpload(row,res, file, fileList){
      //将图片地址赋值给当前行对象
      row.avatar = res.response.data
      //触发更新
      this.$request.post('/user/saveOrUpdate',row).then(res=>{
        if (res.code === '200'){
          this.$message.success('头像上传成功')
        }else {
          this.$message.error(res.data.msg)
        }
      })
    },
    //:on-success="handleUploadSuccess" 上传成功后的钩子
    handleUploadSuccess (response, file, fileList) {
      this.fileList = fileList
    },
    handleCollapse () {
      this.isCollapse = !this.isCollapse
      this.asideWidth = this.isCollapse ? '64px' : '200px'
      this.collapseIcon = this.isCollapse ? 'el-icon-s-unfold' : 'el-icon-s-fold'
    },
    //全屏功能
    handleFulls () {
      if (document.fullscreenElement) {
        document.exitFullscreen()
      } else {
        document.documentElement.requestFullscreen()
      }
    },
    handleLogoOut () {
      localStorage.removeItem('user')
      this.$router.push('/login')
    }
  },
  mounted () {
    this.$request.get('/user').then(res => {
      if (res != null) {
        this.users = res.data
      }
    })
  }
}
</script>
<style>
/*要定位到二级菜单的样式 一级二级菜单都有这个.el-menu-item，二级子菜单在.el-menu--inline中*/
/*!important 覆盖默认样式*/
.el-menu--inline {
  background-color: #000c17 !important;
}

.el-menu--inline .el-menu-item {
  background-color: #000c17 !important;
  padding-left: 49px !important;
}

.el-menu-item:hover, .el-submenu__title:hover {
  color: #fff !important;
}

/*图标也联动高亮*/
.el-submenu__title:hover i {
  color: #fff !important;
}

/*当前激活的菜单项的样式，默认是蓝色*/
.el-menu-item.is-active {
  background-color: #1890ff !important;
  border-radius: 5px;
  width: calc(100% - 8px) !important;
  margin-left: 4px;
}

.el-menu-item.is-active i, .el-menu-item.is-active .el-tooltip {
  margin-left: -4px;
}

/*设置子菜单的高度*/
.el-menu-item, .el-submenu__title {
  height: 40px !important;
  line-height: 40px !important;
}

/*设置展开图标的高度位置*/
.el-submenu__icon-arrow {
  margin: -5px !important;
}

/*需要二级子菜单的最小宽度限制*/
.el-submenu .el-menu-item {
  min-width: 0 !important;
}

/*解决二级菜单高亮时对不齐*/
.el-menu--inline .el-menu-item.is-active {
  padding-left: 45px !important;
}

.el-aside {
  transition: width .3s;
  box-shadow: 2px 0 6px rgba(0, 21, 41, .35);
}

/*logo样式与动画*/
.logo-title {
  margin-left: -4px;
  font-size: 18px;
  transition: all .3s;
}

.el-header {
  box-shadow: 2px 0 6px rgba(0, 21, 41, .35);
  /*头部区域内容居中*/
  display: flex;
  align-items: center;
}
</style>
