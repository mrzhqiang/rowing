<template>
  <div class="app-container">
    <el-form ref="searchDictForm" v-model="dictParams" inline>
      <el-form-item :label="$t('名称')" prop="name">
        <el-input v-model="dictParams.name" clearable/>
      </el-form-item>
      <el-form-item :label="$t('代码')" prop="alpha2Code">
        <el-input v-model="dictParams.alpha2Code" clearable/>
      </el-form-item>
      <el-form-item :label="$t('中文名称')" prop="cnName">
        <el-input v-model="dictParams.cnName" clearable/>
      </el-form-item>
      <el-form-item :label="$t('数字代码')" prop="numericCode">
        <el-input v-model="dictParams.numericCode" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="onDictSearch">
          {{ $t('搜索') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="onResetDictSearch">
          {{ $t('重置') }}
        </el-button>
      </el-form-item>
    </el-form>

    <!-- 暂不允许自己创建，只能通过后端 Excel 文件同步 -->
    <!--<el-row :gutter="10" style="margin-bottom: 1rem">
      <el-col :span="2">
        <el-button v-permission="dictPermission.create"
                   type="primary" icon="el-icon-plus" plain
                   @click="onDictCreate">{{ $t('创建') }}
        </el-button>
      </el-col>
    </el-row>-->

    <el-table v-loading="dictLoading" :data="dictList" row-key="id" size="mini" stripe border highlight-current-row>
      <el-table-column prop="id" label="#" min-width="20" :align="'right'"/>
      <el-table-column prop="name" :label="$t('名称')" min-width="80">
        <template v-slot="scope">
          <el-button size="mini" type="text" @click="onDictEdit(scope, true)">
            {{ scope.row.name }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="alpha2Code" :label="$t('代码')" min-width="20" :align="'center'"/>
      <el-table-column prop="cnName" :label="$t('中文名称')" min-width="80" show-overflow-tooltip/>
      <el-table-column prop="alpha3Code" :label="$t('三位代码')" min-width="40" :align="'center'"/>
      <el-table-column prop="numericCode" :label="$t('数字代码')" min-width="20" :align="'center'"/>
      <el-table-column prop="createdBy" :label="$t('创建人')" min-width="40" :align="'center'"/>
      <el-table-column prop="created" :label="$t('创建时间')" min-width="80" :align="'center'"/>
      <el-table-column prop="updatedBy" :label="$t('更新人')" min-width="40" :align="'center'"/>
      <el-table-column prop="updated" :label="$t('更新时间')" min-width="80" :align="'center'"/>
      <el-table-column :label="$t('操作')" min-width="100" :align="'center'">
        <template v-slot="scope">
          <!--<el-button v-permission="dictPermission.create"
                     size="mini" icon="el-icon-plus" type="text"
                     @click="onDictAdd(scope)">{{ $t('添加') }}
          </el-button>-->
          <el-button v-permission="dictPermission.edit"
                     size="mini" icon="el-icon-edit" type="text"
                     @click="onDictEdit(scope, false)">{{ $t('编辑') }}
          </el-button>
          <el-popconfirm style="margin-left: 10px" :title="$t('确定删除吗？')"
                         @onConfirm="onDictDelete(scope)">
            <el-button slot="reference" v-permission="dictPermission.delete"
                       size="mini" icon="el-icon-delete" type="text">{{ $t('删除') }}
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="dictList.length" :total="dictPage.totalElements"
                :page.sync="dictParams.page" :size.sync="dictParams.size"
                @pagination="findDictList"/>

    <el-dialog :title="dictTitle" :visible.sync="dictVisible"
               :close-on-click-modal="!dictFormEditable"
               append-to-body @close="onDictClose">
      <el-form ref="dictForm" :model="dictForm" :rules="dictRules"
               :disabled="!dictFormEditable" label-width="80px">
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('名称')" prop="name">
              <el-input v-model="dictForm.name"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('代码')" prop="alpha2Code">
              <el-input v-model="dictForm.alpha2Code"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('中文名称')" prop="cnName">
              <el-input v-model="dictForm.cnName"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('三位代码')" prop="alpha3Code">
              <el-input v-model="dictForm.alpha3Code"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('数字代码')" prop="numericCode">
              <el-input v-model="dictForm.numericCode"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="dictFormEditable" type="primary" @click="onDictSubmit">{{ $t('提交') }}</el-button>
        <el-button v-if="dictFormEditable" @click="dictVisible = false">{{ $t('取消') }}</el-button>
        <el-button v-if="!dictFormEditable" @click="dictVisible = false">{{ $t('关闭') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {createDictISO3166, deleteDictISO3166, editDictISO3166, findDictISO3166, searchDictISO3166} from '@/api/dict';
import Pagination from '@/components/Pagination';
import {PERMISSION_MARK} from '@/utils/permission';

export default {
  name: 'CountryRegionCode',
  components: {Pagination},
  data() {
    return {
      dictParams: {
        name: '',
        alpha2Code: '',
        cnName: '',
        numericCode: '',
        page: 0,
        size: 20,
      },
      dictPermission: {...PERMISSION_MARK.dict},
      dictLoading: true,
      dictList: [],
      dictPage: {totalElements: 0, totalPages: 0},
      dictTitle: '',
      dictVisible: false,
      dictForm: {
        id: null,
        name: '',
        alpha2Code: '',
        cnName: '',
        alpha3Code: '',
        numericCode: '',
      },
      dictRules: {
        name: [{required: true, message: this.$t('名称不能为空'), trigger: 'blur'}],
        alpha2Code: [{required: true, message: this.$t('代码不能为空'), trigger: 'blur'}],
        alpha3Code: [{required: true, message: this.$t('三位代码不能为空'), trigger: 'blur'}],
        numericCode: [{required: true, message: this.$t('数字代码不能为空'), trigger: 'blur'}],
      },
      dictFormEditable: false,
    };
  },
  created() {
    this.findDictList();
  },
  methods: {
    findDictList() {
      this.dictLoading = true;
      searchDictISO3166('page', this.dictParams).then(response => {
        this.dictList = response._embedded.dicts;
        this.dictPage = response.page;
        this.dictLoading = false;
      });
    },
    onDictSearch() {
      this.dictParams.page = 0;
      this.findDictList();
    },
    onResetDictSearch() {
      this.dictParams = {
        name: '',
        alpha2Code: '',
        cnName: '',
        numericCode: '',
        page: 0,
        size: 20,
      };
      if (this.$refs.searchDictForm) {
        this.$refs.searchDictForm.resetFields();
      }
      this.findDictList();
    },
    resetDictForm() {
      if (this.$refs.dictForm) {
        this.$refs.dictForm.resetFields();
      }
      this.dictForm = {
        id: null,
        name: '',
        alpha2Code: '',
        cnName: '',
        alpha3Code: '',
        numericCode: '',
      };
    },
    fillDictForm(form) {
      this.dictForm = form;
    },
    onDictCreate() {
      this.resetDictForm();
      this.dictFormEditable = true;
      this.dictTitle = this.$t('创建国家地区代码');
      this.dictVisible = true;
    },
    onDictAdd({row}) {
      this.resetDictForm();
      this.dictFormEditable = true;
      this.dictTitle = this.$t('添加国家地区代码');
      this.dictVisible = true;
    },
    onDictEdit({row}, readonly = false) {
      this.resetDictForm();
      this.dictFormEditable = !readonly;
      findDictISO3166(row.id, 'dict-iso-3166-form').then(response => {
        this.fillDictForm(response);
        this.dictForm.id = row.id;
        this.dictTitle = readonly ? this.$t('查看国家地区代码') : this.$t('编辑国家地区代码');
        this.dictVisible = true;
      });
    },
    onDictDelete({row}) {
      deleteDictISO3166(row.id).then(() => {
        this.$message.success(this.$t('国家地区代码 {code} 删除成功！', {code: row.code}));
        this.findDictList();
      });
    },
    onDictClose() {
      // do nothing
    },
    onDictSubmit() {
      this.$refs.dictForm.validate(valid => {
        if (valid) {
          // 只修改页面上的字段
          const data = {
            name: this.dictForm.name,
            alpha2Code: this.dictForm.alpha2Code,
            cnName: this.dictForm.cnName,
            alpha3Code: this.dictForm.alpha3Code,
            numericCode: this.dictForm.numericCode
          };
          if (this.dictForm.id) {
            editDictISO3166(this.dictForm.id, data).then(() => {
              this.$message.success(this.$t('国家地区代码 {code} 更新成功！', {code: this.dictForm.code}));
              this.dictVisible = false;
              this.findDictList();
            });
          } else {
            createDictISO3166(data).then(() => {
              this.$message.success(this.$t('国家地区代码 {code} 创建成功！', {code: this.dictForm.code}));
              this.dictVisible = false;
              this.findDictList();
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
