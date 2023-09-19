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

    <el-row :gutter="10" style="margin-bottom: 1rem">
      <el-col :span="2">
        <el-button v-permission="examPermission.create"
                   type="primary" icon="el-icon-plus" plain @click="onExamCreate">
          {{ $t('创建考试') }}
        </el-button>
      </el-col>
    </el-row>

    <el-table v-loading="examLoading" :data="examList" row-key="id" size="mini"
              stripe border highlight-current-row>
      <el-table-column prop="id" label="#" min-width="20" :align="'right'"/>
      <el-table-column prop="title" :label="$t('标题')" min-width="100" show-overflow-tooltip/>
      <el-table-column prop="code" :label="$t('编码')" min-width="50" show-overflow-tooltip/>
      <el-table-column prop="subjectLabel" :label="$t('科目')" min-width="40" :align="'center'"/>
      <el-table-column prop="difficulty" :label="$t('难度')" min-width="40" :align="'center'">
        <template v-slot="scope">
          <el-rate v-model="scope.row.difficulty" :texts="difficultyTexts" show-text/>
        </template>
      </el-table-column>
      <el-table-column prop="status" :label="$t('状态')" min-width="40" :align="'center'"/>
      <el-table-column prop="startTime" :label="$t('开始时间')" min-width="80" :align="'center'"/>
      <el-table-column prop="endTime" :label="$t('结束时间')" min-width="80" :align="'center'"/>
      <el-table-column prop="createdBy" :label="$t('创建人')" min-width="40" :align="'center'"/>
      <el-table-column prop="created" :label="$t('创建时间')" min-width="80" :align="'center'"/>
      <el-table-column prop="updatedBy" :label="$t('更新人')" min-width="40" :align="'center'"/>
      <el-table-column prop="updated" :label="$t('更新时间')" min-width="80" :align="'center'"/>
      <el-table-column :label="$t('操作')" min-width="100" :align="'center'">
        <template v-slot="scope">
          <el-button v-permission="examPermission.edit"
                     size="mini" icon="el-icon-edit" type="text"
                     @click="onExamEdit(scope)">{{ $t('编辑') }}
          </el-button>
          <el-popconfirm style="margin-left: 10px" :title="$t('确定删除吗？')"
                         @onConfirm="onExamDelete(scope)">
            <el-button slot="reference" v-permission="examPermission.delete"
                       size="mini" icon="el-icon-circle-close" type="text">{{ $t('删除') }}
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="examList.length" :total="examPage.totalElements"
                :page.sync="examParams.page" :size.sync="examParams.size"
                @pagination="findExamList"/>

    <el-dialog v-el-drag-dialog :title="examTitle" :visible.sync="examVisible"
               :close-on-click-modal="!examFormEditable" append-to-body @click="onExamClose">
      <el-form ref="examForm" :model="examForm" :rules="examRules"
               :disabled="!examFormEditable" label-width="80px">
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('标题')" prop="title">
              <el-input v-model="examForm.title"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('科目')" prop="subject">
              <el-select v-model="examForm.subject">
                <el-option v-for="item in subjectOptions"
                           :key="item.value" :label="item.label" :value="clearTemplate(item._links.self.href)"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('难度')" prop="difficulty">
              <el-rate v-model="examForm.difficulty" :texts="difficultyTexts" show-text/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('时间')" prop="startTime">
              <el-date-picker v-model="examForm.startTime" type="datetime" :value-format="momentDateTimeFormat"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('规则')" prop="rule">
              <el-select v-model="examForm.rule">
                <el-option v-for="item in ruleOptions"
                           :key="item.value" :label="item.label" :value="clearTemplate(item._links.self.href)"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="24">
            <el-form-item :label="$t('描述')" prop="description">
              <el-input v-model="examForm.description" type="textarea" rows="3"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button v-if="examFormEditable" type="primary" @click="onExamSubmit">{{ $t('提交') }}</el-button>
        <el-button v-if="examFormEditable" @click="examVisible = false">{{ $t('取消') }}</el-button>
        <el-button v-if="!examFormEditable" @click="examVisible = false">{{ $t('关闭') }}</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {DICT_CODES, searchDict} from '@/api/dict';
import {createExam, deleteExam, editExam, findExam, searchExam} from '@/api/exam';
import {searchExamRule} from '@/api/examRule';
import {clearTemplate} from '@/api/rest';
import Pagination from '@/components/Pagination';
import elDragDialog from '@/directive/el-drag-dialog';
import {dateTimeFormat, momentDateTimeFormat} from '@/utils/common.js';
import {PERMISSION_MARK} from '@/utils/permission';
import moment from 'moment';

export default {
  name: 'ExamList',
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
      pickerOptions: {
        shortcuts: [{
          text: '一周后',
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
      examTitle: '',
      examVisible: false,
      examFormEditable: false,
      examForm: {
        id: null,
        title: '',
        rule: '',
        code: '',
        subject: '',
        difficulty: 3,
        startTime: null,
        description: ''
      },
      examRules: {
        title: [{required: true, message: '考试标题不能为空', trigger: 'blur'}],
        subject: [{required: true, message: '考试科目不能为空', trigger: 'blur'}],
        startTime: [{required: true, message: '考试时间不能为空', trigger: 'blur'}],
        rule: [{required: true, message: '考试规则不能为空', trigger: 'blur'}],
      },
      subjectOptions: [],
      ruleOptions: [],
    };
  },
  created() {
    this.findExamSubjectDict();
    this.findExamRuleList();
    this.findExamList();
  },
  methods: {
    clearTemplate,
    findExamSubjectDict() {
      const params = {code: DICT_CODES.examSubject, projection: 'dict-item-option'};
      searchDict('code', params).then(response => {
        this.subjectOptions = response._embedded.items;
      });
    },
    findExamRuleList() {
      const params = {projection: 'exam-rule-option'};
      searchExamRule('list', params).then(response => {
        this.ruleOptions = response._embedded.examRules;
      });
    },
    findExamList() {
      this.examLoading = true;
      searchExam('page', this.examParams).then(response => {
        this.examList = response._embedded.exams;
        this.examPage = response.page;
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
    onExamCreate() {
      this.resetExamForm();
      this.examTitle = '创建考试';
      this.examVisible = true;
    },
    resetExamForm() {
      if (this.$refs.examForm) {
        this.$refs.examForm.resetFields();
      }
      this.examForm = {
        id: null,
        title: '',
        rule: '',
        code: '',
        subject: '',
        difficulty: 3,
        startTime: null,
        description: ''
      };
    },
    fillExamForm(form) {
      this.examForm = form;
    },
    onExamEdit({row}) {
      this.resetExamForm();
      findExam(row.id, 'exam-form').then(response => {
        this.fillExamForm(response);
        this.examForm.id = row.id;
        this.examTitle = '编辑考试';
        this.examVisible = true;
      });
    },
    onExamDelete({row}) {
      if (row && row.id) {
        deleteExam(row.id).then(() => {
          const message = `考试 [${this.examForm.title}] 删除成功！`;
          this.$message.success(message);
          this.findExamList();
        });
      }
    },
    onExamClose() {
      // do something
    },
    onExamSubmit() {
      this.$refs.examForm.validate(valid => {
        if (valid) {
          const data = {
            title: this.examForm.title,
            subject: this.examForm.subject,
            difficulty: this.examForm.difficulty,
            startTime: this.examForm.startTime,
            rule: this.examForm.rule,
            description: this.examForm.description,
          };
          if (this.examForm.id) {
            editExam(this.examForm.id, data).then(() => {
              this.$message.success(`考试 [${data.title}] 更新成功！`);
              this.examVisible = false;
              this.findExamList();
            });
          } else {
            createExam(data).then(() => {
              this.$message.success(`考试 [${data.title}] 创建成功！`);
              this.examVisible = false;
              this.findExamList();
            });
          }
        }
      });
    },
  }
};
</script>

<style scoped>
.el-rate {
  line-height: 2;
}
</style>
