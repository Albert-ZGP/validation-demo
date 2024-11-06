[TOC]

------

## 一、区别

先总结一下它们的区别：

1. **来源**
    - **@Validated** ：是Spring框架特有的注解，属于Spring的一部分，也是**JSR 303**的一个变种。它提供了一些 **@Valid** 所没有的额外功能，比如分组验证。
    - **@Valid**：Java EE提供的标准注解，它是**JSR 303**规范的一部分，主要用于Hibernate Validation等场景。
2. **注解位置**
    - **@Validated** ： 用在类、方法、方法参数上。但不能用于成员属性。
    - **@Valid**：可以用在方法、构造函数、方法参数、成员属性上。
3. **分组**
    - **@Validated** ：支持分组验证，可以更细致地控制验证过程。此外，由于它是Spring专有的，因此可以更好地与Spring的其他功能（如Spring的依赖注入）集成。
    - **@Valid**：主要支持标准的Bean验证功能，不支持分组验证。
4. **嵌套验证**
    - **@Validated** ：不支持嵌套验证。
    - **@Valid**：支持嵌套验证，可以嵌套验证对象内部的属性。
5. **@Validated**  和 **@Valid** 是验证开关注解，需要配合 `jakarta.validation.constraints` 包下参数约束注解使用

所有参数注解含义

![image-20241021111545214](./../../markdownImages/image-20241021111545214.png)

## 二、@Valid 用法(导错包不生效)

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

此包下的

```Java
import javax.validation.constraints
```

### 2.1、实体属性添加

#### 1.单个实体

```java
public class User {
    @NotBlank(message = "用户名不为空")
    private String username;
    @NotBlank(message = "密码不为空")
    private String password;
}
```

#### 2.嵌套实体

```java
public class User {
    @NotBlank(message = "用户名不为空")
    private String username;
    @NotBlank(message = "密码不为空")
    private String password;
    
    @Valid //不加此注解，则不会校验
    @NotNull(message = "用户住址不能为空")
    private Address address;
}
```
```java
public class Address {
    @NotBlank(message = "地址详情不能为空")
    private String info;
}
```

### 2.2、接口添加

```java
    @PostMapping("/user")
    public void addUserInfo(@RequestBody @Valid User user) {
       
    }
```

### 2.3、全局异常捕获

```java
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)//最高优先级，防止有其他的全局异常捕获导致这个无法捕获
public class ExceptionAdvice {
	@ExceptionHandler(value = {MethodArgumentNotValidException.class})
	public R handleValidException(MethodArgumentNotValidException m){
			Map<String,String> errorMsg = m.getBindingResult.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField,FieldError::getDefaultMessage,(k1,k2) -> k1));
			return R.FAILED("数据异常").setData(errorMsg);
	}
}
```

## 三、@Validated 分组用法

分组是在实体类验证中常用的一种技术，它允许你根据不同的场景对验证规则进行分组，从而在不同的情况下应用不同的验证规则。

```java
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserCopyVo {
    @NotEmpty(groups = update.class, message = 'id不能为空')
    private String id;
    @NotEmpty()
    private String userName;
    private String avatar;//头像

    public UserCopyVo(UserEntity user) {
        this.userName = user.getUserName();
        this.id = user.getUuid();
        this.avatar = user.getAvatar();
    }

    /**
     * 设置分组
     * 继承 jakarta.validation.groups.Default 自定义验证组
     */
    public interface add extends Default {
    }

    public interface update extends Default {
    }
}
```
```java
    @ApiOperation(value = 'test', notes = 'test action')
    @PostMapping('/test')
    public String test(@Validated(UserCopyVo.update.class) @RequestBody UserCopyVo u) {
        System.out.println(u);
        return 'success0000';
    }
```

## 四、@Valid拓展

有的时候，我们发现`@Valid`的一些注解无法满足我们的特殊开发需求，那就需要对其注解进行拓展了。
要拓展@Valid注解，可以按照以下步骤进行操作：

### 4.1、创建一个自定义的注解

用`@Constraint`注解来定义这个自定义注解，并指定一个自定义的校验器类。

```java
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AnnotateValidator.class)
public @interface Annotate{
    String message() default "Invalid value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
```

### 4.2、创建一个自定义的校验器类

创建自定义校验器类实现`ConstraintValidator`接口，并重写`initialize`和`isValid`方法。

```java
public class AnnotateValidator implements ConstraintValidator<Annotate, String> {
    @Override
    public void initialize(Annotate annotation) {
        // 初始化校验器，可以空着
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 执行自己的校验逻辑，返回布尔值即可
        return value != null && value.startsWith("custom");
    }
}
```

`CustomValidator`校验器类实现了`ConstraintValidator<CustomValid, String>`接口，其中`CustomValid`是自定义注解的类型，`String`是要校验的值的类型。

### 4.3、测试

在需要校验的字段或方法参数上使用自定义的注解。

```java
public class Test{
    @CustomValid(message = "参数格式不对")
    private String customField;
}
```
```java
public class MyController {
    @PostMapping("/myEndpoint")
    public void myEndpoint(@Valid @RequestBody Test myTest) {
        // ...
    }
}
```



> 参考文章
> - [别再混淆了！一文带你搞懂@Valid和@Validated的区别_validated和valid区别-CSDN博客](https://blog.csdn.net/qq_39654841/article/details/136397004)
> - [@Validated注解详解，分组校验，嵌套校验，@Valid和@Validated 区别，Spring Boot @Validated_java validated注解-CSDN博客](https://blog.csdn.net/Chioce/article/details/130010515)
> - [@Vaild的作用及其用法-CSDN博客](https://blog.csdn.net/mocoll/article/details/133014827)
