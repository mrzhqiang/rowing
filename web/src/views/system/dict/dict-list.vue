<template>
  <div class="app-container">
    <el-form ref="searchDictForm" v-model="dictParams" inline>
      <el-form-item :label="$t('名称')" prop="name">
        <el-input v-model="dictParams.name" clearable/>
      </el-form-item>
      <el-form-item :label="$t('代码')" prop="code">
        <el-input v-model="dictParams.code" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="onDictSearch">{{ $t('搜索') }}</el-button>
        <el-button icon="el-icon-refresh" @click="onResetDictSearch">{{ $t('重置') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" style="margin-bottom: 1rem">
      <el-col :span="2">
        <el-button v-permission="dictPermission.create" type="primary"
                   icon="el-icon-plus" plain @click="onDictCreate">{{ $t('创建字典组') }}
        </el-button>
      </el-col>
    </el-row>

    <el-table v-loading="dictLoading" :data="dictList" row-key="id" size="mini"
              stripe border highlight-current-row>
      <el-table-column prop="id" label="#" min-width="20" :align="'right'"/>
      <el-table-column prop="name" :label="$t('名称')" min-width="100" :align="'center'">
        <template v-slot="scope">
          <el-button size="mini" type="text" @click="onDictEdit(scope, true)">
            {{ scope.row.name }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="code" :label="$t('代码')" min-width="50" :align="'center'"/>
      <el-table-column prop="type" :label="$t('类型')" min-width="40" :align="'center'"/>
      <el-table-column prop="freeze" :label="$t('是否冻结')" min-width="20" :align="'center'"/>
      <el-table-column prop="createdBy" :label="$t('创建人')" min-width="40" :align="'center'"/>
      <el-table-column prop="created" :label="$t('创建时间')" min-width="80" :align="'center'"/>
      <el-table-column prop="updatedBy" :label="$t('更新人')" min-width="40" :align="'center'"/>
      <el-table-column prop="updated" :label="$t('更新时间')" min-width="80" :align="'center'"/>
      <el-table-column :label="$t('操作')" min-width="100" :align="'center'">
        <template v-slot="scope">
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
            <el-form-item :label="$t('代码')" prop="code">
              <el-input v-model="dictForm.code"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <el-divider v-if="!dictFormCreate" content-position="left">{{ $t('字典项') }}</el-divider>
      <el-row v-if="!dictFormCreate" :gutter="10" style="margin-bottom: 1rem">
        <el-col :span="2">
          <el-button v-permission="dictPermission.create" type="primary"
                     icon="el-icon-plus" plain @click="onDictItemCreate">{{ $t('新增字典项') }}
          </el-button>
        </el-col>
      </el-row>

      <el-table v-if="!dictFormCreate" :data="dictItemList" row-key="id" size="mini"
                max-height="480" stripe border highlight-current-row>
        <el-table-column prop="id" label="#" min-width="20" :align="'right'"/>
        <el-table-column prop="label" :label="$t('标签')" min-width="100" :align="'center'">
          <template v-slot="scope">
            <el-button size="mini" type="text" @click="onDictItemEdit(scope, true)">
              {{ scope.row.label }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column prop="value" :label="$t('码值')" min-width="50" :align="'center'"/>
        <el-table-column prop="createdBy" :label="$t('创建人')" min-width="40" :align="'center'"/>
        <el-table-column prop="created" :label="$t('创建时间')" min-width="80" :align="'center'"/>
        <el-table-column prop="updatedBy" :label="$t('更新人')" min-width="40" :align="'center'"/>
        <el-table-column prop="updated" :label="$t('更新时间')" min-width="80" :align="'center'"/>
        <el-table-column :label="$t('操作')" min-width="100" :align="'center'">
          <template v-slot="scope">
            <el-button v-permission="dictPermission.edit"
                       size="mini" icon="el-icon-edit" type="text"
                       @click="onDictItemEdit(scope, false)">{{ $t('编辑') }}
            </el-button>
            <el-popconfirm style="margin-left: 10px" :title="$t('确定删除吗？')"
                           @onConfirm="onDictItemDelete(scope)">
              <el-button slot="reference" v-permission="dictPermission.delete"
                         size="mini" icon="el-icon-delete" type="text">{{ $t('删除') }}
              </el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button v-if="dictFormEditable" type="primary" @click="onDictSubmit">{{ $t('提交') }}</el-button>
        <el-button v-if="dictFormEditable" @click="dictVisible = false">{{ $t('取消') }}</el-button>
        <el-button v-if="!dictFormEditable" @click="dictVisible = false">{{ $t('关闭') }}</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="dictItemTitle" :visible.sync="dictItemVisible"
               :close-on-click-modal="!dictItemFormEditable"
               append-to-body @close="onDictItemClose">
      <el-form ref="dictItemForm" :model="dictItemForm" :rules="dictItemRules"
               :disabled="!dictItemFormEditable" label-width="80px">
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('标签')" prop="label">
              <el-input v-model="dictItemForm.label"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('码值')" prop="value">
              <el-input v-model="dictItemForm.value"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="dictItemFormEditable" type="primary" @click="onDictItemSubmit">{{ $t('提交') }}</el-button>
        <el-button v-if="dictItemFormEditable" @click="dictItemVisible = false">{{ $t('取消') }}</el-button>
        <el-button v-if="!dictItemFormEditable" @click="dictItemVisible = false">{{ $t('关闭') }}</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {
  createDict,
  createDictItem,
  deleteDict,
  deleteDictItem,
  editDict,
  editDictItem,
  findDict,
  findDictItem,
  searchDict
} from '@/api/dict';
import {clearTemplate} from '@/api/rest';
import Pagination from '@/components/Pagination';
import {PERMISSION_MARK} from '@/utils/permission';

export default {
  name: 'DictList',
  components: {Pagination},
  data() {
    return {
      dictParams: {
        name: '',
        code: '',
        page: 0,
        size: 20,
      },
      dictPermission: {...PERMISSION_MARK.dict},
      dictLoading: false,
      dictList: [],
      dictItemList: [],
      dictPage: {totalElements: 0, totalPages: 0},
      dictTitle: '',
      dictItemTitle: '',
      dictVisible: false,
      dictItemVisible: false,
      dictForm: {
        id: null,
        name: '',
        code: ''
      },
      dictItemForm: {
        id: null,
        label: '',
        value: '',
        group: null
      },
      dictRules: {
        name: [{required: true, message: this.$t('字典组名称不能为空'), trigger: 'blur'}],
        code: [{required: true, message: this.$t('字典组代码不能为空'), trigger: 'blur'}],
      },
      dictItemRules: {
        label: [{required: true, message: this.$t('字典项标签不能为空'), trigger: 'blur'}],
        value: [{required: true, message: this.$t('字典项码值不能为空'), trigger: 'blur'}],
      },
      dictFormEditable: false,
      dictFormCreate: false,
      dictItemFormEditable: false,
    };
  },
  created() {
    this.findDictList();
  },
  methods: {
    findDictList() {
      this.dictLoading = true;
      searchDict('page', this.dictParams).then(response => {
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
        code: '',
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
        code: ''
      };
      this.dictItemList = [];
    },
    fillDictForm(form) {
      this.dictForm = form;
      this.dictItemList = form.items;
      this.dictItemForm.group = form._links.self.href;
    },
    resetDictItemForm() {
      if (this.$refs.dictItemForm) {
        this.$refs.dictItemForm.resetFields();
      }
      this.dictItemForm = {
        id: null,
        label: '',
        value: '',
        group: clearTemplate(this.dictForm._links.self.href)
      };
    },
    fillDictItemForm(form) {
      this.dictItemForm = form;
      this.dictItemForm.group = clearTemplate(form._links.group.href);
    },
    onDictCreate() {
      this.resetDictForm();
      this.dictFormEditable = true;
      this.dictFormCreate = true;
      this.dictTitle = this.$t('创建字典组');
      this.dictVisible = true;
    },
    onDictItemCreate() {
      this.resetDictItemForm();
      this.dictItemFormEditable = true;
      this.dictItemTitle = this.$t('新增字典项');
      this.dictItemVisible = true;
    },
    onDictEdit({row}, readonly = false) {
      this.resetDictForm();
      this.dictFormEditable = !readonly;
      this.dictFormCreate = false;
      findDict(row.id, 'dict-form').then(response => {
        this.fillDictForm(response);
        this.dictForm.id = row.id;
        this.dictTitle = readonly ? this.$t('查看字典组') : this.$t('编辑字典组');
        this.dictVisible = true;
      });
    },
    onDictItemEdit({row}, readonly = false) {
      this.resetDictItemForm();
      this.dictItemFormEditable = !readonly;
      findDictItem(row.id, 'dict-item-form').then(response => {
        this.fillDictItemForm(response);
        this.dictItemForm.id = row.id;
        this.dictItemTitle = readonly ? this.$t('查看字典项') : this.$t('编辑字典项');
        this.dictItemVisible = true;
      });
    },
    onDictDelete({row}) {
      deleteDict(row.id).then(() => {
        this.$message.success(`字典组 [${row.code}] 删除成功！`);
        this.findDictList();
      });
    },
    reloadDictForm() {
      if (this.dictForm.id) {
        findDict(this.dictForm.id, 'dict-form').then(response => {
          this.fillDictForm(response);
        });
      }
    },
    onDictItemDelete({row}) {
      deleteDictItem(row.id).then(() => {
        this.$message.success(`字典项 [${row.value}] 删除成功！`);
        this.reloadDictForm();
      });
    },
    onDictClose() {
      // do nothing
    },
    onDictItemClose() {
      // do nothing
    },
    onDictSubmit() {
      this.$refs.dictForm.validate(valid => {
        if (valid) {
          // 只修改页面上的字段
          const data = {
            name: this.dictForm.name,
            code: this.dictForm.code,
            type: 'CUSTOM'
          };
          if (this.dictForm.id) {
            editDict(this.dictForm.id, data).then(() => {
              this.$message.success(`字典组 [${data.name}] 更新成功！`);
              this.dictVisible = false;
              this.findDictList();
            });
          } else {
            createDict(data).then(() => {
              this.$message.success(`字典组 [${data.name}] 创建成功！`);
              this.dictVisible = false;
              this.findDictList();
            });
          }
        }
      });
    },
    onDictItemSubmit() {
      this.$refs.dictItemForm.validate(valid => {
        if (valid) {
          // 只修改页面上的字段
          const data = {
            label: this.dictItemForm.label,
            value: this.dictItemForm.value,
            group: this.dictItemForm.group
          };
          if (this.dictItemForm.id) {
            editDictItem(this.dictItemForm.id, data).then(() => {
              this.$message.success(`字典项 [${data.label}] 更新成功！`);
              this.dictItemVisible = false;
              this.reloadDictForm();
            });
          } else {
            createDictItem(data).then(() => {
              this.$message.success(`字典项 [${data.label}] 创建成功！`);
              this.dictItemVisible = false;
              this.reloadDictForm();
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
