<template>
  <div class="list">
    <div class="header">
      <div class="tabs-container">
        <el-tabs v-model="activeName" @tab-click="handleClick" class="custom-tabs">
          <el-tab-pane label="推荐" name="recommend"></el-tab-pane>
          <el-tab-pane label="最新" name="latest"></el-tab-pane>
          <el-tab-pane label="我的文章" name="myBlog"></el-tab-pane>
          <el-tab-pane label="我的点赞" name="myLike"></el-tab-pane>
          <el-tab-pane label="我的收藏" name="myFavorite"></el-tab-pane>
        </el-tabs>
      </div>
      <el-button type="primary" class="create-button" @click="createPost">
        <el-icon><Edit /></el-icon>发布文章
      </el-button>
    </div>
    
    <div v-if="!posts">
      <el-empty :image-size="300" description="正在加载"></el-empty>
    </div>
    <div v-else-if="posts.length==0">
      <el-empty :image-size="300" description="暂无数据"></el-empty>
    </div>
    <div v-else>
      <div class="posts-container">
        <div class="post-card" v-for="post in posts" :key="post.id" @click="viewPost(post.id, post.title)">
          <div class="blog-post">
            <h2 class="post-title">{{ post.title }}</h2>
            <p class="post-content">{{ post.summary }}</p>
            <div class="post-footer">
              <div class="post-info">
                <span class="post-author">{{ post.username }}</span>
                <span class="post-date">{{ post.time }}</span>
              </div>
              <div class="like-favorite-comment">
                <div v-if="!post.isPublic" class="icon-container">
                  <el-icon><Lock /></el-icon>
                </div>
                <div class="view icon-container">
                  <el-icon><View /></el-icon>
                  <span>{{ post.viewSize }}</span>
                </div>
                <div class="like icon-container">
                  <i :class="{'iconfont icon-zan': post.myLike, 'iconfont icon-zan1': !post.myLike}"></i>
                  <span>{{ post.likeSize }}</span>
                </div>
                <div class="favorite icon-container">
                  <i :class="{'iconfont icon-shoucang': post.myFavorite, 'iconfont icon-shoucang1': !post.myFavorite}"></i>
                  <span>{{ post.favoriteSize }}</span>
                </div>
                <div class="comments icon-container">
                  <el-icon><ChatDotSquare /></el-icon>
                  <span>{{ post.commentSize }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { api_getAll } from '@/api/blog';
import { Edit, Lock, View, ChatDotSquare } from '@element-plus/icons-vue'

export default {
  components: {
    Edit,
    Lock,
    View,
    ChatDotSquare
  },
  mounted() {
    this.getAll(this.activeName);
  },

  data() {
    return {
      posts: null,
      activeName: 'recommend',
    };
  },

  methods: {
    handleClick(tab) {
      this.activeName = tab.props.name;
      console.log(this.activeName);
      this.getAll(this.activeName);
    },

    getAll(sortIndex) {
      this.posts = null;
      api_getAll(sortIndex)
      .then((response) => {
        this.posts = response.data.data.items;    
      })
    },

    viewPost(id, title) {
      this.$router.push({ name: 'BlogPost', params: { id, title } });
    },

    createPost() {
      this.$router.push('/community/create');
    }
  }
};
</script>

<style scoped>
.list {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.tabs-container {
  flex-grow: 1;
}

.custom-tabs :deep(.el-tabs__item) {
  font-size: 16px;
  padding: 0 20px;
}

.create-button {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 10px 20px;
  border-radius: 4px;
  font-size: 14px;
}

.posts-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.post-card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  padding: 20px;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

.post-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.post-title {
  font-size: 18px;
  margin-bottom: 12px;
  color: #333;
  font-weight: 600;
}

.post-content {
  font-size: 14px;
  color: #666;
  margin-bottom: 16px;
  line-height: 1.5;
}

.post-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.post-info {
  display: flex;
  gap: 10px;
  color: #999;
  font-size: 13px;
}

.like-favorite-comment {
  display: flex;
  gap: 16px;
  align-items: center;
}

.icon-container {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #666;
}

.icon-container span {
  font-size: 13px;
}

:deep(.el-icon) {
  font-size: 16px;
}

.like i.iconfont.icon-zan {
  color: #ff6b6b;
}

.like i.iconfont.icon-zan1 {
  color: #666;
}

.favorite i.iconfont.icon-shoucang {
  color: #ffd700;
}

.favorite i.iconfont.icon-shoucang1 {
  color: #666;
}

</style>