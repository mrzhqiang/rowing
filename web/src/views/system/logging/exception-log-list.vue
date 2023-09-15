<template>
  <div class="app-container">
    <el-form ref="logSearchForm" v-model="logParams" inline>
      <el-form-item :label="$t('请求URL')" prop="url">
        <el-input v-model="logParams.url" clearable/>
      </el-form-item>
      <el-form-item :label="$t('异常代码')" prop="code">
        <el-input v-model="logParams.code" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini"
                   @click="onLogSearch">{{ $t('搜索') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini"
                   @click="onResetLogSearch">{{ $t('重置') }}
        </el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="logLoading" :data="logList" row-key="id" stripe border>
      <el-table-column prop="id" label="#" min-width="20" :align="'right'"/>
      <el-table-column prop="status" :label="$t('状态码')" min-width="20"/>
      <el-table-column prop="method" :label="$t('请求方法')" min-width="30" :align="'center'"/>
      <el-table-column prop="url" :label="$t('请求URL')" min-width="100" show-overflow-tooltip/>
      <el-table-column prop="query" :label="$t('查询参数')" min-width="80" show-overflow-tooltip/>
      <el-table-column prop="address" :label="$t('客户端IP')" min-width="50" show-overflow-tooltip/>
      <el-table-column prop="sessionId" :label="$t('会话ID')" min-width="100" show-overflow-tooltip/>
      <el-table-column prop="message" :label="$t('异常消息')" min-width="100" show-overflow-tooltip/>
      <el-table-column prop="code" :label="$t('异常代码')" min-width="40" :align="'center'"/>
      <el-table-column prop="operator" :label="$t('操作人')" min-width="40" :align="'center'"/>
      <el-table-column prop="timestamp" :label="$t('操作时间')" min-width="80" :align="'center'"/>
      <el-table-column :label="$t('操作')" min-width="50" :align="'center'">
        <template v-slot="scope">
          <el-button size="mini" icon="el-icon-notebook-2" type="text"
                     :disabled="scope.trace"
                     @click="onLogTrace(scope)">{{ $t('堆栈') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="logList.length" :total="logPage.totalElements"
                :page.sync="logParams.page" :size.sync="logParams.size"
                @pagination="findLogList"/>

    <el-dialog v-el-drag-dialog :title="logTraceTitle"
               :visible.sync="logTraceVisible" width="66%" append-to-body>
      <el-card class="stack-trace" shadow="hover">
        <pre>{{ logTrace }}</pre>
      </el-card>
      <div slot="footer" class="dialog-footer">
        <el-button @click="onLogTraceClose">{{ $t('关闭') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {searchExceptionLog} from '@/api/log';
import Pagination from '@/components/Pagination';
import elDragDialog from '@/directive/el-drag-dialog';

export default {
  name: 'ExceptionLogList',
  directives: {elDragDialog},
  components: {Pagination},
  data() {
    return {
      logParams: {
        url: '',
        code: '',
        page: 0,
        size: 20,
      },
      logLoading: true,
      logList: [],
      logPage: {totalElements: 0, totalPages: 0},
      logTraceTitle: this.$t('异常日志堆栈'),
      logTraceVisible: false,
      logTrace: ''
    };
  },
  created() {
    this.findLogList();
  },
  methods: {
    findLogList() {
      this.logLoading = true;
      searchExceptionLog('page', this.logParams).then(response => {
        this.logList = response._embedded.exceptionLogs;
        this.logPage = response.page;
        this.logLoading = false;
      });
    },
    onLogSearch() {
      this.logParams.page = 0;
      this.findLogList();
    },
    onResetLogSearch() {
      this.logParams = {
        url: '',
        code: '',
        page: 0,
        size: 20,
      };
      if (this.$refs.logSearchForm) {
        this.$refs.logSearchForm.resetFields();
      }
      this.findLogList();
    },
    onLogTrace({row}) {
      this.logTrace = row.trace;
      this.logTraceVisible = true;
    },
    onLogTraceClose() {
      this.logTraceVisible = false;
      this.logTrace = '';
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
