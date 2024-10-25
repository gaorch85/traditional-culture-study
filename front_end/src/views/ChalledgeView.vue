<template>
  <!-- 修改: 重新设计顶部布局 -->
  <div class="header">
      <div class="topic-section">
        <h1 class="topic-title">{{ today_topic }}</h1>
        <p class="topic-intro">{{ today_introduction }}</p>
      </div>
      <div class="date-card">
        <div class="date-content">
          <span class="year-month">{{ currentYear }}/{{ currentMonth }}</span>
          <span class="day">{{ currentDate }}</span>
        </div>
      </div>
    

    <!-- 修改: 重新设计挑战进度和按钮区域 -->
    <div class="challenge-section">
      <div class="progress-steps">
        <el-steps :active="active" finish-status="success" class="custom-steps">
          <el-step title="简单" :description="try_simple" />
          <el-step title="中等" :description="try_mid" />
          <el-step title="困难" :description="try_hard" />
        </el-steps>
      </div>
      
      <div class="challenge-buttons">
        <el-button 
          class="challenge-btn simple"
          @click="onClickSimple"
          type="primary"
          :icon="Document">
          挑战简单
        </el-button>
        <el-button 
          class="challenge-btn medium"
          @click="onClickMid"
          type="warning"
          :disabled="isMid"
          :icon="DocumentAdd">
          挑战中等
        </el-button>
        <el-button 
          class="challenge-btn hard"
          @click="onClickHard"
          type="danger"
          :disabled="isHard"
          :icon="DocumentChecked">
          挑战困难
        </el-button>
      </div>
    </div>
    <el-dialog
      v-model="dialogFormVisible_simple"
      title="每日问答--简单"
      class="custom-dialog"
      width="600px">
      <div class="question-container">
        <div class="question-content">{{ simple_question }}</div>
        <el-form :model="form" :rules="rules" ref="formRef">
          <el-form-item label="作答区" :label-width="formLabelWidth" prop="answer">
            <el-input
              v-model="form.answer"
              type="textarea"
              :rows="6"
              placeholder="请输入你的答案..."
              class="custom-textarea"
            />
          </el-form-item>
          <el-form-item class="dialog-footer">
            <el-button @click="dialogFormVisible_simple = false">取消</el-button>
            <el-button type="primary" @click="onsubmit(1)">提交答案</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>

    <div class="question_area">
      <el-dialog v-model="dialogFormVisible_mid" title="每日问答--中等" width="500">
        <div class="question"><el-text>{{ mid_question }}</el-text></div>
        <el-form :model="form" :rules="rules" ref="formRef">
          <el-form-item label="作答区" :label-width="formLabelWidth" prop="answer">
            <el-input v-model="form.answer" style="width: 400px" :rows="5" type="textarea" placeholder="输入你的答案..." />
          </el-form-item>
          <el-form-item>
            <el-button @click="dialogFormVisible_mid = false">取消</el-button>
            <el-button type="primary" @click="onsubmit(2)">
              提交
            </el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
    </div>
    <div class="question_area">
      <el-dialog v-model="dialogFormVisible_hard" title="每日问答--困难" width="500">
        <div class="question"><el-text>{{ hard_question }}</el-text></div>
        <el-form :model="form" :rules="rules" ref="formRef">
          <el-form-item label="作答区" :label-width="formLabelWidth" prop="answer">
            <el-input v-model="form.answer" style="width: 400px" :rows="5" type="textarea" placeholder="输入你的答案..." />
          </el-form-item>
          <el-form-item>
            <el-button @click="dialogFormVisible_hard = false">取消</el-button>
            <el-button type="primary" @click="onsubmit(3)">
              提交
            </el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
    </div>

    <div v-if="successVisible" class="success-message" :class="scoreClass">
      <div class="success-content">
        <div class="success-icon">
          <el-icon size="48px">
            <component :is="scoreIcon"></component>
          </el-icon>
        </div>
        <h2 class="success-header">{{ suggestion_before }}</h2>
        <div class="success-score">
          得分: <span>{{ score }}</span>
        </div>
        <div class="success-feedback">{{ suggestion }}</div>
        <el-button class="close-btn" @click="successVisible = false">
          关闭
        </el-button>
      </div>
    </div>

    <div class="history-section">
      <h2 class="history-title">答题历史</h2>
      <el-table
        :data="tableData"
        class="custom-table"
        height="300px"
        style="width: 100%">
        <el-table-column prop="time" label="时间" width="180">
          <template #default="scope">
            <div class="table-cell">
              <el-icon><Timer /></el-icon>
              <span>{{ scope.row.time }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="level" label="难度等级" width="180">
          <template #default="scope">
            <div style="display: flex; align-items: center">
              <span style="margin-left: 10px">{{ scope.row.level }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="grade" label="分数" width="180">
          <template #default="scope">
            <div style="display: flex; align-items: center">
              <span style="margin-left: 10px">{{ scope.row.grade }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="操作">
          <template #default="scope">
            <el-button size="small" @click="onClickHistory(scope.row)">
              查看
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="history_itemVisible" title="详情" width="500" :before-close="handleClose">
      <span>尝试时间：</span>
      <div class="history_time">{{ history_time }}</div>
      <span>难度等级：</span>
      <div class="history_level">{{ history_level }}</div>
      <span>分数：</span>
      <div class="history_grade">{{ history_grade }}</div>
      <span>你的回答：</span>
      <div class="history_answer">{{ history_answer }}</div>
      <span>评语：</span>
      <div class="history_suggestion">{{ history_suggestion }}</div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="history_itemVisible = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, reactive, onMounted } from 'vue';
import { useStore } from 'vuex';
import { useRoute } from 'vue-router';
import { api_getCurAnswer, api_getAnswerCount, api_getDailyInfo, api_listAnswers } from '@/api/challedge';
import { api_increasePoint } from '@/api/user'
import { ElMessage } from 'element-plus';

const store = useStore();
const route = useRoute();

const routeDate = computed(() => {
  const dateParam = route.params.date;
  return dateParam ? new Date(dateParam) : new Date();
});

const currentYear = computed(() => routeDate.value.getFullYear());
const currentMonth = computed(() => routeDate.value.getMonth() + 1);
const currentDate = computed(() => routeDate.value.getDate());

const active = ref(0);
const try_times_simple = ref(0);
const try_times_mid = ref(0);
const try_times_hard = ref(0);

const isMid = computed(() => active.value < 1);
const isHard = computed(() => active.value < 2);

const today_topic = ref("");
const today_introduction = ref("");

const simple_question = ref('');
const mid_question = ref('');
const hard_question = ref('');

const questionIds = reactive({
  simple: null,
  mid: null,
  hard: null
});

const dialogFormVisible_simple = ref(false);
const dialogFormVisible_mid = ref(false);
const dialogFormVisible_hard = ref(false);

const formLabelWidth = ref('70px');
const form = reactive({
  answer: '',
});

const successVisible = ref(false);
const score = ref(0);
const suggestion = ref("");
const current_select = ref(0);

const tableData = ref([]);

const rules = {
  answer: [
    { required: true, message: '作答区不能为空', trigger: 'blur' },
  ],
};

const history_itemVisible = ref(false);
const history_time = ref('');
const history_grade = ref('');
const history_level = ref('');
const history_answer = ref('');
const history_suggestion = ref('');

const scoreClass = computed(() => {
  if (score.value >= 80) return "success-message--excellent";
  else if (score.value >= 60) return "success-message--pass";
  else return "success-message--fail";
});

const suggestion_before = computed(() => {
  if (score.value >= 80) return "优秀回答！恭喜你！ 🎉";
  else if (score.value >= 60) return "答题通过！恭喜你！ 🎉";
  else return "未通过😣，再试一次吧";
});

const try_simple = computed(() => "尝试次数：" + try_times_simple.value);
const try_mid = computed(() => "尝试次数：" + try_times_mid.value);
const try_hard = computed(() => "尝试次数：" + try_times_hard.value);

const onClickSimple = () => {
  dialogFormVisible_simple.value = true;
  current_select.value = 1;
};

const onClickMid = () => {
  dialogFormVisible_mid.value = true;
  current_select.value = 2;
};

const onClickHard = () => {
  dialogFormVisible_hard.value = true;
  current_select.value = 3;
};

const onsubmit = async (level) => {
  let questionId;
  switch (level) {
    case 1:
      questionId = questionIds.simple;
      dialogFormVisible_simple.value = false;
      break;
    case 2:
      questionId = questionIds.mid;
      dialogFormVisible_mid.value = false;
      break;
    case 3:
      questionId = questionIds.hard;
      dialogFormVisible_hard.value = false;
      break;
  }

  try {
    const response = await api_getCurAnswer({ questionId, content: form.answer });
    if (response.data.code === 200) {
      score.value = response.data.data.score;
      if(score.value >= 80) {
        const increasePointResponse = await api_increasePoint(10);
        (increasePointResponse.data.code == 200) ? ElMessage.success('积分+10！') : ElMessage.error('积分异常!');
      } else if(score.value >= 60) {
        const increasePointResponse = await api_increasePoint(5);
        (increasePointResponse.data.code == 200) ? ElMessage.success('积分+5！') : ElMessage.error('积分异常!');
      } 
      suggestion.value = response.data.data.reason;
      successVisible.value = true;
      
      // Update try times
      updateTryTimes(level);
      
      // Refresh history
      await fetchHistory();

      // Update active step
      if (score.value >= 60) {
        active.value = Math.max(active.value, level);
      }
    }
  } catch (error) {
    console.error('Error submitting answer:', error);
  }

  form.answer = ''; // Clear the form
};

const updateTryTimes = async (level) => {
  let questionId;
  switch (level) {
    case 1:
      questionId = questionIds.simple;
      break;
    case 2:
      questionId = questionIds.mid;
      break;
    case 3:
      questionId = questionIds.hard;
      break;
  }

  try {
    const response = await api_getAnswerCount(questionId);
    if (response.data.code === 200) {
      switch (level) {
        case 1:
          try_times_simple.value = response.data.data;
          break;
        case 2:
          try_times_mid.value = response.data.data;
          break;
        case 3:
          try_times_hard.value = response.data.data;
          break;
      }
    }
  } catch (error) {
    console.error('Error updating try times:', error);
  }
};

const fetchHistory = async () => {
  try {
    const [simpleHistory, midHistory, hardHistory] = await Promise.all([
      api_listAnswers(questionIds.simple),
      api_listAnswers(questionIds.mid),
      api_listAnswers(questionIds.hard)
    ]);

    const allHistory = [
      ...simpleHistory.data.data.map(item => ({ ...item, level: '简单' })),
      ...midHistory.data.data.map(item => ({ ...item, level: '中等' })),
      ...hardHistory.data.data.map(item => ({ ...item, level: '困难' }))
    ];

    tableData.value = allHistory.map(item => ({
      time: item.date,
      level: item.level,
      grade: item.score,
      content: item.content,
      reason: item.reason
    }));
  } catch (error) {
    console.error('Error fetching history:', error);
  }
};

const onClickHistory = (row) => {
  history_itemVisible.value = true;
  history_level.value = row.level;
  history_grade.value = row.grade;
  history_time.value = row.time;
  history_answer.value = row.content;
  history_suggestion.value = row.reason;
};

const handleClose = () => {
  history_itemVisible.value = false;
};

onMounted(async () => {
  try {
    const response = await api_getDailyInfo(route.params.date);
    
    if (response.data.code === 200) {
      const { theme, questions } = response.data.data;
      
      today_topic.value = theme.content;
      today_introduction.value = theme.intro;
      
      questions.forEach(question => {
        switch (question.level) {
          case 1:
            simple_question.value = question.content;
            questionIds.simple = question.id;
            break;
          case 2:
            mid_question.value = question.content;
            questionIds.mid = question.id;
            break;
          case 3:
            hard_question.value = question.content;
            questionIds.hard = question.id;
            break;
        }
      });
      
      // Fetch initial try counts
      await Promise.all([
        updateTryTimes(1),
        updateTryTimes(2),
        updateTryTimes(3)
      ]);
      
      // Fetch initial history
      await fetchHistory();
    }
  } catch (error) {
    console.error('Error fetching daily info:', error);
    // You might want to show an error message to the user here
  }
});
</script>

<style scoped>
.header {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
  background: #ffffff;
  border-radius: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

/* Topic Section */
.topic-section {
  flex: 1;
  margin-right: 2rem;
  padding: 1rem 0;
}

.topic-title {
  font-size: 2.5rem;
  color: #1a1f36;
  margin-bottom: 1rem;
  font-weight: 700;
  line-height: 1.2;
  letter-spacing: -0.5px;
}

.topic-intro {
  font-size: 1.1rem;
  color: #4a5568;
  line-height: 1.8;
  margin-bottom: 2rem;
}

/* Date Card */
.date-card {
  background: linear-gradient(135deg, #3b82f6 0%, #1e40af 100%);
  border-radius: 16px;
  padding: 1.5rem;
  color: white;
  min-width: 180px;
  text-align: center;
  box-shadow: 0 10px 25px rgba(59, 130, 246, 0.2);
  transition: transform 0.3s ease;
}

.date-card:hover {
  transform: translateY(-5px);
}

.year-month {
  font-size: 1.1rem;
  font-weight: 500;
  opacity: 0.9;
  display: block;
  margin-bottom: 0.5rem;
}

.day {
  font-size: 3.5rem;
  font-weight: 800;
  line-height: 1;
}

/* Challenge Section */
.challenge-section {
  background: #ffffff;
  border-radius: 20px;
  padding: 2rem;
  margin: 2rem 0;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

.progress-steps {
  margin-bottom: 2rem;
}

.challenge-buttons {
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-top: 2rem;
}

.challenge-btn {
  padding: 0.75rem 1.5rem;
  font-size: 1rem;
  font-weight: 600;
  border-radius: 12px;
  transition: all 0.3s ease;
  min-width: 140px;
}

.challenge-btn.simple {
  background: linear-gradient(135deg, #60a5fa 0%, #3b82f6 100%);
  border: none;
}

.challenge-btn.medium {
  background: linear-gradient(135deg, #fbbf24 0%, #d97706 100%);
  border: none;
}

.challenge-btn.hard {
  background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
  border: none;
}

.challenge-btn:not(:disabled):hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
}

/* Dialog Styles */
.custom-dialog {
  border-radius: 20px;
}

.question-container {
  padding: 1.5rem;
}

.question-content {
  font-size: 1.1rem;
  color: #1a1f36;
  line-height: 1.8;
  margin-bottom: 1.5rem;
  padding: 1rem;
  background: #f8fafc;
  border-radius: 12px;
}

.custom-textarea {
  border-radius: 12px;
  border: 2px solid #e2e8f0;
  transition: all 0.3s ease;
}

.custom-textarea:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

/* History Section */
.history-section {
  background: #ffffff;
  border-radius: 20px;
  padding: 2rem;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

.history-title {
  font-size: 1.5rem;
  color: #1a1f36;
  margin-bottom: 1.5rem;
  font-weight: 600;
}

.custom-table {
  border-radius: 12px;
  overflow: hidden;
  border: 1px solid #e2e8f0;
}

/* Success Message */
.success-message {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  width: 90%;
  max-width: 500px;
  z-index: 1001; /* 确保在遮罩层之上 */
}
.success-message--excellent {
  border-top: 5px solid #22c55e;
}

.success-message--pass {
  border-top: 5px solid #3b82f6;
}

.success-message--fail {
  border-top: 5px solid #ef4444;
}

.success-content {
  position: relative;
  padding: 2rem;
  text-align: center;
}

.success-header {
  font-size: 1.5rem;
  color: #1a1f36;
  margin-bottom: 1rem;
}

.success-score {
  font-size: 2rem;
  font-weight: 700;
  color: #3b82f6;
  margin: 1rem 0;
}

.success-feedback {
  color: #4a5568;
  line-height: 1.8;
  margin: 1rem 0;
}

/* Responsive Design */
@media (max-width: 768px) {
  .header {
    padding: 1rem;
  }

  .topic-title {
    font-size: 2rem;
  }

  .date-card {
    width: 100%;
    margin-top: 1rem;
  }

  .challenge-buttons {
    flex-direction: column;
  }

  .challenge-btn {
    width: 100%;
  }

  .success-message {
    width: 95%;
    margin: 0 1rem;
  }
}

/* Additional Utility Classes */
.table-cell {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.dialog-footer {
  margin-top: 1.5rem;
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
}
</style>