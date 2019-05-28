# mvc流程
1. DispatcherServlet拦截用户请求
2. 根据HandlerMapping配置将请求映射到处理器
3. 请求适配器执行处理器
4. 从适配器中获取执行后返回的ModelAndView
5. ModelAndView的解析和渲染，最后写入response响应
