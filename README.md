# MyBatis pagination support for MySQL 5.x

[![PayPal](https://img.shields.io/badge/%24-paypal-f39c12.svg)][paypal-donations]
[![Build Status](https://travis-ci.org/andromedarabbit/mybatis-pagination.svg?branch=master)](https://travis-ci.org/andromedarabbit/mybatis-pagination)
[![codebeat badge](https://codebeat.co/badges/deb8d38e-5f3a-444d-b1c7-2b5a845b63e4)](https://codebeat.co/projects/github-com-andromedarabbit-mybatis-pagination)

MyBatis comes with pagination support. Using `RowBounds` plugin, you can limit the result set(see [Supplied Plugins](http://mybatis.org/generator/reference/plugins.html)).

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

As you might notice, there is no `limit` in the generated query. MyBatis does not come with the native support for MySQL. MyBatis does not know anything about `limit` and not use it to filter the result set. It retrieves all the relevant records into memory and filters out unnecessary records there. A disaster in the production!

You should implement your own MyBatis plugin to make [`RowBounds plugin`](http://mybatis.org/generator/reference/plugins.html) use MySQL's `limit` feature. Unfortunately, many implementation examples out there have their own flaws. That is the reason why I have implemented my own plugin for myself and you.

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

or with Java annotation configuration:

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

## Supported databases

* MySQL 5.x
* MariaDB 5.x / 10.x

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

* [Docker Compose](https://docs.docker.com/compose/) is required to run the local build. **Docker for Mac** is recommended for Mac OS X users.


### Build the project

Run `docker-compose up -d && ./build.sh`, then it will build and test the whole project:

```bash
[INFO] -------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] -------------------------------------------------------------
[INFO] Total time: 6.838 s
[INFO] Finished at: 2016-06-29T23:26:07+09:00
[INFO] Final Memory: 20M/213M
[INFO] --------------------------------------------------------------
```


[paypal-donations]: https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=VG4JMPL7SDBGG&lc=KR&item_name=andromedarabbit%2fmybatis%2dpagination&item_number=mybatis%2dpagination&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted
