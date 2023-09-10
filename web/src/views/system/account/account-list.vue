<template>
  <div class="app-container">
    <el-form ref="searchAccountForm" v-model="accountParams" inline>
      <el-form-item :label="$t('用户名')" prop="username">
        <el-input v-model="accountParams.username" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="onAccountSearch">
          {{ $t('搜索') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="onResetAccountSearch">
          {{ $t('重置') }}
        </el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" style="margin-bottom: 1rem">
      <el-col :span="2">
        <el-button v-permission="accountPermission.create"
                   type="primary" icon="el-icon-plus" plain
                   @click="onAccountCreate">{{ $t('创建账户') }}
        </el-button>
      </el-col>
    </el-row>

    <el-table v-loading="accountLoading" :data="accountList" row-key="id" stripe border highlight-current-row>
      <el-table-column prop="id" label="#" min-width="20" :align="'right'"/>
      <el-table-column prop="username" :label="$t('用户名')" min-width="50" show-overflow-tooltip>
        <template v-slot="scope">
          <el-button size="mini" type="text" @click="onAccountEdit(scope, true)">
            {{ scope.row.username }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="password" :label="$t('密码')" min-width="100" show-overflow-tooltip/>
      <el-table-column prop="type" :label="$t('类型')" min-width="40" :align="'center'"/>
      <el-table-column prop="nickname" :label="$t('用户昵称')" min-width="50" show-overflow-tooltip/>
      <el-table-column prop="createdBy" :label="$t('创建人')" min-width="40" :align="'center'"/>
      <el-table-column prop="created" :label="$t('创建时间')" min-width="80" :align="'center'"/>
      <el-table-column prop="updatedBy" :label="$t('更新人')" min-width="40" :align="'center'"/>
      <el-table-column prop="updated" :label="$t('更新时间')" min-width="80" :align="'center'"/>
      <el-table-column :label="$t('操作')" min-width="100" :align="'center'">
        <template v-slot="scope">
          <el-button v-permission="accountPermission.edit"
                     size="mini" icon="el-icon-edit" type="text"
                     @click="onAccountEdit(scope, false)">{{ $t('编辑') }}
          </el-button>
          <el-popconfirm style="margin-left: 10px" :title="$t('确定删除吗？')"
                         @onConfirm="onAccountDelete(scope)">
            <el-button slot="reference" v-permission="accountPermission.delete"
                       size="mini" icon="el-icon-delete" type="text">{{ $t('删除') }}
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="accountList.length" :total="accountPage.totalElements"
                :page.sync="accountParams.page" :size.sync="accountParams.size"
                @pagination="findAccountList"/>

    <el-dialog :title="accountTitle" :visible.sync="accountVisible"
               :close-on-click-modal="!accountFormEditable"
               append-to-body @close="onAccountClose">
      <el-form ref="accountForm" :model="accountForm" :rules="accountRules"
               :disabled="!accountFormEditable" label-width="100px">
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('用户名')" prop="username">
              <el-input v-model="accountForm.username" :disabled="!accountFormCreate"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('类型')" prop="type">
              <el-select v-model="accountForm.type">
                <el-option v-for="item in accountTypeOptions"
                           :key="item.value" :label="item.label" :value="item.value"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="!accountFormCreate" :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('失效期限')" prop="expired">
              <el-date-picker v-model="accountForm.expired" type="datetime"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('失败次数')" prop="failedCount">
              <el-input-number v-model="accountForm.failedCount" controls-position="right" min="0"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('锁定时间')" prop="locked">
              <el-date-picker v-model="accountForm.locked" type="datetime"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="!accountFormCreate" :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('密码失效期限')" prop="passwordExpired">
              <el-date-picker v-model="accountForm.passwordExpired" type="datetime"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('是否禁用')" prop="disabled">
              <el-switch v-model="accountForm.disabled"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="accountFormEditable" type="primary" @click="onAccountSubmit">{{ $t('提交') }}</el-button>
        <el-button v-if="accountFormEditable" @click="accountVisible = false">{{ $t('取消') }}</el-button>
        <el-button v-if="!accountFormEditable" @click="accountVisible = false">{{ $t('关闭') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {createAccount, deleteAccount, editAccount, findAccount, searchAccount} from '@/api/account';
import {DICT_CODES, searchDict} from '@/api/dict';
import Pagination from '@/components/Pagination';
import {PERMISSION_MARK} from '@/utils/permission';

export default {
  name: 'AccountList',
  components: {Pagination},
  data() {
    return {
      accountParams: {
        username: '',
        page: 0,
        size: 20,
      },
      accountPermission: {...PERMISSION_MARK.account},
      accountLoading: true,
      accountList: [],
      accountPage: {totalElements: 0, totalPages: 0},
      accountTitle: '',
      accountVisible: false,
      accountForm: {
        id: null,
        username: '',
        type: 'USER',
        expired: null,
        failedCount: 0,
        locked: null,
        passwordExpired: null,
        disabled: false
      },
      accountRules: {
        username: [{required: true, message: this.$t('用户名不能为空'), trigger: 'blur'}],
        type: [{required: true, message: this.$t('账户类型不能为空'), trigger: 'blur'}]
      },
      accountFormEditable: false,
      accountFormCreate: false,
      accountTypeOptions: [],
    };
  },
  created() {
    this.findAccountType();
    this.findAccountList();
  },
  methods: {
    findAccountType() {
      searchDict('code', {code: DICT_CODES.accountType}).then(response => {
        this.accountTypeOptions = response._embedded.items;
      });
    },
    findAccountList() {
      this.accountLoading = true;
      searchAccount('page', this.accountParams).then(response => {
        this.accountList = response._embedded.accounts;
        this.accountPage = response.page;
        this.accountLoading = false;
      });
    },
    onAccountSearch() {
      this.accountParams.page = 0;
      this.findAccountList();
    },
    onResetAccountSearch() {
      this.accountParams = {
        username: '',
        page: 0,
        size: 20,
      };
      if (this.$refs.searchAccountForm) {
        this.$refs.searchAccountForm.resetFields();
      }
      this.findAccountList();
    },
    resetAccountForm() {
      if (this.$refs.accountForm) {
        this.$refs.accountForm.resetFields();
      }
      this.accountForm = {
        id: null,
        username: '',
        type: 'USER',
        expired: null,
        failedCount: 0,
        locked: null,
        passwordExpired: null,
        disabled: false
      };
    },
    fillAccountForm(form) {
      this.accountForm = form;
    },
    onAccountCreate() {
      this.resetAccountForm();
      this.accountFormEditable = true;
      this.accountFormCreate = true;
      this.accountTitle = this.$t('创建账户');
      this.accountVisible = true;
    },
    onAccountEdit({row}, readonly = false) {
      this.resetAccountForm();
      this.accountFormEditable = !readonly;
      this.accountFormCreate = false;
      findAccount(row.id, 'account-form').then(response => {
        this.fillAccountForm(response);
        this.accountForm.id = row.id;
        this.accountTitle = readonly ? this.$t('查看账户') : this.$t('编辑账户');
        this.accountVisible = true;
      });
    },
    onAccountDelete({row}) {
      deleteAccount(row.id).then(() => {
        this.$message.success(this.$t('账户 {title} 删除成功！', {title: row.username}));
        this.findAccountList();
      });
    },
    onAccountClose() {
      // do nothing
    },
    onAccountSubmit() {
      this.$refs.accountForm.validate(valid => {
        if (valid) {
          // 只修改页面上的字段
          const data = {
            type: this.accountForm.type,
            expired: this.accountForm.expired,
            failedCount: this.accountForm.failedCount,
            locked: this.accountForm.locked,
            passwordExpired: this.accountForm.passwordExpired,
            disabled: this.accountForm.disabled
          };
          if (this.accountForm.id) {
            editAccount(this.accountForm.id, data).then(() => {
              this.$message.success(this.$t('账户 {title} 更新成功！', {title: this.accountForm.username}));
              this.accountVisible = false;
              this.findAccountList();
            });
          } else {
            data.username = this.accountForm.username;
            createAccount(data).then(() => {
              this.$message.success(this.$t('账户 {title} 创建成功！', {title: this.accountForm.username}));
              this.accountVisible = false;
              this.findAccountList();
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
