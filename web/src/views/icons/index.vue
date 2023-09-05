<template>
  <div class="icons-container">
    <aside>
      <a href="https://panjiachen.github.io/vue-element-admin-site/guide/advanced/icon.html" target="_blank">Add and use
      </a>
    </aside>
    <el-input v-model="name" style="position: relative;"
              :placeholder="$t('请输入名称搜索图标')" clearable
              @clear="filterIcons" @input.native="filterIcons">
      <i slot="suffix" class="el-icon-search el-input__icon"/>
    </el-input>
    <el-tabs v-model="tabName" type="border-card" @tab-click="filterIcons">
      <el-tab-pane label="Icons" name="icons">
        <div class="grid">
          <div v-for="item of iconList" :key="item" @click="handleClipboard(generateIconCode(item),$event)">
            <el-tooltip placement="top">
              <div slot="content">
                {{ generateIconCode(item) }}
              </div>
              <div class="icon-item">
                <svg-icon :icon-class="item" class-name="disabled"/>
                <span>{{ item }}</span>
              </div>
            </el-tooltip>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="Element-UI Icons" name="element-ui-icons">
        <div class="grid">
          <div v-for="item of iconList" :key="item" @click="handleClipboard(generateElementIconCode(item),$event)">
            <el-tooltip placement="top">
              <div slot="content">
                {{ generateElementIconCode(item) }}
              </div>
              <div class="icon-item">
                <i :class="'el-icon-' + item"/>
                <span>{{ item }}</span>
              </div>
            </el-tooltip>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import clipboard from '@/utils/clipboard';
import svgIcons from '@/icons/svg-icons';
import elementIcons from '@/icons/element-icons';

export default {
  name: 'Icons',
  data() {
    return {
      svgIcons,
      elementIcons,
      iconList: [],
      name: '',
      tabName: 'icons',
    };
  },
  created() {
    this.initIconList();
  },
  methods: {
    generateIconCode(symbol) {
      return `<svg-icon icon-class="${symbol}" />`;
    },
    generateElementIconCode(symbol) {
      return `<i class="el-icon-${symbol}" />`;
    },
    initIconList() {
      this.iconList = this.tabName === 'icons' ? [...svgIcons] : [...elementIcons];
    },
    filterIcons() {
      this.initIconList();
      if (this.name) {
        this.iconList = this.iconList.filter(item => item.includes(this.name));
      }
    },
    handleClipboard(text, event) {
      clipboard(text, event);
    },
  }
};
</script>

<style lang="scss" scoped>
.icons-container {
  margin: 10px 20px 0;
  overflow: hidden;

  .grid {
    position: relative;
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  }

  .icon-item {
    margin: 20px;
    height: 85px;
    text-align: center;
    width: 100px;
    float: left;
    font-size: 30px;
    color: #24292e;
    cursor: pointer;
  }

  span {
    display: block;
    font-size: 16px;
    margin-top: 10px;
  }

  .disabled {
    pointer-events: none;
  }
}
</style>
