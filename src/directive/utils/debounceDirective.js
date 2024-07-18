const debounceDirective = {
  bind(el, binding) {
    let timer = null;

    const eventHandler = () => {
      if (timer) {
        clearTimeout(timer);
      }
      timer = setTimeout(() => {
        binding.value();
      }, 500);
    };

    // 将事件处理程序绑定到元素上，并在元素上存储它以便以后删除
    el.__vueDebounceHandler__ = eventHandler;
    el.addEventListener('click', eventHandler);
  },
  unbind(el) {
    // 从元素中删除事件处理程序
    el.removeEventListener('click', el.__vueDebounceHandler__);
    delete el.__vueDebounceHandler__;
  },
  update(el, binding) {
    // 更新指令时重新绑定事件处理程序
    el.removeEventListener('click', el.__vueDebounceHandler__);

    let timer = null;
    const eventHandler = () => {
      if (timer) {
        clearTimeout(timer);
      }
      timer = setTimeout(() => {
        binding.value();
      }, 500);
    };

    el.__vueDebounceHandler__ = eventHandler;
    el.addEventListener('click', eventHandler);
  }
};
export default debounceDirective;