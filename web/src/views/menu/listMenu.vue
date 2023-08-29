<template>
  <div class="app-container">
    <el-table v-loading="menuLoading" :data="menuData" row-key="id" stripe size="mini" default-expand-all>
      <el-table-column prop="title" label="标题" width="200" show-overflow-tooltip>
        <template v-slot="data">
          <span>{{ generateTitle(data.row.title) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="icon" label="图标" width="100">
        <template v-slot="data">
          <svg-icon :icon-class="data.row.icon"/>
        </template>
      </el-table-column>
      <el-table-column prop="fullPath" label="路径" width="300" show-overflow-tooltip/>
      <el-table-column prop="created" label="创建时间" width="150"/>
      <el-table-column prop="updated" label="更新时间" width="150"/>
      <el-table-column prop="enabled" label="是否启用" width="100"/>
      <el-table-column prop="ordered" label="排序"/>
    </el-table>
    <pagination v-show="menuData.length" :total="page.totalElements"
                :page.sync="params.page" :size.sync="params.size"
                @pagination="findRootMenus"/>
  </div>
</template>

<script>
import {generateTitle} from '@/utils/i18n';
import {searchMenu} from '@/api/menu';
import Pagination from '@/components/Pagination';

export default {
  name: 'ListMenu',
  components: {Pagination},
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      };
      return statusMap[status];
    }
  },
  data() {
    return {
      menuLoading: true,
      menuData: [],
      page: {
        totalElements: 0,
        totalPages: 0
      },
      params: {
        page: 1,
        size: 20
      }
    };
  },
  created() {
    this.findRootMenus();
  },
  methods: {
    generateTitle,
    findRootMenus() {
      this.menuLoading = true;
      searchMenu('root', this.params).then(response => {
        this.menuData = response._embedded.menu || [];
        this.page = response.page;
        this.menuLoading = false;
      });
    },
  }
};
</script>

<style scoped>
</style>
