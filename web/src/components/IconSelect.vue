<template>
  <div class="icon-body">
    <el-input v-model="name" style="position: relative;"
              :placeholder="$t('请输入名称搜索图标')" clearable
              @clear="filterIcons" @input.native="filterIcons">
      <i slot="suffix" class="el-icon-search el-input__icon"/>
    </el-input>
    <div class="icon-list">
      <el-tabs v-model="iconType" type="border-card" @tab-click="filterIcons">
        <el-tab-pane :label="$t('icons')" name="icons">
          <div class="grid">
            <div v-for="item of iconList" :key="item" class="icon-item" @click="selectedIcon(item)">
              <svg-icon :icon-class="item" class-name="disabled" style="height: 16px; width: 16px;"/>
              <span>{{ item }}</span>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane :label="$t('element-ui-icons')" name="element-ui-icons">
          <div class="grid">
            <div v-for="item of iconList" :key="item" class="icon-item" @click="selectedIcon(`el-icon-${item}`)">
              <i :class="`el-icon-${item}`" style="height: 16px; width: 16px;"/>
              <span>{{ `el-icon-${item}` }}</span>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import svgIcons from '@/icons/svg-icons';
import elementIcons from '@/icons/element-icons';

export default {
  name: 'IconSelect',
  data() {
    return {
      svgIcons,
      elementIcons,
      name: '',
      iconList: [],
      iconType: 'icons'
    };
  },
  methods: {
    initIconList() {
      this.iconList = this.iconType === 'icons' ? [...svgIcons] : [...elementIcons];
    },
    filterIcons() {
      this.initIconList();
      if (this.name) {
        this.iconList = this.iconList.filter(item => item.includes(this.name));
      }
    },
    selectedIcon(name) {
      this.$emit('selected', name);
      document.body.click();
    },
    reset() {
      this.name = '';
      this.initIconList();
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.icon-body {
  width: 100%;
  padding: 10px;

  .icon-list {
    height: 100%;

    span {
      margin-left: 4px;
      display: inline-block;
      vertical-align: -0.15em;
      fill: currentColor;
      overflow: hidden;
    }

    .grid {
      position: relative;
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
      margin-bottom: -5px;
      overflow-y: scroll;
      max-height: 480px;
    }

    .icon-item {
      width: 100%;
      height: 32px;
      float: left;
      cursor: pointer;
    }

    .disabled {
      pointer-events: none;
    }
  }
}
</style>
