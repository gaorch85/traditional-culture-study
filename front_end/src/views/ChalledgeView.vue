<template>
  <div class="challedge">
    <div class="date">
      <div class="year">{{ currentYear }}/{{ currentMonth }}</div>
      <div class="day">{{ currentDate }}</div>
    </div>
    <div class="topic">
      <div class="today_topic_1">今日主题</div>
      <div class="today_topic_2">{{ today_topic }}</div>
      <div class="introduction">{{ today_introduction }}</div>
    </div>
    <div class="step">
      <el-steps style="max-width: 600px" :active="active" finish-status="success">
        <el-step title="简单" :description="try_simple" />
        <el-step title="中等" :description="try_mid" />
        <el-step title="困难" :description="try_hard" />
      </el-steps>
    </div>
    <div class="challedge_button">
      <el-button class="cb" style="margin-top: 12px" @click="onCilckSimple">挑战简单</el-button>
      <el-button class="cb" style="margin-top: 12px" @click="onCilckMid" :disabled="isMId">挑战中等</el-button>
      <el-button style="margin-top: 12px" @click="onCilckHard" :disabled="isHard">挑战困难</el-button>
    </div>
    <div class="question_area">
      <el-dialog v-model="dialogFormVisible_simple" title="每日问答--简单" width="500">
        <div class="question"><el-text>{{ simple_question }}</el-text></div>
        <el-form :model="form" :rules="rules" ref="formRef">
          <el-form-item label="作答区" :label-width="formLabelWidth" prop="answer">
            <el-input v-model="form.answer" style="width: 400px" :rows="5" type="textarea" placeholder="输入你的答案..." />
          </el-form-item>
          <el-form-item>
            <el-button @click="dialogFormVisible_simple = false">取消</el-button>
            <el-button type="primary" @click="onsubmit(1)">
              提交
            </el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
    </div>
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

    <div v-if="successVisible" class="success-message" :style="{ '--success_message_color': success_message_color }">
      <div class="success-message_close"><el-icon @click="successVisible = false;">
          <CloseBold />
        </el-icon></div>
      {{ suggestion_before }}
      <div><span>你的分数：</span><span>{{ score }}</span></div>
      <div>{{ suggestion }}</div>
    </div>

    <div class="history">
      <el-table :data="tableData" height="200px" style="width: 100% ">
        <el-table-column prop="time" label="尝试时间" width="180">
          <template #default="scope">
            <div style="display: flex; align-items: center">
              <el-icon>
                <timer />
              </el-icon>
              <span style="margin-left: 10px">{{ scope.row.time }}</span>
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
            <el-button size="small" @click="onCilckHistory(scope.row)">
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

const isMId = computed(() => active.value < 1);
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

const success_message_color = computed(() => {
  if (score.value >= 80) return "#4CAF50";
  else if (score.value >= 60) return "#409EFF";
  else return "#909399";
});

const suggestion_before = computed(() => {
  if (score.value >= 80) return "优秀回答！恭喜你！ 🎉";
  else if (score.value >= 60) return "答题通过！恭喜你！ 🎉";
  else return "未通过😣，再试一次吧";
});

const try_simple = computed(() => "尝试次数：" + try_times_simple.value);
const try_mid = computed(() => "尝试次数：" + try_times_mid.value);
const try_hard = computed(() => "尝试次数：" + try_times_hard.value);

const onCilckSimple = () => {
  dialogFormVisible_simple.value = true;
  current_select.value = 1;
};

const onCilckMid = () => {
  dialogFormVisible_mid.value = true;
  current_select.value = 2;
};

const onCilckHard = () => {
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
      suggestion.value = response.data.data.reason;
      successVisible.value = true;
      
      // Update try times
      updateTryTimes(level);
      
      // Refresh history
      await fetchHistory();
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

const onCilckHistory = (row) => {
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
.challedge{
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.date {
  background-image: url('@/assets/img/date_background.png');
  /* 替换为您的背景图片URL */
  background-size: cover;
  /* 确保背景图片覆盖整个div */
  background-position: right top;
  /* 将背景图位置设置为右上角 */
  height: 150px;
  /* 根据需要设置高度 */
  width: 150px;
  /* 根据需要设置宽度 */

  padding: 10px;
  /* 给内容添加一些内边距 */
  position: absolute;
  /* 使得绝对定位的子元素相对于此元素定位 */
  left: 82%;

}

.year {
  font-size: xx-large;
  margin-top: 20px;
  margin-left: 7px;
  color: black;
}

.day {
  font-size: 60px;
  color: black;
  /* 设置文本颜色，以便在背景上可见 */
  margin-left: 27px;
}

.topic {
  background-image: url('@/assets/img/topic.png');
  /* 图片原始大小 423*796，比例：0.53 */
  background-size: contain;
  /* 确保背景图片覆盖整个div */
  background-position: center;
  background-repeat: no-repeat;
  width: 230px;
  height: 433px;
  display: flex;
  /* 使用 flexbox 布局 */
  flex-direction: column;
  /* 垂直排列子元素 */
  align-items: center;
  /* 水平居中对齐子元素 */
  position: relative;
}

.today_topic_1 {
  margin-top: 7px;
  font-size: x-large;
}

.today_topic_2 {
  font-size: 50px;
  margin-bottom: 10px;
}

.introduction {
  width: 80%;
}

.step {
  height: 1px;
  width: 540px;
  position: relative;
  left: 330px;
  bottom: 400px;
}

.challedge_button {
  height: 1px;
  width: 600px;
  position: relative;
  left: 300px;
  bottom: 300px;
}

.cb {
  margin-right: 140px;
}

.question {
  margin: 15px;
}

.success-message {
  animation: fadeIn 0.5s ease-in-out;
  position: absolute;
  left: 43%;
  top: 43%;
  padding: 20px;
  background-color: var(--success_message_color);
  color: white;
  border-radius: 5px;
  z-index: 100;
}

.success-message_close {
  position: relative;
  left: 90%;

}

.history {
  height: 2px;
  position: relative;
  width: 620px;
  left: 300px;
  bottom: 217px;
}

.history_time {
  margin-bottom: 5px;
}

.history_level {
  margin-bottom: 5px;
}

.history_grade {
  margin-bottom: 5px;
}

.history_answer {
  margin-bottom: 5px;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: scale(0.8);
  }

  to {
    opacity: 1;
    transform: scale(1);
  }
}
</style>