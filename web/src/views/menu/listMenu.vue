<template>
  <div class="app-container">
    <el-table v-loading="menuLoading" :data="menuData" row-key="id" stripe default-expand-all>
      <el-table-column prop="id" label="#" width="48" :align="'right'"/>
      <el-table-column prop="title" label="标题" min-width="120" show-overflow-tooltip>
        <template v-slot="data">
          <span>{{ generateTitle(data.row.title) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="icon" label="图标" min-width="96">
        <template v-slot="data">
          <i v-if="data.row.icon && data.row.icon.includes('el-icon')" :class="data.row.icon"/>
          <svg-icon v-else-if="data.row.icon" :icon-class="data.row.icon"/>
        </template>
      </el-table-column>
      <el-table-column prop="fullPath" label="路径" min-width="240" show-overflow-tooltip/>
      <el-table-column prop="created" label="创建时间" min-width="120"/>
      <el-table-column prop="updated" label="更新时间" min-width="120"/>
      <el-table-column prop="enabled" label="是否启用" min-width="96"/>
      <el-table-column prop="ordered" label="排序" min-width="48"/>
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
