<template>
  <div v-loading="loading" class="app-container">
    <el-result v-if="examStatus === 'MARKING'"
               :icon="examStart.icon" :title="examStart.title" :sub-title="examStart.subTitle">
      <template slot="extra">
        <el-button v-permission="paperPermission.marker" type="primary"
                   @click="onExamMarkStart">开始阅卷
        </el-button>
      </template>
    </el-result>
    <el-result v-if="examStatus === 'FINISHED'"
               :icon="examResult.icon" :title="examResult.title" :sub-title="examResult.subTitle">
      <template slot="extra">
        <el-button v-permission="paperPermission.marker" type="primary"
                   @click="onExamResult">查看结果
        </el-button>
      </template>
    </el-result>

    <transition name="fade-transform" mode="out-in">
      <div v-if="['MARKER_PAPER','PAPER_RESULT'].includes(examStatus)" style="width: 100%">
        <sticky>
          <el-card>
            <el-row type="flex" :align="'middle'">
              <el-col :span="examStatus === 'MARKER_PAPER' ? 20 : 24">
                <el-descriptions border class="hidden-lg-and-down">
                  <el-descriptions-item :label="$t('标题')">{{ examInfo.title }}</el-descriptions-item>
                  <el-descriptions-item :label="$t('科目')">{{ examInfo.subjectLabel }}</el-descriptions-item>
                  <el-descriptions-item :label="$t('难度')">
                    <el-rate v-model="examInfo.difficulty" :texts="difficultyTexts" show-text/>
                  </el-descriptions-item>
                  <el-descriptions-item :label="$t('时长')">
                    {{ displayDuration(examInfo.rule.duration) }}
                  </el-descriptions-item>
                  <el-descriptions-item :label="$t('总分数')">{{ examInfo.rule.totalScore }}</el-descriptions-item>
                  <el-descriptions-item :label="$t('合格分数')">{{ examInfo.rule.passScore }}</el-descriptions-item>
                  <el-descriptions-item :label="$t('开始时间')">{{ examInfo.startTime }}</el-descriptions-item>
                  <el-descriptions-item :label="$t('结束时间')">{{ examInfo.endTime }}</el-descriptions-item>
                  <el-descriptions-item :label="$t('描述')">{{ examInfo.description }}</el-descriptions-item>
                </el-descriptions>
              </el-col>
              <el-col v-if="examStatus === 'MARKER_PAPER'" :span="4" class="text-center">
                <el-button type="primary" icon="el-icon-caret-right"
                           @click="onExamMarkSubmit">{{ $t('完成批阅') }}
                </el-button>
              </el-col>
            </el-row>
          </el-card>
        </sticky>
        <el-row :gutter="24" style="margin: 16px 0">
          <el-col :span="6" :xs="24">
            <el-card>
              <div slot="header" class="text-center">
                <span style="font-weight: bold">答题卡</span>
              </div>
              <el-row :gutter="24" class="exam-card-tag text-center">
                <el-tag type="info">未作答</el-tag>
                <el-tag type="success">已作答</el-tag>
                <el-tag type="warning">当前题</el-tag>
                <el-tag v-if="examStatus === 'PAPER_RESULT'" type="danger">答错题</el-tag>
              </el-row>
              <el-row :gutter="24" class="exam-card-tag">
                <el-col v-for="card in examPaper.cards" :key="card.id" :span="24">
                  <p class="exam-card-title">
                    {{ card.title }}（共 {{ card.answers?.length }} 题，共 {{ card.totalScore }} 分）
                  </p>
                  <el-tag v-for="answer in card.answers" :key="answer.id" :type="examCardOptionStatus(answer)"
                          @click="onExamCardOption(card, answer)">{{ answer.ordered }}
                  </el-tag>
                </el-col>
              </el-row>

              <el-descriptions v-if="examStatus === 'PAPER_RESULT'" style="margin-top: 16px"
                               direction="vertical" border class="hidden-lg-and-down">
                <el-descriptions-item :label="$t('成绩')">{{ examPaper.result }}</el-descriptions-item>
              </el-descriptions>
            </el-card>
          </el-col>
          <el-col :span="18" :xs="24">
            <el-card v-if="paperAnswer && paperAnswer.question" class="question-content">
              <p>{{ paperAnswer.ordered }}.{{ paperAnswer.question.stem }}</p>
              <!-- 判断题、单选题 -->
              <div v-if="['LOGIC', 'SINGLE'].includes(paperAnswer.question.type)">
                <el-radio-group v-model="paperAnswer.selectOptionId">
                  <el-radio v-for="item in paperAnswer.question.options" :key="item.id" :label="item.id" disabled>
                    {{ item.label }}.{{ item.content }}
                  </el-radio>
                </el-radio-group>
              </div>
              <!-- 多选题 -->
              <div v-if="['MULTIPLE'].includes(paperAnswer.question.type)">
                <el-checkbox-group v-model="paperAnswer.chooseOptionIds">
                  <el-checkbox v-for="item in paperAnswer.question.options" :key="item.id" :label="item.id" disabled>
                    {{ item.label }}.{{ item.content }}
                  </el-checkbox>
                </el-checkbox-group>
              </div>
              <!-- 主观题 -->
              <div v-if="!['LOGIC', 'SINGLE', 'MULTIPLE'].includes(paperAnswer.question.type)">
                <!-- TODO 富文本编辑器，或者 markdown 编辑器 -->
                <el-input v-model="paperAnswer.answer" type="textarea" rows="5" disabled/>
              </div>

              <!-- 批阅相关 -->
              <el-descriptions direction="vertical" border class="hidden-lg-and-down">
                <el-descriptions-item :label="$t('参考答案')">
                  <el-tag type="success">{{ rightAnswer }}</el-tag>
                </el-descriptions-item>
                <el-descriptions-item :label="$t('考生答题')">
                  <el-tag :type="takeAnswerStatus">{{ takeAnswer }}</el-tag>
                </el-descriptions-item>
                <el-descriptions-item :label="$t('试题得分')">
                  <el-tag type="info">{{ answerScore }}</el-tag>
                </el-descriptions-item>
                <el-descriptions-item :label="$t('答案解析')">
                  <el-tag type="success">{{ answerExplained }}</el-tag>
                </el-descriptions-item>
              </el-descriptions>

              <div v-if="!['LOGIC', 'SINGLE', 'MULTIPLE'].includes(paperAnswer.question.type) && examStatus === 'MARKER_PAPER'"
                   style="margin: 8px 0">
                <p>阅卷分数：</p>
                <el-input-number v-model="paperAnswer.score" controls-position="right"
                                 min="0" :precision="2" :max="paperAnswer.questionScore"/>
                <p>阅卷批语：</p>
                <el-input v-model="paperAnswer.comment" type="textarea" rows="3"/>
              </div>

            </el-card>

            <div style="margin-top: 24px">
              <el-button :disabled="hasPreviousDisabled" type="primary" icon="el-icon-back" @click="onPrevious()">
                {{ $t('上一题') }}
              </el-button>
              <el-button :disabled="hasNextDisabled" type="warning" icon="el-icon-right" @click="onNext()">
                {{ $t('下一题') }}
              </el-button>
              <el-button v-if="hasNextDisabled && examStatus === 'MARKER_PAPER'"
                         type="primary" icon="el-icon-caret-right" @click="onExamMarkSubmit">
                {{ $t('完成批阅') }}
              </el-button>
            </div>
          </el-col>
        </el-row>
      </div>
    </transition>
  </div>
</template>

<script>
import {findExam, findMarkerPaper, finishExamPaper, saveExamPaperMarker} from '@/api/exam';
import Sticky from '@/components/Sticky';
import elDragDialog from '@/directive/el-drag-dialog';
import {PERMISSION_MARK} from '@/utils/permission';
import 'element-ui/lib/theme-chalk/display.css';

export default {
  name: 'ExamTakerPaper',
  directives: {elDragDialog},
  components: {Sticky},
  data() {
    return {
      difficultyTexts: [
        this.$t('exam.difficultyLv1'),
        this.$t('exam.difficultyLv2'),
        this.$t('exam.difficultyLv3'),
        this.$t('exam.difficultyLv4'),
        this.$t('exam.difficultyLv5')
      ],
      durationOptions: [
        {label: '15 分钟', value: 15 * 60},
        {label: '30 分钟', value: 30 * 60},
        {label: '45 分钟', value: 45 * 60},
        {label: '60 分钟', value: 60 * 60},
        {label: '90 分钟', value: 90 * 60},
        {label: '120 分钟', value: 120 * 60},
        {label: '150 分钟', value: 150 * 60},
        {label: '180 分钟', value: 180 * 60},
      ],
      loading: true,
      examId: null,
      examStatus: '',
      examInfo: {
        id: null,
        title: '',
        subjectLabel: '',
        code: '',
        difficulty: 3,
        rule: {
          duration: '',
          totalScore: '',
          passLine: '',
          passScore: ''
        },
        status: '',
        statusCode: '',
        startTime: '',
        endTime: '',
        description: ''
      },
      examStart: {
        icon: 'success',
        title: '试卷已提交',
        subTitle: '请点击下方按钮开始阅卷！'
      },
      examResult: {
        icon: 'info',
        title: '阅卷已完成',
        subTitle: '请点击下方按钮查看结果！'
      },
      paperPermission: {...PERMISSION_MARK.examPaper},
      examPaper: {},
      paperCard: {},
      paperAnswer: {
        selectOptionId: '',
        chooseOptionIds: [],
        answer: '',
        answerUrl: ''
      },
    };
  },
  computed: {
    rightAnswer: function () {
      const question = this.paperAnswer.question;
      if (question) {
        if (question.options && question.options.length) {
          return question.options.filter(it => it.righted).map(it => it.label).join(',');
        }
        if (question.solution) {
          return question.solution;
        }
        if (question.solutionUrl) {
          return question.solutionUrl;
        }
      }
      return '暂无';
    },
    answerExplained: function () {
      const question = this.paperAnswer.question;
      if (question) {
        if (question.explained) {
          return question.explained;
        }
        if (question.explainedUrl) {
          return question.explainedUrl;
        }
      }
      return '暂无';
    },
    takeAnswer: function () {
      if (this.paperAnswer.selectOption) {
        return this.paperAnswer.selectOption.label;
      }
      if (this.paperAnswer.chooseOptions && this.paperAnswer.chooseOptions.length) {
        return this.paperAnswer.chooseOptions.map(it => it.label).join(',');
      }
      return this.paperAnswer.answer ? this.paperAnswer.answer : this.paperAnswer.answerUrl;
    },
    takeAnswerStatus: function () {
      return this.paperAnswer.score > 0 ? 'success' : 'danger';
    },
    answerScore: function () {
      if (this.paperAnswer.score) {
        return this.paperAnswer.score.toFixed(2);
      }
      return '0';
    },
    hasPreviousDisabled: function () {
      const cards = this.examPaper.cards;
      for (let i = 0; i < cards.length; i++) {
        const card = cards[i];
        if (this.paperCard && card && this.paperCard.id === card.id) {
          const answers = card.answers;
          for (let j = 0; j < answers.length; j++) {
            const answer = answers[j];
            if (this.paperAnswer && answer && this.paperAnswer.id === answer.id) {
              return i <= 0 && j <= 0;
            }
          }
        }
      }
      return true;
    },
    hasNextDisabled: function () {
      const cards = this.examPaper.cards;
      for (let i = 0; i < cards.length; i++) {
        const card = cards[i];
        if (this.paperCard && card && this.paperCard.id === card.id) {
          const answers = card.answers;
          for (let j = 0; j < answers.length; j++) {
            const answer = answers[j];
            if (this.paperAnswer && answer && this.paperAnswer.id === answer.id) {
              // 当前不是最后一道大题，如果下一道大题没有小题，则禁用下一道题
              // 当前是最后一道大题，并且是大题中的最后一道小题，则禁用下一道题
              return (i < cards.length - 1 && (!cards[i + 1].answers || !cards[i + 1].answers.length)) ||
                  (i >= cards.length - 1 && j >= answers.length - 1);
            }
          }
        }
      }
      return true;
    },
  },
  created() {
    this.examId = this.$route.params.id;
    this.findExamInfo();
  },
  methods: {
    displayDuration(duration) {
      if (!duration) {
        return '';
      }
      return this.durationOptions.find(it => it.value === duration).label || '';
    },
    findExamInfo() {
      findExam(this.examId, 'exam-info').then(data => {
        this.examInfo = data;
        this.examStatus = this.examInfo.statusCode;
        this.loading = false;
      });
    },
    onExamMarkStart() {
      findMarkerPaper(this.examId).then(data => {
        this.examStatus = 'MARKER_PAPER';
        this.examPaper = data;
        if (data.cards && data.cards.length) {
          const card = data.cards[0];
          if (card && card.answers && card.answers.length) {
            this.paperCard = card;
            this.paperAnswer = card.answers[0];
          }
        }
      });
    },
    onExamResult() {
      findMarkerPaper(this.examId).then(data => {
        this.examStatus = 'PAPER_RESULT';
        this.examPaper = data;
        if (data.cards && data.cards.length) {
          const card = data.cards[0];
          if (card && card.answers && card.answers.length) {
            this.paperCard = card;
            this.paperAnswer = card.answers[0];
          }
        }
      });
    },
    onExamMarkSubmit() {
      finishExamPaper(this.examId, this.paperAnswer).then(() => {
        this.$message.success('您已完成当前试卷的批阅！');
        this.examStatus = 'FINISHED';
      });
    },
    examCardOptionStatus(answer) {
      if (this.examStatus === 'PAPER_RESULT') {
        return this.paperAnswer.id === answer.id ? 'warning' : (answer.score > 0 ? 'success' : 'danger');
      }
      return this.paperAnswer.id === answer.id ? 'warning' : (answer.hasAnswer ? 'success' : 'info');
    },
    onExamCardOption(card, answer) {
      this.savePaperMarker();
      this.paperCard = card;
      this.paperAnswer = answer;
    },
    savePaperMarker() {
      if (['LOGIC', 'SINGLE', 'MULTIPLE'].includes(this.paperAnswer.question.type) || this.examStatus !== 'MARKER_PAPER') {
        return;
      }
      saveExamPaperMarker(this.examId, this.paperAnswer).then(() => console.log('save paper marker success.'));
    },
    onPrevious() {
      this.savePaperMarker();
      let answers = this.paperCard.answers;
      for (let j = 0; j < answers.length; j++) {
        const answer = answers[j];
        if (this.paperAnswer && answer && this.paperAnswer.id === answer.id) {
          if (j > 0) {
            this.paperAnswer = answers[j - 1];
            return;
          }

          const cards = this.examPaper.cards;
          for (let i = 0; i < cards.length; i++) {
            const card = cards[i];
            if (this.paperCard && card && this.paperCard.id === card.id) {
              if (i > 0) {
                this.paperCard = cards[i - 1];
                answers = this.paperCard.answers;
                if (answers && answers.length) {
                  this.paperAnswer = answers[answer.length - 1];
                  return;
                }
              }
            }
          }
        }
      }
    },
    onNext() {
      this.savePaperMarker();
      let answers = this.paperCard.answers;
      for (let j = 0; answers && j < answers.length; j++) {
        const answer = answers[j];
        if (this.paperAnswer && answer && this.paperAnswer.id === answer.id) {
          if (j < answers.length - 1) {
            this.paperAnswer = answers[j + 1];
            return;
          }

          const cards = this.examPaper.cards;
          for (let i = 0; i < cards.length; i++) {
            const card = cards[i];
            if (this.paperCard && card && this.paperCard.id === card.id) {
              if (i < cards.length - 1) {
                this.paperCard = cards[i + 1];
                answers = this.paperCard.answers;
                if (answers && answers.length) {
                  this.paperAnswer = answers[0];
                  return;
                }
              }
            }
          }
        }
      }
    },
  }
};
</script>

<style scoped>
.el-rate {
  line-height: 2;
}

.el-rate-mini {
  line-height: 0;
}

.question-content {

}

.question-content div {
  line-height: 30px;
}

.el-checkbox-group label, .el-radio-group label {
  width: 100%;
}

.exam-card-title {
  background: #eee;
  line-height: 36px;
  text-align: left;
  font-size: 14px;
  padding-left: 8px;
  font-weight: bold;
}

.exam-card-tag {
  padding-left: 8px;
}

.exam-card-tag span {
  cursor: pointer;
  margin: 2px;
}

/deep/
.el-radio, .el-checkbox {
  padding: 8px 16px 8px 8px;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  margin-bottom: 8px;
}

.is-checked {
  border: #409eff 1px solid;
}

/deep/
.el-checkbox__inner {
  display: none;
}

/deep/
.el-radio__inner {
  display: none;
}

/deep/
.el-checkbox__label {
  line-height: 30px;
}

/deep/
.el-radio__label {
  line-height: 30px;
}

</style>
