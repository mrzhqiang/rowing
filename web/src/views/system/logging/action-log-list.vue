<template>
  <div class="app-container">
    <el-form ref="logSearchForm" v-model="logParams" inline>
      <el-form-item :label="$t('操作')" prop="action">
        <el-input v-model="logParams.action" clearable/>
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
      <el-table-column prop="action" :label="$t('操作')" min-width="40" show-overflow-tooltip/>
      <el-table-column prop="type" :label="$t('类型')" min-width="50" show-overflow-tooltip/>
      <el-table-column prop="target" :label="$t('调用类')" min-width="100" show-overflow-tooltip/>
      <el-table-column prop="method" :label="$t('方法')" min-width="50" show-overflow-tooltip/>
      <el-table-column prop="params" :label="$t('参数')" min-width="80" show-overflow-tooltip/>
      <el-table-column prop="state" :label="$t('状态')" min-width="50" show-overflow-tooltip/>
      <el-table-column prop="result" :label="$t('结果')" min-width="100" show-overflow-tooltip/>
      <el-table-column prop="operator" :label="$t('操作人')" min-width="50" show-overflow-tooltip/>
      <el-table-column prop="ip" :label="$t('IP')" min-width="50" show-overflow-tooltip/>
      <el-table-column prop="location" :label="$t('位置')" min-width="40" show-overflow-tooltip/>
      <el-table-column prop="device" :label="$t('设备')" min-width="100" show-overflow-tooltip/>
      <el-table-column prop="timestamp" :label="$t('时间')" min-width="80" :align="'center'"/>
    </el-table>
    <pagination v-show="logList.length" :total="logPage.totalElements"
                :page.sync="logParams.page" :size.sync="logParams.size"
                @pagination="findLogList"/>
  </div>
</template>

<script>
import {searchActionLog} from '@/api/log';
import Pagination from '@/components/Pagination';

export default {
  name: 'ActionLogList',
  components: {Pagination},
  data() {
    return {
      logParams: {
        action: '',
        page: 0,
        size: 20,
      },
      logLoading: true,
      logList: [],
      logPage: {totalElements: 0, totalPages: 0},
    };
  },
  created() {
    this.findLogList();
  },
  methods: {
    findLogList() {
      this.logLoading = true;
      searchActionLog('page', this.logParams).then(response => {
        this.logList = response._embedded.actionLogs;
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
        action: '',
        page: 0,
        size: 20,
      };
      if (this.$refs.logSearchForm) {
        this.$refs.logSearchForm.resetFields();
      }
      this.findLogList();
    },
  }
};
</script>

<style scoped>

</style>
