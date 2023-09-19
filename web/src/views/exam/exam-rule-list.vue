<template>
  <div class="app-container">
    <el-form ref="examRuleSearchForm" v-model="examRuleParams" inline>
      <el-form-item>
        <el-button type="primary" icon="el-icon-refresh" size="mini"
                   @click="onExamRuleSearch">{{ $t('刷新') }}
        </el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" style="margin-bottom: 1rem">
      <el-col :span="2">
        <el-button v-permission="examRulePermission.create"
                   type="primary" icon="el-icon-plus" plain
                   @click="onExamRuleCreate">{{ $t('创建') }}
        </el-button>
      </el-col>
    </el-row>

    <el-table v-loading="examRuleLoading" :data="examRuleList" row-key="id" size="mini"
              stripe border highlight-current-row>
      <el-table-column prop="id" label="#" min-width="20" :align="'right'"/>
      <el-table-column prop="title" :label="$t('标题')" min-width="100" show-overflow-tooltip>
        <template v-slot="scope">
          <el-button size="mini" type="text" @click="onExamRuleEdit(scope, true)">
            {{ scope.row.title }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="duration" :label="$t('时长')" min-width="100" :align="'center'"/>
      <el-table-column prop="totalScore" :label="$t('分数')" min-width="50" :align="'center'"/>
      <el-table-column prop="passLine" :label="$t('合格线')" min-width="40" :align="'center'"/>
      <el-table-column prop="passScore" :label="$t('合格分数')" min-width="40" :align="'center'"/>
      <el-table-column prop="strategy" :label="$t('策略')" min-width="40" :align="'center'"/>
      <el-table-column prop="createdBy" :label="$t('创建人')" min-width="40" :align="'center'"/>
      <el-table-column prop="created" :label="$t('创建时间')" min-width="80" :align="'center'"/>
      <el-table-column prop="updatedBy" :label="$t('更新人')" min-width="40" :align="'center'"/>
      <el-table-column prop="updated" :label="$t('更新时间')" min-width="80" :align="'center'"/>
      <el-table-column :label="$t('操作')" min-width="100" :align="'center'">
        <template v-slot="scope">
          <el-button v-permission="examRulePermission.edit"
                     size="mini" icon="el-icon-edit" type="text"
                     @click="onExamRuleEdit(scope, false)">{{ $t('编辑') }}
          </el-button>
          <el-popconfirm style="margin-left: 10px" :title="$t('确定删除吗？')"
                         @onConfirm="onExamRuleDelete(scope)">
            <el-button slot="reference" v-permission="examRulePermission.delete"
                       size="mini" icon="el-icon-delete" type="text">{{ $t('删除') }}
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="examRuleList.length" :total="examRulePage.totalElements"
                :page.sync="examRuleParams.page" :size.sync="examRuleParams.size"
                @pagination="findExamRuleList"/>

    <el-dialog v-el-drag-dialog :title="examRuleTitle" :visible.sync="examRuleVisible"
               :close-on-click-modal="!examRuleFormEditable"
               append-to-body @click="onExamRuleClose">
      <el-form ref="examRuleForm" :model="examRuleForm" :rules="examRuleRules"
               :disabled="!examRuleFormEditable" label-width="80px">
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('标题')" prop="title">
              <el-input v-model="examRuleForm.title"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('时长')" prop="duration">
              <el-select v-model="examRuleForm.duration">
                <el-option v-for="item in durationOptions" :key="item.value"
                           :label="item.label" :value="item.value"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('总分数')" prop="totalScore">
              <el-input-number v-model="examRuleForm.totalScore" :min="1"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('合格线')" prop="passLine">
              <el-input-number v-model="examRuleForm.passLine" :precision="2"
                               :min="0" :step="0.01" :max="1" @change="onPassLineChange"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('合格分数')" prop="passScore">
              <el-input-number v-model="examRuleForm.passScore" :min="1" @change="onPassScoreChange"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('策略')" prop="strategy">
              <el-select v-model="examRuleForm.strategy">
                <el-option v-for="item in examStrategyOptions" :key="item.value"
                           :label="item.label" :value="item.value"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider v-if="!examRuleFormCreate" content-position="left">{{ $t('考试模式') }}</el-divider>
        <el-row v-if="!examRuleFormCreate" :gutter="10" style="margin-bottom: 1rem">
          <el-col :span="2">
            <el-button v-permission="examModePermission.create" type="primary" icon="el-icon-plus"
                       plain @click="onExamModeCreate">{{ $t('创建') }}
            </el-button>
          </el-col>
        </el-row>

        <el-table v-if="!examRuleFormCreate" :data="examModeList" row-key="id" stripe border>
          <el-table-column prop="id" label="#" min-width="20" :align="'right'"/>
          <el-table-column prop="ordered" :label="$t('排序')" min-width="100" show-overflow-tooltip/>
          <el-table-column prop="type" :label="$t('题型')" min-width="50" show-overflow-tooltip/>
          <el-table-column prop="score" :label="$t('分值')" min-width="40" :align="'center'"/>
          <el-table-column prop="amount" :label="$t('题量')" min-width="40" :align="'center'"/>
          <el-table-column prop="question1" :label="$t('试题一')" min-width="40" :align="'center'"/>
          <el-table-column prop="question2" :label="$t('试题二')" min-width="40" :align="'center'"/>
          <el-table-column prop="question3" :label="$t('试题三')" min-width="40" :align="'center'"/>
          <el-table-column prop="question4" :label="$t('试题四')" min-width="40" :align="'center'"/>
          <el-table-column prop="question5" :label="$t('试题五')" min-width="40" :align="'center'"/>
          <el-table-column :label="$t('操作')" min-width="100" :align="'center'">
            <template v-slot="scope">
              <el-button v-permission="examModePermission.edit"
                         size="mini" icon="el-icon-edit" type="text"
                         @click="onExamModeEdit(scope)">{{ $t('编辑') }}
              </el-button>
              <el-popconfirm style="margin-left: 10px" :title="$t('确定删除吗？')"
                             @onConfirm="onExamModeDelete(scope)">
                <el-button slot="reference" v-permission="examModePermission.delete"
                           size="mini" icon="el-icon-circle-close" type="text">{{ $t('删除') }}
                </el-button>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
      </el-form>

      <div slot="footer">
        <el-button v-if="examRuleFormEditable" type="primary" @click="onExamRuleSubmit">{{ $t('提交') }}</el-button>
        <el-button v-if="examRuleFormEditable" @click="examRuleVisible = false">{{ $t('取消') }}</el-button>
        <el-button v-if="!examRuleFormEditable" @click="examRuleVisible = false">{{ $t('关闭') }}</el-button>
      </div>
    </el-dialog>

    <el-dialog v-el-drag-dialog :title="examModeTitle" :visible.sync="examModeVisible"
               :close-on-click-modal="false"
               append-to-body @click="onExamModeClose">
      <el-form ref="examModeForm" :model="examModeForm" :rules="examModeRules" label-width="80px">
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('排序')" prop="ordered">
              <el-input-number v-model="examModeForm.ordered"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('题型')" prop="type">
              <el-select v-model="examModeForm.type">
                <el-option v-for="item in examTypeOptions" :key="item.value"
                           :label="item.label" :value="clearTemplate(item._links.self.href)"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('分值')" prop="score">
              <el-input-number v-model="examModeForm.score"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('题量')" prop="amount">
              <el-input-number v-model="examModeForm.amount" :min="1"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('问题一')" prop="question1">
              <el-select v-model="examModeForm.question1"
                         :disabled="examRuleForm.strategy === 'FIXED'">
                <el-option v-for="item in examQuestionOptions" :key="item.value"
                           :label="item.label" :value="item.value"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('问题二')" prop="question2">
              <el-select v-model="examModeForm.question2"
                         :disabled="examRuleForm.strategy === 'FIXED'">
                <el-option v-for="item in examQuestionOptions" :key="item.value"
                           :label="item.label" :value="item.value"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('问题三')" prop="question3">
              <el-select v-model="examModeForm.question3"
                         :disabled="examRuleForm.strategy === 'FIXED'">
                <el-option v-for="item in examQuestionOptions" :key="item.value"
                           :label="item.label" :value="item.value"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('问题四')" prop="question4">
              <el-select v-model="examModeForm.question4"
                         :disabled="examRuleForm.strategy === 'FIXED'">
                <el-option v-for="item in examQuestionOptions" :key="item.value"
                           :label="item.label" :value="item.value"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('问题五')" prop="question5">
              <el-select v-model="examModeForm.question5"
                         :disabled="examRuleForm.strategy === 'FIXED'">
                <el-option v-for="item in examQuestionOptions" :key="item.value"
                           :label="item.label" :value="item.value"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button type="primary" @click="onExamModeSubmit">{{ $t('提交') }}</el-button>
        <el-button @click="examModeVisible = false">{{ $t('取消') }}</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {DICT_CODES, searchDict} from '@/api/dict';
import {deleteExamMode, findExamMode} from '@/api/examMode';
import {searchExamQuestion} from '@/api/examQuestion';
import {createExamRule, deleteExamRule, editExamRule, findExamRule, pageExamRule} from '@/api/examRule';
import {clearTemplate} from '@/api/rest';
import Pagination from '@/components/Pagination';
import elDragDialog from '@/directive/el-drag-dialog';
import {PERMISSION_MARK} from '@/utils/permission';

export default {
  name: 'ExamRuleList',
  directives: {elDragDialog},
  components: {Pagination},
  data() {
    return {
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
      examStrategyOptions: [],
      examTypeOptions: [],
      examQuestionOptions: [],
      examRuleParams: {
        page: 0,
        size: 20,
      },
      examRulePermission: {...PERMISSION_MARK.examRule},
      examModePermission: {...PERMISSION_MARK.examMode},
      examRuleLoading: false,
      examRuleList: [],
      examModeList: [],
      examRulePage: {totalElements: 0, totalPages: 0},
      examRuleTitle: '',
      examModeTitle: '',
      examRuleVisible: false,
      examModeVisible: false,
      examRuleFormEditable: false,
      examRuleFormCreate: true,
      examRuleForm: {
        id: null,
        duration: 60 * 60,
        totalScore: 100,
        passLine: 0.6,
        passScore: 100 * 0.6,
        strategy: 'RANDOM',
      },
      examModeForm: {
        id: null,
        ordered: 1,
        type: '',
        score: 0,
        amount: 0,
        question1: '',
        question2: '',
        question3: '',
        question4: '',
        question5: '',
      },
      examRuleRules: {
        title: [{require: true, message: '请输入标题', trigger: 'blur'}],
        duration: [{require: true, message: '请选择时长', trigger: 'blur'}],
        totalScore: [{require: true, message: '请输入总分数', trigger: 'blur'}],
        passLine: [{require: true, message: '请输入合格线', trigger: 'blur'}],
        passScore: [{require: true, message: '请输入合格分数', trigger: 'blur'}],
        strategy: [{require: true, message: '请选择策略', trigger: 'blur'}],
      },
      examModeRules: {
        ordered: [{require: true, message: '请输入排序', trigger: 'blur'}],
        type: [{require: true, message: '请选择题型', trigger: 'blur'}],
        score: [{require: true, message: '请输入分值', trigger: 'blur'}],
        amount: [{require: true, message: '请输入题量', trigger: 'blur'}],
      },
    };
  },
  created() {
    this.findExamRuleList();
    this.findExamStrategyDict();
  },
  methods: {
    clearTemplate,
    findExamRuleList() {
      this.examRuleLoading = true;
      // 如果需要搜索，才替换为 searchExamRule 并提供搜索路径
      pageExamRule(this.examRuleParams).then(response => {
        this.examRuleList = response._embedded.examRules;
        this.examRulePage = response.page;
        this.examRuleLoading = false;
      });
    },
    findExamStrategyDict() {
      const params = {code: DICT_CODES.examStrategy, projection: 'dict-item-option'};
      searchDict('code', params).then(response => {
        this.examStrategyOptions = response._embedded.items;
      });
    },
    findExamTypeDict() {
      const params = {code: DICT_CODES.examType, projection: 'dict-item-option'};
      searchDict('code', params).then(response => {
        this.examTypeOptions = response._embedded.items;
      });
    },
    findExamQuestionOptions() {
      const params = {type: this.examModeForm.type, projection: 'exam-question-option'};
      searchExamQuestion('list', params).then(response => {
        this.examQuestionOptions = response._embedded.examQuestions;
      });
    },
    onExamRuleSearch() {
      this.examRuleParams.page = 0;
      this.findExamRuleList();
    },
    resetExamRuleForm() {
      if (this.$refs.examRuleForm) {
        this.$refs.examRuleForm.resetFields();
      }
      this.examRuleForm = {
        id: null,
        duration: 60 * 60,
        totalScore: 100,
        passLine: 0.6,
        passScore: 100 * 0.6,
        strategy: 'RANDOM',
      };
    },
    onExamRuleCreate() {
      this.resetExamRuleForm();
      this.examRuleFormEditable = true;
      this.examRuleFormCreate = true;
      this.examRuleTitle = '创建考试规则';
      this.examRuleVisible = true;
    },
    fillExamRuleForm(form) {
      this.examRuleForm = form;
    },
    onExamRuleEdit({row}, readonly = false) {
      this.resetExamRuleForm();
      this.examRuleFormEditable = true;
      this.examRuleFormCreate = false;
      findExamRule(row.id, 'exam-rule-form').then(response => {
        this.fillExamRuleForm(response);
        this.examRuleTitle = '编辑考试规则';
        this.examRuleVisible = true;
      });
    },
    onExamRuleDelete({row}) {
      if (row && row.id) {
        deleteExamRule(row.id).then(() => {
          this.$message.success(`考试规则 [${row.id}] 删除成功！`);
          this.findExamRuleList();
        });
      }
    },
    onExamRuleClose() {
      // do something
    },
    onPassLineChange(value) {
      this.examRuleForm.passScore = value * this.examRuleForm.totalScore;
    },
    onPassScoreChange(value) {
      this.examRuleForm.passLine = this.examRuleForm.totalScore / value;
    },
    onExamRuleSubmit() {
      this.$refs.examRuleForm.validate(valid => {
        if (valid) {
          const data = {
            duration: this.examRuleForm.duration,
            totalScore: this.examRuleForm.totalScore,
            passLine: this.examRuleForm.passLine,
            passScore: this.examRuleForm.passScore
          };
          if (this.examRuleForm.id) {
            editExamRule(this.examRuleForm.id, data).then(() => {
              this.$message.success(`考试规则[${this.examRuleForm.id}]更新成功！`);
              this.examRuleVisible = false;
              this.findExamRuleList();
            });
          } else {
            createExamRule(data).then(() => {
              this.$message.success(`考试规则创建成功！`);
              this.examRuleVisible = false;
              this.findExamRuleList();
            });
          }
        }
      });
    },
    resetExamModeForm() {
      if (this.$refs.examModeForm) {
        this.$refs.examModeForm.resetFields();
      }
      this.examModeForm = {
        id: null,
        ordered: 1,
        type: '',
        score: 0,
        amount: 0,
        question1: '',
        question2: '',
        question3: '',
        question4: '',
        question5: '',
      };
    },
    onExamModeCreate() {
      this.resetExamModeForm();
      this.findExamTypeDict();
      this.findExamQuestionOptions();
      this.examModeTitle = '创建考试模式';
      this.examModeVisible = true;
    },
    fillExamModeForm(form) {
      this.examModeForm = form;
      if (this.examRuleForm.strategy === 'FIXED') {
        this.findExamQuestionOptions();
      }
    },
    onExamModeEdit({row}) {
      this.resetExamModeForm();
      findExamMode(row.id, 'exam-mode-form').then(response => {
        this.fillExamModeForm(response);
        this.examModeTitle = '编辑考试模式';
        this.examModeVisible = true;
      });
    },
    reloadExamRuleForm() {
      if (this.examRuleForm.id) {
        findExamRule(this.examRuleForm.id, 'exam-rule-form').then(response => {
          this.fillExamRuleForm(response);
        });
      }
    },
    onExamModeDelete({row}) {
      if (row && row.id) {
        deleteExamMode(row.id).then(() => {
          this.$message.success(`考试模式 [${row.id}] 删除成功！`);
          this.reloadExamRuleForm();
        });
      }
    },
    onExamModeClose() {
      // do something
    },
    onExamModeSubmit() {
      // do something
    },
  }
};
</script>

<style scoped>
.el-rate {
  line-height: 2;
}
</style>
