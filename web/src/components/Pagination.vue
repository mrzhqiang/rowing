<template>
  <div class="pagination-container" :class="{'hidden':hidden}">
    <el-pagination :background="background" :layout="layout"
                   :total="total" :current-page.sync="currentPage" :page-size.sync="pageSize"
                   :page-sizes="pageSizes" v-bind="$attrs"
                   @size-change="handleSizeChange"
                   @current-change="handleCurrentChange"/>
  </div>
</template>

<script>
import {scrollTo} from '@/utils/scroll-to';

export default {
  name: 'Pagination',
  props: {
    total: {
      required: true,
      type: Number
    },
    page: {
      type: Number,
      default: 1
    },
    size: {
      type: Number,
      default: 20
    },
    pageSizes: {
      type: Array,
      default() {
        // max page size is 1000
        return [10, 20, 50, 100, 200, 500, 1000];
      }
    },
    layout: {
      type: String,
      default: 'total, sizes, prev, pager, next, jumper'
    },
    small: {
      type: Boolean,
      default: true
    },
    background: {
      type: Boolean,
      default: true
    },
    autoScroll: {
      type: Boolean,
      default: true
    },
    hidden: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    currentPage: {
      get() {
        return this.page;
      },
      set(val) {
        this.$emit('update:page', Math.max(0, val));
      }
    },
    pageSize: {
      get() {
        return this.size;
      },
      set(val) {
        this.$emit('update:size', Math.max(20, val));
      }
    }
  },
  methods: {
    handleSizeChange(val) {
      this.$emit('pagination', {page: Math.max(0, this.currentPage), size: val});
      if (this.autoScroll) {
        scrollTo(0, 800, null);
      }
    },
    handleCurrentChange(val) {
      this.$emit('pagination', {page: Math.max(0, val), size: this.pageSize});
      if (this.autoScroll) {
        scrollTo(0, 800, null);
      }
    }
  }
};
</script>

<style scoped>
.pagination-container {
  background: #fff;
  padding: 16px 16px;
  text-align: right;
}

.pagination-container.hidden {
  display: none;
}
</style>
