<template>
  <div class="app-container">
    <el-form ref="examQuestionSearchForm" v-model="examQuestionParams" inline>
      <el-form-item :label="$t('编码')" prop="code">
        <el-input v-model="examQuestionParams.code" clearable/>
      </el-form-item>
      <el-form-item :label="$t('题型')" prop="type">
        <el-select v-model="examQuestionParams.types" multiple collapse-tags>
          <el-option v-for="item in examQuestionTypeDictItems" :key="item.value"
                     :label="item.label" :value="item.value"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('题干')" prop="stem">
        <el-input v-model="examQuestionParams.stem" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="onExamQuestionSearch">{{ $t('搜索') }}</el-button>
        <el-button icon="el-icon-refresh" @click="onResetExamQuestionSearch">{{ $t('重置') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" style="margin-bottom: 1rem">
      <el-col :span="2">
        <el-button v-permission="examQuestionPermission.create"
                   type="primary" icon="el-icon-plus" plain @click="onExamQuestionCreate">
          {{ $t('创建试题') }}
        </el-button>
      </el-col>
    </el-row>

    <el-table v-loading="examQuestionLoading" :data="examQuestionList" row-key="id" size="mini"
              stripe border highlight-current-row>
      <el-table-column prop="id" label="#" min-width="20" :align="'right'"/>
      <el-table-column prop="bankTitle" :label="$t('题库')" min-width="50" show-overflow-tooltip/>
      <el-table-column prop="code" :label="$t('编码')" min-width="100" show-overflow-tooltip/>
      <el-table-column prop="type" :label="$t('题型')" min-width="40" :align="'center'"/>
      <el-table-column prop="difficulty" :label="$t('难度')" min-width="80" :align="'center'">
        <template v-slot="scope">
          <el-rate v-model="scope.row.difficulty" :texts="difficultyTexts" class="el-rate-mini" show-text disabled/>
        </template>
      </el-table-column>
      <el-table-column prop="stem" :label="$t('题干')" min-width="200" show-overflow-tooltip/>
      <el-table-column prop="remark" :label="$t('备注')" min-width="100" show-overflow-tooltip/>
      <el-table-column prop="createdBy" :label="$t('创建人')" min-width="40" :align="'center'"/>
      <el-table-column prop="created" :label="$t('创建时间')" min-width="80" :align="'center'"/>
      <el-table-column prop="updatedBy" :label="$t('更新人')" min-width="40" :align="'center'"/>
      <el-table-column prop="updated" :label="$t('更新时间')" min-width="80" :align="'center'"/>
      <el-table-column :label="$t('操作')" min-width="100" :align="'center'">
        <template v-slot="scope">
          <el-button v-permission="examQuestionPermission.edit"
                     size="mini" icon="el-icon-edit" type="text"
                     @click="onExamQuestionEdit(scope)">{{ $t('编辑') }}
          </el-button>
          <el-popconfirm style="margin-left: 10px" :title="$t('确定删除吗？')"
                         @onConfirm="onExamQuestionDelete(scope)">
            <el-button slot="reference" v-permission="examQuestionPermission.delete"
                       size="mini" icon="el-icon-circle-close" type="text">{{ $t('删除') }}
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="examQuestionList.length" :total="examQuestionPage.totalElements"
                :page.sync="examQuestionParams.page" :size.sync="examQuestionParams.size"
                @pagination="findExamQuestionList"/>

    <el-dialog v-el-drag-dialog :title="examQuestionTitle" :visible.sync="examQuestionVisible"
               :close-on-click-modal="false" append-to-body @click="onExamQuestionClose">
      <el-form ref="examQuestionForm" :model="examQuestionForm" :rules="examQuestionRules"
               label-width="80px">
        <el-row :gutter="10">
          <el-col :span="10">
            <el-form-item :label="$t('题库')" prop="bankId">
              <el-select v-model="examQuestionForm.bankId">
                <el-option v-for="item in examQuestionBankOptions" :key="item.id"
                           :label="item.title" :value="item.id"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item :label="$t('难度')" prop="difficulty">
              <el-rate v-model="examQuestionForm.difficulty" :texts="difficultyTexts" show-text/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="20">
            <el-form-item :label="$t('编码')" prop="code">
              <el-input v-model="examQuestionForm.code" disabled/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="20">
            <el-form-item :label="$t('题干')" prop="stem">
              <el-input v-model="examQuestionForm.stem" type="textarea"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="10">
            <el-form-item :label="$t('题型')" prop="type">
              <el-select v-model="examQuestionForm.type">
                <el-option v-for="item in examQuestionTypeDictItems" :key="item.value"
                           :label="item.label" :value="item.value"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item :label="$t('正确选项')" prop="rightOptionId">
              <el-select v-model="examQuestionForm.rightOptionId" :disabled="!isSingleOption">
                <el-option v-for="item in examQuestionOptionOptions" :key="item.id"
                           :label="item.label" :value="item.id"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="10">
            <el-form-item :label="$t('答案')" prop="solution">
              <el-input v-model="examQuestionForm.solution" :disabled="isObjectiveType"/>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item :label="$t('答案链接')" prop="solutionUrl">
              <el-input v-model="examQuestionForm.solutionUrl" :disabled="isObjectiveType"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="10">
            <el-form-item :label="$t('解析')" prop="explained">
              <el-input v-model="examQuestionForm.explained"/>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item :label="$t('解析链接')" prop="explainedUrl">
              <el-input v-model="examQuestionForm.explainedUrl"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="20">
            <el-form-item :label="$t('备注')" prop="remark">
              <el-input v-model="examQuestionForm.remark" type="textarea" rows="3"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider v-if="!examQuestionFormCreate && isObjectiveType"
                    content-position="left">{{ $t('试题选项') }}
        </el-divider>
        <el-row v-if="!examQuestionFormCreate && isObjectiveType"
                :gutter="10" style="margin-bottom: 1rem">
          <el-col :span="2">
            <el-button v-permission="examQuestionOptionPermission.create" type="primary" icon="el-icon-plus"
                       plain @click="onExamQuestionOptionCreate">{{ $t('创建选项') }}
            </el-button>
          </el-col>
        </el-row>

        <el-table v-if="!examQuestionFormCreate && isObjectiveType" :data="examQuestionOptionList"
                  row-key="id" size="mini" max-height="240" stripe border>
          <el-table-column prop="label" :label="$t('标签')" min-width="30" :align="'center'"/>
          <el-table-column prop="content" :label="$t('内容')" min-width="100" show-overflow-tooltip/>
          <el-table-column prop="righted" :label="$t('是否正确')" min-width="40" :align="'center'">
            <template v-slot="scope">
              <el-switch v-model="scope.row.righted" disabled/>
            </template>
          </el-table-column>
          <el-table-column prop="sloppyMode" :label="$t('是否宽松')" min-width="40" :align="'center'">
            <template v-slot="scope">
              <el-switch v-model="scope.row.sloppyMode" disabled/>
            </template>
          </el-table-column>
          <el-table-column prop="scoreRatio" :label="$t('分值占比')" min-width="40" :align="'center'"/>
          <el-table-column prop="createdBy" :label="$t('创建人')" min-width="40" :align="'center'"/>
          <el-table-column prop="created" :label="$t('创建时间')" min-width="80" :align="'center'"/>
          <el-table-column prop="updatedBy" :label="$t('更新人')" min-width="40" :align="'center'"/>
          <el-table-column prop="updated" :label="$t('更新时间')" min-width="80" :align="'center'"/>
          <el-table-column :label="$t('操作')" min-width="100" :align="'center'">
            <template v-slot="scope">
              <el-button v-permission="examQuestionOptionPermission.edit"
                         size="mini" icon="el-icon-edit" type="text"
                         @click="onExamQuestionOptionEdit(scope)">{{ $t('编辑') }}
              </el-button>
              <el-popconfirm style="margin-left: 10px" :title="$t('确定删除吗？')"
                             @onConfirm="onExamQuestionOptionDelete(scope)">
                <el-button slot="reference" v-permission="examQuestionOptionPermission.delete"
                           size="mini" icon="el-icon-circle-close" type="text">{{ $t('删除') }}
                </el-button>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
      </el-form>

      <div slot="footer">
        <el-button type="primary" @click="onExamQuestionSubmit">{{ $t('提交') }}</el-button>
        <el-button @click="examQuestionVisible = false">{{ $t('取消') }}</el-button>
      </div>
    </el-dialog>

    <el-dialog v-el-drag-dialog :title="examQuestionOptionTitle" :visible.sync="examQuestionOptionVisible"
               :close-on-click-modal="false" append-to-body @click="onExamQuestionOptionClose">
      <el-form ref="examQuestionOptionForm" :model="examQuestionOptionForm" :rules="examQuestionOptionRules"
               label-width="80px">
        <el-row :gutter="10">
          <el-col :span="6">
            <el-form-item :label="$t('标签')" prop="label">
              <el-select v-model="examQuestionOptionForm.label">
                <el-option v-for="item in examQuestionOptionLabelOptions" :key="item.value"
                           :label="item.label" :value="item.value"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('是否正确')" prop="righted">
              <el-switch v-model="examQuestionOptionForm.righted"/>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('是否宽松')" prop="sloppyMode">
              <el-switch v-model="examQuestionOptionForm.sloppyMode"/>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('分值占比')" prop="scoreRatio">
              <el-input-number v-model="examQuestionOptionForm.scoreRatio"
                               :precision="2" :min="0" :step="0.01" :max="1"
                               :disabled="!examQuestionOptionForm.sloppyMode"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="24">
            <el-form-item :label="$t('内容')" prop="content">
              <el-input v-model="examQuestionOptionForm.content" type="textarea" rows="3"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <div slot="footer">
        <el-button type="primary" @click="onExamQuestionOptionSubmit">{{ $t('提交') }}</el-button>
        <el-button @click="examQuestionOptionVisible = false">{{ $t('取消') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {DICT_CODES, searchDict} from '@/api/dict';
import {
  createExamQuestion,
  deleteExamQuestion,
  editExamQuestion,
  findExamQuestion,
  searchExamQuestion
} from '@/api/examQuestion';
import {searchExamQuestionBank} from '@/api/examQuestionBank';
import {
  createExamQuestionOption,
  deleteExamQuestionOption,
  editExamQuestionOption,
  findExamQuestionOption,
} from '@/api/examQuestionOption';
import {clearTemplate, findOptionUrl} from '@/api/rest';
import Pagination from '@/components/Pagination';
import elDragDialog from '@/directive/el-drag-dialog';
import {deepClone} from '@/utils';
import {PERMISSION_MARK} from '@/utils/permission';

export default {
  name: 'ExamQuestionList',
  directives: {elDragDialog},
  components: {Pagination},
  data() {
    return {
      difficultyTexts: [
        this.$t('exam.difficultyLv1'),
        this.$t('exam.difficultyLv2'),
        this.$t('exam.difficultyLv3'),
        this.$t('exam.difficultyLv4'),
        this.$t('exam.difficultyLv5')
      ],
      examQuestionParams: {
        code: '',
        types: [],
        stem: '',
        page: 0,
        size: 20,
      },
      examQuestionPermission: {...PERMISSION_MARK.examQuestion},
      examQuestionOptionPermission: {...PERMISSION_MARK.examQuestionOption},
      examQuestionLoading: false,
      examQuestionList: [],
      examQuestionOptionList: [],
      examQuestionPage: {totalElements: 0, totalPages: 0},
      examQuestionTitle: '',
      examQuestionOptionTitle: '',
      examQuestionVisible: false,
      examQuestionOptionVisible: false,
      examQuestionFormCreate: false,
      examQuestionForm: {
        id: null,
        bankId: null,
        code: '',
        type: '',
        difficulty: 3,
        stem: '',
        rightOptionId: null,
        solution: '',
        solutionUrl: '',
        explained: '',
        explainedUrl: '',
        remark: '',
      },
      examQuestionOptionForm: {
        id: null,
        question: null,
        label: 'A',
        content: '',
        righted: false,
        sloppyMode: false,
        scoreRatio: 0,
      },
      examQuestionRules: {
        bankId: [{required: true, message: '题库不能为空', trigger: 'blur'}],
        type: [{required: true, message: '题型不能为空', trigger: 'blur'}],
        stem: [{required: true, message: '题干不能为空', trigger: 'blur'}],
      },
      examQuestionOptionRules: {
        label: [{required: true, message: '标签不能为空', trigger: 'blur'}],
        content: [{required: true, message: '内容不能为空', trigger: 'blur'}],
      },
      examQuestionTypeDictItems: [],
      examQuestionBankOptions: [],
      examQuestionOptionOptions: [],
      examQuestionOptionLabelOptions: [
        {label: 'A', value: 'A'},
        {label: 'B', value: 'B'},
        {label: 'C', value: 'C'},
        {label: 'D', value: 'D'},
        {label: 'E', value: 'E'},
        {label: 'F', value: 'F'},
      ],
    };
  },
  computed: {
    isObjectiveType: function () {
      return ['LOGIC', 'SINGLE', 'MULTIPLE'].includes(this.examQuestionForm.type);
    },
    isSingleOption: function () {
      return ['LOGIC', 'SINGLE'].includes(this.examQuestionForm.type);
    },
  },
  created() {
    this.findExamQuestionTypeDict();
    this.findExamQuestionBankOptions();
    this.findExamQuestionList();
  },
  methods: {
    findExamQuestionTypeDict() {
      const params = {code: DICT_CODES.examQuestionType, projection: 'dict-item-option'};
      searchDict('code', params).then(response => {
        this.examQuestionTypeDictItems = response._embedded.items;
      });
    },
    findExamQuestionBankOptions() {
      const params = {projection: 'exam-question-bank-option'};
      searchExamQuestionBank('list', params).then(response => {
        this.examQuestionBankOptions = response._embedded.examQuestionBanks;
      });
    },
    async findExamQuestionList() {
      this.examQuestionLoading = true;
      const params = deepClone(this.examQuestionParams);
      if (!params.types || !params.types.length) {
        if (!this.examQuestionTypeDictItems || !this.examQuestionTypeDictItems.length) {
          const dictParams = {code: DICT_CODES.examQuestionType, projection: 'dict-item-option'};
          const response = await searchDict('code', dictParams);
          params.types = response._embedded.items.map(it => it.value);
        } else {
          params.types = this.examQuestionTypeDictItems.map(it => it.value);
        }
      }
      searchExamQuestion('page', params).then(response => {
        this.examQuestionList = response._embedded.examQuestions;
        this.examQuestionPage = response.page;
        this.examQuestionLoading = false;
      });
    },
    onExamQuestionSearch() {
      this.examQuestionParams.page = 0;
      this.findExamQuestionList();
    },
    onResetExamQuestionSearch() {
      this.examQuestionParams = {
        code: '',
        types: [],
        stem: '',
        page: 0,
        size: 20,
      };
      if (this.$refs.examQuestionSearchForm) {
        this.$refs.examQuestionSearchForm.resetFields();
      }
      this.findExamQuestionList();
    },
    resetExamQuestionForm() {
      if (this.$refs.examQuestionForm) {
        this.$refs.examQuestionForm.resetFields();
      }
      this.examQuestionForm = {
        id: null,
        bankId: null,
        code: '',
        type: '',
        difficulty: 3,
        stem: '',
        rightOptionId: null,
        solution: '',
        solutionUrl: '',
        explained: '',
        explainedUrl: '',
        remark: '',
      };
    },
    resetExamQuestionOptionForm() {
      if (this.$refs.examQuestionOptionForm) {
        this.$refs.examQuestionOptionForm.resetFields();
      }
      this.examQuestionOptionForm = {
        id: null,
        question: null,
        label: 'A',
        content: '',
        righted: false,
        sloppyMode: false,
        scoreRatio: 0,
      };
      if (this.examQuestionOptionList && this.examQuestionOptionList.length) {
        if (this.examQuestionOptionList.length < this.examQuestionOptionLabelOptions.length) {
          this.examQuestionOptionForm.label = this.examQuestionOptionLabelOptions[this.examQuestionOptionList.length].value;
        }
      }
    },
    onExamQuestionCreate() {
      this.resetExamQuestionForm();
      this.examQuestionTitle = '创建试题';
      this.examQuestionFormCreate = true;
      this.examQuestionVisible = true;
    },
    onExamQuestionOptionCreate() {
      this.resetExamQuestionOptionForm();
      this.examQuestionOptionTitle = '创建选项';
      this.examQuestionOptionVisible = true;
    },
    fillExamQuestionForm(form) {
      this.examQuestionForm = form;
      this.examQuestionOptionOptions = form.options;
      this.examQuestionOptionList = form.options;
    },
    fillExamQuestionOptionForm(form) {
      this.examQuestionOptionForm = form;
    },
    onExamQuestionEdit({row}) {
      this.resetExamQuestionForm();
      findExamQuestion(row.id, 'exam-question-form').then(response => {
        this.fillExamQuestionForm(response);
        this.examQuestionTitle = '编辑试题';
        this.examQuestionFormCreate = false;
        this.examQuestionVisible = true;
      });
    },
    onExamQuestionOptionEdit({row}) {
      this.resetExamQuestionOptionForm();
      findExamQuestionOption(row.id, 'exam-question-option-form').then(response => {
        this.fillExamQuestionOptionForm(response);
        this.examQuestionOptionTitle = '编辑选项';
        this.examQuestionOptionVisible = true;
      });
    },
    onExamQuestionDelete({row}) {
      if (row && row.id) {
        deleteExamQuestion(row.id).then(() => {
          this.$message.success(`试题 [${row.code}] 删除成功！`);
          this.findExamQuestionList();
        });
      }
    },
    reloadExamQuestionForm() {
      if (this.examQuestionForm.id) {
        findExamQuestion(this.examQuestionForm.id, 'exam-question-form').then(response => {
          this.fillExamQuestionForm(response);
        });
      }
    },
    onExamQuestionOptionDelete({row}) {
      if (row && row.id) {
        deleteExamQuestionOption(row.id).then(() => {
          this.$message.success(`选项 [${row.label}] 删除成功！`);
          this.reloadExamQuestionForm();
        });
      }
    },
    onExamQuestionClose() {
      // do something
    },
    onExamQuestionOptionClose() {
      // do something
    },
    onExamQuestionSubmit() {
      this.$refs.examQuestionForm.validate(valid => {
        if (valid) {
          const data = {
            // 代码由后台接口自动生成
            // code: this.examQuestionForm.code,
            bank: findOptionUrl(this.examQuestionBankOptions, this.examQuestionForm.bankId),
            type: this.examQuestionForm.type,
            difficulty: this.examQuestionForm.difficulty,
            stem: this.examQuestionForm.stem,
            rightOption: findOptionUrl(this.examQuestionOptionOptions, this.examQuestionForm.rightOptionId),
            solution: this.examQuestionForm.solution,
            solutionUrl: this.examQuestionForm.solutionUrl,
            explained: this.examQuestionForm.explained,
            explainedUrl: this.examQuestionForm.explainedUrl,
            remark: this.examQuestionForm.remark,
          };
          if (this.examQuestionForm.id) {
            editExamQuestion(this.examQuestionForm.id, data).then(() => {
              this.$message.success(`试题 [${this.examQuestionForm.code}] 更新成功！`);
              this.examQuestionVisible = false;
              this.findExamQuestionList();
            });
          } else {
            createExamQuestion(data).then(() => {
              this.$message.success(`试题 [${this.examQuestionForm.code}] 创建成功！`);
              this.examQuestionVisible = false;
              this.findExamQuestionList();
            });
          }
        }
      });
    },
    onExamQuestionOptionSubmit() {
      this.$refs.examQuestionOptionForm.validate(valid => {
        if (valid) {
          const data = {
            question: clearTemplate(this.examQuestionForm._links.self.href),
            label: this.examQuestionOptionForm.label,
            content: this.examQuestionOptionForm.content,
            righted: this.examQuestionOptionForm.righted,
            sloppyMode: this.examQuestionOptionForm.sloppyMode,
            scoreRatio: this.examQuestionOptionForm.scoreRatio,
          };
          if (this.examQuestionOptionForm.id) {
            editExamQuestionOption(this.examQuestionOptionForm.id, data).then(() => {
              this.$message.success(`选项 [${data.label}] 更新成功！`);
              this.examQuestionOptionVisible = false;
              this.reloadExamQuestionForm();
            });
          } else {
            createExamQuestionOption(data).then(() => {
              this.$message.success(`选项 [${data.label}] 创建成功！`);
              this.examQuestionOptionVisible = false;
              this.reloadExamQuestionForm();
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

.el-rate-mini {
  line-height: 0;
}
</style>
