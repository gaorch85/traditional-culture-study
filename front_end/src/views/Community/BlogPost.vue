<template>
    <div class="post">
      <div v-if="post">
        <el-page-header @back="goBack" :title="post.title">
        </el-page-header>
  
        <br><br>
  
        <div class="blog-header">
          <div class="blog-title">
            <h1>{{ post.title }}</h1>
          </div>
          <div class="blog-meta">
            <div class="author-info">
              <span class="author-name">{{ post.username }}</span>
            </div>
            <div class="time-info">
              <el-icon><Clock /></el-icon>
              <span class="publish-date">{{ post.time }}</span>
            </div>
            <div class="view-info">
              <el-icon><View /></el-icon>
              <span class="publish-date">{{ post.viewSize }}</span>
            </div>
            <div class="visibility-info">
              <div v-if="post.isPublic">
                <span class="publish-date">public</span>
              </div>
              <div v-else>
                <el-icon><Lock /></el-icon>
                <span class="publish-date">private</span>
              </div>
            </div>
          </div>
        </div>
  
        <br><br>
  
        <div class="markdown-container">
          <MarkdownContainer :markdownContent="post.content"></MarkdownContainer>
        </div>
  
        <div class="like_favorite">
          <div class="like">
            <i 
              :class="{'iconfont icon-zan': post.myLike, 'iconfont icon-zan1': !post.myLike}" 
              @click="like">
            </i>
            <span>{{ post.likeSize }}</span>
          </div>
          <div class="favorite">
            <i 
              :class="{'iconfont icon-shoucang': post.myFavorite, 'iconfont icon-shoucang1': !post.myFavorite}" 
              @click="favorite">
            </i>
            <span>{{ post.favoriteSize }}</span>
          </div>
        </div>
  
        <div v-if="post.myBlog" class="Post-delete">
          <el-button type="danger" @click="deletePost()">
            <el-icon><Delete /></el-icon>
            删除文章
          </el-button>
          <el-button type="primary" @click="editPost()">
            <el-icon><Edit /></el-icon>
            编辑文章
          </el-button>
          <el-button type="success" @click="updatePost()">
            {{ post.isPublic ? "设为私密" : "设为公开" }}
          </el-button>
        </div>
        
        <div class="comment">
          <div class="comment-box">
            <el-input
              v-model="comment"
              type="textarea"
              :rows="2"
              :autosize="{ minRows: 2, maxRows: 4 }"
              placeholder="写下你的评论..."
            >
            </el-input>
            <br><br>
            <div class="comment-button">
              <el-button type="primary" @click="addComment">发表评论</el-button>
            </div>
          </div>
  
          <br><br><br>
          <div id="cmt-section">
            <div v-for="comment in comments"  class="cmt-block" :key="comment.id">
              <div class="cmt-header">
                <div class="cmt-delete" v-if="post.myBlog||comment.myComment">
                  <el-button
                    type="danger"
                    circle
                    @click="deleteComment(comment.id)"
                    size="small"
                  >
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </div>
                <div class="cmt-name">
                  {{comment.username}}
                </div>
                <div class="cmt-time">
                  {{comment.region}} | {{comment.createdAt}}
                </div>
              </div>
              <div class="cmt-value">
                {{comment.content}}
              </div>
            </div>
          </div>
        </div>
        
      </div>
      <div v-else>
        <el-empty :image-size="300" description="正在加载"></el-empty>
      </div>
    </div>
  </template>
  
  <script>
  import MarkdownContainer from "../../components/MarkdownContainer.vue";
  import { api_getPostById, api_insert_like, api_delete_like, api_insert_favorite, api_delete_favorite, 
    api_getCommentsById, api_add_comment, api_delete_comment, api_delete, api_update_status } from "@/api/blog"
  import {
    Clock,
    View,
    Lock,
    Delete,
    Edit
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
        post: null,
        comments: [],
        comment: ''
      };
    },
    mounted()
    {
        this.getPost();
        this.getComment();
    },
    created() {
      //VueMarkdownIt.use(markdownItHighlight);
    },
    methods: {
  
        getPost()
        {
            api_getPostById(this.postId)
            .then((response)=>
            {
                if(response.data.code == 20001)
                  if(this.$route.name != '404')
                    this.$router.push('/404');
                this.post = response.data.data.items;    
            });
        },

        deletePost()
        {
          this.$confirm('即将删除文章, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'error',
            center: true
            }).then(() => {
              console.log("开始删除");
                api_delete(this.postId)
                .then((response)=>
                {
                  if(response.data.code = 40000)
                  {
                    this.$message.success('删除文章成功!');
                    this.$router.push({ name: "Blog-List"});
                    this.$emit('blogDeleted', this.postId);
                  }
                });
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消'
                });
            });
        },

        editPost()
        {
          this.$router.push({ name: "EditBlog"});
        },

        updatePost()
        {
          if(this.post.isPublic == true)
          {
            this.$confirm('确认将该文章设为私有显示?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'error',
            center: true
            }).then(() => {
              
              api_update_status(this.postId)
                .then((response)=>
                {
                  if(response.data.code = 40000)
                  {
                    this.$message.success('成功设为私有!');
                    this.getPost();
                  }
                });
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消'
                });
            });

          }
          else
          {
            this.$confirm('确认将该文章设为公有显示?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'error',
            center: true
            }).then(() => {
              
              api_update_status(this.postId)
                .then((response)=>
                {
                  if(response.data.code = 40000)
                  {
                    this.$message.success('成功设为公有!');
                    this.getPost();
                  }
                });
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消'
                });
            });
          }
          
        },

        getComment()
        {
          api_getCommentsById(this.postId)
          .then((response)=>
          {
            this.comments = response.data.data.items;    
          });
        },

        goBack()
        {
          //console.log("返回");
          this.$router.push({ name: "community"});
        },

        like()
        {
          if(this.post.myLike)
          {
            api_delete_like(this.post.myLikeId)
              .then((response)=>
              {
                  //this.post = response.data.data.items;    
                  //console.log(this.posts);
                  this.$message.info('取消点赞');
                  this.getPost();
              });
          }
          else
          {
            api_insert_like(this.postId)
            .then((response)=>
            {
                //this.post = response.data.data.items;    
                //console.log(this.posts);
                this.$message.success('点赞成功');
                this.getPost();
            });
          }
            
        },

        favorite()
        {
          if(this.post.myFavorite)
          {
            api_delete_favorite(this.post.myFavoriteId)
            .then((response)=>
            {
                //this.post = response.data.data.items;    
                //console.log(this.posts);
                this.$message.info('取消收藏');
                this.getPost();
            });

          }
          else
          {
            api_insert_favorite(this.postId)
            .then((response)=>
            {
                //this.post = response.data.data.items;    
                //console.log(this.posts);
                this.$message.success('收藏成功');
                this.getPost();
            });
          }
        },

        addComment()
        {
          if (this.comment.trim() === '') {
            this.$message.error('评论内容不能为空');
            return;
          }
          else
          {
            api_add_comment(this.postId,{content: this.comment})
            .then((response)=>
            {
              this.getComment();
              this.comment = '';
              this.$message.success('评论成功');
            });
          }
        },

        deleteComment(id)
        {
          this.$confirm('即将删除评论, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'error',
            center: true
            }).then(() => {
                api_delete_comment(id)
                .then((response)=>
                {
                  if(response.data.code = 40000)
                  {
                    this.$message.success('删除评论成功!');
                    this.getComment();
                  }
                });
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消'
                });
            });
          
        }
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
  max-width: 900px; /* 最大宽度 */
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

.author-info {
  display: flex;
  align-items: center;
}

.author-info .author-name {
  margin-left: 10px;
  font-size: 1.2em;
  color: #666;
}

.time-info {
  display: flex;
  align-items: center;
  font-size: 1em;
  color: #999;
}

.time-info el-icon {
  margin-right: 5px;
}

.view-info {
  display: flex;
  align-items: center;
  font-size: 1em;
  color: #999;
}

.view-info el-icon {
  margin-right: 5px;
}


.visibility-info {
  display: flex;
  align-items: center;
  font-size: 1em;
  color: #999;
}

.visibility-info el-icon {
  margin-right: 5px;
}


.like_favorite {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 20px 0;
  gap: 20px;
}

.like, .favorite {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.like i, .favorite i {
  font-size: 24px;
  margin-right: 5px;
}

.like i.iconfont.icon-zan,
.like i.iconfont.icon-zan1,
.favorite i.iconfont.icon-shoucang,
.favorite i.iconfont.icon-shoucang1 {
  transition: color 0.3s;
}

.like i.iconfont.icon-zan {
  color: #ff6b6b; /* 红色代表点赞 */
}

.like i.iconfont.icon-zan1 {
  color: #cccccc; /* 灰色代表未点赞 */
}

.favorite i.iconfont.icon-shoucang {
  color: #ffd700; /* 金色代表收藏 */
}

.favorite i.iconfont.icon-shoucang1 {
  color: #cccccc; /* 灰色代表未收藏 */
}

.Post-delete {
  display: flex;
  justify-content: center;
  margin-top: 15px;
  margin-bottom: 15px;
}


.comment {
  max-width: 900px; /* 最大宽度 */
  margin: 0 auto; /* 居中显示 */
  font-family: Arial, sans-serif; /* 字体样式 */
  line-height: 1.6; /* 行高 */
  color: #333; /* 文字颜色 */
  font-size: 16px; /* 字体大小 */

}

.cmt-block {
    border: 1px solid #ccc;
    border-radius: 5px;
    padding: 10px;
    margin-bottom: 10px;
    background-color: #f9f9f9;
}

.cmt-name {
    font-weight: bold;
    margin-bottom: 5px;
}

.cmt-time {
    color: #666;
    font-size: 12px;
    margin-bottom: 5px;
}

.cmt-value {
    margin-top: 5px;
}

.cmt-header {
    position: relative; /* 让容器成为相对定位的父元素 */
}

.cmt-delete {
    position: absolute; /* 将图标设置为绝对定位，相对于其父元素进行定位 */
    top: 0; /* 图标距离容器顶部的距离 */
    right: 0; /* 图标距离容器右侧的距离 */
    /* 其他样式 */
}
</style>
  