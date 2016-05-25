# MyBatis pagination support for MySQL 5.x

MyBatis comes with pagination support. Using `RowBounds` you can limit the result set(see [Supplied Plugins](http://mybatis.org/generator/reference/plugins.html)).

However, it does not work the way you might think it should. Run the following code block:

```java
@Autowired
private MysqlUserDao dao;

@Test
public void testGetUsers() {
    final int offset = 1;
    final int limit = 10;

    MysqlUserExample example = new MysqlUserExample();
    example.createCriteria()
            .andHostIsNotNull()
            ;

    RowBounds bounds = new RowBounds(offset, limit);
    List<MysqlUser> users = dao.getUsers(example, bounds);
    assertThat(users, notNullValue());
}
```

and see the query logging behind the hood:

```
==>  Preparing: select Host, User, Password, Select_priv, Insert_priv, Update_priv, Delete_priv, Create_priv, Drop_priv, Reload_priv, Shutdown_priv, Process_priv, File_priv, Grant_priv, References_priv, Index_priv, Alter_priv, Show_db_priv, Super_priv, Create_tmp_table_priv, Lock_tables_priv, Execute_priv, Repl_slave_priv, Repl_client_priv, Create_view_priv, Show_view_priv, Create_routine_priv, Alter_routine_priv, Create_user_priv, Event_priv, Trigger_priv, Create_tablespace_priv, ssl_type, max_questions, max_updates, max_connections, max_user_connections, plugin, password_expired, is_role from user WHERE ( Host is not null )
==> Parameters:
<==    Columns: Host, User, Password, Select_priv, Insert_priv, Update_priv, Delete_priv, Create_priv, Drop_priv, Reload_priv, Shutdown_priv, Process_priv, File_priv, Grant_priv, References_priv, Index_priv, Alter_priv, Show_db_priv, Super_priv, Create_tmp_table_priv, Lock_tables_priv, Execute_priv, Repl_slave_priv, Repl_client_priv, Create_view_priv, Show_view_priv, Create_routine_priv, Alter_routine_priv, Create_user_priv, Event_priv, Trigger_priv, Create_tablespace_priv, ssl_type, max_questions, max_updates, max_connections, max_user_connections, plugin, password_expired, is_role
<==        Row: localhost, root, , Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, , 0, 0, 0, 0, , N, N
<==        Row: jadens.local, root, , Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, , 0, 0, 0, 0, , N, N
<==        Row: 127.0.0.1, root, , Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, , 0, 0, 0, 0, , N, N
<==        Row: ::1, root, , Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, , 0, 0, 0, 0, , N, N
<==        Row: localhost, , , N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, , 0, 0, 0, 0, , N, N
<==        Row: jadens.local, , , N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, , 0, 0, 0, 0, , N, N
<==      Total: 6
```

As you might notice, there is no `limit` in the generated query. MyBatis does not comes with the native support for MySQL. It does not know anything about `limit` and not use it to filter the result set. It gets all the relevant records and filter out unnecessary records in the memory. A disaster in the production!

You should implement your own MyBatis plugin to make [`RowBounds plugin`](http://mybatis.org/generator/reference/plugins.html) to use MySQL's `limit` feature. Unfortunately, many implementation examples in the web have their own flaws. That is the reason why I have implemented my own plugin for myself and you.

With the simple plugin configuration, your life will be much easier:

```xml
<!-- XML-based -->
<bean id="PaginationInterceptor" class="net.andromedarabbit.mybatis.pagination.plugin.PaginationInterceptor"/>
<bean id="PaginationResultSetHandler" class="net.andromedarabbit.mybatis.pagination.plugin.PaginationResultSetHandlerInterceptor"/>

<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource" />
    <property name="plugins">
        <array>
            <ref bean="PaginationInterceptor" />
            <ref bean="PaginationResultSetHandler" />
        </array>
    </property>
</bean>
```

```java
// Annotation-based
@Bean
public SqlSessionFactoryBean sqlSessionFactoryBean(
       DataSource dataSource,
       ApplicationContext applicationContext) throws IOException {

   SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
   factoryBean.setDataSource(dataSource);
   factoryBean.setPlugins(new Interceptor[]{
           new PaginationInterceptor(),
           new PaginationResultSetHandlerInterceptor()
   });
   return factoryBean;
}
```


See the query logging again:

```
==>  Preparing: select Host, User, Password, Select_priv, Insert_priv, Update_priv, Delete_priv, Create_priv, Drop_priv, Reload_priv, Shutdown_priv, Process_priv, File_priv, Grant_priv, References_priv, Index_priv, Alter_priv, Show_db_priv, Super_priv, Create_tmp_table_priv, Lock_tables_priv, Execute_priv, Repl_slave_priv, Repl_client_priv, Create_view_priv, Show_view_priv, Create_routine_priv, Alter_routine_priv, Create_user_priv, Event_priv, Trigger_priv, Create_tablespace_priv, ssl_type, max_questions, max_updates, max_connections, max_user_connections, plugin, password_expired, is_role from user WHERE ( Host is not null ) limit ?, ?
==> Parameters: 1(Integer), 10(Integer)
<==    Columns: Host, User, Password, Select_priv, Insert_priv, Update_priv, Delete_priv, Create_priv, Drop_priv, Reload_priv, Shutdown_priv, Process_priv, File_priv, Grant_priv, References_priv, Index_priv, Alter_priv, Show_db_priv, Super_priv, Create_tmp_table_priv, Lock_tables_priv, Execute_priv, Repl_slave_priv, Repl_client_priv, Create_view_priv, Show_view_priv, Create_routine_priv, Alter_routine_priv, Create_user_priv, Event_priv, Trigger_priv, Create_tablespace_priv, ssl_type, max_questions, max_updates, max_connections, max_user_connections, plugin, password_expired, is_role
<==        Row: jadens.local, root, , Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, , 0, 0, 0, 0, , N, N
<==        Row: 127.0.0.1, root, , Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, , 0, 0, 0, 0, , N, N
<==        Row: ::1, root, , Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, Y, , 0, 0, 0, 0, , N, N
<==        Row: localhost, , , N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, , 0, 0, 0, 0, , N, N
<==        Row: jadens.local, , , N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, , 0, 0, 0, 0, , N, N
<==      Total: 5
```


You can see the difference. The generated query uses `limit` and the result set is smaller than the previous one.

## Adding to your project

You can add **mybatis-pagination** to your project by using [Jitpack](https://jitpack.io/#andromedarabbit/mybatis-pagination):

### Gradle dependencies

```gradle
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}

dependencies {
  compile 'com.github.andromedarabbit:mybatis-pagination:0.0.1'
}
```

### Maven dependencies

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
	</repository>
</repositories>

<dependency>
  <groupId>com.github.andromedarabbit</groupId>
  <artifactId>mybatis-pagination</artifactId>
  <version>0.0.1</version>
</dependency>
```

## Build the project by yourself

### Prerequites

* [Docker](https://www.docker.com/) is required to run the local build. **Docker for Mac** is recommended for Mac OS X users.
* [Codeship Jet is required.](https://codeship.com/documentation/docker/installation/)


### Build the project

Run `./build.sh`, then it will build and test the whole project:

```bash
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
{ContainerRunStdout=step_name:"build" service_name:"app_build"}: [INFO] Total time: 4.817 s
[INFO] Finished at: 2016-05-25T15:56:56+09:00
{ContainerRunStdout=step_name:"build" service_name:"app_build"}: [INFO] Final Memory: 22M/151M
[INFO] ------------------------------------------------------------------------
{StepFinished=step_name:"build" type:STEP_FINISHED_TYPE_SUCCESS}
```
