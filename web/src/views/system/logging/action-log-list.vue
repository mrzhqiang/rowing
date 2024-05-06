<template>
  <div class="app-container">
    <el-form ref="logSearchForm" v-model="logParams" inline>
      <el-form-item :label="$t('操作')" prop="type">
        <el-select v-model="logParams.type" clearable>
          <el-option v-for="item in actionTypeDictItems"
                     :key="item.value" :label="item.label" :value="item.value"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="onLogSearch">{{ $t('搜索') }}</el-button>
        <el-button icon="el-icon-refresh" @click="onResetLogSearch">{{ $t('重置') }}</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="logLoading" :data="logList" row-key="id" size="mini"
              stripe border highlight-current-row>
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
import {DICT_CODES, searchDict} from '@/api/dict';
import {searchActionLog} from '@/api/log';
import Pagination from '@/components/Pagination';

export default {
  name: 'ActionLogList',
  components: {Pagination},
  data() {
    return {
      logParams: {
        type: '',
        page: 0,
        size: 20,
      },
      logLoading: false,
      logList: [],
      logPage: {totalElements: 0, totalPages: 0},
      actionTypeDictItems: [],
    };
  },
  created() {
    this.findLogList();
    this.findActionType();
  },
  methods: {
    findActionType() {
      searchDict('code', {code: DICT_CODES.actionType}).then(response => {
        this.actionTypeDictItems = response._embedded.items;
      });
    },
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
        type: '',
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
