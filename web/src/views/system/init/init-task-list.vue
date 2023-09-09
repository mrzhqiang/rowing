<template>
  <div class="app-container">
    <el-form ref="initTaskSearchForm" v-model="initTaskParams" inline>
      <el-form-item :label="$t('名称')" prop="name">
        <el-input v-model="initTaskParams.name" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini"
                   @click="onInitTaskSearch">{{ $t('搜索') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini"
                   @click="onResetInitTaskSearch">{{ $t('重置') }}
        </el-button>
      </el-form-item>
    </el-form>

    <!-- TODO：后续再实现自定义初始化任务功能 -->
    <!--<el-row :gutter="10" style="margin-bottom: 1rem">
      <el-col :span="2">
        <el-button v-permission="initTaskPermission.initTask.create"
                   type="primary" icon="el-icon-plus" plain
                   @click="onInitTaskCreate">{{ $t('创建') }}
        </el-button>
      </el-col>
    </el-row>-->

    <el-table v-loading="initTaskLoading" :data="initTaskList" row-key="id" stripe border>
      <el-table-column prop="id" label="#" min-width="20" :align="'right'"/>
      <el-table-column prop="name" :label="$t('名称')" min-width="80" show-overflow-tooltip/>
      <el-table-column prop="path" :label="$t('路径')" min-width="120" show-overflow-tooltip/>
      <el-table-column prop="type" :label="$t('类型')" min-width="40" :align="'center'"/>
      <el-table-column prop="status" :label="$t('状态')" min-width="40" :align="'center'"/>
      <el-table-column prop="ordered" :label="$t('执行顺序')" min-width="40" :align="'center'"/>
      <el-table-column prop="discard" :label="$t('是否废弃')" min-width="40" :align="'center'"/>
      <el-table-column prop="createdBy" :label="$t('创建人')" min-width="40" :align="'center'"/>
      <el-table-column prop="created" :label="$t('创建时间')" min-width="80" :align="'center'"/>
      <el-table-column prop="updatedBy" :label="$t('更新人')" min-width="40" :align="'center'"/>
      <el-table-column prop="updated" :label="$t('更新时间')" min-width="80" :align="'center'"/>
      <el-table-column :label="$t('操作')" min-width="100" :align="'center'">
        <template v-slot="scope">
          <el-button size="mini" icon="el-icon-notebook-2" type="text"
                     @click="onInitTaskLog(scope)">{{ $t('日志') }}
          </el-button>
          <el-popconfirm style="margin: 0 10px" :title="$t('确定执行吗？')"
                         @onConfirm="onInitTaskExecute(scope)">
            <el-button slot="reference" v-permission="initTaskPermission.execute"
                       size="mini" icon="el-icon-caret-right" type="text">{{ $t('执行') }}
            </el-button>
          </el-popconfirm>
          <el-button v-permission="initTaskPermission.edit"
                     size="mini" icon="el-icon-edit" type="text"
                     @click="onInitTaskEdit(scope)">{{ $t('编辑') }}
          </el-button>
          <el-popconfirm v-if="scope.row.discardCode === 'NO'"
                         style="margin-left: 10px" :title="$t('确定废弃吗？')"
                         @onConfirm="onInitTaskDiscard(scope)">
            <el-button slot="reference" v-permission="initTaskPermission.edit"
                       size="mini" icon="el-icon-circle-close" type="text">{{ $t('废弃') }}
            </el-button>
          </el-popconfirm>
          <el-popconfirm v-if="scope.row.discardCode === 'YES'"
                         style="margin-left: 10px" :title="$t('确定启用吗？')"
                         @onConfirm="onInitTaskDiscard(scope)">
            <el-button slot="reference" v-permission="initTaskPermission.edit"
                       size="mini" icon="el-icon-circle-check" type="text">{{ $t('启用') }}
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="initTaskList.length" :total="initTaskPage.totalElements"
                :page.sync="initTaskParams.page" :size.sync="initTaskParams.size"
                @pagination="findInitTaskList"/>

    <el-dialog v-el-drag-dialog :title="initTaskTitle" :visible.sync="initTaskVisible"
               :close-on-click-modal="false" append-to-body>
      <el-form ref="initTaskForm" :model="initTaskForm" :rules="initTaskRules" label-width="80px">
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('名称')" prop="name">
              <el-input v-model="initTaskForm.name"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('状态')" prop="status">
              <el-select v-model="initTaskForm.status">
                <el-option v-for="item in statusOptions"
                           :key="item.value" :label="item.label" :value="item.value"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('排序')" prop="ordered">
              <el-input-number v-model="initTaskForm.ordered" controls-position="right" :min="1"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="initTaskVisible = false">{{ $t('取消') }}</el-button>
        <el-button type="primary" @click="onInitTaskSubmit">{{ $t('提交') }}</el-button>
      </div>
    </el-dialog>

    <el-drawer :title="initTaskLogTitle" :visible.sync="initTaskLogVisible" destroy-on-close>
      <el-timeline>
        <el-timeline-item v-for="log in initTaskLogList" :key="log.id"
                          placement="top" :timestamp="log.created"
                          :type="log.trace ? 'danger' : 'success'">
          <el-card>
            <div slot="header" class="clearfix">
              <span :style="log.trace ? 'color: red' : ''">{{ log.message }}</span>
            </div>
            <span style="margin-right: 8px">{{ $t('执行人') }}</span>
            <el-tag :type="log.trace ? 'danger' : 'success'">{{ log.createdBy }}</el-tag>
            <span style="margin-left: 8px; margin-right: 8px">{{ $t('执行时间') }}</span>
            <el-tag :type="log.trace ? 'danger' : 'success'">{{ log.created }}</el-tag>
            <el-button v-if="log.trace" type="text" style="float: right; padding: 3px 0"
                       @click="onInitTaskLogTraceOpen(log.trace)">{{ $t('查看详情') }}
            </el-button>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </el-drawer>

    <el-dialog v-el-drag-dialog :title="initTaskLogTraceTitle"
               :visible.sync="initTaskLogTraceVisible" width="66%" append-to-body>
      <el-card class="stack-trace" shadow="hover">
        <pre>{{ initTaskLogTrace }}</pre>
      </el-card>
      <div slot="footer" class="dialog-footer">
        <el-button @click="onInitTaskLogTraceClose">{{ $t('关闭') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog';
import {PERMISSION_MARK} from '@/utils/permission';
import {searchDict, DICT_CODES} from '@/api/dict';
import {findInitTask, editInitTask, searchInitTask, createInitTask, executeInitTask} from '@/api/init';
import rest from '@/api/rest';
import Pagination from '@/components/Pagination';

export default {
  name: 'InitTaskList',
  directives: {elDragDialog},
  components: {Pagination},
  data() {
    return {
      initTaskParams: {
        name: '',
        page: 0,
        size: 20,
      },
      initTaskPermission: {...PERMISSION_MARK.initTask},
      initTaskLoading: true,
      initTaskList: [],
      initTaskPage: {totalElements: 0, totalPages: 0},
      initTaskTitle: '',
      initTaskVisible: false,
      initTaskForm: {
        id: null,
        name: '',
        path: '',
        type: '',
        status: '',
        ordered: 1,
        discard: 'NO'
      },
      initTaskRules: {},
      statusOptions: [],
      initTaskLogTitle: this.$t('初始化任务日志'),
      initTaskLogVisible: false,
      initTaskLogList: [],
      initTaskLogTraceTitle: this.$t('初始化任务日志堆栈'),
      initTaskLogTraceVisible: false,
      initTaskLogTrace: ''
    };
  },
  created() {
    this.findStatusDict();
    this.findInitTaskList();
  },
  methods: {
    findStatusDict() {
      searchDict('code', {code: DICT_CODES.taskStatus}).then(response => {
        this.statusOptions = response._embedded.items;
      });
    },
    findInitTaskList() {
      this.initTaskLoading = true;
      searchInitTask('page', this.initTaskParams).then(response => {
        this.initTaskList = response._embedded.initTasks;
        this.initTaskPage = response.page;
        this.initTaskLoading = false;
      });
    },
    onInitTaskSearch() {
      this.initTaskParams.page = 0;
      this.findInitTaskList();
    },
    onResetInitTaskSearch() {
      this.initTaskParams = {
        name: '',
        page: 0,
        size: 20,
      };
      if (this.$refs.initTaskSearchForm) {
        this.$refs.initTaskSearchForm.resetFields();
      }
      this.findInitTaskList();
    },
    onInitTaskExecute({row}) {
      executeInitTask(row.path).then(() => {
        this.$message.success('执行成功！');
      });
    },
    onInitTaskLog({row}) {
      rest.link(row._links.logs.href).then(response => {
        this.initTaskLogVisible = true;
        this.initTaskLogList = response._embedded.initTaskLogs;
      });
    },
    resetInitTaskForm() {
      if (this.$refs.initTaskForm) {
        this.$refs.initTaskForm.resetFields();
      }
      this.initTaskForm = {
        id: null,
        name: '',
        path: '',
        type: '',
        status: '',
        ordered: 1,
        discard: 'NO'
      };
    },
    fillInitTaskForm(form) {
      this.initTaskForm = form;
    },
    onInitTaskEdit({row}) {
      this.resetInitTaskForm();
      findInitTask(row.id, 'init-task-form').then(response => {
        this.fillInitTaskForm(response);
        this.initTaskForm.id = row.id;
        this.initTaskTitle = this.$t('编辑初始化任务');
        this.initTaskVisible = true;
      });
    },
    onInitTaskDiscard({row}) {
      const discard = row.discardCode === 'YES' ? 'NO' : 'YES';
      if (row && row.id) {
        editInitTask(row.id, {discard}).then(() => {
          const message = row.discardCode === 'YES'
              ? this.$t('初始化任务 {title} 启用成功！', {title: this.initTaskForm.title})
              : this.$t('初始化任务 {title} 废弃成功！', {title: this.initTaskForm.title});
          this.$message.success(message);
          this.findInitTaskList();
        });
      }
    },
    onInitTaskSubmit() {
      this.$refs.initTaskForm.validate(valid => {
        if (valid) {
          // 只能编辑名称、状态、执行顺序
          const data = {
            name: this.initTaskForm.name,
            status: this.initTaskForm.status,
            ordered: this.initTaskForm.ordered,
          };
          if (this.initTaskForm.id) {
            editInitTask(this.initTaskForm.id, data).then(() => {
              this.$message.success(this.$t('初始化任务 {title} 更新成功！', {title: this.initTaskForm.title}));
              this.initTaskVisible = false;
              this.findInitTaskList();
            });
          } else {
            createInitTask(this.initTaskForm).then(() => {
              this.$message.success(this.$t('初始化任务 {title} 创建成功！', {title: this.initTaskForm.title}));
              this.initTaskVisible = false;
              this.findInitTaskList();
            });
          }
        }
      });
    },
    onInitTaskLogTraceOpen(trace) {
      this.initTaskLogTrace = trace;
      this.initTaskLogTraceVisible = true;
    },
    onInitTaskLogTraceClose() {
      this.initTaskLogTrace = '';
      this.initTaskLogTraceVisible = false;
    },
  }
};
</script>

<style scoped>
.stack-trace {
  overflow: auto;
  font-family: monospace;
  background-color: #f5f7fa;
}
</style>
