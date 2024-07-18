# “西小红”西南大学红十字会志愿服务管理系统—后端
⭐基于SpringBoot与Vue的志愿服务管理系统系统，用于简化志愿服务流程，提高志愿服务效率。

**接口文档**：https://apifox.com/apidoc/shared-51abf509-a67a-4bff-9b64-21ae1adc42bd

**后端技术栈**：

- 😄SpringBoot + MyBatis-Plus
- 🔒SpringSecurity + JWT+Redis：鉴权
- ❤Redis：用户信息缓存、志愿活动招募数据缓存
- ✉️JavaMail+FreeMarker：邮件模板的填充和发送功能
- 🧚‍♂️okHttp + Netty建立前端、后端、大模型的websocket连接，调用星火大模型API实现流式问答。
- 🛒Rediss事务、Lua脚本实现限量志愿者招募人数秒杀
- ⛷️RabbitMQ消息重试机制与乐观锁保证活动邮件正确发送 
- :file_folder:七牛云OSS：图片存储 

**前端技术栈**：

​	后台：Vue2+ vue-template-admin + Element-UI

​	微信小程序：原生开发

**部署**：Docker + Nginx

**TODO**：

- [ ] 更完善的权限控制
- [ ] 拆为微服务

本系统还有很多不完善的地方，如果您有宝贵的建议、不满 or 疑问，欢迎提issue！作者将尽快回复。

如果你对这个项目感兴趣，想要一起让它变得更好，欢迎联系我一起contribute！mail：qwretrh@qq.com

## 效果预览

![image](https://gitee.com/world_heping/RedCross/raw/front-admin/src/assets/readme/1721279956558.png)

![image](https://gitee.com/world_heping/RedCross/raw/front-admin/src/assets/readme/1721279704101.png)

![image](https://gitee.com/world_heping/RedCross/raw/front-admin/src/assets/readme/1721279851836.png)

![image](https://gitee.com/world_heping/RedCross/raw/front-admin/src/assets/readme/1721279902568.png)

## 后端运行

**后端项目在master分支下，前端后台项目在frontend-admin分支下，前端微信小程序在front-weixin分支下。**

将RedCross项目clone至本地目录。刷新maven依赖

若能够启动，在framework/src/main/resources下创建.env文件用以更改`application.yml`中MySQL、Redis、RabbitMQ相关设置，以及大模型与七牛云key，再次Run，项目应能正确运行。

**env模板如下：**

```xml
# 开发环境
DEV_BASEURL=
# 生产环境
PROD_BASEURL=
# 测试环境
TEST_BASEURL=
# MySQL配置
MYSQL_USER_NAME=
MYSQL_PASSWORD=
# Redis配置
REDIS_PASSWORD=
# RabbitMQ配置
RABBITMQ_USER_NAME=
RABBITMQ_PASSWORD=
# 邮件配置
MAIL_HOST=
MAIL_USER_NAME=
MAIL_PASSWORD=
# 七牛云OSS配置
OSS_ACCESS_KEY=
OSS_SECRET_KEY=
OSS_BUCKET=
# 星火大模型配置
LLM_APPID=
LLM_API_SECRET=
LLM_API_KEY=
LLM_HOST_URL=
# 微信配置
WEIXIN_JSCODE2_SESSION_URL=
WEIXIN_APP_ID=
WEIXIN_SECRET=
```

## 部署

### 注：

部署采用Linux云服务器+docker容器化的方式进行部署，一切有关文件存储在/mydata目录下

```shell
mkdir /mydata
```

2024.7.7更新

[可以采用docker-Compose的方式部署](#dockercompose) 

### 一、docker安装

#### 1.安装相关依赖

``` shell
yum install -y yum-utils  device-mapper-persistent-data   lvm2
```

#### 2.设置 docker repo 的 yum 位置 

```shell
yum-config-manager  --add-repo  https://download.docker.com/linux/centos/docker-ce.repo 
```

#### 3.安装docker

```shel
yum install docker-ce docker-ce-cli containerd.io
```

#### 4.启动docker

```shell
systemctl start docker
```

#### 5.开机自启docker

```shell
systemctl enable docker
```

#### 6.Docker开启远程API(记得开放2375端口)

修改docker.service文件

```shell 
vim /usr/lib/systemd/system/docker.service
```

需要修改的部分：

```shell 
ExecStart=/usr/bin/dockerd -H fd:// --containerd=/run/containerd/containerd.sock
```

修改后的部分：

```shell 
ExecStart=/usr/bin/dockerd -H tcp://0.0.0.0:2375 -H unix://var/run/docker.sock
```

#### 配置生效

```shell
systemctl daemon-reload
```

#### 重新启动Docker服务

```shell
systemctl restart docker
```

#### 7.配置镜像仓库

```shell
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
    "registry-mirrors": [
        "https://docker.m.daocloud.io",
        "https://huecker.io",
        "https://dockerhub.timeweb.cloud",
        "https://noohub.ru"
    ]
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker 
```

#### 8.可视化工具Portainer部署(可以不管)

```shell
 docker volume create portainer_data
 
 docker run -d -p 8000:8000 -p 9000:9000 --name=portainer --restart=always -v /var/run/docker.sock:/var/run/docker.sock -v portainer_data:/data portainer/portainer-ce
 
 
 #用于部署运行所依赖的服务
version: '3.9'
services:
  # 基础环境组件
  # 1.Portainer
  portainer:
    image: portainer/portainer-ce
    container_name: portainer
    command: -H unix:///var/run/docker.sock
    restart: always
    deploy:
      resources:
        limits:
          cpus: '0.50'
          memory: 800M
        reservations:
          cpus: '0.1'
          memory: 256M
    ports:
      - "9999:9000"
      - "8000:8000"
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock #数据文件挂载
      - portainer_data:/data portainer/portainer-ce #配置文件挂载
      - /etc/localtime:/etc/localtime:ro
      - /etc/timezone/timezone:/etc/timezone:ro
 
# 存储卷
volumes:
  portainer_data:
```



### 二、数据库配置

#### 1. 数据库镜像创建（采用mysql数据库）

```shell
docker pull mysql:8.0 //拉取mysql8.0镜像
```

#### 2. 数据库容器创建

##### 2.1在本地创建文件夹/mydata/mysql（ -v 是将文件挂载到容器内部，形成映射 ）

```shell
mkdir /mydata/mysql

docker run -d --name mysql --restart=always -p 3306:3306 -v /mydata/mysql:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=theRedCross1993 mysql:8.0
```

#### 3.数据库sql导入

##### 3.1 进入容器

```shell
docker exec -it mysql8 /bin/bash
```

##### 3.2 数据库容器进入数据库后运行   

```shell
mysql -uroot -ptheRedCross1993

alter user 'root'@'%' identified with mysql_native_password by 'theRedCross1993';
flush privileges;

exit
exit
docker restart mysql8
```

### 三、RabbitMQ

#### 1.创建一个文件夹用于挂载rabbitmq的插件

~~~shell
mkdir /mydata/mq-plugins
~~~

#### 2.查找我们需要的镜像

~~~shell
docker search rabbitmq
~~~

#### 3.拉取镜像(我们选择了STARS数最多的官方镜像，此处需要注意，默认[rabbitmq](https://so.csdn.net/so/search?q=rabbitmq&spm=1001.2101.3001.7020)镜像是不带web端管理插件的，所以指定了镜像tag为3.8-management，表示下载包含web管理插件版本镜像)

~~~shell
docker pull rabbitmq:3.8-management
~~~

#### 4.创建工具容器

```shell
docker run \
 --name mq \
 --hostname mq \
 -p 15672:15672 \
 -p 5672:5672 \
 -d \
 --privileged=true \
 rabbitmq:3.8-management
```

创建工具容器目的是将容器内部插件目录拷贝出来，在新建容器时将宿主机的目录挂载到容器内部。

#### 5.拷贝出来插件目录

~~~shell
docker cp mq:/plugins/. /mydata/mq-plugins
~~~

#### 6.再将工具容器删除后创建容器

 ~~~shell
# 删除工具容器
docker kill mq
docker rm mq

# 新建容器
docker run \
 -e RABBITMQ_DEFAULT_USER=theRedCross \
 -e RABBITMQ_DEFAULT_PASS=theRedCross1993 \
 -v /mydata/mq-plugins:/plugins \
 --name mq \
 --hostname mq \
 -p 15672:15672 \
 -p 5672:5672 \
 -d \
 --privileged=true \
 --restart=always \
 rabbitmq:3.8-management
 ~~~

### 四、Redis

#### 1.Redis配置

##### 1.1 Redis服务器配置文件

~~~xml
# 绑定IP地址
#解除本地限制 注释bind 127.0.0.1  
#bind 127.0.0.1  
 
# 服务器端口号  
port 6379 
 
#配置密码，不要可以删掉
requirepass theRedCross1993
 
#这个配置不要会和docker -d 命令 冲突
# 服务器运行模式，Redis以守护进程方式运行,默认为no，改为yes意为以守护进程方式启动，可后台运行，除非kill进程，改为yes会使配置文件方式启动redis失败，如果后面redis启动失败，就将这个注释掉
daemonize no
 
#当Redis以守护进程方式运行时，Redis默认会把pid写入/var/run/redis.pid文件，可以通过pidfile指定(自定义)
#pidfile /data/dockerData/redis/run/redis6379.pid  
 
#默认为no，redis持久化，可以改为yes
appendonly yes
 
#当客户端闲置多长时间后关闭连接，如果指定为0，表示关闭该功能
timeout 60
# 服务器系统默认配置参数影响 Redis 的应用
maxclients 10000
tcp-keepalive 300
 
#指定在多长时间内，有多少次更新操作，就将数据同步到数据文件，可以多个条件配合（分别表示900秒（15分钟）内有1个更改，300秒（5分钟）内有10个更改以及60秒内有10000个更改）
save 900 1
save 300 10
save 60 10000
 
# 按需求调整 Redis 线程数
tcp-backlog 511
 
# 设置数据库数量，这里设置为16个数据库  
databases 16
 
# 启用 AOF, AOF常规配置
appendonly yes
appendfsync everysec
no-appendfsync-on-rewrite no
auto-aof-rewrite-percentage 100
auto-aof-rewrite-min-size 64mb
 
# 慢查询阈值
slowlog-log-slower-than 10000
slowlog-max-len 128
 
# 是否记录系统日志，默认为yes  
syslog-enabled yes  
 
#指定日志记录级别，Redis总共支持四个级别：debug、verbose、notice、warning，默认为verbose
loglevel notice
  
# 日志输出文件，默认为stdout，也可以指定文件路径  
logfile stdout
 
# 日志文件
#logfile /var/log/redis/redis-server.log

# 系统内存调优参数   
# 按需求设置
hash-max-ziplist-entries 512
hash-max-ziplist-value 64
list-max-ziplist-entries 512
list-max-ziplist-value 64
set-max-intset-entries 512
zset-max-ziplist-entries 128
zset-max-ziplist-value 64
~~~

###### 1.1.1配置解释

~~~xml
daemonize no
# 默认情况下，redis不是在后台运行的。如果需要在后台运行，把该项的值更改为yes。
 
pidfile /var/run/redis.pid
# 当redis在后台运行的时候，redis默认会把pid文件放在/var/run/redis.pid，你可以配置到其他位置。当运行多个redis服务时，需要指定不同的pid文件和端口。
 
port 6379
# 指定redis运行的端口，默认是6379。
  
bind 127.0.0.1
# 指定redis只接收来自于该IP地址的请求看，如果不进行设置，那么将处理所有请求。在生产环境中最好设置该项。
 
loglevel debug
# 指定日志记录级别，其中redis总共支持四个级别：debug、verbose、notice、warning，默认为verbose。
# 1 . debug表示记录很多信息,用于开发和测试
# 2．verbose表示记录有用的信息, 但不像debug会记录那么多
# 3．notice表示普通的verbose，常用于生产环境
# 4．warning 表示只有非常重要或者严重的信息会记录到日志
 
logfile /var/log/redis/redis.log
# 配置log文件地址,默认值为stdout。若后台模式会输出到/dev/null。
 
databases 16
# 可用数据库数，默认值为16，默认数据库为0，数据库范围在0~15之间切换，彼此隔离。
 
save
# 保存数据到磁盘，格式为save，指出在多长时间内，有多少次更新操作，就将数据同步到数据文件rdb。相当于条件触发抓取快照，这个可以多个条件配合。
# save 9001就表示900秒内至少有1个key被改变就保存数据到磁盘。
 
rdbcompression yes
# 存储至本地数据库时(持久化到rdb文件)是否压缩数据，默认为yes。
 
dbfilename dump.rdb
# 本地持久化数据库文件名，默认值为dump.rdb。
 
dir ./
# 工作目录，数据库镜像备份的文件放置的路径。这里的路径跟文件名要分开配置是因为redis在进行备份时，先会将当前数据库的状态写入到一个临时文件中，等备份完成时，再把该临时文件替换为上面所指定的文件。 而这里的临时文件和上面所配置的备份文件都会放在这个指定的路径当中，AOF文件也会存放在这个目录下面。 注意这里必须指定一个目录而不是文件。
 
slaveof
# 主从复制，设置该数据库为其他数据库的从数据库。设置当本机为slave服务时，设置master服务的IP地址及端口。 在redis启动时,它会自动从master进行数据同步。
 
masterauth
# 当master服务设置了密码保护时(用requirepass制定的密码)slave服务连接master的密码。
 
slave-serve-stale-data yes
# 当从库同主机失去连接或者复制正在进行，从机库有两种运行方式：
# 如果slave-serve-stale-data设置为 yes(默认设置)，从库会继续相应客户端的请求。
# 如果slave-serve-stale-data是指为no，除去INFO和SLAVOF命令之外的任何请求都会返回一个错误"SYNC with master in progress"。
 
repl-ping-slave-period 10
# 从库会按照一个时间间隔向主库发送PING，可以通过repl-ping-slave-period设置这个时间间隔,默认是10秒。
 
repl-timeout 60
# 设置主库批量数据传输时间或者ping回复时间间隔，默认值是60秒，一定要确保repl-timeout大于repl-ping-slave-period。
 
requirepass foobared
# 设置客户端连接后进行任何其他指定前需要使用的密码。因为redis速度相当快，所以在一台比较好的服务器平台下, 一个外部的用户可以在一秒钟进行150K次的密码尝试，这意味着你需要指定非常强大的密码来防止暴力破解。
 
rename command CONFIG “”
# 命令重命名，在一个共享环境下可以重命名相对危险的命令，比如把CONFIG重名为一个不容易猜测的字符：
rename-command CONFIG b840fc02d524045429941cc15f59e41cb7be6c52
# 如果想删除一个命令，直接把它重命名为一个空字符""即可：rename-command CONFIG “”。
 
maxclients 128
# 设置同一时间最大客户端连接数，默认无限制。redis可以同时打开的客户端连接数为redis进程可以打开的最大文件描述符数。
# 如果设置 maxclients 0，表示不作限制。当客户端连接数到达限制时，redis会关闭新的连接并向客户端返回max number of clients reached错误信息。
 
maxmemory
# 指定redis最大内存限制。redis在启动时会把数据加载到内存中，达到最大内存后，redis会先尝试清除已到期或即将到期的key，redis同时也会移除空的list对象。当此方法处理后,仍然到达最大内存设置，将无法再进行写入操作，但仍然可以进行读取操作。
# 注意：redis新的vm机制，会把key存放内存，value会存放在swap区。
 
maxmemory-policy volatile-lru
# 当内存达到最大值的时候redis会选择删除哪些数据呢？有五种方式可供选择：
# 1．volatile-lru代表利用LRU算法移除设置过期时间的key(LRU：最近使用LeastRecentlyUsed)
# 2．allkeys-lru代表利用LRU算法移除任何key
# 3．volatile-random代表移除设置过过期时间的随机key
# 4．allkeys_random代表移除一个随机的key
# 5． volatile-ttl代表移除即将过期的key(minor TTL)
# 6． noeviction代表不移除任何key，只是返回一个写错误
# 注意：对于上面的策略，如果没有合适的key可以移除，写的时候redis会返回一个错误。
 
appendonly no
# 默认情况下，redis会在后台异步的把数据库镜像备份到磁盘，但是该备份是非常耗时的，而且备份也不能很频繁。 如果发生诸如拉闸限电、拔插头等状况，那么将造成比较大范围的数据丢失，所以redis提供了另外一种更加高效的数据库备份及灾难恢复方式。
# 开启append only模式之后，redis会把所接收到的每一次写操作请求都追加到appendonly. aof文件中。当redis重新启动时，会从该文件恢复出之前的状态，但是这样会造成appendonly. aof文件过大，所以redis还支持BGREWRITEAOF指令对appendonly.aof。
 
appendfilename appendonly.aof
# AOF文件名称，默认为"appendonly.aof"。
 
appendfsync everysec
# redis支持三种同步AOF文件的策略：
# 1．no代表不进行同步,系统去操作
# 2．always代表每次有写操作都进行同步
# 3．everysec代表对写操作进行累积，每秒同步一次，默认是"everysec"，按照速度和安全折中这是最好的
 
slowlog-log-slower-than 10000
# 记录超过特定执行时间的命令。执行时间不包括I/O计算，比如连接客户端，返回结果等。只是命令执行时间，可以通过两个参数设置slow log：一个是告诉Redis执行超过多少时间被记录的参数slowlog-log-slower-than(微妙)，另一个是slow log 的长度。
# 当一个新命令被记录的时候最早的命令将被从队列中移除，下面的时间以微妙微单位，因此1000000代表一分钟。注意制定一个负数将关闭慢日志，而设置为0将强制每个命令都会记录。
 
hash-max-zipmap-entries 512 && hash-maxz-ipmap-value 64
# 当hash中包含超过指定元素个数并且最大的元素没有超过临界时，hash将以一种特殊的编码方式(大大减少内存使用)来存储，这里可以设置这两个临界值。Redis Hash对应Value内部实际就是一个HashMap，实际这里会有2种不同实现。这个Hash的成员比较少时redis为了节省内存会采用类似一维数组的方式来紧凑存储，而不会采用真正的HashMap结构，对应的value redisObject的encoding为zipmap。当成员数量增大时会自动转成真正的HashMap，此时encoding为ht。
 
hash-max-zipmap-entries 512 512
# list数据类型多少节点以下会采用去指针的紧凑存储格式。
 
list-max-ziplist-value 64
# 数据类型节点值大小小于多少字节会采用紧凑存储格式。
 
setmaxintsetentries 512
# set数据类型内部数据如果全部是数值型,且包含多少节点以下会采用紧凑格式存储。
 
zsetmaxziplistentries 128
# zsort数据类型多少节点以下会采用去指针的紧凑存储格式。
 
zsetmaxziplistvalue 64
# zsort数据类型节点值大小小于多少字节会采用紧凑存储格式。
 
activerehashing yes
# redis将在每100毫秒时使用1毫秒的CPU时间来对redis的hash表进行重新hash，可以降低内存的使用。
# 当你的使用场景中，有非常严格的实时性需要，不能够接受redis时不时的对请求有2毫秒的延迟的话，把这项配置为no。如果没有这么严格的实时性要求，可以设置为yes，以便能够尽可能快的释放内存。
~~~

##### 1.2 宿主机中创建redis目录用于挂载

~~~shell
mkdir /mydata/redis

# 用于存放配置文件
touch /mydata/redis/redis.conf

# 用于存放数据
touch /mydata/redis/data

# 用于存放日志
touch /mydata/redis/logs
~~~

##### 1.3 拉取Redis镜像

```shell
docker pull redis
```

##### 1.4 构建容器

```构建容器
docker run \
-p 6379:6379 \
--name redis \
-v /mydata/redis/redis.conf:/etc/redis/redis.config \
-v /mydata/redis/data:/var/lib/redis \
-v /mydata/redis/logs:/logs \
-d \
--privileged=true \
--restart=always \
redis:latest \
redis-server /etc/redis/redis.config
```

-d代表后台运行容器

#### 2.linux用定时调用lua脚本来清除redis中存储的退选数据

##### 2.1 Lua脚本存放在/mydata/lua下名为del_keys.lua的文件中

~~~lua
local keys = redis.call('KEYS', 'cancelSchedule*')  
for i,key in ipairs(keys) do  
    redis.call('DEL', key)  
end  
return 'Keys deleted'
~~~

##### 2.2 拷贝进入redis容器中

~~~shell
docker cp /mydata/lua/del_keys.lua redis:/opt
~~~

##### 2.3 delete_redis_keys.sh  

~~~shell
#!/bin/bash  
# delete_redis_keys.sh  
  
# 替换以下变量为你的容器名称或ID以及密码  
CONTAINER_NAME_OR_ID="redis"  
REDIS_PASSWORD="123456"  
  
# 执行docker exec命令  
docker exec -i $CONTAINER_NAME_OR_ID redis-cli -a $REDIS_PASSWORD --eval /opt/del_keys.lua ,0
~~~

##### 2.4 添加执行权限

~~~shell
chmod +x delete_redis_keys.sh
~~~

##### 2.5 设置定时任务

~~~shell
crontab -e 
0 0 1 * * /mydata/shell/delete_redis_keys.sh
~~~

### 五、后端

#### 1.Dockerfile编写

##### admin

```dockerfile
FROM openjdk:8
VOLUME /tmp
ADD admin-1.0-SNAPSHOT.jar admin.jar
EXPOSE 7070
ENTRYPOINT ["java","-jar","/admin.jar"]
```

##### weixin

```dockerfile
FROM openjdk:8
VOLUME /tmp
ADD weixin-1.0-SNAPSHOT.jar weixin.jar
EXPOSE 7777
ENTRYPOINT ["java","-jar","/weixin.jar"]
```

#### 2. 通过Dockerfile创建镜像

    docker build -t weixin .
    docker build -t admin .

#### 3. 创建容器（小程序放行端口7777，后台放行端口7070）

    docker run -d -p 7070:7070 --restart=always -e TZ="Asia/Shanghai"  --name admin admin
    docker run -d -p 7777:7777 --restart=always -e TZ="Asia/Shanghai" --name weixin weixin

### 六、nginx配置

#### 1. 创建工具容器

    docker run -d --name nginx01 -p 80:80  nginx:latest

#### 2. 导出默认配置文件

##### 2.1 新建本地nginx配置文件

    mkdir /mydata/nginx

##### 2.2 映射工具容器内文件到本地

    docker cp nginx01:/etc/nginx/nginx.conf /mydata/nginx/
    docker cp nginx01:/etc/nginx/conf.d /mydata/nginx/conf/
    docker cp nginx01:/usr/share/nginx/html /mydata/nginx/html
    docker cp nginx01:/var/log/nginx/ /mydata/nginx/logs/

##### 2.3 删除工具容器

    docker kill nginx01
    docker rm nginx01

#### 3.创建用于部署前端的nginx容器 

##### 3.1 前台用80端口,后台用8080端口,这里容器使用端口80和8080,云服务器也要放行对应端口

    docker run -d  --name nginx -p 80:80 -p 8080:8080 --restart=always -v /mydata/nginx/nginx.conf:/etc/nginx/nginx.conf -v /mydata/nginx/logs:/var/log/nginx -v /mydata/nginx/html:/usr/share/nginx/html -v /mydata/nginx/conf:/etc/nginx/conf.d --privileged=true     -e TZ=Asia/Shanghai nginx:latest

#### 4. 在nginx下的html创建用于存放前端打包好的代码的文件夹

#### 5. 修改/nginx/conf下的配置文件（两个conf）

##### 5.1 admin.conf 

```xml
server {
    listen       8080;
    listen  [::]:8080;
    server_name  admin;

    location  /{
        root   /usr/share/nginx/html/admin;
        try_files $uri $uri/ @router;
        index  index.html index.htm;
    }
    location @router {
    rewrite ^.*$ /index.html last;
    }
    
    location  /dev-api/{
        proxy_pass   http://124.70.136.242:7070/;
    }
    location  /prod-api/{
        proxy_pass   http://124.70.136.242:7070/;
    }
    location  /stage-api/{
        proxy_pass   http://124.70.136.242:7070/;
    }

    error_page   500 502 503 504  /50x.html;
    location = /50x.html {
        root   /usr/share/nginx/html;
    }
    
}
```

到这里就部署完成了可以访问浏览器ip:8080端口查看

### 七、Docker Compose</span>

#### 1.安装

```shell
# 下载某版本docker-compose文件并改名docker-compose移动至/usr/local/bin
curl -L "https://github.com/docker/compose/releases/download/1.28.3/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
# 授权docker-compose可执行
chmod +x /usr/local/bin/docker-compose
# 查看是否安装成功
docker-compose -v
```

#### 2.目录层级（将打包好的后端jar与前端存放在指定目录以及数据库sql文件）

```shell
.
├── backend
│   ├── admin
│   │   └── admin-1.0-SNAPSHOT.jar
│   └── weixin
│       └── weixin-1.0-SNAPSHOT.jar
├── docker-compose.yml
├── Dockerfile
├── frontend
│   └── dist
│       ├── favicon.ico
│       ├── index.html
│       └── static
├── mysql
│   └── init
│       └── redcross.sql
├── nginx
│   ├── conf.d
│   │   └── default.conf
│   └── nginx.conf
└── redis
    └── redis.conf
```

#### 3.docker-compose.yml编写

```shell
version: '3'

services:
  admin:
    build:
      context: .
      dockerfile: Dockerfile
      target: admin
    container_name: admin
    restart: always
    command: ["java", "-jar", "/admin.jar"]
    ports:
      - "7070:7070"
    depends_on:
      - redis
      - rabbitmq
      - mysql
    networks:
      - app-network

  weixin:
    build:
      context: .
      dockerfile: Dockerfile
      target: weixin
    container_name: weixin
    restart: always
    command: ["java", "-jar", "/weixin.jar"]
    ports:
      - "7777:7777"
    depends_on:
      - redis
      - rabbitmq
      - mysql
    networks:
      - app-network

  frontend:
    image: nginx:alpine
    container_name: nginx
    restart: always
    volumes:
      - ./frontend/dist:/usr/share/nginx/html
      - ./nginx/nginx.conf:/etc/nginx/nginx.conf
      - ./nginx/conf.d:/etc/nginx/conf.d
    ports:
      - "80:80"
      - "8080:8080"
    networks:
      - app-network
      
  redis:
    image: redis:latest
    restart: always
    container_name: redis
    volumes:
      - ./redis/redis.conf:/usr/local/etc/redis/redis.conf
    command: ["redis-server",  "/usr/local/etc/redis/redis.conf"]
    ports:
      - "6379:6379" 
    networks:
      - app-network

  rabbitmq:
    image: rabbitmq:management
    container_name: mq
    restart: always
    ports:
      - "15672:15672"  # RabbitMQ 管理界面
      - "5672:5672"
    environment:
      RABBITMQ_DEFAULT_USER: theRedCross
      RABBITMQ_DEFAULT_PASS: theRedCross1993
    networks:
      - app-network

  mysql:
    image: mysql:8.0
    container_name: mysql8
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: theRedCross1993
    volumes:
      - ./mysql/init:/docker-entrypoint-initdb.d/
    ports:
      - "3306:3306"  # RabbitMQ 管理界面
    networks:
      - app-network
  portainer:
    image: portainer/portainer-ce
    container_name: portainer
    command: -H unix:///var/run/docker.sock
    restart: always
    deploy:
      resources:
        limits:
          cpus: '0.50'
          memory: 800M
        reservations:
          cpus: '0.1'
          memory: 256M
    ports:
      - "9999:9000"
      - "8000:8000"
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock #数据文件挂载
      - portainer_data:/data portainer/portainer-ce #配置文件挂载
      - /etc/localtime:/etc/localtime:ro
      - /etc/timezone/timezone:/etc/timezone:ro
 
# 存储卷
volumes:
  portainer_data:
networks:
  app-network:
```

#### 4.Dockerfile编写

```shell
# Dockerfile
FROM openjdk:8 as base
WORKDIR /app

# 定义第一个构建阶段 weixin
FROM base as weixin
COPY backend/weixin/weixin-1.0-SNAPSHOT.jar weixin.jar
EXPOSE 7777
RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo "Asia/Shanghai" > /etc/timezone
ENTRYPOINT ["java", "-jar", "weixin.jar"]

# 定义第二个构建阶段 admin
FROM base as admin
COPY backend/admin/admin-1.0-SNAPSHOT.jar admin.jar
EXPOSE 7070
RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo "Asia/Shanghai" > /etc/timezone
ENTRYPOINT ["java", "-jar", "admin.jar"]
```

#### 5.一键部署

在根目录下执行

```shell
docker-compose up -d
```

### 问题

#### 志愿活动没有显示排班

后端容器时间问题 -e TZ="Asia/Shanghai"
