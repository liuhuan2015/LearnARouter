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

在接收方通过@Autowired注解来进行接收
```java
    @Autowired(name = "username")
    String name;
    @Autowired(name = "userage")
    int age;
    @Autowired(name = "student")
    Student student;
```
**注意：接收方同样需要inject，否则接收不到数据**
```java
  ARouter.getInstance().inject(this);
```

#### 五、界面跳转动画
直接调用withTransition,里面传入两个动画即可（R.anim.xxx）
```java
    ARouter.getInstance().build("/app/SimpleActivity").withTransition(R.anim.enter_anim, R.anim.exit_anim).navigation();
```

#### 六、使用URI进行跳转
ARouter框架也可以使用URI进行匹配跳转，只需匹配路径一致即可进行跳转。使用这种跳转方式有什么使用场景呢？不知道。
```java
    findViewById(R.id.btn_jump_by_uri).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(PathConstants.PATH_JUMP_BY_URI);
                ARouter.getInstance().build(uri).navigation();
            }
        });
```
#### 七、Fragment跳转
使用ARouter也可以进行Fragment的跳转，可参照Activity的跳转，第一步仍是先写上类注释，然后是强转。
```java
    Fragment fragment=(Fragment)ARouter.getInstance().build(Constance.ACTIVITY_URL_FRAGMENT).navigation();
```
#### 八、进阶用法---拦截器

拦截器是ARouter框架的一个亮点，说起拦截器，可能印象更加深刻的是OkHttp的拦截器，OkHttp的拦截器主要是用来拦截请求体（比如添加请求Cookie）和拦截响应体（判断token是否过期），
在真正的请求和响应前做一些判断和修改然后再去进行操作，大抵这就是拦截器的简单概念。

ARouter的拦截器，是通过实现IInterceptor接口，重写init()和process()方法去完成拦截器内部操作的。
```java
@Interceptor(priority = 1)
public class UseIInterceptor implements IInterceptor {

    @Override
    public void init(Context context) {
        Log.e("-----", "UseIInterceptor 拦截器 init ...");
    }

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        String name = Thread.currentThread().getName();

        Log.e("-----", "UseIInterceptor 拦截器开始执行，线程名称 ：" + name);
    }

}
```
定义ARouter拦截器必须要使用@Interceptor(priority = 1)类注解，里面的priority声明了拦截器的优先级，数值越小的，优先级越高，会最先执行。
ARouter的拦截器中是不允许有两个拦截器的优先级一样的，有一样的话项目编译时就会报错。

拦截器使用:
```java
        findViewById(R.id.btn_jump_with_interceptor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(PathConstants.PATH_URL_INTERCEPTOR)
                        .navigation(MainActivity.this,
                                new NavigationCallback() {
                                    @Override
                                    public void onFound(Postcard postcard) {
                                        // 路由目标被发现时调用
                                        Log.e("-----", "onFound...");
                                        String group = postcard.getGroup();
                                        String path = postcard.getPath();
                                        Log.e("-----", "group: " + group + " path: " + path);
                                    }

                                    @Override
                                    public void onLost(Postcard postcard) {
                                        Log.e("-----", "onLost...");
                                    }

                                    @Override
                                    public void onArrival(Postcard postcard) {
                                        // 路由到达后调用
                                        Log.e("-----", "onArrival...");

                                    }

                                    @Override
                                    public void onInterrupt(Postcard postcard) {
                                        // 路由拦截时调用
                                        Log.e("-----", "onInterrupt...");
                                    }
                                });
            }
        });
```

项目运行时，就会进行两个拦截器的初始化，然后调用 NavigationCallback 这个回调函数里面的onFound(),
然后执行拦截器里面的process()方法，最终回调到NavigationCallback里面的onArrival()方法。拦截器的工作流程大概如此。

对于NavigationCallback的简单理解：ARouter在路由跳转的过程中，我们可以监听路由的具体过程，onFound，onArrival，onLost，onInterrupt。
这四个方法中的 Postcard 参数表示什么意思呢？A container that contains the roadmap.(直译过来意思就是：一个包含线路图的容器),
通过它可以获得路由相应的信息。

路径的组：
```java
  String group = postcard.getGroup();
```
ARouter框架在项目编译期会扫描所有的注册页面/字段/拦截器，在运行期不会一股脑全部加载进来，而是使用了分组管理。
根据打印的日志，可以发现 getGroup() 的值默认就是第一个//(两个分隔符)之间的内容。

因此，我们也可以使用ARouter的分组，来进行界面跳转。

#### 九、ARouter如何实现类似startActivityForResult()?
```java
    findViewById(R.id.btn_mock_startActivityForResult).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ARouter.getInstance().build(PathConstants.PATH_STARTACTIVITY_FORRESULT)
                    .withString("name", "zhangsan")
                    .withInt("age", 3)
                    .navigation(MainActivity.this, 123);
        }
    });
```












































