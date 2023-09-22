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
      <el-table-column prop="title" :label="$t('标题')" min-width="100" show-overflow-tooltip>
        <template v-slot="scope">
          <el-button size="mini" type="text" @click="onExamEdit(scope, true)">
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
      <el-table-column prop="createdBy" :label="$t('创建人')" min-width="40" :align="'center'"/>
      <el-table-column prop="created" :label="$t('创建时间')" min-width="80" :align="'center'"/>
      <el-table-column prop="updatedBy" :label="$t('更新人')" min-width="40" :align="'center'"/>
      <el-table-column prop="updated" :label="$t('更新时间')" min-width="80" :align="'center'"/>
      <el-table-column :label="$t('操作')" min-width="120" :align="'center'">
        <template v-slot="scope">
          <el-button v-permission="examPermission.taker"
                     size="mini" icon="el-icon-user" type="text"
                     @click="onExamTaker(scope)">{{ $t('考生') }}
          </el-button>
          <el-button v-permission="examPermission.marker"
                     size="mini" icon="el-icon-s-custom" type="text"
                     @click="onExamMarker(scope)">{{ $t('阅卷人') }}
          </el-button>
          <el-popconfirm style="margin: 0 10px" :title="$t('一旦准备将不能再修改任何内容，请确认！')"
                         @onConfirm="onExamWaiting(scope)">
            <el-button slot="reference" v-permission="examPermission.edit"
                       size="mini" icon="el-icon-ice-cream" type="text">{{ $t('准备') }}
            </el-button>
          </el-popconfirm>
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
            <el-form-item :label="$t('科目')" prop="subjectValue">
              <el-select v-model="examForm.subjectValue">
                <el-option v-for="item in subjectDictItems" :key="item.value" :label="item.label" :value="item.value"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('时间')" prop="startTime">
              <el-date-picker v-model="examForm.startTime" type="datetime"
                              :picker-options="pickerOptions" :value-format="dateTimeFormat"/>
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
            <el-form-item :label="$t('标题')" prop="title">
              <el-input v-model="examForm.title">
                <el-button v-if="examFormEditable" slot="append"
                           :disabled="!examFormEditable"
                           @click="onExamGenerateTitle">{{ $t('生成标题') }}
                </el-button>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('规则')" prop="rule">
              <el-select v-model="examForm.ruleId">
                <el-option v-for="item in ruleOptions"
                           :key="item.id" :label="item.title" :value="item.id"/>
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

      <div v-if="!examFormEditable && examForm.status === 'WAITING' && examStartTime"
           style="width: 100%; text-align: center;">
        <el-statistic :value="examStartTime" time-indices title="考试已准备好，即将开始："/>
      </div>

      <div slot="footer">
        <el-button v-if="examFormEditable" type="primary" @click="onExamSubmit">{{ $t('提交') }}</el-button>
        <el-button v-if="examFormEditable" @click="examVisible = false">{{ $t('取消') }}</el-button>
        <el-button v-if="!examFormEditable" @click="examVisible = false">{{ $t('关闭') }}</el-button>
      </div>
    </el-dialog>

    <el-dialog v-el-drag-dialog :title="$t('考生名单')" :visible.sync="examTakerVisible"
               :close-on-click-modal="false" width="30%" append-to-body>
      <div class="text-center">
        <el-transfer v-model="examTakerList" :data="accountOptions"
                     style="text-align: left; display: inline-block"
                     :titles="['可选列表', '已选列表']"/>
      </div>
      <div slot="footer">
        <el-button type="primary" @click="onExamTakerSubmit">{{ $t('提交') }}</el-button>
        <el-button @click="examTakerVisible = false">{{ $t('取消') }}</el-button>
      </div>
    </el-dialog>
    <el-dialog v-el-drag-dialog :title="$t('阅卷人名单')" :visible.sync="examMarkerVisible"
               :close-on-click-modal="false" width="30%" append-to-body>
      <div class="text-center">
        <el-transfer v-model="examMarkerList" :data="accountOptions"
                     style="text-align: left; display: inline-block"
                     :titles="['可选列表', '已选列表']"/>
      </div>
      <div slot="footer">
        <el-button type="primary" @click="onExamMarkerSubmit">{{ $t('提交') }}</el-button>
        <el-button @click="examMarkerVisible = false">{{ $t('取消') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {searchAccount} from '@/api/account';
import {DICT_CODES, findDictItemUri, searchDict} from '@/api/dict';
import {
  createExam,
  deleteExam,
  editExam,
  findExam,
  prepareExam,
  searchExam,
  updateExamMarkers,
  updateExamTakers
} from '@/api/exam';
import {searchExamRule} from '@/api/examRule';
import {findOptionUrl} from '@/api/rest';
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
      examTakerList: [],
      examMarkerList: [],
      examPage: {totalElements: 0, totalPages: 0},
      examTitle: '',
      examVisible: false,
      examTakerVisible: false,
      examMarkerVisible: false,
      examFormEditable: false,
      examForm: {
        id: null,
        title: '',
        subjectValue: '',
        difficulty: 3,
        ruleId: null,
        code: '',
        startTime: null,
        description: ''
      },
      examRules: {
        title: [{required: true, message: '考试标题不能为空', trigger: 'blur'}],
        subjectValue: [{required: true, message: '考试科目不能为空', trigger: 'blur'}],
        ruleId: [{required: true, message: '考试规则不能为空', trigger: 'blur'}],
        startTime: [{required: true, message: '考试时间不能为空', trigger: 'blur'}],
      },
      subjectDictItems: [],
      ruleOptions: [],
      accountOptions: [],
    };
  },
  computed: {
    examStartTime: function () {
      if (this.examForm.startTime) {
        return moment(this.examForm.startTime).toDate();
      }
      return null;
    },
  },
  created() {
    this.findExamSubjectDict();
    this.findExamRuleList();
    this.findAccountList();
    this.findExamList();
  },
  methods: {
    findExamSubjectDict() {
      const params = {code: DICT_CODES.examSubject, projection: 'dict-item-option'};
      searchDict('code', params).then(response => {
        this.subjectDictItems = response._embedded.items;
      });
    },
    findExamRuleList() {
      const params = {projection: 'exam-rule-option'};
      searchExamRule('list', params).then(response => {
        this.ruleOptions = response._embedded.examRules;
      });
    },
    findAccountList() {
      // FIXME 正常来说应该通过一定的规则去生成考生名单，而不是在这里加载所有账户来选择
      const params = {projection: 'account-transfer'};
      searchAccount('list', params).then(response => {
        this.accountOptions = response._embedded.accounts;
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
    resetExamForm() {
      if (this.$refs.examForm) {
        this.$refs.examForm.resetFields();
      }
      this.examForm = {
        id: null,
        title: '',
        subjectValue: '',
        difficulty: 3,
        ruleId: null,
        code: '',
        startTime: null,
        description: ''
      };
    },
    onExamCreate() {
      this.resetExamForm();
      this.examFormEditable = true;
      this.examTitle = '创建考试';
      this.examVisible = true;
    },
    fillExamForm(form) {
      this.examForm = form;
    },
    onExamTaker({row}) {
      findExam(row.id, 'exam-form').then(response => {
        this.fillExamForm(response);
        this.examTakerList = response.takers.map(it => it.id);
        this.examTakerVisible = true;
      });
    },
    onExamMarker({row}) {
      findExam(row.id, 'exam-form').then(response => {
        this.fillExamForm(response);
        this.examMarkerList = response.markers.map(it => it.id);
        this.examMarkerVisible = true;
      });
    },
    onExamWaiting({row}) {
      if (row.id) {
        prepareExam(row.id).then(() => {
          this.$message.success(`考试 [${row.title}] 准备成功！`);
        });
      }
    },
    onExamEdit({row}, readonly = false) {
      this.resetExamForm();
      this.examFormEditable = !readonly;
      findExam(row.id, 'exam-form').then(response => {
        this.fillExamForm(response);
        this.examTitle = readonly ? '查看考试' : '编辑考试';
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
    onExamGenerateTitle() {
      if (!this.examForm.subjectValue) {
        this.$message.error('生成标题必须先选择考试科目');
        return;
      }
      if (!this.examForm.startTime) {
        this.$message.error('生成标题必须先选择考试时间');
        return;
      }
      const first = moment(this.examForm.startTime).format('yyyy年MM月');
      const second = this.subjectDictItems.find(it => it.value === this.examForm.subjectValue).label;
      this.examForm.title = `${first}${second}考试`;
      if (this.$refs.examForm) {
        this.$refs.examForm.clearValidate('title');
      }
    },
    onExamSubmit() {
      this.$refs.examForm.validate(valid => {
        if (valid) {
          const data = {
            title: this.examForm.title,
            subject: findDictItemUri(this.subjectDictItems, this.examForm.subjectValue),
            difficulty: this.examForm.difficulty,
            startTime: this.examForm.startTime,
            rule: findOptionUrl(this.ruleOptions, this.examForm.ruleId),
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
    onExamTakerSubmit() {
      const data = this.examTakerList.filter(it => it != null);
      if (this.examForm.id) {
        updateExamTakers(this.examForm.id, data).then(() => {
          this.$message.success(`考生名单更新成功！`);
          this.examTakerVisible = false;
        });
      }
    },
    onExamMarkerSubmit() {
      const data = this.examMarkerList.filter(it => it != null);
      if (this.examForm.id) {
        updateExamMarkers(this.examForm.id, data).then(() => {
          this.$message.success(`阅卷人名单更新成功！`);
          this.examMarkerVisible = false;
        });
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

</style>
