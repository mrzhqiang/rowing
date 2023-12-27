<template>
  <div class="app-container">
    <el-form ref="searchRoleForm" v-model="roleParams" inline>
      <el-form-item :label="$t('名称')" prop="name">
        <el-input v-model="roleParams.name" clearable/>
      </el-form-item>
      <el-form-item :label="$t('代码')" prop="code">
        <el-input v-model="roleParams.code" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="onRoleSearch">{{ $t('搜索') }}</el-button>
        <el-button icon="el-icon-refresh" @click="onResetRoleSearch">{{ $t('重置') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" style="margin-bottom: 1rem">
      <el-col :span="2">
        <el-button v-permission="rolePermission.create"
                   type="primary" icon="el-icon-plus" plain
                   @click="onRoleCreate">{{ $t('创建角色') }}
        </el-button>
      </el-col>
    </el-row>

    <el-table v-loading="roleLoading" :data="roleList" row-key="id" size="mini"
              stripe border highlight-current-row>
      <el-table-column prop="id" label="#" min-width="20" :align="'right'"/>
      <el-table-column prop="name" :label="$t('名称')" min-width="50" show-overflow-tooltip>
        <template v-slot="scope">
          <el-button size="mini" type="text" @click="onRoleEdit(scope, true)">
            {{ scope.row.name }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="code" :label="$t('代码')" min-width="100"/>
      <el-table-column prop="immutable" :label="$t('是否不可变')" min-width="40" :align="'center'">
        <template v-slot="scope">
          <el-switch v-model="scope.row.immutable" disabled/>
        </template>
      </el-table-column>
      <el-table-column prop="createdBy" :label="$t('创建人')" min-width="40" :align="'center'"/>
      <el-table-column prop="created" :label="$t('创建时间')" min-width="80" :align="'center'"/>
      <el-table-column prop="updatedBy" :label="$t('更新人')" min-width="40" :align="'center'"/>
      <el-table-column prop="updated" :label="$t('更新时间')" min-width="80" :align="'center'"/>
      <el-table-column :label="$t('操作')" min-width="100" :align="'center'">
        <template v-slot="scope">
          <el-button v-permission="rolePermission.edit"
                     size="mini" icon="el-icon-edit" type="success" plain
                     @click="onRoleEdit(scope)">{{ $t('编辑') }}
          </el-button>
          <el-popconfirm style="margin-left: 10px" :title="$t('确定删除吗？')"
                         @confirm="onRoleDelete(scope)">
            <el-button slot="reference" v-permission="rolePermission.delete"
                       :disabled="scope.row.immutable"
                       size="mini" icon="el-icon-delete" type="danger" plain>{{ $t('删除') }}
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="roleList.length" :total="rolePage.totalElements"
                :page.sync="roleParams.page" :size.sync="roleParams.size"
                @pagination="findRoleList"/>

    <el-dialog :title="roleTitle" :visible.sync="roleVisible"
               :close-on-click-modal="!roleFormEditable"
               append-to-body @close="onRoleClose">
      <el-form ref="roleForm" :model="roleForm" :rules="roleRules"
               :disabled="!roleFormEditable" label-width="80px">
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('名称')" prop="name">
              <el-input v-model="roleForm.name"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('代码')" prop="code">
              <el-input v-model="roleForm.code" :disabled="roleForm.immutable"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <el-divider v-if="!roleFormCreate" content-position="left">{{ $t('角色授权信息') }}</el-divider>
      <el-collapse v-if="!roleFormCreate" v-model="roleFormCollapse" accordion>
        <el-collapse-item :title="$t('授权账户列表')" name="account">
          <el-row v-if="!roleFormCreate && roleFormEditable" :gutter="10" style="margin-bottom: 1rem">
            <el-col :span="2">
              <el-button v-permission="rolePermission.account"
                         size="mini" type="primary" icon="el-icon-plus" plain
                         @click="onRoleAddAccount">{{ $t('添加账户') }}
              </el-button>
            </el-col>
          </el-row>
          <el-table v-if="!roleFormCreate" :data="roleAccountList" row-key="id" size="mini" max-height="240" stripe border>
            <el-table-column prop="id" label="#" min-width="20" :align="'right'"/>
            <el-table-column prop="username" :label="$t('用户名')" min-width="50" show-overflow-tooltip/>
            <el-table-column prop="type" :label="$t('类型')" min-width="40"/>
            <el-table-column :label="$t('操作')" min-width="100" :align="'center'">
              <template v-slot="scope">
                <el-popconfirm style="margin-left: 10px" :title="$t('确定删除吗？')"
                               @confirm="onRoleDeleteAccount(scope)">
                  <el-button slot="reference" v-permission="rolePermission.account"
                             :disabled="!roleFormEditable"
                             size="mini" icon="el-icon-delete" type="danger" plain>{{ $t('删除') }}
                  </el-button>
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>
        </el-collapse-item>
        <el-collapse-item :title="$t('授权菜单列表')" name="menu">
          <el-row v-if="!roleFormCreate && roleFormEditable" :gutter="10" style="margin-bottom: 1rem">
            <el-col :span="2">
              <el-button v-permission="rolePermission.menu"
                         size="mini" type="primary" icon="el-icon-plus" plain
                         @click="onRoleAddMenu">{{ $t('添加菜单') }}
              </el-button>
            </el-col>
          </el-row>
          <el-table v-if="!roleFormCreate" :data="roleMenuList" row-key="id" size="mini" max-height="240" stripe border>
            <el-table-column prop="id" label="#" min-width="20" :align="'right'"/>
            <el-table-column prop="title" :label="$t('标题')" min-width="50" show-overflow-tooltip/>
            <el-table-column prop="path" :label="$t('路径')" min-width="100" show-overflow-tooltip/>
            <el-table-column :label="$t('操作')" min-width="100" :align="'center'">
              <template v-slot="scope">
                <el-popconfirm style="margin-left: 10px" :title="$t('确定删除吗？')"
                               @confirm="onRoleDeleteMenu(scope)">
                  <el-button slot="reference" v-permission="rolePermission.menu"
                             :disabled="!roleFormEditable"
                             size="mini" icon="el-icon-delete" type="danger" plain>{{ $t('删除') }}
                  </el-button>
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>
        </el-collapse-item>
        <el-collapse-item :title="$t('授权菜单资源列表')" name="menuResource">
          <el-row v-if="!roleFormCreate && roleFormEditable" :gutter="10" style="margin-bottom: 1rem">
            <el-col :span="2">
              <el-button v-permission="rolePermission.menuResource"
                         size="mini" type="primary" icon="el-icon-plus" plain
                         @click="onRoleAddMenuResource">{{ $t('添加菜单资源') }}
              </el-button>
            </el-col>
          </el-row>
          <el-table v-if="!roleFormCreate" :data="roleMenuResourceList" row-key="id" size="mini" max-height="240" stripe border>
            <el-table-column prop="id" label="#" min-width="20" :align="'right'"/>
            <el-table-column prop="name" :label="$t('名称')" min-width="50" show-overflow-tooltip/>
            <el-table-column prop="authority" :label="$t('权限')" min-width="100" show-overflow-tooltip/>
            <el-table-column :label="$t('操作')" min-width="100" :align="'center'">
              <template v-slot="scope">
                <el-popconfirm style="margin-left: 10px" :title="$t('确定删除吗？')"
                               @confirm="onRoleDeleteMenuResource(scope)">
                  <el-button slot="reference" v-permission="rolePermission.menuResource"
                             :disabled="!roleFormEditable"
                             size="mini" icon="el-icon-delete" type="danger" plain>{{ $t('删除') }}
                  </el-button>
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>
        </el-collapse-item>
      </el-collapse>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="roleFormEditable" type="primary" @click="onRoleSubmit">{{ $t('提交') }}</el-button>
        <el-button v-if="roleFormEditable" @click="roleVisible = false">{{ $t('取消') }}</el-button>
        <el-button v-if="!roleFormEditable" @click="roleVisible = false">{{ $t('关闭') }}</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {
  createRole,
  deleteRole,
  deleteRoleAccount,
  deleteRoleMenu,
  deleteRoleMenuResource,
  editRole,
  findRole,
  searchRole
} from '@/api/role';
import Pagination from '@/components/Pagination';
import {PERMISSION_MARK} from '@/utils/permission';

export default {
  name: 'RoleList',
  components: {Pagination},
  data() {
    return {
      roleParams: {
        name: '',
        code: '',
        page: 0,
        size: 20,
      },
      rolePermission: {...PERMISSION_MARK.role},
      roleLoading: false,
      roleList: [],
      roleAccountList: [],
      roleMenuList: [],
      roleMenuResourceList: [],
      rolePage: {totalElements: 0, totalPages: 0},
      roleTitle: '',
      roleVisible: false,
      roleForm: {
        id: null,
        name: '',
        code: '',
        immutable: false,
      },
      roleRules: {
        name: [{required: true, message: this.$t('角色名称不能为空'), trigger: 'blur'}],
        code: [{required: true, message: this.$t('角色代码不能为空'), trigger: 'blur'}],
      },
      roleFormEditable: false,
      roleFormCreate: false,
      roleFormCollapse: '',
    };
  },
  created() {
    this.findRoleList();
  },
  methods: {
    findRoleList() {
      this.roleLoading = true;
      searchRole('page', this.roleParams).then(response => {
        this.roleList = response._embedded.roles;
        this.rolePage = response.page;
        this.roleLoading = false;
      });
    },
    onRoleSearch() {
      this.roleParams.page = 0;
      this.findRoleList();
    },
    onResetRoleSearch() {
      this.roleParams = {
        name: '',
        code: '',
        page: 0,
        size: 20,
      };
      if (this.$refs.searchRoleForm) {
        this.$refs.searchRoleForm.resetFields();
      }
      this.findRoleList();
    },
    resetRoleForm() {
      if (this.$refs.roleForm) {
        this.$refs.roleForm.resetFields();
      }
      this.roleForm = {
        id: null,
        name: '',
        code: '',
        immutable: false,
      };
    },
    fillRoleForm(form) {
      this.roleForm = form;
      this.roleAccountList = form.accounts;
      this.roleMenuList = form.menus;
      this.roleAccountList = form.menuRresources;
    },
    onRoleCreate() {
      this.resetRoleForm();
      this.roleFormEditable = true;
      this.roleFormCreate = true;
      this.roleTitle = this.$t('创建角色');
      this.roleVisible = true;
    },
    onRoleAddAccount() {
      this.$message.info('添加账户待实现');
    },
    onRoleAddMenu() {
      this.$message.info('添加菜单待实现');
    },
    onRoleAddMenuResource() {
      this.$message.info('添加菜单资源待实现');
    },
    onRoleEdit({row}, readonly = false) {
      this.resetRoleForm();
      this.roleFormEditable = !readonly;
      this.roleFormCreate = false;
      findRole(row.id, 'role-form').then(response => {
        this.fillRoleForm(response);
        this.roleTitle = readonly ? this.$t('查看角色') : this.$t('编辑角色');
        this.roleVisible = true;
      });
    },
    onRoleDelete({row}) {
      deleteRole(row.id).then(() => {
        this.$message.success(`角色 [${row.title}] 删除成功！`);
        this.findRoleList();
      });
    },
    reloadRoleForm() {
      if (this.roleForm.id) {
        findRole(this.roleForm.id, 'role-form').then(response => {
          this.fillRoleForm(response);
        });
      }
    },
    onRoleDeleteAccount({row}) {
      deleteRoleAccount(row.id).then(() => {
        this.$message.success(`角色账户 [${row.title}] 删除成功！`);
        this.reloadRoleForm();
      });
    },
    onRoleDeleteMenu({row}) {
      deleteRoleMenu(row.id).then(() => {
        this.$message.success(`角色菜单 [${row.title}] 删除成功！`);
        this.reloadRoleForm();
      });
    },
    onRoleDeleteMenuResource({row}) {
      deleteRoleMenuResource(row.id).then(() => {
        this.$message.success(`角色菜单资源 [${row.title}] 删除成功！`);
        this.reloadRoleForm();
      });
    },
    onRoleClose() {
      this.roleFormCollapse = '';
    },
    onRoleSubmit() {
      this.$refs.roleForm.validate(valid => {
        if (valid) {
          // 只修改页面上的字段
          const data = {
            name: this.roleForm.name,
            code: this.roleForm.code,
          };
          if (this.roleForm.id) {
            if (this.roleForm.immutable) {
              delete this.roleForm.code;
            }
            editRole(this.roleForm.id, data).then(() => {
              this.$message.success(`角色 [${data.name}] 更新成功！`);
              this.roleVisible = false;
              this.findRoleList();
            });
          } else {
            createRole(data).then(() => {
              this.$message.success(`角色 [${data.name}] 创建成功！`);
              this.roleVisible = false;
              this.findRoleList();
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
