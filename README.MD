## 通用库

## libcommon
为经常使用的工具库

依赖
`compile 'com.hongfans:common:1.0.0'`


## libcommon1
为较少使用的工具库

依赖
`compile 'com.hongfans:common1:1.0.0'`

## libcommonhf
为 hf 特有的工具库

---

# 模块之间的依赖关系
* com.hongfans.common 不依赖任何第三方库, 包括不依赖 supportv7,
不依赖 retrofit ？

* com.hongfans.common1 可以依赖 com.hongfans.common