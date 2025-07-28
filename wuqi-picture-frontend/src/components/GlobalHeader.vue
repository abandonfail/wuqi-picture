<template>
  <div id="globalHeader">
    <a-row :wrap="false">
      <a-col flex="200px">
        <router-link to="/">
          <div class="title-bar">
            <img class="logo" src="../assets/logo.png" alt="logo" />
            <div class="title">梧栖图行</div>
          </div>
        </router-link>
      </a-col>
      <a-col flex="auto">
        <a-menu
          v-model:selectedKeys="current"
          mode="horizontal"
          :items="items"
          @click="doMenuClick"
        />
      </a-col>
      <!-- 用户信息展示栏 -->
      <a-col flex="120px">
        <div class="user-login-status">
          <div v-if="loginUserStore.loginUser.id">
            <a-dropdown>
              <a-space :size="8">
                <!-- VIP 图标 -->
                <a-avatar
                  :src="loginUserStore.loginUser.userRole === 'vip'
                    ? '/vipImage/vip.png'
                    : '/vipImage/vip-gray.png'"
                  shape="square"
                  style="background-color: transparent; cursor: pointer"
                  @click="vipModalVisible = true"
                />
                <!-- 头像 -->
                <a-avatar :src="loginUserStore.loginUser.userAvatar" />
                {{ loginUserStore.loginUser.userName ?? '无名' }}
              </a-space>
              <template #overlay>
                <a-menu>
                  <a-menu-item>
                    <router-link to="/profile">
                      <HomeOutlined />
                      个人主页
                    </router-link>
                  </a-menu-item>
                  <a-menu-item>
                    <router-link to="/my_space">
                      <UserOutlined />
                      我的空间
                    </router-link>
                  </a-menu-item>
                  <a-menu-item @click="doLogout">
                    <LogoutOutlined />
                    退出登录
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </div>
          <div v-else>
            <a-button type="primary" href="/user/login">登录</a-button>
          </div>
        </div>
      </a-col>
    </a-row>
    <!-- VIP 详情弹窗 -->
    <a-modal
      v-model:visible="vipModalVisible"
      title="VIP 会员详情"
      :footer="null"
    >
      <template v-if="loginUserStore.loginUser.userRole === 'vip'">
        <a-divider />
        <p><strong>会员编号：</strong>{{ loginUserStore.loginUser.vipNumber || '暂无' }}</p>
        <p>
          <strong>过期时间：</strong>
          {{ loginUserStore.loginUser.vipExpireTime
            ? dayjs(loginUserStore.loginUser.vipExpireTime).format('YYYY-MM-DD HH:mm:ss')
            : '未知' }}
        </p>
      </template>

      <template v-else>
        <p style="font-size: 16px; margin-bottom: 8px">
          ⚠️ 您目前还不是会员，请联系
          <a-tooltip title="微信：c3240529156">
            <a href="javascript:void(0)">管理员</a>
          </a-tooltip>
          升级为 VIP。
        </p>
      </template>
    </a-modal>
  </div>
</template>
<script lang="ts" setup>
import { computed, h, ref } from 'vue'
import { HomeOutlined, LogoutOutlined, UserOutlined } from '@ant-design/icons-vue'
import { MenuProps, message } from 'ant-design-vue'
import { useRouter } from 'vue-router'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import { userLogoutUsingPost } from '@/api/userController.ts'
import dayjs from 'dayjs';

const vipModalVisible = ref(false)

const loginUserStore = useLoginUserStore()

// 未经过滤的菜单项
const originItems = [
  {
    key: '/',
    icon: () => h(HomeOutlined),
    label: '主页',
    title: '主页',
  },
  {
    key: '/add_picture',
    label: '创建图片',
    title: '创建图片',
  },
  {
    key: '/admin/userManage',
    label: '用户管理',
    title: '用户管理',
  },
  {
    key: '/admin/pictureManage',
    label: '图片管理',
    title: '图片管理',
  },
  {
    key: '/admin/spaceManage',
    label: '空间管理',
    title: '空间管理',
  },
  {
    key: '/myPictureManagePage',
    label: '我的图片',
    title: '我的图片',
  },
  {
    key: '/user_exchange_vip',
    label: '兑换会员',
    title: '兑换会员',
  },
]

// 根据权限过滤菜单项
const filterMenus = (menus = [] as MenuProps['items']) => {
  return menus?.filter((menu) => {
    // 管理员才能看到 /admin 开头的菜单
    if (menu?.key?.startsWith('/admin')) {
      const loginUser = loginUserStore.loginUser
      if (!loginUser || loginUser.userRole !== 'admin') {
        return false
      }
    }
    return true
  })
}

// 展示在菜单的路由数组
const items = computed(() => filterMenus(originItems))

const router = useRouter()
// 当前要高亮的菜单项
const current = ref<string[]>([])
// 监听路由变化，更新高亮菜单项
router.afterEach((to, from, next) => {
  current.value = [to.path]
})

// 路由跳转事件
const doMenuClick = ({ key }) => {
  router.push({
    path: key,
  })
}

// 用户注销
const doLogout = async () => {
  const res = await userLogoutUsingPost()
  if (res.data.code === 0) {
    loginUserStore.setLoginUser({
      userName: '未登录',
    })
    message.success('退出登录成功')
    await router.push('/user/login')
  } else {
    message.error('退出登录失败，' + res.data.message)
  }
}
</script>

<style scoped>
#globalHeader {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 64px; /* 或根据你实际高度 */
  z-index: 1000;
  background: #fff;
  box-shadow: 0 2px 8px #f0f1f2;
  padding: 0 24px;
}

.page-content {
  padding-top: 64px; /* 要和 header 的高度一致 */
}


#globalHeader .title-bar {
  display: flex;
  align-items: center;
}

.title {
  color: black;
  font-size: 18px;
  margin-left: 16px;
}

.logo {
  height: 48px;
}
</style>
