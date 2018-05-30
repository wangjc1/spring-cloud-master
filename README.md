
Spring Boot SPI实现原理
Spring Boot里面有很多初始化实现，这些实现是通过SPI来加载的，在META-INF/spring.factories文件里指明了默认接口的实现，而这个配置文件是通过SpringFactoriesLoader类来解析的。

@EnableAutoConfiguration注解解析类
以默认容器配置为例：
spring.factories：
org.springframework.boot.autoconfigure.web.aEmbeddedServletContainerAutoConfiguration,\

public static List<String> loadFactoryNames(Class<?> factoryClass, ClassLoader classLoader) {
	String factoryClassName = factoryClass.getName();
	try {
		Enumeration<URL> urls = (classLoader != null ? classLoader.getResources(FACTORIES_RESOURCE_LOCATION) :
				ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION));
		List<String> result = new ArrayList<String>();
		while (urls.hasMoreElements()) {
			URL url = urls.nextElement();
			Properties properties = PropertiesLoaderUtils.loadProperties(new UrlResource(url));
			//获取所有的配置URL
			String factoryClassNames = properties.getProperty(factoryClassName);
			result.addAll(Arrays.asList(StringUtils.commaDelimitedListToStringArray(factoryClassNames)));
		}
		return result;
	}
	catch (IOException ex) {
		throw new IllegalArgumentException("Unable to load [" + factoryClass.getName() +
				"] factories from location [" + FACTORIES_RESOURCE_LOCATION + "]", ex);
	}
}

http://rensanning.iteye.com/blog/2355938
在EmbeddedServletContainerAutoConfiguration类中初始化Tomcat容器
@Configuration
@ConditionalOnClass({ Servlet.class, Tomcat.class })
@ConditionalOnMissingBean(value = EmbeddedServletContainerFactory.class, search = SearchStrategy.CURRENT)
public static class EmbeddedTomcat {
	@Bean
	public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() {
		return new TomcatEmbeddedServletContainerFactory();
	}
}

ConfigurationClassBeanDefinitionReader，把Tomcat容器类加载成Spring Definition Bean
private void registerBeanDefinitionForImportedConfigurationClass(ConfigurationClass configClass) {
	AnnotationMetadata metadata = configClass.getMetadata();
	AnnotatedGenericBeanDefinition configBeanDef = new AnnotatedGenericBeanDefinition(metadata);

	ScopeMetadata scopeMetadata = scopeMetadataResolver.resolveScopeMetadata(configBeanDef);
	configBeanDef.setScope(scopeMetadata.getScopeName());
	String configBeanName = this.importBeanNameGenerator.generateBeanName(configBeanDef, this.registry);
	AnnotationConfigUtils.processCommonDefinitionAnnotations(configBeanDef, metadata);

	BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(configBeanDef, configBeanName);
	definitionHolder = AnnotationConfigUtils.applyScopedProxyMode(scopeMetadata, definitionHolder, this.registry);
	this.registry.registerBeanDefinition(definitionHolder.getBeanName(), definitionHolder.getBeanDefinition());
	configClass.setBeanName(configBeanName);

	if (logger.isDebugEnabled()) {
		logger.debug("Registered bean definition for imported class '" + configBeanName + "'");
	}
}





//返回类中定义的公共、私有、保护的内部类，但不包括从父类继承的内部类
Class[] getDeclaredClasses()

//返回类定义的公共的内部类,以及从父类、父接口那里继承来的内部类
Class[] getClasses()



ConfigurationClassParser把配置文件中的类转换成ConfigurationClass对象
public void parse(Set<BeanDefinitionHolder> configCandidates) {
	this.deferredImportSelectors = new LinkedList<DeferredImportSelectorHolder>();

	for (BeanDefinitionHolder holder : configCandidates) {
		BeanDefinition bd = holder.getBeanDefinition();
		try {
			//如果入口类上含有@EnableAutoConfiguration注解
			if (bd instanceof AnnotatedBeanDefinition) {
				parse(((AnnotatedBeanDefinition) bd).getMetadata(), holder.getBeanName());
			}
			else if (bd instanceof AbstractBeanDefinition && ((AbstractBeanDefinition) bd).hasBeanClass()) {
				parse(((AbstractBeanDefinition) bd).getBeanClass(), holder.getBeanName());
			}
			else {
				parse(bd.getBeanClassName(), holder.getBeanName());
			}
		}
		catch (BeanDefinitionStoreException ex) {
			throw ex;
		}
		catch (Throwable ex) {
			throw new BeanDefinitionStoreException(
					"Failed to parse configuration class [" + bd.getBeanClassName() + "]", ex);
		}
	}

	//解析@Import(EnableAutoConfigurationImportSelector.class)资源，selectImports()方法返回要导入的资源列表
	processDeferredImportSelectors();
}

利用EnableAutoConfigurationImportSelector这个类获取到SPI的接口列表，通过解析类文件，加载内部类等
private void processDeferredImportSelectors() {
	List<DeferredImportSelectorHolder> deferredImports = this.deferredImportSelectors;
	this.deferredImportSelectors = null;
	Collections.sort(deferredImports, DEFERRED_IMPORT_COMPARATOR);

	for (DeferredImportSelectorHolder deferredImport : deferredImports) {
		ConfigurationClass configClass = deferredImport.getConfigurationClass();
		try {

			String[] imports = deferredImport.getImportSelector().selectImports(configClass.getMetadata());
			processImports(configClass, asSourceClass(configClass), asSourceClasses(imports), false);
		}
		catch (BeanDefinitionStoreException ex) {
			throw ex;
		}
		catch (Throwable ex) {
			throw new BeanDefinitionStoreException(
					"Failed to process import candidates for configuration class [" +
					configClass.getMetadata().getClassName() + "]", ex);
		}
	}
}


访问者模式，从class中及其的内部类中获取bean对象，并建立beanName和bean的映射关系
SimpleMetadataReader(Resource resource, ClassLoader classLoader) throws IOException {
	InputStream is = new BufferedInputStream(resource.getInputStream());
	ClassReader classReader;
	try {
		classReader = new ClassReader(is);
	}
	catch (IllegalArgumentException ex) {
		throw new NestedIOException("ASM ClassReader failed to parse class file - " +
				"probably due to a new Java class file version that isn't supported yet: " + resource, ex);
	}
	finally {
		is.close();
	}

	AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor(classLoader);
	classReader.accept(visitor, ClassReader.SKIP_DEBUG);

	this.annotationMetadata = visitor;
	// (since AnnotationMetadataReadingVisitor extends ClassMetadataReadingVisitor)
	this.classMetadata = visitor;
	this.resource = resource;
}


@Configuration注解不加到类上发现也没啥问题，看了源码发现他被@Component标注了，说明其真正的作用是把配置类也变成一个容器对象，可以被扫描到
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration {}

@Configuration注解的解析类ConfigurationClassPostProcessor，主要解析方法如下，里面代码比较多，这里只关注比较核心的一句
public void processConfigBeanDefinitions(BeanDefinitionRegistry registry) {
	//Spring 会把要Spring容器bean先解析成ConfigurationBean，这个解析过程也是比较复杂的，然后再包装成BeanDefinition对象注册到容器中
	this.reader.loadBeanDefinitions(configClasses);
}

SpringApplication
public ConfigurableApplicationContext run(String... args) {
	StopWatch stopWatch = new StopWatch();
	stopWatch.start();
	ConfigurableApplicationContext context = null;
	FailureAnalyzers analyzers = null;
	configureHeadlessProperty();
	//获取SpringApplicationRunListener接口的实现类(配置在META-INF/spring.factories文件中),共分为4步：
	//1. 先创建一个new SpringApplicationRunListeners()集合类，用于存放SpringApplicationRunListener，做统一调用等操作
	//2. 调用getSpringFactoriesInstances()方法，通过listener的构造方法来实例化
	//3. 从spring.factories文件中加载listener的实现，调用SpringFactoriesLoader.loadFactoryNames(type, classLoader))
	//4. 通过调用资源类的构造方法来实例化资源类List<T> instances = createSpringFactoriesInstances
	SpringApplicationRunListeners listeners = getRunListeners(args);
	listeners.starting();
	try {
		//SpringApplication.run(Application.class,args);处理入口类里args参数
		//用IDEA运行时，发现默认会添加一个spring.output.ansi.enabled=ALWAYS参数，可以启用彩色日志输出
		ApplicationArguments applicationArguments = new DefaultApplicationArguments(
				args);


		//1. 获取系统属性和环境变量：getOrCreateEnvironment()
		//2. listeners里有个ConfigFileApplicationListener，用于加载配置文件
		//3. 先获取应用名(可以通过--spring.config.name=xxx参数配置)，如果没有设置则取默认值"application"
		//4. 获取profiles.active参数(通过--spring.profiles.active=dev参数配置或属性文件配置)
		//5. 在"classpath:/,classpath:/config/,file:./,file:./config/"路径下搜索 location + appName + "-" + profile + "." + ext
		//6. 例如搜索application-dev.properties、application-dev.yml、application-dev.xml等配置文件
		//6.
		ConfigurableEnvironment environment = prepareEnvironment(listeners,
				applicationArguments);

		//自定义启动图案，在根下创建banner.txt文件
		//关闭启动图案setBannerMode(Banner.Mode.OFF);
		Banner printedBanner = printBanner(environment);
		context = createApplicationContext();
		analyzers = new FailureAnalyzers(context);
		prepareContext(context, environment, listeners, applicationArguments,
				printedBanner);
		refreshContext(context);
		afterRefresh(context, applicationArguments);
		listeners.finished(context, null);
		stopWatch.stop();
		if (this.logStartupInfo) {
			new StartupInfoLogger(this.mainApplicationClass)
					.logStarted(getApplicationLog(), stopWatch);
		}
		return context;
	}
	catch (Throwable ex) {
		handleRunFailure(context, listeners, analyzers, ex);
		throw new IllegalStateException(ex);
	}
}



private <T> Collection<? extends T> getSpringFactoriesInstances(Class<T> type,
		Class<?>[] parameterTypes, Object... args) {
	// 从当前线程中获取资源句柄，这个句柄客户获取当前运行环境下所有资源
	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	// Use names and ensure unique to protect against duplicates
	//资源名称列表，从spring.factories文件中根据type类型获取对应的资源
	Set<String> names = new LinkedHashSet<String>(
			SpringFactoriesLoader.loadFactoryNames(type, classLoader));
	//通过调用资源类的构造方法来完成实例化
	List<T> instances = createSpringFactoriesInstances(type, parameterTypes,
			classLoader, args, names);
	//如果资源类有多种实现，按Oder接口排序
	AnnotationAwareOrderComparator.sort(instances);
	return instances;
}





java是一门动态单分配和静态多分配的语言
有人把 Java 的重载说成是“静态多分派”，有待商榷。重载是在编译时决定要用哪个函数，是静态的，而一般来说，分派常常是指在运行时，如何决定调用哪个函数，是动态的。

单分派所调用的函数，由单个量决定，常常是被调用的Object 的实际类型，而不考虑传入参数的实际类型。但多分派，通常会考虑传入参数的实际类型。当然，具体细节在各个语言中还有不同。

举个例子，在Java中，先创建三个类， Fruit, Apple, Banana, 当然，apple，banana，是 Fruit 的子类。
class Fruit{
}

class Apple extends Fruit{
}

class Banana extends Fruit{
}

再创建两个类，People, Boy。Boy是People的子类，并 override People中的三个method.

class People{
    public void eat(Fruit f)
    {
        System.out.println("People eat Fruit");
    }
    public void eat(Apple f)
    {
        System.out.println("People eat Apple");
    }
    public void eat(Banana f)
    {
        System.out.println("People eat Banana");
    }
}

class Boy extends People{
    public void eat(Fruit f)
    {
        System.out.println("Boy eats Fruit");
    }
    public void eat(Apple f)
    {
        System.out.println("Boy eats Apple");
    }
    public void eat(Banana f)
    {
        System.out.println("Boy eats Banana");
    }

再来个main()，看看print 出来的是什么。
 public static void main(String[] argu)
  {
        People boy = new Boy();
        Fruit apple = new Apple();
        Fruit banana = new Banana();

        boy.eat(apple);
        boy.eat(banana);
   }

结果是：
Boy eats Fruit
Boy eats Fruit
分派发生在此，“boy”被声明为People，但调用的依然是Boy的函数，也就是所谓的多态。但是，虽然传入的参数"apple"的实际类型是Apple，但调用的函数依然是Boy的“ public void eat(Fruit f)”，不是“ public void eat(Apple f)”。运行时，Java参数的实际类型不影响分派，这就是单分派了。

在java中，利用访问者模式，可以实现多分配
//在运行时，传入不同node的子类，可以访问不同的node
visitor.visit(Node node)