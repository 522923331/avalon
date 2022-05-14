# Maven 中<optional>true</optional>和<scope>provided</scope>之间的区别

## 传递依赖

依赖管理是maven提供的主要功能之一，无论我们需要什么依赖，只需将它们添加到 POM.xml 中，在构建或运行时所有必要的类和资源都会自动添加到项目的 classpath 中。

Maven 中的依赖是有传递（Transitive）性的，默认会包含传递的依赖，这样就不用手动引用每一个依赖了。比如下面这个依赖关系中，A 依赖 B，B 依赖了 C……，如果你依赖 A 的话，就会自动包含 A/B/C/D/E

```mathematica
 A
  ├── B
  │   └── C
  │       └── D 
  └── E
      └── D 
```

但是传递依赖也带来了一个问题，比如下面这个例子：

```mathematica
  A
  ├── B
  │   └── C
  │       └── D 2.0
  └── E
      └── D 1.0
```

由于传递依赖，`D 2.0` 和 `D 1.0` 都会被加入 ClassPath 中，但因为它们版本不同，很可能会有包冲突等一系列问题。解决这个依赖传递导致的冲突问题，有两种方案：

**「1」** 在使用者，也就是发起依赖方进行排除

```xml
<dependency>
  <groupId>group-a</groupId>
  <artifactId>artifact-a</artifactId>
  <version>1.0</version>
  <exclusions>
    <exclusion>
      <groupId>group-c</groupId>
      <artifactId>excluded-artifact</artifactId>
    </exclusion>
  </exclusions>
</dependency>
```

**「2」** 在提供方，将依赖的范围定义为**不传递**，这样在构建时就不会包含这些**不传递**的依赖包了。不传递的配置有两种方式，也是本文讨论的重点

**「2.1」** 定义 dependency scope 为 provided

```xml
<dependency>
  <groupId>group</groupId>
  <artifactId>artifact-d</artifactId>
  <version>2.0</version>
    <!-- 不传递 -->
  <scope>provided</scope>
</dependency>
```

**「2.2」** 定义 `<optional>`

```xml
<dependency>
  <groupId>group</groupId>
  <artifactId>artifact-d</artifactId>
  <version>2.0</version>
  <!-- 不传递 -->
  <optional>true</optional>
</dependency>
```

在添加依赖项时，我们可以使用 `<optional>true</optional>` 标志：

在这两种**不传递**配置下，依赖关系都将在声明它们的模块的 classpath 中，但是使用将它们定义为依赖关系的模块不会在其他项目中传递它们，即不会形成依赖传递。

那`<optional>`和 `provided scope` 之间又有什么区别呢？

## 从语义来上理解

### optional

可选的，可以理解为此功能/此依赖可选，如果不需要某项功能，可以不引用这个包。

### scope provided

提供的，可以理解为此包不由我直接提供，需要调用者/容器提供。

## 举个例子说明二者的使用场景和区别

### optional

现开发了一个类似Hibernate的框架，叫Summer吧，致敬下Spring，提供了多种数据库方言的支持:mysql/oracle/db2/postgresql...
每种数据库支持也独立了一个module，Summer的依赖中配置了每种数据库的支持包：summer-mysql-support/summer-oracle-support...

但是实际引用此框架/依赖时，并不需要所有数据库方言的支持。此时可以把数据库的支持包都配置为可选的`<optional>true</optional>`。
引用此框架时，只需按需引入自己需要的方言支持包即可，避免了冗余繁杂的依赖，也降低了jar包冲突的风险。

### scope provided

现有一普通Web工程，必然会用到servlet-api这个包。但是实际上这个包一定是由容器提供的，因为我们这个web会部署到容器内，容器会提供servlet-api，如果此时项目中再引用的话就会造成重复引用，会有版本不一致的风险。

## 总结

**二者从功能来看，都做到了依赖不传递。但在语义上表示不同，使用时按场景选择就好。**