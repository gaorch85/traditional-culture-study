<template>
    <div class="post">
        <el-page-header @back="goBack">
        </el-page-header>
        <br><br>
        <div class="blog-header">
          <div class="blog-title">
            <h1>{{ post.title }}</h1>
          </div>
        </div>
  
        <br><br>
  
        <div class="markdown-container">
          <MarkdownContainer :markdownContent="post.content"></MarkdownContainer>
        </div>
  
  
        <div class="Post-delete">
          <el-button type="primary" @click="learnOver()">
            <el-icon><Check /></el-icon>
            学习完成
          </el-button>
        </div>
        
    </div>
  </template>
  
  <script>
  import MarkdownContainer from "../../components/MarkdownContainer.vue";
  import data from './data.json'
  import {
    Clock,
    View,
    Lock,
    Delete,
    Edit,
    Check
  } from '@element-plus/icons-vue'
  export default {
    name: 'BlogPost',
    components:{
      MarkdownContainer,
      Clock,
      View,
      Lock,
      Delete,
      Edit
    },
    data() {
      return {
        postId: this.$route.params.id,
        post: {
            title: '默认',
            content: '默认'
        },
      };
    },
    mounted()
    {
        this.getPost();
    },
    created() {
    },
    methods: {
  
        async getPost()
        {
            const foundPost = data.posts.find(post => post.id === this.postId)
            const response = await fetch(`/data/${this.postId}.md`)
            const content = await response.text()
            if (foundPost) {
                this.post = {
                title: foundPost.title,
                content: content
                }
            } else {
                this.post = {
                title: '文章未找到',
                content: '# 抱歉，未找到该文章'
                }
            }
        },

        learnOver()
        {
            this.$message.success('学习完成!');
            this.$store.dispatch('knowledge/completeNode', this.postId)
            this.$router.push({ name: "explore"});
        },


        goBack()
        {
          //console.log("返回");
          this.$router.push({ name: "explore"});
        },

    },
    watch: {
      '$route.params.id'(newId) {
        this.postId = newId;
        this.getPost();
      },
    },
  };
  </script>

<style>
.el-icon {
  margin-right: 4px;
  vertical-align: middle;
}

.comment-button {
  text-align: right;
  margin-top: 16px;
}

.post {
  max-width: 1200px; /* 最大宽度 */
  margin: 0 auto; /* 居中显示 */
  font-family: Arial, sans-serif; /* 字体样式 */
}

.blog-header {
  padding: 20px;
  max-width: 900px;
  border-bottom: 1px solid #ddd;
}

.blog-title {
  margin-bottom: 10px;
}

.blog-title h1 {
  font-size: 2.5em;
  font-weight: bold;
  margin: 0;
  color: #333;
}

.blog-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}





</style>
  