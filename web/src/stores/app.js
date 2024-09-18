import { defineStore } from "pinia";
import { reactive, ref } from "vue";
import { getLanguage } from '@/lang/index.js';

const sidebar = reactive({
  opened: true,
  withoutAnimation: false
});
const device = ref('desktop');
const langCode = getLanguage();
const language = ref(langCode);
const size = ref('medium');

function toggleSidebar() {
  sidebar.opened = !sidebar.opened;
  sidebar.withoutAnimation = false;
}

function closeSidebar(withoutAnimation) {
  sidebar.opened = false;
  sidebar.withoutAnimation = withoutAnimation;
}

function toggleDevice(newDevice) {
  device.value = newDevice;
}

function setLanguage(newLanguage) {
  language.value = newLanguage;
}

function setSize(newSize) {
  size.value = newSize;
}

export const useAppStore = defineStore('app', () => {
  return { sidebar, device, language, size, toggleSidebar, closeSidebar, toggleDevice, setLanguage, setSize }
}, { persist: true });
