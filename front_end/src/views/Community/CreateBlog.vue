<template>
  <div class="blog-form-container">
    <div class="form-header">
      <el-page-header :icon="null" @back="goBack">
        <template #content>
          <div class="page-header-content">
            <h1 class="page-title">创建新文章</h1>
            <p class="page-subtitle">分享你的想法和经验</p>
          </div>
        </template>
      </el-page-header>
    </div>

    <div class="form-content">
      <el-form :model="form" label-position="top" class="blog-form">
        <el-form-item>
          <el-input
            v-model="form.title"
            placeholder="请输入文章标题"
            class="title-input"
            :maxlength="30"
            show-word-limit
          >
            <template #prefix>
              <el-icon class="input-icon"><Edit /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item class="editor-container">
          <mavon-editor 
            v-model="form.content"
            class="custom-editor"
            :toolbars="toolbars"
            placeholder="请输入文章内容..."
          ></mavon-editor>
        </el-form-item>

        <div class="form-footer">
          <div class="visibility-toggle">
            <span class="visibility-label">文章可见性：</span>
            <el-switch
              v-model="form.isPublic"
              active-text="公开"
              inactive-text="私密"
              class="custom-switch"
            />
          </div>
          
          <el-button type="primary" size="large" @click="insert" class="submit-button">
            <el-icon><DocumentAdd /></el-icon>
            发布文章
          </el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import { mavonEditor } from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import { api_insert } from '@/api/blog'
import { Edit, DocumentAdd } from '@element-plus/icons-vue'

export default {
  components: {
    mavonEditor,
    Edit,
    DocumentAdd
  },
  data() {
    return {
      form: {
        title: '',
        content: '',
        isPublic: true
      },
      toolbars: {
        bold: true,
        italic: true,
        header: true,
        underline: true,
        strikethrough: true,
        mark: true,
        superscript: true,
        subscript: true,
        quote: true,
        ol: true,
        ul: true,
        link: true,
        imagelink: true,
        code: true,
        table: true,
        fullscreen: true,
        readmodel: true,
        help: true,
        undo: true,
        redo: true,
        navigation: true,
        subfield: true,
        preview: true,
      }
    }
  },
  methods: {
    insert() {
      if (!this.form.title.trim() || !this.form.content.trim()) {
        this.$message({
          message: '请填写文章标题和内容',
          type: 'warning'
        })
        return
      }

      this.$confirm('确认发布这篇文章？', '发布确认', {
        confirmButtonText: '发布',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        api_insert(this.form)
          .then((response) => {
            if(response.data.code === 20000) {
              this.$message({
                type: 'success',
                message: '文章发布成功！'
              })
              this.form = {
                title: '',
                content: '',
                isPublic: true
              }
              this.$router.push({ name: "community" })
            } else {
              this.$message.error('发布失败，请稍后重试')
            }
          })
      }).catch(() => {})
    },
    goBack() {
      this.$router.push({ name: "community" })
    }
  }
}
</script>

<style scoped>
.blog-form-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
  min-height: 100vh;
}

.form-header {
  background: white;
  padding: 24px;
  border-radius: 8px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.page-header-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.page-subtitle {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

.form-content {
  background: white;
  padding: 32px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.title-input :deep(.el-input__wrapper) {
  padding: 12px 16px;
  font-size: 16px;
}

.input-icon {
  font-size: 18px;
  color: #909399;
  margin-right: 8px;
}

.editor-container {
  margin-top: 24px;
}

.custom-editor {
  min-height: 500px;
  min-width: 1135px;
  border-radius: 4px;
}

.form-footer {
  margin-top: 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 24px;
  border-top: 1px solid #ebeef5;
}

.visibility-toggle {
  display: flex;
  align-items: center;
  gap: 12px;
}

.visibility-label {
  font-size: 14px;
  color: #606266;
}

.custom-switch :deep(.el-switch__label) {
  color: #606266;
}

.submit-button {
  padding: 12px 24px;
  font-size: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

:deep(.el-switch__label) {
  font-size: 14px;
}

:deep(.mavon-editor) {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

:deep(.v-note-wrapper) {
  z-index: 1;
}
</style>