<template>
  <div class="app-container">
    <el-form ref="examSearchForm" v-model="examParams" inline>
      <el-form-item :label="$t('标题')" prop="title">
        <el-input v-model="examParams.title" clearable/>
      </el-form-item>
      <el-form-item :label="$t('编码')" prop="code">
        <el-input v-model="examParams.code" clearable/>
      </el-form-item>
      <el-form-item :label="$t('考试时间')" prop="startTime">
        <el-date-picker v-model="examParams.firstStart" type="datetime"
                        :placeholder="$t('请输入起始时间')"
                        :picker-options="pickerOptions" :value-format="dateTimeFormat"/>
        <el-date-picker v-model="examParams.secondStart" type="datetime"
                        :placeholder="$t('请输入结束时间')"
                        :picker-options="pickerOptions" :value-format="dateTimeFormat"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="onExamSearch">{{ $t('搜索') }}</el-button>
        <el-button icon="el-icon-refresh" @click="onResetExamSearch">{{ $t('重置') }}</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="examLoading" :data="examList" row-key="id" size="mini"
              stripe border highlight-current-row>
      <el-table-column prop="id" label="#" min-width="20" :align="'right'"/>
      <el-table-column prop="title" :label="$t('标题')" min-width="100" show-overflow-tooltip>
        <template v-slot="scope">
          <el-button size="mini" type="text" @click="onExamInfo(scope)">
            {{ scope.row.title }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="code" :label="$t('编码')" min-width="100" show-overflow-tooltip/>
      <el-table-column prop="subjectLabel" :label="$t('科目')" min-width="40" :align="'center'"/>
      <el-table-column prop="difficulty" :label="$t('难度')" min-width="80" :align="'center'">
        <template v-slot="scope">
          <el-rate v-model="scope.row.difficulty" class="el-rate-mini" :texts="difficultyTexts" show-text disabled/>
        </template>
      </el-table-column>
      <el-table-column prop="status" :label="$t('状态')" min-width="40" :align="'center'"/>
      <el-table-column prop="startTime" :label="$t('开始时间')" min-width="80" :align="'center'"/>
      <el-table-column prop="endTime" :label="$t('结束时间')" min-width="80" :align="'center'"/>
      <el-table-column :label="$t('操作')" min-width="120" :align="'center'">
        <template v-slot="scope">
          <el-button v-permission="examPermission.markerStart"
                     size="mini" icon="el-icon-s-custom" type="success" plain
                     @click="onExamMarkerStart(scope)">{{ $t('开始阅卷') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="examList.length" :total="examPage.totalElements"
                :page.sync="examParams.page" :size.sync="examParams.size"
                @pagination="findExamList"/>

    <el-dialog v-el-drag-dialog :visible.sync="examVisible" :title="examTitle" append-to-body>
      <el-descriptions border>
        <el-descriptions-item :label="$t('标题')">{{ examInfo.title }}</el-descriptions-item>
        <el-descriptions-item :label="$t('科目')">{{ examInfo.subjectLabel }}</el-descriptions-item>
        <el-descriptions-item :label="$t('编码')">{{ examInfo.code }}</el-descriptions-item>
        <el-descriptions-item :label="$t('难度')">
          <el-rate v-model="examInfo.difficulty" :texts="difficultyTexts" show-text/>
        </el-descriptions-item>
        <el-descriptions-item :label="$t('时长')">{{ displayDuration(examInfo.rule.duration) }}</el-descriptions-item>
        <el-descriptions-item :label="$t('总分数')">{{ examInfo.rule.totalScore }}</el-descriptions-item>
        <el-descriptions-item :label="$t('合格线')">{{ displayPassLine(examInfo.rule.passLine) }}</el-descriptions-item>
        <el-descriptions-item :label="$t('合格分数')">{{ examInfo.rule.passScore }}</el-descriptions-item>
        <el-descriptions-item :label="$t('状态')">{{ examInfo.status }}</el-descriptions-item>
        <el-descriptions-item :label="$t('开始时间')">{{ examInfo.startTime }}</el-descriptions-item>
        <el-descriptions-item :label="$t('结束时间')">{{ examInfo.endTime }}</el-descriptions-item>
        <el-descriptions-item :label="$t('描述')">{{ examInfo.description }}</el-descriptions-item>
      </el-descriptions>

      <div v-if="examInfo.statusCode === 'WAITING' && examStartTime" class="exam-statistic">
        <el-statistic :value="examStartTime" time-indices title="考试已准备好，即将开始："/>
      </div>

      <div v-if="examInfo.statusCode === 'RUNNING' && examEndTime" class="exam-statistic">
        <el-statistic :value="examEndTime" time-indices title="考试正在进行，距离结束："/>
      </div>

      <div slot="footer">
        <el-button @click="examVisible = false">{{ $t('关闭') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {findExam, markerExam} from '@/api/exam';
import Pagination from '@/components/Pagination';
import elDragDialog from '@/directive/el-drag-dialog';
import {dateTimeFormat, momentDateTimeFormat} from '@/utils/common.js';
import {PERMISSION_MARK} from '@/utils/permission';
import moment from 'moment';

export default {
  name: 'MyExamList',
  directives: {elDragDialog},
  components: {Pagination},
  data() {
    return {
      momentDateTimeFormat,
      dateTimeFormat,
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
      pickerOptions: {
        shortcuts: [{
          text: '一周之后',
          onClick(picker) {
            const start = moment().add(7, 'd').format(momentDateTimeFormat);
            picker.$emit('pick', start);
          }
        }, {
          text: '一个月后',
          onClick(picker) {
            const start = moment().add(1, 'M').format(momentDateTimeFormat);
            picker.$emit('pick', start);
          }
        }, {
          text: '三个月后',
          onClick(picker) {
            const start = moment().add(3, 'M').format(momentDateTimeFormat);
            picker.$emit('pick', start);
          }
        }],
      },
      examParams: {
        title: '',
        code: '',
        firstStart: moment().startOf('M').format(momentDateTimeFormat),
        secondStart: moment().endOf('M').format(momentDateTimeFormat),
        page: 0,
        size: 20,
      },
      examPermission: {...PERMISSION_MARK.exam},
      examLoading: false,
      examList: [],
      examPage: {totalElements: 0, totalPages: 0},
      examTitle: '考试信息',
      examVisible: false,
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
    };
  },
  computed: {
    examStartTime: function () {
      if (this.examInfo.startTime) {
        return moment(this.examInfo.startTime).toDate();
      }
      return null;
    },
    examEndTime: function () {
      if (this.examInfo.endTime) {
        return moment(this.examInfo.endTime).toDate();
      }
      return null;
    },
  },
  created() {
    this.findExamList();
  },
  methods: {
    displayDuration(duration) {
      if (!duration) {
        return '';
      }
      return this.durationOptions.find(it => it.value === duration).label || '';
    },
    displayPassLine(passLine) {
      return (passLine * 100) + '%';
    },
    findExamList() {
      this.examLoading = true;
      markerExam(this.examParams).then(response => {
        this.examList = response.content;
        this.examPage.totalPages = response.totalPages;
        this.examPage.totalElements = response.totalElements;
        this.examLoading = false;
      });
    },
    onExamSearch() {
      this.examParams.page = 0;
      this.findExamList();
    },
    onResetExamSearch() {
      this.examParams = {
        title: '',
        code: '',
        firstStart: moment().startOf('M').format(momentDateTimeFormat),
        secondStart: moment().endOf('M').format(momentDateTimeFormat),
        page: 0,
        size: 20,
      };
      if (this.$refs.examSearchForm) {
        this.$refs.examSearchForm.resetFields();
      }
      this.findExamList();
    },
    onExamInfo({row}) {
      findExam(row.id, 'exam-info').then(data => {
        this.examInfo = data;
        this.examVisible = true;
      });
    },
    onExamMarkerStart({row}) {
      // FIXME 前期为了快速开发使用 id 参数，后续为避免猜到其他考试 id，应该使用 code 参数
      this.$router.push({name: 'ExamMarkerPaper', params: {id: row.id}});
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

.exam-statistic {
  width: 100%;
  text-align: center;
  margin-top: 16px;
}

</style>
