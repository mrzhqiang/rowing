<template>
  <div class="app-container">
    <el-form ref="examQuestionBankSearchForm" v-model="examQuestionBankParams" inline>
      <el-form-item :label="$t('标题')" prop="title">
        <el-input v-model="examQuestionBankParams.title" clearable/>
      </el-form-item>
      <el-form-item :label="$t('科目')" prop="subjects">
        <el-select v-model="examQuestionBankParams.subjects" multiple>
          <el-option v-for="item in subjectOptions" :key="item.value" :label="item.label" :value="item.value"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="onExamQuestionBankSearch">{{ $t('搜索') }}</el-button>
        <el-button icon="el-icon-refresh" @click="onResetExamQuestionBankSearch">{{ $t('重置') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" style="margin-bottom: 1rem">
      <el-col :span="2">
        <el-button v-permission="examQuestionBankPermission.create"
                   type="primary" icon="el-icon-plus" plain @click="onExamQuestionBankCreate">
          {{ $t('创建题库') }}
        </el-button>
      </el-col>
    </el-row>

    <el-table v-loading="examQuestionBankLoading" :data="examQuestionBankList" row-key="id" size="mini"
              stripe border highlight-current-row>
      <el-table-column prop="id" label="#" min-width="20" :align="'right'"/>
      <el-table-column prop="title" :label="$t('标题')" min-width="100" show-overflow-tooltip>
        <template v-slot="scope">
          <el-button size="mini" type="text" @click="onExamQuestionBankEdit(scope, true)">
            {{ scope.row.title }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="subjectLabel" :label="$t('科目')" min-width="80" show-overflow-tooltip/>
      <el-table-column prop="description" :label="$t('描述')" min-width="200" show-overflow-tooltip/>
      <el-table-column prop="createdBy" :label="$t('创建人')" min-width="40" :align="'center'"/>
      <el-table-column prop="created" :label="$t('创建时间')" min-width="80" :align="'center'"/>
      <el-table-column prop="updatedBy" :label="$t('更新人')" min-width="40" :align="'center'"/>
      <el-table-column prop="updated" :label="$t('更新时间')" min-width="80" :align="'center'"/>
      <el-table-column :label="$t('操作')" min-width="100" :align="'center'">
        <template v-slot="scope">
          <el-button v-permission="examQuestionBankPermission.edit"
                     size="mini" icon="el-icon-edit" type="text"
                     @click="onExamQuestionBankEdit(scope, false)">{{ $t('编辑') }}
          </el-button>
          <el-popconfirm style="margin-left: 10px" :title="$t('确定删除吗？')"
                         @onConfirm="onExamQuestionBankDelete(scope)">
            <el-button slot="reference" v-permission="examQuestionBankPermission.delete"
                       size="mini" icon="el-icon-circle-close" type="text">{{ $t('删除') }}
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="examQuestionBankList.length" :total="examQuestionBankPage.totalElements"
                :page.sync="examQuestionBankParams.page" :size.sync="examQuestionBankParams.size"
                @pagination="findExamQuestionBankList"/>

    <el-dialog v-el-drag-dialog :title="examQuestionBankTitle" :visible.sync="examQuestionBankVisible"
               :close-on-click-modal="!examQuestionBankFormEditable" append-to-body @click="onExamQuestionBankClose">
      <el-form ref="examQuestionBankForm" :model="examQuestionBankForm" :rules="examQuestionBankRules"
               :disabled="!examQuestionBankFormEditable" label-width="80px">
        <el-row :gutter="10">
          <el-col :span="10">
            <el-form-item :label="$t('标题')" prop="title">
              <el-input v-model="examQuestionBankForm.title"/>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item :label="$t('科目')" prop="subjectValue">
              <el-select v-model="examQuestionBankForm.subjectValue">
                <el-option v-for="item in subjectOptions" :key="item.value" :label="item.label" :value="item.value"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="20">
            <el-form-item :label="$t('描述')" prop="description">
              <el-input v-model="examQuestionBankForm.description" type="textarea" rows="3"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <el-divider v-if="!examQuestionBankFormCreate" content-position="left">{{ $t('试题列表') }}</el-divider>
      <el-table v-if="!examQuestionBankFormCreate" :data="examQuestionList" row-key="id" size="mini"
                max-height="480" stripe border highlight-current-row>
        <el-table-column prop="type" :label="$t('题型')" min-width="40" :align="'center'"/>
        <el-table-column prop="difficulty" :label="$t('难度')" min-width="80" :align="'center'">
          <template v-slot="scope">
            <el-rate v-model="scope.row.difficulty" :texts="difficultyTexts" class="el-rate-mini" show-text/>
          </template>
        </el-table-column>
        <el-table-column prop="stem" :label="$t('题干')" min-width="200" show-overflow-tooltip/>
        <el-table-column prop="createdBy" :label="$t('创建人')" min-width="40" :align="'center'"/>
        <el-table-column prop="created" :label="$t('创建时间')" min-width="80" :align="'center'"/>
        <el-table-column prop="updatedBy" :label="$t('更新人')" min-width="40" :align="'center'"/>
        <el-table-column prop="updated" :label="$t('更新时间')" min-width="80" :align="'center'"/>
      </el-table>

      <div slot="footer">
        <el-button type="primary" @click="onExamQuestionBankSubmit">{{ $t('提交') }}</el-button>
        <el-button @click="examQuestionBankVisible = false">{{ $t('取消') }}</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {DICT_CODES, findDictItemUri, searchDict} from '@/api/dict';
import {
  createExamQuestionBank,
  deleteExamQuestionBank,
  editExamQuestionBank,
  findExamQuestionBank,
  searchExamQuestionBank
} from '@/api/examQuestionBank';
import Pagination from '@/components/Pagination';
import elDragDialog from '@/directive/el-drag-dialog';
import {deepClone} from '@/utils';
import {PERMISSION_MARK} from '@/utils/permission';

export default {
  name: 'ExamQuestionBankList',
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
      examQuestionBankParams: {
        title: '',
        subjects: [],
        page: 0,
        size: 20,
      },
      examQuestionBankPermission: {...PERMISSION_MARK.examQuestionBank},
      examQuestionBankLoading: false,
      examQuestionBankList: [],
      examQuestionList: [],
      examQuestionBankPage: {totalElements: 0, totalPages: 0},
      examQuestionBankTitle: '',
      examQuestionBankVisible: false,
      examQuestionBankFormEditable: false,
      examQuestionBankFormCreate: false,
      examQuestionBankForm: {
        id: null,
        title: '',
        subjectValue: '',
        description: ''
      },
      examQuestionBankRules: {
        title: [{required: true, message: '标题不能为空', trigger: 'blur'}],
        subjectValue: [{required: true, message: '科目不能为空', trigger: 'blur'}],
      },
      subjectOptions: [],
    };
  },
  created() {
    this.findExamQuestionBankSubjectDict();
    this.findExamQuestionBankList();
  },
  methods: {
    findExamQuestionBankSubjectDict() {
      const params = {code: DICT_CODES.examSubject, projection: 'dict-item-option'};
      searchDict('code', params).then(response => {
        this.subjectOptions = response._embedded.items;
      });
    },
    async findExamQuestionBankList() {
      this.examQuestionBankLoading = true;
      // 深拷贝是为了避免搜索条件被覆盖
      const params = deepClone(this.examQuestionBankParams);
      // 科目参数为空，自动补全所有科目作为请求参数
      if (!params.subjects || !params.subjects.length) {
        // 可能是第一次进入页面，科目的选项还没有加载到位，这里自己去加载一次
        if (!this.subjectOptions || !this.subjectOptions.length) {
          const dictParams = {code: DICT_CODES.examSubject, projection: 'dict-item-option'};
          const response = await searchDict('code', dictParams);
          params.subjects = response._embedded.items.map(it => it.value);
        } else {
          params.subjects = this.subjectOptions.map(it => it.value);
        }
      }
      searchExamQuestionBank('page', params).then(response => {
        this.examQuestionBankList = response._embedded.examQuestionBanks;
        this.examQuestionBankPage = response.page;
        this.examQuestionBankLoading = false;
      });
    },
    onExamQuestionBankSearch() {
      this.examQuestionBankParams.page = 0;
      this.findExamQuestionBankList();
    },
    onResetExamQuestionBankSearch() {
      this.examQuestionBankParams = {
        title: '',
        subjects: [],
        page: 0,
        size: 20,
      };
      if (this.$refs.examQuestionBankSearchForm) {
        this.$refs.examQuestionBankSearchForm.resetFields();
      }
      this.findExamQuestionBankList();
    },
    resetExamQuestionBankForm() {
      if (this.$refs.examQuestionBankForm) {
        this.$refs.examQuestionBankForm.resetFields();
      }
      this.examQuestionBankForm = {
        id: null,
        title: '',
        subjectValue: '',
        description: ''
      };
    },
    onExamQuestionBankCreate() {
      this.resetExamQuestionBankForm();
      this.examQuestionBankFormCreate = true;
      this.examQuestionBankFormEditable = true;
      this.examQuestionBankTitle = '创建题库';
      this.examQuestionBankVisible = true;
    },
    fillExamQuestionBankForm(form) {
      this.examQuestionBankForm = form;
      this.examQuestionList = form.questions;
    },
    onExamQuestionBankEdit({row}, readonly = false) {
      this.resetExamQuestionBankForm();
      this.examQuestionBankFormCreate = false;
      this.examQuestionBankFormEditable = !readonly;
      findExamQuestionBank(row.id, 'exam-question-bank-form').then(response => {
        this.fillExamQuestionBankForm(response);
        this.examQuestionBankTitle = readonly ? '查看题库' : '编辑题库';
        this.examQuestionBankVisible = true;
      });
    },
    onExamQuestionBankDelete({row}) {
      if (row && row.id) {
        deleteExamQuestionBank(row.id).then(() => {
          this.$message.success(`题库 [${row.title}] 删除成功！`);
          this.findExamQuestionBankList();
        });
      }
    },
    onExamQuestionBankClose() {
      // do something
    },
    onExamQuestionBankSubmit() {
      this.$refs.examQuestionBankForm.validate(valid => {
        if (valid) {
          const data = {
            title: this.examQuestionBankForm.title,
            subject: findDictItemUri(this.subjectOptions, this.examQuestionBankForm.subjectValue),
            description: this.examQuestionBankForm.description,
          };
          if (this.examQuestionBankForm.id) {
            editExamQuestionBank(this.examQuestionBankForm.id, data).then(() => {
              this.$message.success(`题库 [${data.title}] 更新成功！`);
              this.examQuestionBankVisible = false;
              this.findExamQuestionBankList();
            });
          } else {
            createExamQuestionBank(data).then(() => {
              this.$message.success(`题库 [${data.title}] 创建成功！`);
              this.examQuestionBankVisible = false;
              this.findExamQuestionBankList();
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
