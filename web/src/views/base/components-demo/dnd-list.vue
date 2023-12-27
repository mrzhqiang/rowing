<template>
  <div class="components-container">
    <aside>drag-list base on
      <a href="https://github.com/SortableJS/Vue.Draggable" target="_blank">Vue.Draggable</a>
    </aside>
    <div class="editor-container">
      <dnd-list :list1="list1" :list2="list2" list1-title="List" list2-title="Article pool"/>
    </div>
  </div>
</template>

<script>
import DndList from '@/components/DndList';

export default {
  name: 'DndListDemo',
  components: {DndList},
  data() {
    return {
      list1: [],
      list2: []
    };
  },
  created() {
  },
  methods: {
  }
};
</script>

