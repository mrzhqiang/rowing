<template>
  <div class="app-container">
    <el-form ref="searchForm" v-model="params" inline>
      <el-form-item :label="$t('标题')" prop="title">
        <el-input v-model="params.title"/>
      </el-form-item>
      <el-form-item :label="$t('路径')" prop="path">
        <el-input v-model="params.path"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="onSearch">
          {{ $t('搜索') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="onResetSearch">
          {{ $t('重置') }}
        </el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" style="margin-bottom: 1rem">
      <el-col :span="2">
        <el-button v-permission="permission.menu.create"
                   type="primary" icon="el-icon-plus" plain
                   @click="onCreate">{{ $t('创建') }}
        </el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" row-key="id" stripe border highlight-current-row>
      <el-table-column prop="id" label="#" min-width="20" :align="'right'"/>
      <el-table-column prop="icon" :label="$t('图标')" min-width="40" :align="'center'">
        <template v-slot="scope">
          <i v-if="scope.row.icon && scope.row.icon.includes('el-icon')" :class="scope.row.icon"/>
          <svg-icon v-else-if="scope.row.icon" :icon-class="scope.row.icon"/>
        </template>
      </el-table-column>
      <el-table-column prop="title" :label="$t('标题')" min-width="50" show-overflow-tooltip>
        <template v-slot="scope">
          <el-button size="mini" type="text" @click="onEdit(scope)">
            {{ generateTitle(scope.row.title) }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="path" :label="$t('路径')" min-width="100" show-overflow-tooltip/>
      <el-table-column prop="fullPath" :label="$t('完整路径')" min-width="100" show-overflow-tooltip/>
      <el-table-column prop="component" :label="$t('组件')" min-width="50" show-overflow-tooltip/>
      <el-table-column prop="enabled" :label="$t('是否启用')" min-width="40" :align="'center'"/>
      <el-table-column prop="createdBy" :label="$t('创建人')" min-width="40" :align="'center'"/>
      <el-table-column prop="created" :label="$t('创建时间')" min-width="80" :align="'center'"/>
      <el-table-column prop="updatedBy" :label="$t('更新人')" min-width="40" :align="'center'"/>
      <el-table-column prop="updated" :label="$t('更新时间')" min-width="80" :align="'center'"/>
      <el-table-column :label="$t('操作')" min-width="100" :align="'center'">
        <template v-slot="scope">
          <el-button v-permission="permission.menu.create"
                     size="mini" icon="el-icon-plus" type="text"
                     @click="onAdd(scope)">{{ $t('添加') }}
          </el-button>
          <el-button v-permission="permission.menu.edit"
                     size="mini" icon="el-icon-edit" type="text"
                     @click="onEdit(scope, false)">{{ $t('编辑') }}
          </el-button>
          <el-button v-permission="permission.menu.delete"
                     size="mini" icon="el-icon-delete" type="text"
                     @click="onDelete(scope)">{{ $t('删除') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="dataList.length" :total="page.totalElements"
                :page.sync="params.page" :size.sync="params.size"
                @pagination="findMenuData"/>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" :close-on-click-modal="!formEditable"
               append-to-body @close="onClose">
      <el-form ref="form" :model="form" :rules="rules" :disabled="!formEditable" label-width="80px">
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('上级菜单')" prop="parent">
              <!--不要用 element-ui 的 Cascader 级联选择器，是个大坑，问题超多！-->
              <tree-select v-model="form.parent" :options="options" :normalizer="normalizer"
                           :disabled="!formEditable" :show-count="true" :placeholder="$t('选择上级菜单')"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('图标')" prop="icon">
              <el-popover ref="iconPopover" placement="bottom-start" width="680" trigger="click"
                          @show="$refs['iconSelect'].reset()">
                <icon-select ref="iconSelect" @selected="onIconSelected"/>
                <el-input slot="reference" v-model="form.icon" :placeholder="$t('点击选择图标')" readonly>
                  <i v-if="form.icon && form.icon.includes('el-icon')" slot="prefix" :class="form.icon"/>
                  <svg-icon v-else-if="form.icon" slot="prefix" :icon-class="form.icon" class="el-input__icon"/>
                  <i v-else slot="prefix" class="el-icon-search el-input__icon"/>
                </el-input>
              </el-popover>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('标题')" prop="title">
              <el-input v-model="form.title"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('路径')" prop="path">
              <el-input v-model="form.path"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('组件')" prop="component">
              <el-input v-model="form.component"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('重定向')" prop="redirect">
              <el-input v-model="form.redirect"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('排序')" prop="ordered">
              <el-input-number v-model="form.ordered" controls-position="right" :min="1"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('启用')" prop="enabled">
              <el-radio-group v-model="form.enabled">
                <el-radio v-for="item in logicDict"
                          :key="item.value" :label="item.value">{{ item.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-collapse v-model="formOther" accordion>
          <el-collapse-item :title="$t('展开更多选项')" name="other">
            <el-row :gutter="10">
              <el-col :span="8">
                <el-form-item :label="$t('是否隐藏')" prop="hidden">
                  <el-switch v-model="form.hidden"/>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item :label="$t('活动菜单')" prop="activeMenu">
                  <el-input v-model="form.activeMenu" :disabled="!form.hidden"/>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item :label="$t('置顶展示')" prop="alwaysShow">
                  <el-switch v-model="form.alwaysShow"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :span="8">
                <el-form-item :label="$t('取消缓存')" prop="noCache">
                  <el-switch v-model="form.noCache"/>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item :label="$t('固定标签')" prop="affix">
                  <el-switch v-model="form.affix"/>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item :label="$t('面包屑栏')" prop="breadcrumb">
                  <el-switch v-model="form.breadcrumb"/>
                </el-form-item>
              </el-col>
            </el-row>
          </el-collapse-item>
        </el-collapse>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="formEditable" type="primary" @click="onSubmit">{{ $t('提交') }}</el-button>
        <el-button v-if="formEditable" @click="onCancel">{{ $t('取消') }}</el-button>
        <el-button v-if="!formEditable" @click="onCancel">{{ $t('关闭') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {clearTemplate} from '@/api/rest';
import utils from '@/utils/common';
import {generateTitle} from '@/utils/i18n';
import {PERMISSION_MARK} from '@/utils/permission';
import {createMenu, findMenu, editMenu, deleteMenu, searchMenu} from '@/api/menu';
import {searchDict, DICT_CODES} from '@/api/dict';
import Pagination from '@/components/Pagination';
import TreeSelect from '@riophae/vue-treeselect';
import '@riophae/vue-treeselect/dist/vue-treeselect.css';
import IconSelect from '@/components/IconSelect';

export default {
  name: 'MenuList',
  components: {Pagination, TreeSelect, IconSelect},
  data() {
    return {
      params: {
        // 注意：搜索的所有条件参数必须传 '' 值，否则将导致查询不到任何数据
        title: '',
        path: '',
        page: 0,
        size: 20
      },
      permission: {...PERMISSION_MARK},
      loading: true,
      dataList: [],
      page: {totalElements: 0, totalPages: 0},
      dialogTitle: this.$t('创建菜单'),
      dialogVisible: false,
      initForm: {
        id: null,
        parent: '',
        icon: '',
        title: '',
        path: '',
        component: '',
        redirect: null,
        activeMenu: null,
        hidden: false,
        alwaysShow: undefined,
        noCache: false,
        affix: undefined,
        breadcrumb: true,
        ordered: 1,
        enabled: 'YES'
      },
      form: {...this.initForm},
      rules: {
        title: [{required: true, message: this.$t('菜单名称不能为空'), trigger: 'blur'}],
        path: [{required: true, message: this.$t('菜单路径不能为空'), trigger: 'blur'}],
        component: [{required: true, message: this.$t('菜单组件不能为空'), trigger: 'blur'}]
      },
      formEditable: false,
      formOther: '',
      options: [],
      logicDict: [],
    };
  },
  created() {
    this.findMenuTree();
    this.findLogicDict();
    this.findMenuData();
  },
  methods: {
    generateTitle,
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      if (node.id === '') {
        return node;
      }
      clearTemplate(node._links.self);
      return {
        id: node._links.self.href,
        label: this.generateTitle(node.label),
        children: node.children,
        isDisabled: node.isDisabled
      };
    },
    findMenuTree() {
      searchMenu('tree', {projection: 'menu-option'}).then(response => {
        const menu = {id: '', label: this.generateTitle('(无)'), isDisabled: false};
        this.options = [menu, ...response._embedded.menu];
      });
    },
    findLogicDict() {
      searchDict('code', {code: DICT_CODES.logic}).then(response => {
        this.logicDict = response._embedded.items;
      });
    },
    findMenuData() {
      this.loading = true;
      searchMenu('page', this.params).then(response => {
        this.dataList = response._embedded.menu;
        this.page = response.page;
        this.loading = false;
      });
    },
    onSearch() {
      this.findMenuData();
    },
    onResetSearch() {
      this.params = {title: '', path: '', page: 0, size: 20};
      if (this.$refs.searchForm) {
        this.$refs.searchForm.resetFields();
      }
      this.findMenuData();
    },
    resetForm() {
      if (this.$refs.form) {
        this.$refs.form.resetFields();
      }
      this.form = {...this.initForm};
      this.formOther = '';
    },
    fillForm(form) {
      this.form = form;
      this.form.parent = '';
      if (form.parentId) {
        for (const option of this.options) {
          const node = utils.findTreeNode(option, form.parentId);
          if (node) {
            clearTemplate(node._links.self);
            this.form.parent = node._links.self.href;
            break;
          }
        }
      }
    },
    onCreate() {
      this.resetForm();
      this.formEditable = true;
      this.dialogTitle = this.$t('创建菜单');
      this.dialogVisible = true;
    },
    onAdd({row}) {
      this.resetForm();
      this.formEditable = true;
      // spring data rest 在添加其他实体时，需要使用 URL 而不是实体 id
      clearTemplate(row._links.self);
      this.form.parent = row._links.self.href;
      this.dialogTitle = this.$t('添加菜单');
      this.dialogVisible = true;
    },
    onEdit({row}, readonly = true) {
      this.resetForm();
      this.formEditable = !readonly;
      findMenu(row.id, 'menu-detail').then(response => {
        this.fillForm(response);
        this.form.id = row.id;
        this.dialogTitle = readonly ? this.$t('查看菜单') : this.$t('编辑菜单');
        this.dialogVisible = true;
      });
    },
    onDelete({row}) {
      this.$confirm(this.$t('此操作将永久删除该菜单, 是否继续?'), this.$t('提示'), {
        confirmButtonText: this.$t('确定'),
        cancelButtonText: this.$t('取消'),
        type: 'warning'
      }).then(() => {
        deleteMenu(row.id).then(() => {
          this.$message.success(this.$tc('菜单 {title} 删除成功！', {title: row.title}));
          this.findMenuData();
        });
      }).catch(() => {
        console.log(this.$t('已取消删除'));
      });
    },
    onClose() {
      this.formOther = '';
      if (this.$refs.iconPopover) {
        this.$refs.iconPopover.doClose();
      }
    },
    onIconSelected(name) {
      this.form.icon = name;
    },
    onSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          // 不是绝对地址（外部链接），才进行二次校验
          const parent = this.form.parent;
          const path = this.form.path;
          if (!utils.isAbsoluteUrl(path)) {
            // 如果设置上级菜单，则 path 不能以 / 开头
            if (parent && path.startsWith('/')) {
              this.$message.error(this.$tc('子级菜单的路径不能以 {path} 开头！', {path: '/'}));
              return;
            }
            if (!parent && !path.startsWith('/')) {
              this.$message.error(this.$tc('顶级菜单的路径必须以 {path} 开头！', {path: '/'}));
              return;
            }
          }
          if (this.form.id) {
            editMenu(this.form.id, this.form).then(() => {
              this.$message.success(this.$tc('菜单 {title} 更新成功！', {title: this.form.title}));
              this.dialogVisible = false;
              this.findMenuData();
            });
          } else {
            createMenu(this.form).then(() => {
              this.$message.success(this.$tc('菜单 {title} 创建成功！', {title: this.form.title}));
              this.dialogVisible = false;
              this.findMenuData();
            });
          }
        }
      });
    },
    onCancel() {
      this.dialogVisible = false;
    },
  }
};
</script>

<style scoped>
</style>
