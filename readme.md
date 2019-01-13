>学习目文章[探索Android路由框架-ARouter之基本使用（一）](https://www.jianshu.com/p/6021f3f61fa6)

[ARouter github地址](https://github.com/alibaba/ARouter)

#### 一、前言

ARouter是阿里android技术团队开源的一款路由框架，这款路由框架可以为应用提供更好更丰富的跳转方案。

比如：
1. 支持解析标准URL进行跳转，并自动注入参数到目标页面中；
2. 支持添加多个拦截器，自定义拦截顺序（满足拦截器设置的条件才允许跳转）

阿里云栖社区的一段话：原生路由方案一般是通过显式intent和隐式intent两种方案实现（跳转Activity 或 Fragment）。

在显式intent的情况下，因为会存在直接的类依赖的问题，导致耦合非常严重；而在隐式intent的情况下，会出现规则集中式管理，
导致协作变得非常困难。一般而言规则配置都是在Manifest中的，这就导致了扩展性较差。

除此之外，使用原生的路由方案会出现跳转过程无法控制的问题，一旦使用了startActivity()就无法插手其中任何环节了，只能交给系统管理，
这就导致了在跳转失败的情况下无法降级，而是会直接抛出运营级的异常。这种情况下，如果使用自定义的路由组件就可以解决以上问题。

比如：
1. 通过URL索引解决类依赖的问题
2. 通过分布式管理页面配置解决隐式intent集中式管理Path的问题
3. 自己实现整个路由过程可以拥有良好的扩展性，可以通过AOP的方式解决跳转过程无法控制的问题，与此同时也能够提供非常灵活的降级方式。

#### 二、简单使用

1. 按照官方文档添加依赖
2. Application中初始化
3. 给需要通过ARouter路由跳转的 Activity 或 Fragment 添加path标识
```java
    @Route(path = "/app/SimpleActivity")
    public class SimpleActivity extends BaseActivity {
        ...
    }
```
4.在要发起跳转的位置进行ARouter注入
```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ARouter.getInstance().inject(this);
        ...
        }
```
5. 触发跳转
```java
     ARouter.getInstance().build("/app/SimpleActivity").navigation();
```

这里可以看到：
1. 如果路径的标签过多就要单独写一个类，把所有的标签统一管理起来，以方便后续开发和维护。
2. 每个页面的注入，即 ARouter.getInstance().inject(this); 这句代码出现的次数会非常的多，需要进行抽取。
而且按照常规的逻辑，有注入，一般就会有解绑或者释放资源，这个也许要进行抽取处理。

#### 三、简单封装

1. path是一定要统一管理的（放到一个类中去）
2. 优秀的第三方框架如果有注入或者绑定的API，那与之对应的一般就会有释放或者解绑资源的API。
在Application中进行的ARouter的初始化，同样在Application要进行destory
```java
    @Override
    public void onTerminate() {
        super.onTerminate();
        ARouter.getInstance().destroy();
    }
```

#### 四、带参数的界面跳转
带参数的跳转是很常见的功能，使用intent跳转可以通过Bundle传递数据，而通过ARouter跳转，传递参数需要注意：

1. 对象需要Parcelable或者Serializable序列化
2. 字符串、char、int等基本类型都是可以传递的，也可以直接传Bundle、数组、列表等很多对象































