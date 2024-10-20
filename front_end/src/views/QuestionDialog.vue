<template>
    <div class="question-dialog">
      <p class="question-text">{{ question }}</p>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" class="answer-form">
        <el-form-item label="你的回答" prop="answer">
          <el-input
            v-model="form.answer"
            type="textarea"
            :rows="4"
            placeholder="请输入你的回答"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm(formRef)">提交</el-button>
          <el-button @click="cancel">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </template>
  
  <script setup>
  import { ref, reactive } from 'vue';
  import { ElMessage } from 'element-plus';
  
  const props = defineProps({
    question: {
      type: String,
      required: true,
    },
  });
  
  const emit = defineEmits(['submit', 'cancel']);
  
  const formRef = ref(null);
  
  const form = reactive({
    answer: '',
  });
  
  const rules = {
    answer: [
      { required: true, message: '请输入你的回答', trigger: 'blur' },
      { min: 1, max: 500, message: '回答长度应在 1 到 500 个字符之间', trigger: 'blur' }
    ],
  };
  
  const submitForm = async (formEl) => {
    if (!formEl) return;
    await formEl.validate((valid, fields) => {
      if (valid) {
        emit('submit', form.answer);
        form.answer = ''; // Clear the form after submission
      } else {
        console.log('error submit!', fields);
        ElMessage.error('请检查并填写所有必填字段');
      }
    });
  };
  
  const cancel = () => {
    form.answer = ''; // Clear the form
    emit('cancel');
  };
  </script>
  
  <style scoped>
  .question-dialog {
    padding: 20px;
  }
  
  .question-text {
    font-size: 1.1em;
    color: #303133;
    margin-bottom: 20px;
    line-height: 1.6;
  }
  
  .answer-form {
    margin-top: 20px;
  }
  </style>