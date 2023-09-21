<template>
  <div class="app-container">
    <el-form ref="searchSettingForm" v-model="settingParams" inline>
      <el-form-item :label="$t('名称')" prop="name">
        <el-input v-model="settingParams.name" clearable/>
      </el-form-item>
      <el-form-item :label="$t('代码')" prop="code">
        <el-input v-model="settingParams.path" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="onSettingSearch">{{ $t('搜索') }}</el-button>
        <el-button icon="el-icon-refresh" @click="onResetSettingSearch">{{ $t('重置') }}</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="settingLoading" :data="settingList" row-key="id" size="mini"
              stripe border highlight-current-row>
      <el-table-column prop="id" label="#" min-width="20" :align="'right'"/>
      <el-table-column prop="type" :label="$t('类型')" min-width="50"/>
      <el-table-column prop="name" :label="$t('名称')" min-width="50" show-overflow-tooltip>
        <template v-slot="scope">
          <el-button size="mini" type="text" @click="onSettingEdit(scope, true)">
            {{ scope.row.name }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="code" :label="$t('代码')" min-width="80"/>
      <el-table-column prop="content" :label="$t('内容')" min-width="100" show-overflow-tooltip/>
      <el-table-column prop="style" :label="$t('风格')" min-width="80" show-overflow-tooltip/>
      <el-table-column prop="createdBy" :label="$t('创建人')" min-width="40" :align="'center'"/>
      <el-table-column prop="created" :label="$t('创建时间')" min-width="80" :align="'center'"/>
      <el-table-column prop="updatedBy" :label="$t('更新人')" min-width="40" :align="'center'"/>
      <el-table-column prop="updated" :label="$t('更新时间')" min-width="80" :align="'center'"/>
      <el-table-column :label="$t('操作')" min-width="100" :align="'center'">
        <template v-slot="scope">
          <el-button v-permission="settingPermission.edit"
                     size="mini" icon="el-icon-edit" type="text"
                     @click="onSettingEdit(scope)">{{ $t('编辑') }}
          </el-button>
          <el-popconfirm style="margin-left: 10px" :title="$t('确定删除吗？')"
                         @onConfirm="onSettingDelete(scope)">
            <el-button slot="reference" v-permission="settingPermission.delete"
                       size="mini" icon="el-icon-delete" type="text">{{ $t('删除') }}
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="settingList.length" :total="settingPage.totalElements"
                :page.sync="settingParams.page" :size.sync="settingParams.size"
                @pagination="findSettingList"/>

    <el-dialog :title="settingTitle" :visible.sync="settingVisible"
               :close-on-click-modal="!settingFormEditable"
               append-to-body @close="onSettingClose">
      <el-form ref="settingForm" :model="settingForm" :rules="settingRules"
               :disabled="!settingFormEditable" label-width="80px">
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('类型')" prop="type">
              <el-input v-model="settingForm.type" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('名称')" prop="name">
              <el-input v-model="settingForm.name"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('代码')" prop="code">
              <el-input v-model="settingForm.code" disabled/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="24">
            <el-form-item :label="$t('内容')" prop="content">
              <el-input v-model="settingForm.content" type="textarea" rows="3"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="settingFormEditable" type="primary" @click="onSettingSubmit">{{ $t('提交') }}</el-button>
        <el-button v-if="settingFormEditable" @click="settingVisible = false">{{ $t('取消') }}</el-button>
        <el-button v-if="!settingFormEditable" @click="settingVisible = false">{{ $t('关闭') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {deleteSetting, editSetting, findSetting, searchSetting} from '@/api/setting';
import Pagination from '@/components/Pagination';
import {PERMISSION_MARK} from '@/utils/permission';

export default {
  name: 'Setting',
  components: {Pagination},
  data() {
    return {
      settingParams: {
        name: '',
        code: '',
        page: 0,
        size: 20,
      },
      settingPermission: {...PERMISSION_MARK.setting},
      settingLoading: false,
      settingList: [],
      settingPage: {totalElements: 0, totalPages: 0},
      settingTitle: '',
      settingVisible: false,
      settingForm: {
        id: null,
        type: '',
        name: '',
        code: '',
        content: ''
      },
      settingRules: {
        name: [{required: true, message: this.$t('设置名称不能为空'), trigger: 'blur'}],
      },
      settingFormEditable: false,
    };
  },
  created() {
    this.findSettingList();
  },
  methods: {
    findSettingList() {
      this.settingLoading = true;
      searchSetting('page', this.settingParams).then(response => {
        this.settingList = response._embedded.settings;
        this.settingPage = response.page;
        this.settingLoading = false;
      });
    },
    onSettingSearch() {
      this.settingParams.page = 0;
      this.findSettingList();
    },
    onResetSettingSearch() {
      this.settingParams = {
        name: '',
        code: '',
        page: 0,
        size: 20,
      };
      if (this.$refs.searchSettingForm) {
        this.$refs.searchSettingForm.resetFields();
      }
      this.findSettingList();
    },
    resetSettingForm() {
      if (this.$refs.settingForm) {
        this.$refs.settingForm.resetFields();
      }
      this.settingForm = {
        id: null,
        type: '',
        name: '',
        code: '',
        content: ''
      };
    },
    fillSettingForm(form) {
      this.settingForm = form;
    },
    onSettingEdit({row}, readonly = false) {
      this.resetSettingForm();
      this.settingFormEditable = !readonly;
      findSetting(row.id, 'setting-form').then(response => {
        this.fillSettingForm(response);
        this.settingTitle = readonly ? this.$t('查看设置') : this.$t('编辑设置');
        this.settingVisible = true;
      });
    },
    onSettingDelete({row}) {
      deleteSetting(row.id).then(() => {
        this.$message.success(`系统设置 [${row.name}] 删除成功！`);
        this.findSettingList();
      });
    },
    onSettingClose() {
      // do nothing
    },
    onSettingSubmit() {
      this.$refs.settingForm.validate(valid => {
        if (valid) {
          // 只修改页面上的字段
          const data = {
            name: this.settingForm.name,
            content: this.settingForm.content
          };
          if (this.settingForm.id) {
            editSetting(this.settingForm.id, data).then(() => {
              this.$message.success(`系统设置 [${data.name}] 更新成功！`);
              this.settingVisible = false;
              this.findSettingList();
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
