<template>
  <div>
    <el-container>
      <!--侧边栏-->
      <el-aside :width="asideWidth" style="min-height: 100vh;background-color: #001529">
        <!--侧边栏logo-->
        <div style="height: 60px;color: white; display: flex; align-items: center;justify-content: center">
          <img src="@/assets/imgs/logo.png" style="width: 50px;height: 50px">
          <span class="logo-title" v-show="!isCollapse">失物招领后台</span>
        </div>
        <!--侧边栏菜单-->
        <!--default-active 选中后的菜单项变为激活状态-->
        <el-menu :default-openeds="['info']" :collapse="isCollapse" :collapse-transition="false" router background-color="#001529"
                 text-color="rgba(255,255,255,0.65)" active-text-color="#fff"
                 style="border: none" :default-active="$route.path">
          <!--一级菜单-->
          <el-menu-item index="/home">
            <!--i里面放着图标-->
            <i class="el-icon-s-home"></i>
            <span slot="title">系统首页</span>
          </el-menu-item>
          <!--二级菜单-->
          <el-submenu index="info">
            <template slot="title" >
              <i class="el-icon-menu"></i><span>系统管理</span>
            </template>
            <el-menu-item index="/user" v-if="loginUser.role ==='ADMIN'">用户信息</el-menu-item>
            <el-menu-item index="/news">新闻信息</el-menu-item>
            <el-menu-item index="/notice">系统公告</el-menu-item>
            <el-menu-item index="/logs">系统日志</el-menu-item>
            <el-menu-item index="/charts">数据统计</el-menu-item>
            <el-menu-item index="/orders">订单管理</el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>

      <el-container>
        <!--头部区域-->
        <el-header>
          <i :class="collapseIcon" style="font-size: 26px;" @click="handleCollapse"></i>
          <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-left: 20px">
            <el-breadcrumb-item :to="{ path: '/' }">主页</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: $route.path}">{{ this.$route.meta.title }}</el-breadcrumb-item>
          </el-breadcrumb>
          <div style="flex: 1;width: 0;display: flex;align-items: center;justify-content: flex-end">
            <i class="el-icon-quanping" style="font-size: 26px" @click="handleFulls"></i>
            <el-dropdown placement="bottom">
              <div style="display: flex;align-items: center;cursor: default">
                <img :src="loginUser.avatar||'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" style="margin-left: 10px;width: 35px;height: 35px;border-radius: 50%">
                <span style="margin-left: 10px">{{loginUser.name}}</span>
              </div>
              <!--头像下拉框-->
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="$router.push('/person')">个人信息</el-dropdown-item>
                <el-dropdown-item @click.native="$router.push('/password')">修改密码</el-dropdown-item>
                <el-dropdown-item @click.native="handleLogoOut">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </el-header>

        <!--  主体区域-->
        <el-main>
          <router-view @update:user="updateUser"/>
        </el-main>

      </el-container>

    </el-container>
  </div>
</template>

<script>
export default {
  name: 'Manager',
  data () {
    return {
      isCollapse: false, //不收缩
      asideWidth: '200px',
      collapseIcon: 'el-icon-s-fold',
      loginUser: JSON.parse(localStorage.getItem('user') || '{}'),
    }
  },
  mounted () {

  },
  methods: {
    //获取子组件的数据，更新父组件的数据
    updateUser(user){
      this.loginUser = JSON.parse(JSON.stringify(user));
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
