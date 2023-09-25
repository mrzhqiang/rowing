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
                   @click="onExamRuleCreate">{{ $t('创建规则') }}
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
      <el-table-column prop="duration" :label="$t('时长')" min-width="40" :align="'center'">
        <template v-slot="scope">
          {{ displayDuration(scope.row.duration) }}
        </template>
      </el-table-column>
      <el-table-column prop="totalScore" :label="$t('分数')" min-width="40" :align="'center'"/>
      <el-table-column prop="passLine" :label="$t('合格线(%)')" min-width="40" :align="'center'">
        <template v-slot="scope">
          {{ (scope.row.passLine * 100).toFixed(0) + '%' }}
        </template>
      </el-table-column>
      <el-table-column prop="passScore" :label="$t('合格分数')" min-width="40" :align="'center'"/>
      <el-table-column prop="strategy" :label="$t('策略')" min-width="40" :align="'center'"/>
      <el-table-column prop="createdBy" :label="$t('创建人')" min-width="40" :align="'center'"/>
      <el-table-column prop="created" :label="$t('创建时间')" min-width="80" :align="'center'"/>
      <el-table-column prop="updatedBy" :label="$t('更新人')" min-width="40" :align="'center'"/>
      <el-table-column prop="updated" :label="$t('更新时间')" min-width="80" :align="'center'"/>
      <el-table-column :label="$t('操作')" min-width="100" :align="'center'">
        <template v-slot="scope">
          <el-button v-permission="examRulePermission.edit"
                     size="mini" icon="el-icon-edit" type="success" plain
                     @click="onExamRuleEdit(scope)">{{ $t('编辑') }}
          </el-button>
          <el-popconfirm style="margin-left: 10px" :title="$t('确定删除吗？')"
                         @confirm="onExamRuleDelete(scope)">
            <el-button slot="reference" v-permission="examRulePermission.delete"
                       size="mini" icon="el-icon-delete" type="danger" plain>{{ $t('删除') }}
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
              <el-input-number v-model="examRuleForm.totalScore" :min="1" @change="onTotalScoreChange"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('合格线(%)')" prop="passLinePercent">
              <el-input-number v-model="examRuleForm.passLinePercent" :min="1" :max="100" @change="onPassLineChange"/>
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
                <el-option v-for="item in examModeStrategyDictItems" :key="item.value"
                           :label="item.label" :value="item.value"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider v-if="!examRuleFormCreate && examRuleFormEditable"
                    content-position="left">{{ $t('模式列表') }}
        </el-divider>
        <el-row v-if="!examRuleFormCreate && examRuleFormEditable" :gutter="10" style="margin-bottom: 1rem">
          <el-col :span="2">
            <el-button v-permission="examModePermission.create" type="primary" icon="el-icon-plus"
                       :disabled="isOverTotalScore"
                       plain @click="onExamModeCreate">{{ $t('添加模式') }}
            </el-button>
          </el-col>
        </el-row>

        <el-table v-if="!examRuleFormCreate && examRuleFormEditable" :data="examModeList"
                  row-key="id" size="mini" stripe border>
          <el-table-column prop="ordered" :label="$t('排序')" min-width="20" :align="'center'"/>
          <el-table-column prop="type" :label="$t('题型')" min-width="50" :align="'center'"/>
          <el-table-column prop="score" :label="$t('分值')" min-width="20" :align="'center'"/>
          <el-table-column prop="amount" :label="$t('题量')" min-width="20" :align="'center'"/>
          <el-table-column prop="question1Code" :label="$t('试题一')" min-width="50" show-overflow-tooltip/>
          <el-table-column prop="question2Code" :label="$t('试题二')" min-width="50" show-overflow-tooltip/>
          <el-table-column prop="question3Code" :label="$t('试题三')" min-width="50" show-overflow-tooltip/>
          <el-table-column prop="question4Code" :label="$t('试题四')" min-width="50" show-overflow-tooltip/>
          <el-table-column prop="question5Code" :label="$t('试题五')" min-width="50" show-overflow-tooltip/>
          <el-table-column :label="$t('操作')" min-width="100" :align="'center'">
            <template v-slot="scope">
              <el-button v-permission="examModePermission.edit"
                         size="mini" icon="el-icon-edit" type="success" plain
                         @click="onExamModeEdit(scope)">{{ $t('编辑') }}
              </el-button>
              <el-popconfirm style="margin-left: 10px" :title="$t('确定删除吗？')"
                             @confirm="onExamModeDelete(scope)">
                <el-button slot="reference" v-permission="examModePermission.delete"
                           size="mini" icon="el-icon-close" type="danger" plain>{{ $t('删除') }}
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
               :close-on-click-modal="false" append-to-body @click="onExamModeClose">
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
                <el-option v-for="item in examQuestionTypeDictItems" :key="item.value"
                           :label="item.label" :value="item.value"/>
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
              <el-select v-model="examModeForm.question1" :disabled="!isFixedStrategy">
                <el-option v-for="item in examQuestionOptions" :key="item.id"
                           :label="item.code" :value="item.id"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('问题二')" prop="question2">
              <el-select v-model="examModeForm.question2" :disabled="!isFixedStrategy">
                <el-option v-for="item in examQuestionOptions" :key="item.id"
                           :label="item.code" :value="item.id"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('问题三')" prop="question3">
              <el-select v-model="examModeForm.question3" :disabled="!isFixedStrategy">
                <el-option v-for="item in examQuestionOptions" :key="item.id"
                           :label="item.code" :value="item.id"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('问题四')" prop="question4">
              <el-select v-model="examModeForm.question4" :disabled="!isFixedStrategy">
                <el-option v-for="item in examQuestionOptions" :key="item.id"
                           :label="item.code" :value="item.id"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('问题五')" prop="question5">
              <el-select v-model="examModeForm.question5" :disabled="!isFixedStrategy">
                <el-option v-for="item in examQuestionOptions" :key="item.id"
                           :label="item.code" :value="item.id"/>
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
import {createExamMode, deleteExamMode, editExamMode, findExamMode} from '@/api/examMode';
import {searchExamQuestion} from '@/api/examQuestion';
import {createExamRule, deleteExamRule, editExamRule, findExamRule, pageExamRule} from '@/api/examRule';
import {clearTemplate, findOptionUrl} from '@/api/rest';
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
      examModeStrategyDictItems: [],
      examQuestionTypeDictItems: [],
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
      examModeFormCreate: true,
      examRuleForm: {
        id: null,
        title: '',
        duration: 60 * 60,
        totalScore: 100,
        passLinePercent: 60,
        passScore: 60,
        strategy: 'RANDOM',
      },
      examModeForm: {
        id: null,
        ordered: 1,
        type: '',
        score: 0,
        amount: 0,
        question1Id: '',
        question2Id: '',
        question3Id: '',
        question4Id: '',
        question5Id: '',
      },
      examRuleRules: {
        title: [{required: true, message: '请输入标题', trigger: 'blur'}],
        duration: [{required: true, message: '请选择时长', trigger: 'blur'}],
        strategy: [{required: true, message: '请选择策略', trigger: 'blur'}],
      },
      examModeRules: {
        ordered: [{required: true, message: '请输入排序', trigger: 'blur'}],
        type: [{required: true, message: '请选择题型', trigger: 'blur'}],
        score: [{required: true, message: '请输入分值', trigger: 'blur'}],
        amount: [{required: true, message: '请输入题量', trigger: 'blur'}],
      },
    };
  },
  computed: {
    isFixedStrategy: function () {
      return ['FIXED'].includes(this.examRuleForm.strategy);
    },
    isOverTotalScore: function () {
      const sumScore = this.examModeList.reduce((sum, node) => {
        sum = sum + node.score;
        return sum;
      }, 0);
      return sumScore > this.examRuleForm.totalScore;
    }
  },
  created() {
    this.findExamModeStrategyDict();
    this.findExamQuestionTypeDict();
    this.findExamRuleList();
  },
  methods: {
    findExamModeStrategyDict() {
      const params = {code: DICT_CODES.examModeStrategy, projection: 'dict-item-option'};
      searchDict('code', params).then(response => {
        this.examModeStrategyDictItems = response._embedded.items;
      });
    },
    findExamQuestionTypeDict() {
      const params = {code: DICT_CODES.examQuestionType, projection: 'dict-item-option'};
      searchDict('code', params).then(response => {
        this.examQuestionTypeDictItems = response._embedded.items;
      });
    },
    findExamRuleList() {
      this.examRuleLoading = true;
      // 如果需要搜索，才替换为 searchExamRule 并提供搜索路径
      pageExamRule(this.examRuleParams).then(response => {
        this.examRuleList = response._embedded.examRules;
        this.examRulePage = response.page;
        this.examRuleLoading = false;
      });
    },
    findExamQuestionOptions() {
      if (this.examQuestionOptions && this.examQuestionOptions.length) {
        return;
      }
      const params = {type: this.examModeForm.type, projection: 'exam-question-form'};
      searchExamQuestion('list', params).then(response => {
        this.examQuestionOptions = response._embedded.examQuestions;
      });
    },
    onExamRuleSearch() {
      this.examRuleParams.page = 0;
      this.findExamRuleList();
    },
    displayDuration(duration) {
      return this.durationOptions.find(it => it.value === duration).label || '';
    },
    resetExamRuleForm() {
      if (this.$refs.examRuleForm) {
        this.$refs.examRuleForm.resetFields();
      }
      this.examRuleForm = {
        id: null,
        title: '',
        duration: 60 * 60,
        totalScore: 100,
        passLinePercent: 60,
        passScore: 60,
        strategy: 'RANDOM',
      };
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
        question1Id: '',
        question2Id: '',
        question3Id: '',
        question4Id: '',
        question5Id: '',
      };
      if (this.examModeList && this.examModeList.length) {
        this.examModeForm.ordered = this.examModeList.reduce((max, node) => {
          return node.ordered > max ? node.ordered : max;
        }, 0) + 1;
      }
    },
    onExamRuleCreate() {
      this.resetExamRuleForm();
      this.examRuleFormCreate = true;
      this.examRuleFormEditable = true;
      this.examRuleTitle = '创建规则';
      this.examRuleVisible = true;
    },
    onExamModeCreate() {
      this.findExamQuestionOptions();
      this.resetExamModeForm();
      this.examModeFormCreate = true;
      this.examModeTitle = '创建模式';
      this.examModeVisible = true;
    },
    fillExamRuleForm(form) {
      this.examRuleForm = form;
      this.examModeList = form.modes;
    },
    fillExamModeForm(form) {
      this.examModeForm = form;
      if (this.examRuleForm.strategy === 'FIXED') {
        this.findExamQuestionOptions();
      }
    },
    onExamRuleEdit({row}, readonly = false) {
      this.resetExamRuleForm();
      this.examRuleFormCreate = false;
      this.examRuleFormEditable = !readonly;
      findExamRule(row.id, 'exam-rule-form').then(response => {
        this.fillExamRuleForm(response);
        this.examRuleTitle = readonly ? '查看规则' : '编辑规则';
        this.examRuleVisible = true;
      });
    },
    onTotalScoreChange(value) {
      this.examRuleForm.passScore = ((this.examRuleForm.passLinePercent / 100) * value).toFixed(2);
    },
    onPassLineChange(value) {
      this.examRuleForm.passScore = ((value / 100) * this.examRuleForm.totalScore).toFixed(2);
    },
    onPassScoreChange(value) {
      this.examRuleForm.passLinePercent = ((value / this.examRuleForm.totalScore) * 100).toFixed(0);
    },
    onExamModeEdit({row}) {
      this.resetExamModeForm();
      this.examModeFormCreate = false;
      findExamMode(row.id, 'exam-mode-form').then(response => {
        this.fillExamModeForm(response);
        this.examModeTitle = '编辑模式';
        this.examModeVisible = true;
      });
    },
    onExamRuleDelete({row}) {
      if (row && row.id) {
        deleteExamRule(row.id).then(() => {
          this.$message.success(`规则 [${row.id}] 删除成功！`);
          this.findExamRuleList();
        });
      }
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
          this.$message.success(`模式 [${row.id}] 删除成功！`);
          this.reloadExamRuleForm();
        });
      }
    },
    onExamRuleClose() {
      // do something
    },
    onExamModeClose() {
      // do something
    },
    onExamRuleSubmit() {
      this.$refs.examRuleForm.validate(valid => {
        if (valid) {
          const data = {
            title: this.examRuleForm.title,
            duration: this.examRuleForm.duration,
            totalScore: this.examRuleForm.totalScore,
            passLine: (this.examRuleForm.passLinePercent / 100).toFixed(2),
            passScore: this.examRuleForm.passScore,
            strategy: this.examRuleForm.strategy
          };
          if (this.examRuleForm.id) {
            editExamRule(this.examRuleForm.id, data).then(() => {
              this.$message.success(`规则 [${data.title}] 更新成功！`);
              this.examRuleVisible = false;
              this.findExamRuleList();
            });
          } else {
            createExamRule(data).then(() => {
              this.$message.success(`规则 [${data.title}] 创建成功！`);
              this.examRuleVisible = false;
              this.findExamRuleList();
            });
          }
        }
      });
    },
    onExamModeSubmit() {
      this.$refs.examModeForm.validate(valid => {
        if (valid) {
          const data = {
            rule: clearTemplate(this.examRuleForm._links.self.href),
            ordered: this.examModeForm.ordered,
            type: this.examModeForm.type,
            score: this.examModeForm.score,
            amount: this.examModeForm.amount,
            question1: findOptionUrl(this.examQuestionOptions, this.examModeForm.question1Id),
            question2: findOptionUrl(this.examQuestionOptions, this.examModeForm.question2Id),
            question3: findOptionUrl(this.examQuestionOptions, this.examModeForm.question3Id),
            question4: findOptionUrl(this.examQuestionOptions, this.examModeForm.question4Id),
            question5: findOptionUrl(this.examQuestionOptions, this.examModeForm.question5Id),
          };
          if (this.examModeForm.id) {
            editExamMode(this.examModeForm.id, data).then(() => {
              this.$message.success(`模式 [${this.examModeForm.id}] 更新成功！`);
              this.examModeVisible = false;
              this.reloadExamRuleForm();
            });
          } else {
            createExamMode(data).then(() => {
              this.$message.success(`模式创建成功！`);
              this.examModeVisible = false;
              this.reloadExamRuleForm();
            });
          }
        }
      });
    },
  }
};
</script>

<style scoped>

</style>
