<template>
  <el-dialog
    v-model="dialogVisible"
    :title="isLogin ? '登录' : '注册'"
    width="400px"
    :before-close="handleClose"
    custom-class="login-register-dialog"
  >
    <el-form :model="form" label-position="top" :rules="rules" ref="formRef">
      <el-form-item label="账号" prop="account">
        <el-input v-model="form.account" prefix-icon="el-icon-user" />
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="form.password" type="password" prefix-icon="el-icon-lock" />
      </el-form-item>
      <el-form-item v-if="!isLogin" label="确认密码" prop="confirmPassword">
        <el-input v-model="form.confirmPassword" type="password" prefix-icon="el-icon-lock" />
      </el-form-item>
    </el-form>
    <div class="dialog-footer">
      <el-button type="primary" @click="handleSubmit" :loading="loading">
        {{ isLogin ? '登录' : '注册' }}
      </el-button>
      <el-button @click="switchMode">
        {{ isLogin ? '没有账号？立即注册' : '已有账号？立即登录' }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { ref, reactive, watch } from 'vue'
import { useStore } from 'vuex'
import { setToken } from '@/util/auth'
import { ElMessage } from 'element-plus'
import { api_login, api_signup } from '@/api/user'

export default {
  name: 'LoginRegisterDialog',
  props: {
    visible: Boolean
  },
  emits: ['update:visible', 'login-success'],
  setup(props, { emit }) {
    const store = useStore()
    const formRef = ref(null)
    const dialogVisible = ref(props.visible)
    const isLogin = ref(true)
    const loading = ref(false)

    const form = reactive({
      account: '',
      password: '',
      confirmPassword: ''
    })

    const rules = {
      account: [
        { required: true, message: '请输入账号', trigger: 'blur' },
        { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
      ],
      confirmPassword: [
        { required: true, message: '请确认密码', trigger: 'blur' },
        {
          validator: (rule, value, callback) => {
            if (value !== form.password) {
              callback(new Error('两次输入密码不一致'))
            } else {
              callback()
            }
          },
          trigger: 'blur'
        }
      ]
    }

    watch(() => props.visible, (newValue) => {
      dialogVisible.value = newValue
    })

    const handleClose = () => {
      emit('update:visible', false)
      resetForm()
    }

    const resetForm = () => {
      form.account = ''
      form.password = ''
      form.confirmPassword = ''
      if (formRef.value) {
        formRef.value.resetFields()
      }
    }

    const switchMode = () => {
      isLogin.value = !isLogin.value
      resetForm()
    }

    const handleSubmit = async () => {
      if (!formRef.value)
       return;

      try {
        await formRef.value.validate();
        loading.value = true;
        
        const response = isLogin.value ? await api_login(form) : await api_signup(form);
        if(response.data.code == 200) {
          const token = response.data.data;
          setToken(token);
          ElMessage.success(isLogin.value ? '登录成功' : '注册成功');
          await store.dispatch('user/getUserInfo');
          emit('login-success')
          handleClose()
        }
        else {
          ElMessage.error(isLogin.value ? '账号或密码错误' : '该账号已存在');
        }


        // // 模拟获取 token 成功
        // const token = 'mock_token_' + Date.now()
        // setToken(token)

        // // 不直接设置用户信息，而是触发获取用户信息的 action
        // await store.dispatch('user/getUserInfo')
        
        // ElMessage.success(isLogin.value ? '登录成功' : '注册成功')
        // emit('login-success')
        // handleClose()

        
      } catch (error) {
        ElMessage.error('网络异常')
      } finally {
        loading.value = false
      }
    }

    return {
      dialogVisible,
      isLogin,
      form,
      rules,
      formRef,
      loading,
      handleClose,
      handleSubmit,
      switchMode
    }
  }
}
</script>

<style scoped>
.login-register-dialog {
  border-radius: 8px;
  overflow: hidden;
}

.login-register-dialog :deep(.el-dialog__header) {
  background-color: #f0f2f5;
  padding: 20px;
  margin: 0;
}

.login-register-dialog :deep(.el-dialog__body) {
  padding: 30px;
}

.login-register-dialog :deep(.el-form-item__label) {
  font-weight: bold;
}

.login-register-dialog :deep(.el-input__inner) {
  border-radius: 4px;
}

.dialog-footer {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 20px;
}

.dialog-footer .el-button {
  width: 100%;
  margin: 10px 0;
}

.dialog-footer .el-button--primary {
  background-color: #409eff;
  border-color: #409eff;
}

.dialog-footer .el-button--default {
  background-color: transparent;
  border: none;
  color: #409eff;
}
</style>