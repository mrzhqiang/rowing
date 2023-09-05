<template>
  <div class="app-container">
    <el-form ref="searchMenuForm" v-model="menuParams" inline>
      <el-form-item :label="$t('标题')" prop="title">
        <el-input v-model="menuParams.title"/>
      </el-form-item>
      <el-form-item :label="$t('路径')" prop="path">
        <el-input v-model="menuParams.path"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="onMenuSearch">
          {{ $t('搜索') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="onResetMenuSearch">
          {{ $t('重置') }}
        </el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" style="margin-bottom: 1rem">
      <el-col :span="2">
        <el-button v-permission="menuPermission.create"
                   type="primary" icon="el-icon-plus" plain
                   @click="onMenuCreate">{{ $t('创建') }}
        </el-button>
      </el-col>
    </el-row>

    <el-table v-loading="menuLoading" :data="menuList" row-key="id" stripe border highlight-current-row>
      <el-table-column prop="id" label="#" min-width="20" :align="'right'"/>
      <el-table-column prop="icon" :label="$t('图标')" min-width="20" :align="'center'">
        <template v-slot="scope">
          <i v-if="scope.row.icon && scope.row.icon.includes('el-icon')" :class="scope.row.icon"/>
          <svg-icon v-else-if="scope.row.icon" :icon-class="scope.row.icon"/>
        </template>
      </el-table-column>
      <el-table-column prop="title" :label="$t('标题')" min-width="50" show-overflow-tooltip>
        <template v-slot="scope">
          <el-button size="mini" type="text" @click="onMenuEdit(scope, true)">
            {{ generateTitle(scope.row.title) }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="path" :label="$t('路径')" min-width="100" show-overflow-tooltip/>
      <el-table-column prop="fullPath" :label="$t('完整路径')" min-width="100" show-overflow-tooltip/>
      <el-table-column prop="component" :label="$t('组件')" min-width="50" show-overflow-tooltip/>
      <el-table-column prop="enabled" :label="$t('是否启用')" min-width="40" :align="'center'"/>
      <el-table-column prop="internal" :label="$t('是否内置')" min-width="40" :align="'center'"/>
      <el-table-column prop="createdBy" :label="$t('创建人')" min-width="40" :align="'center'"/>
      <el-table-column prop="created" :label="$t('创建时间')" min-width="80" :align="'center'"/>
      <el-table-column prop="updatedBy" :label="$t('更新人')" min-width="40" :align="'center'"/>
      <el-table-column prop="updated" :label="$t('更新时间')" min-width="80" :align="'center'"/>
      <el-table-column :label="$t('操作')" min-width="100" :align="'center'">
        <template v-slot="scope">
          <el-button v-permission="menuPermission.create"
                     size="mini" icon="el-icon-plus" type="text"
                     @click="onMenuAdd(scope)">{{ $t('添加') }}
          </el-button>
          <el-button v-permission="menuPermission.edit" :disabled="scope.row.internal === 'YES'"
                     size="mini" icon="el-icon-edit" type="text"
                     @click="onMenuEdit(scope, false)">{{ $t('编辑') }}
          </el-button>

          <el-popconfirm style="margin-left: 10px" :title="$t('确定删除吗？')"
                         @onConfirm="onMenuDelete(scope)">
            <el-button slot="reference" v-permission="menuPermission.delete"
                       :disabled="scope.row.internal === 'YES'"
                       size="mini" icon="el-icon-delete" type="text">{{ $t('删除') }}
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="menuList.length" :total="menuPage.totalElements"
                :page.sync="menuParams.page" :size.sync="menuParams.size"
                @pagination="findMenuList"/>

    <el-dialog :title="menuTitle" :visible.sync="menuVisible"
               :close-on-click-modal="!menuFormEditable"
               append-to-body @close="onMenuClose">
      <el-form ref="menuForm" :model="menuForm" :rules="menuRules"
               :disabled="!menuFormEditable" label-width="80px">
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('上级菜单')" prop="parent">
              <!--不要用 element-ui 的 Cascader 级联选择器，是个大坑，问题超多！-->
              <tree-select v-model="menuForm.parent" :options="menuTreeOptions" :normalizer="normalizer"
                           :disabled="!menuFormEditable" :show-count="true" :placeholder="$t('选择上级菜单')"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('图标')" prop="icon">
              <el-popover ref="iconPopover" placement="bottom-start" width="680" trigger="click"
                          @show="$refs['iconSelect'].reset()">
                <icon-select ref="iconSelect" @selected="onMenuIconSelected"/>
                <el-input slot="reference" v-model="menuForm.icon" :placeholder="$t('点击选择图标')" readonly>
                  <i v-if="menuForm.icon && menuForm.icon.includes('el-icon')" slot="prefix" :class="menuForm.icon"/>
                  <svg-icon v-else-if="menuForm.icon" slot="prefix" :icon-class="menuForm.icon" class="el-input__icon"/>
                  <i v-else slot="prefix" class="el-icon-search el-input__icon"/>
                </el-input>
              </el-popover>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('标题')" prop="title">
              <el-input v-model="menuForm.title"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('路径')" prop="path">
              <el-input v-model="menuForm.path"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('组件')" prop="component">
              <el-input v-model="menuForm.component"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('重定向')" prop="redirect">
              <el-input v-model="menuForm.redirect"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('排序')" prop="ordered">
              <el-input-number v-model="menuForm.ordered" controls-position="right" :min="1"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('启用')" prop="enabled">
              <el-radio-group v-model="menuForm.enabled">
                <el-radio v-for="item in logicDict" :key="item.value" :label="item.value">{{ item.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-collapse v-model="menuFormOther" accordion>
          <el-collapse-item :title="$t('展开更多选项')" name="other">
            <el-row :gutter="10">
              <el-col :span="8">
                <el-form-item :label="$t('是否隐藏')" prop="hidden">
                  <el-switch v-model="menuForm.hidden"/>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item :label="$t('活动菜单')" prop="activeMenu">
                  <el-input v-model="menuForm.activeMenu" :disabled="!menuForm.hidden"/>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item :label="$t('置顶展示')" prop="alwaysShow">
                  <el-switch v-model="menuForm.alwaysShow"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :span="8">
                <el-form-item :label="$t('取消缓存')" prop="noCache">
                  <el-switch v-model="menuForm.noCache"/>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item :label="$t('固定标签')" prop="affix">
                  <el-switch v-model="menuForm.affix"/>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item :label="$t('面包屑栏')" prop="breadcrumb">
                  <el-switch v-model="menuForm.breadcrumb"/>
                </el-form-item>
              </el-col>
            </el-row>
          </el-collapse-item>
        </el-collapse>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="menuFormEditable" type="primary" @click="onMenuSubmit">{{ $t('提交') }}</el-button>
        <el-button v-if="menuFormEditable" @click="menuVisible = false">{{ $t('取消') }}</el-button>
        <el-button v-if="!menuFormEditable" @click="menuVisible = false">{{ $t('关闭') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import utils from '@/utils/common';
import {generateTitle} from '@/utils/i18n';
import {PERMISSION_MARK} from '@/utils/permission';
import {DICT_CODES, searchDict} from '@/api/dict';
import {createMenu, deleteMenu, editMenu, findMenu, searchMenu} from '@/api/menu';
import {clearTemplate} from '@/api/rest';
import Pagination from '@/components/Pagination';
import TreeSelect from '@riophae/vue-treeselect';
import '@riophae/vue-treeselect/dist/vue-treeselect.css';
import IconSelect from '@/components/IconSelect';

export default {
  name: 'MenuList',
  components: {Pagination, TreeSelect, IconSelect},
  data() {
    return {
      menuParams: {
        // 注意：搜索的所有条件参数必须传 '' 值，否则将导致查询不到任何数据
        title: '',
        path: '',
        page: 0,
        size: 20,
      },
      menuPermission: {...PERMISSION_MARK.menu},
      menuLoading: true,
      menuList: [],
      menuPage: {totalElements: 0, totalPages: 0},
      menuTitle: '',
      menuVisible: false,
      menuForm: {
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
      menuRules: {
        title: [{required: true, message: this.$t('菜单名称不能为空'), trigger: 'blur'}],
        path: [{required: true, message: this.$t('菜单路径不能为空'), trigger: 'blur'}],
        component: [{required: true, message: this.$t('菜单组件不能为空'), trigger: 'blur'}]
      },
      menuFormEditable: false,
      menuFormOther: '',
      menuTreeOptions: [],
      logicDict: [],
    };
  },
  created() {
    this.findMenuTree();
    this.findLogicDict();
    this.findMenuList();
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
      return {
        id: clearTemplate(node._links.self.href),
        label: this.generateTitle(node.label),
        children: node.children,
        isDisabled: node.isDisabled
      };
    },
    findMenuTree() {
      searchMenu('tree', {projection: 'menu-option'}).then(response => {
        const menu = {id: '', label: this.generateTitle('(无)'), isDisabled: false};
        this.menuTreeOptions = [menu, ...response._embedded.menus];
      });
    },
    findLogicDict() {
      searchDict('code', {code: DICT_CODES.logic}).then(response => {
        this.logicDict = response._embedded.items;
      });
    },
    findMenuList() {
      this.menuLoading = true;
      searchMenu('page', this.menuParams).then(response => {
        this.menuList = response._embedded.menus;
        this.menuPage = response.page;
        this.menuLoading = false;
      });
    },
    onMenuSearch() {
      this.findMenuList();
    },
    onResetMenuSearch() {
      this.menuParams = {
        title: '',
        path: '',
        page: 0,
        size: 20,
      };
      if (this.$refs.searchMenuForm) {
        this.$refs.searchMenuForm.resetFields();
      }
      this.findMenuList();
    },
    resetMenuForm() {
      if (this.$refs.menuForm) {
        this.$refs.menuForm.resetFields();
      }
      this.menuForm = {
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
      };
      this.menuFormOther = '';
    },
    fillMenuForm(form) {
      this.menuForm = form;
      this.menuForm.parent = '';
      if (form.parentId) {
        for (const option of this.menuTreeOptions) {
          const node = utils.findTreeNode(option, form.parentId);
          if (node) {
            this.menuForm.parent = clearTemplate(node._links.self.href);
            break;
          }
        }
      }
    },
    onMenuCreate() {
      this.resetMenuForm();
      this.menuFormEditable = true;
      this.menuTitle = this.$t('创建菜单');
      this.menuVisible = true;
    },
    onMenuAdd({row}) {
      this.resetMenuForm();
      this.menuFormEditable = true;
      // spring data rest 在添加其他实体时，需要使用 URL 而不是实体 id
      this.menuForm.parent = clearTemplate(row._links.self.href);
      this.menuTitle = this.$t('添加菜单');
      this.menuVisible = true;
    },
    onMenuEdit({row}, readonly = false) {
      this.resetMenuForm();
      this.menuFormEditable = !readonly;
      findMenu(row.id, 'menu-form').then(response => {
        this.fillMenuForm(response);
        this.menuForm.id = row.id;
        this.menuTitle = readonly ? this.$t('查看菜单') : this.$t('编辑菜单');
        this.menuVisible = true;
      });
    },
    onMenuDelete({row}) {
      deleteMenu(row.id).then(() => {
        this.$message.success(this.$t('菜单 {title} 删除成功！', {title: row.title}));
        this.findMenuList();
      });
    },
    onMenuClose() {
      this.menuFormOther = '';
      if (this.$refs.iconPopover) {
        this.$refs.iconPopover.doClose();
      }
    },
    onMenuIconSelected(name) {
      this.menuForm.icon = name;
    },
    onMenuSubmit() {
      this.$refs.menuForm.validate(valid => {
        if (valid) {
          // 不是绝对地址（外部链接），才进行二次校验
          const parent = this.menuForm.parent;
          const path = this.menuForm.path;
          if (!utils.isAbsoluteUrl(path)) {
            // 如果设置上级菜单，则 path 不能以 / 开头
            if (parent && path.startsWith('/')) {
              this.$message.error(this.$t('子级菜单的路径不能以 {path} 开头！', {path: '/'}));
              return;
            }
            if (!parent && !path.startsWith('/')) {
              this.$message.error(this.$t('顶级菜单的路径必须以 {path} 开头！', {path: '/'}));
              return;
            }
          }
          // 只修改页面上的字段
          const data = {
            parent: this.menuForm.parent,
            icon: this.menuForm.icon,
            title: this.menuForm.title,
            path: this.menuForm.path,
            component: this.menuForm.component,
            redirect: this.menuForm.redirect,
            activeMenu: this.menuForm.activeMenu,
            hidden: this.menuForm.hidden,
            alwaysShow: this.menuForm.alwaysShow,
            noCache: this.menuForm.noCache,
            affix: this.menuForm.affix,
            breadcrumb: this.menuForm.breadcrumb,
            ordered: this.menuForm.ordered,
            enabled: this.menuForm.enabled
          };
          if (this.menuForm.id) {
            editMenu(this.menuForm.id, data).then(() => {
              this.$message.success(this.$t('菜单 {title} 更新成功！', {title: this.menuForm.title}));
              this.menuVisible = false;
              this.findMenuList();
            });
          } else {
            createMenu(data).then(() => {
              this.$message.success(this.$t('菜单 {title} 创建成功！', {title: this.menuForm.title}));
              this.menuVisible = false;
              this.findMenuList();
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
