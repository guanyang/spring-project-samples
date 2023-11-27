# MySQL使用规范

## 背景
mysql 建表 ,索引 命名 字段类型等需遵循一定的规范 减少故障几率,提高系统效率,利于维护.

## 开发流程规范

![上传成功](image/rule-01.png)

### 命名
- 库名、表名、字段名必须使用小写字母，“_”分割。
- 库名、表名、字段名必须不超过30个字符。
- 库名、表名、字段名见名知意,建议使用名词而不是动词。
- 非唯一索引按照“idx字段名称字段名称[_字段名]”进行命名。
- 唯一索引必须按照“uniq字段名称字段名称[_字段名]”进行命名。
- 索引名称必须使用小写。
- 使用时间散表，表名后缀必须使用特定格式，比如按日散表user_20110209、按月散表user_201102

### 表设计
- 建议使用InnoDB存储引擎。
- 存储精确浮点数必须使用DECIMAL替代FLOAT和DOUBLE。
- 建议使用UNSIGNED存储非负数值。
- 建议使用INT UNSIGNED存储IPV4。
- 整形定义中不添加长度，比如使用INT，而不是INT(4)。
- 使用短数据类型，比如取值范围为0-80时，使用TINYINT UNSIGNED。
- 不建议使用ENUM类型，使用TINYINT来代替。
- 尽可能不使用TEXT、BLOB类型。
- VARCHAR(N)，N表示的是字符数不是字节数，比如VARCHAR(255)，可以最大可存储255个汉字，需要根据实际的宽度来选择N。
- VARCHAR(N)，N尽可能小，因为MySQL一个表中所有的VARCHAR字段最大长度是65535个字节，进行排序和创建临时表一类的内存操作时，会使用N的长度申请内存。
- 表字符集选择UTF8。
- 使用VARBINARY存储变长字符串。
- 存储年使用YEAR类型。
- 存储日期使用DATE类型。
- 存储时间（精确到秒）建议使用TIMESTAMP类型，因为TIMESTAMP使用4字节，DATETIME使用8个字节。
- 建议字段定义为NOT NULL，int类型无默认值的，设置默认值0，varchar类型无默认值时，设置默认值’’，如NOT NULL DEFAULT ‘’。
- 将过大字段拆分到其他表中。
- 禁止在数据库中使用VARBINARY、BLOB存储图片、文件等。
- 表结构变更需要通知DBA审核。
- [强制]不使用明文密码
- [建议]拆分大字段和访问频率低的字段, 分离冷热数据,
- [建议]如需分表用hash进行散表,表名后缀用10进制数0开始,
- [建议]首次分表尽量多分,避免二次分表, 二次难度,成本高
- [建议]单表字段数控制在20个以内



### 索引相关
- 非唯一索引按照“idx字段名称字段名称[_字段名]”进行命名。
- 唯一索引必须按照“uniq字段名称字段名称[_字段名]”进行命名。
- 索引名称必须使用小写。
- 索引中的字段数建议不超过5个。
- 单张表的索引数量控制在5个以内。
- 唯一键由3个以下字段组成，并且字段都是整形时，使用唯一键作为主键。
- 没有唯一键或者唯一键不符合5中的条件时，使用自增（或者通过发号器获取）id作为主键。
- 唯一键不和主键重复。
- 索引字段的顺序需要考虑字段值去重之后的个数，个数多的放在前面。
- ORDER BY，GROUP BY，DISTINCT的字段需要添加在索引的后面。
- UPDATE、DELETE语句需要根据WHERE条件添加索引。
- 不建议使用%前缀模糊查询，例如LIKE “%weibo”。
- 对长度过长的VARCHAR字段建立索引时，添加crc32或者MD5 Hash字段，对Hash字段建立索引。
- 合理创建联合索引（避免冗余），(a,b,c) 相当于 (a) 、(a,b) 、(a,b,c)。
- 合理利用覆盖索引。
- SQL变更需要确认索引是否需要变更并通知DBA。

- [建议]innodb主键使用自增列,主键不应该被修改,字符串不应做主键
- [建议]复合索引,区分度最大的字段放在索引前面
- [强制]不在低基数列上建立索引,如 性别
- [强制]不使用外键
- [建议]不在索引列进行数学运算和函数运算
- [建议]不使用反向查询: not in/not like




###  sql语句相关
- 使用prepared statement，可以提供性能并且避免SQL注入。
- SQL语句中IN包含的值不应过多。
- UPDATE、DELETE语句不使用LIMIT。
- WHERE条件中必须使用合适的类型，避免MySQL进行隐式类型转化。
- SELECT语句只获取需要的字段。
- SELECT、INSERT语句必须显式的指明字段名称，不使用SELECT *，不使用INSERT INTO table()。
- 使 用SELECT column_name1, column_name2 FROM table WHERE [condition]而不是SELECT column_name1 FROM table WHERE [condition]和SELECT column_name2 FROM table WHERE [condition]。
- WHERE条件中的非等值条件（IN、BETWEEN、<、<=、>、>=）会导致后面的条件使用不了索引。
- 避免在SQL语句进行数学运算或者函数运算，容易将业务逻辑和DB耦合在一起。
- INSERT语句使用batch提交（INSERT INTO?table VALUES(),(),()……），values的个数不应过多。
- 避免使用存储过程、触发器、函数等，容易将业务逻辑和DB耦合在一起，并且MySQL的存储过程、触发器、函数中存在一定的bug。
- 1000w以上的大表避免使用JOIN。
- 使用合理的SQL语句减少与数据库的交互次数。
- 不使用ORDER BY RAND()，使用其他方法替换。
- 建议使用合理的分页方式以提高分页的效率。
- 统计表中记录数时使用COUNT(*)，而不是COUNT(primary_key)和COUNT(1)。
- 禁止在从库上执行后台管理和统计类型功能的QUERY。可以跟DBA沟通是否放在pg等其他数据库执行
- [强制]避免隐式转换:varchar 传int查询无法使用索引
- [建议]使用in 代替 or
- [强制]in的值不超过1000个
- [强制]使用union all 不用 union
- [强制]不使用select *

### 散表
- 每张表数据量建议控制在5000w以下。
- 可以结合使用hash、range、lookup table进行散表。
- 散表如果使用md5（或者类似的hash算法）进行散表，表名后缀使用16进制，比如user_ff。
- 推荐使用CRC32求余（或者类似的算术算法）进行散表，表名后缀使用数字，数字必须从0开始并等宽，比如散100张表，后缀从00-99。
- 使用时间散表，表名后缀必须使用特定格式，比如按日散表user_20110209、按月散表user_201102

### FAQ

#### 存储精确浮点数必须使用DECIMAL替代FLOAT和DOUBLE。
mysql中的数值类型（不包括整型）： IEEE754浮点数： float（单精度） ， double 或 real （双精度） 定点数： decimal 或 numeric 单精度浮点数的有效数字二进制是24位，按十进制来说，是8位；双精度浮点数的有效数字二进制是53位，按十进制来说，是16 位 一个实数的有效数字超过8位，用单精度浮点数来表示的话，就会产生误差！同样，如果一个实数的有效数字超过16位，用双精度浮点数来表示，也会产生误差

IEEE754标准的计算机浮点数，在内部是用二进制表示的，但在将一个十进制数转换为二进制浮点数时，也会造成误差，原因是不是所有的数都能转换成有限长度的二进制数。 即一个二进制可以准确转换成十进制，但一个带小数的十进制不一定能够准确地用二进制来表示。

实例：
~~~
drop table if exists t;
create table t(value float(10,2));
insert into t values(131072.67),(131072.68);
select value from t;
value
131072.67
131072.69
~~~

####  尽可能不使用TEXT、BLOB类型。
索引排序问题，只能使用max_sort_length的长度或者手工指定ORDER BY SUBSTRING(column, length)的长度来排序 Memory引擘不支持text,blog类型，会在磁盘上生成临时表 可能浪费更多的空间 可能无法使用adaptive hash index 导致使用where没有索引的语句变慢

####  VARCHAR中会产生额外存储吗？
VARCHAR(M)，如果M<256时会使用一个字节来存储长度，如果m>=256则使用两个字节来存储长度。

#### 为什么建议使用TIMESTAMP来存储时间而不是DATETIME？
DATETIME和TIMESTAMP都是精确到秒，优先选择TIMESTAMP，因为TIMESTAMP只有4个字节，而DATETIME8个字节。同时TIMESTAMP具有自动赋值以及自动更新的特性。

如何使用TIMESTAMP的自动赋值属性？

将当前时间作为ts的默认值：ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP。 当行更新时，更新ts的值：ts TIMESTAMP DEFAULT 0 ON UPDATE CURRENT_TIMESTAMP。 可以将1和2结合起来：ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP。

#### 为什么一张表中不能存在过多的索引？
InnoDB的secondary index使用b+tree来存储，因此在UPDATE、DELETE、INSERT的时候需要对b+tree进行调整，过多的索引会减慢更新的速度。

#### 如何对长度大于50的VARCHAR字段建立索引？
下面的表增加一列url_crc32，然后对url_crc32建立索引，减少索引字段的长度，提高效率。
~~~
CREATE TABLE url(
url VARCHAR(255) NOT NULL DEFAULT 0,
url_crc32 INT UNSIGNED NOT NULL DEFAULT 0,
index idx_url(url_crc32)
)
~~~

#### mySQL中如何进行分页？
假如有类似下面分页语句：

SELECT * FROM table ORDER BY TIME DESC LIMIT 10000,10;
这种分页方式会导致大量的io，因为MySQL使用的是提前读取策略。

推荐分页方式：

SELECT * FROM table WHERE TIME<last_TIME ORDER BY TIME DESC LIMIT 10.
SELECT * FROM table inner JOIN(SELECT id FROM table ORDER BY TIME LIMIT 10000,10) as t USING(id)