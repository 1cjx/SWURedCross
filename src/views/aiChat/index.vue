<template>
	<div class="app">
		<!-- 角色背景部分 -->
		<div v-if="role=='volunteer'" class="role_background role_volunteer"></div>
		<div v-if="role=='editor'" class="role_background role_editor"></div>
		<!-- 顶部 -->
		<div class="top">
			<!-- 左上角内容 -->
			<div class="select">
				<!-- 左上角选择会话 -->
				<el-select v-model="sessionId" placeholder="新聊天" style="width: 12%">
					<el-option v-for="item in sessionList" :key="item.id" :label="item.name" :value="item.id">
						<template>
							<el-tooltip :disabled="true" v-overflow-tooltip="item.name" style="display: inline-block; width: 130px;">
								<span> {{ item.name }}</span>
							</el-tooltip>
						</template>
					</el-option>
				</el-select>
				<!-- 左上角新建会话 -->
				<div class="add_session" v-debounce="addSession">
					<el-tooltip content="新聊天" placement="bottom">
						<i class="el-icon-edit-outline"></i>
					</el-tooltip>
				</div>
			</div>
		</div>
		<!-- 中间部分 -->
		<div class="container" ref="container" @scroll="handleScroll">

			<!-- 角色选择部分 -->
			<div class="role_select">
				<button :class="['role','test_red', {test_red_active: role === 'volunteer' }]"
					v-debounce="()=>selectRole('volunteer')">
					资深志愿者
				</button>
				<button :class="['role','test_green', {test_green_active: role === 'editor' }]"
					v-debounce="()=>selectRole('editor')">
					文案小助手
				</button>
			</div>
			<!-- 聊天部分-->
			<div class="chat_container">
				<!-- 空聊天窗口 -->
				<div v-if="questionList.length==0" class="empty_container">
					<div class="empty_content">
						<img src="@/assets/images/logo.png" width="50" height="50"></img>
						<div class="choose_content">
							<div v-for="item in randomQuestion">
								<div v-debounce="()=>sendRandom(item)" class="choose_content_item">
									<img :src="require('@/assets/icons/png/'+item.imgPath+'.png')" width="25px" height="25px">
									<p>{{item.content}}</p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- 历史聊天窗口 -->
				<div v-else v-for="item in questionList">
					<div class="content_item">
						<div :class="item.role">
							<img v-if="item.role=='assistant'" src="@/assets/images/logo.png" width="30px" height="30px"></img>
							<div v-html="renderContent(item.content)"></div>
							<img v-if="item.role=='user'" src="@/assets/images/profile.png" width="30px" height="30px"></img>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- 底部 -->
		<div class="footer">
			<!-- 输入部分 -->
			<div class="content_input">
				<!-- 选择活动部分 -->
				<el-tooltip effect="dark" :disabled="chooseActivity==null" placement="top-start" v-if="role=='editor'">
					<!-- 选择后气泡显示内容 -->
					<div slot="content" class="tips">
						<div class="content">
							{{chooseActivity==null?'':'已选活动：'+formatActivityName(chooseActivity.name)}}
							<i class="el-icon-circle-close" style="cursor: pointer;font-size:15px"
								@click="handleRemoveChooseActivity"></i>
						</div>
					</div>
					<!-- 选择活动图标 -->
					<img :src="require('@/assets/icons/png/choose_activity'+(chooseActivity==null?'':'_active')+'.png')"
						width="25" height="25" @click="handleOpenChooseActivity" />
				</el-tooltip>
				<!-- 输入框 -->
				<el-input v-model="temp" style="width:100%" @keyup.enter.native="sendInput" placeholder="快来输入消息吧~"></el-input>
				<!-- 发送与暂停 -->
				<img v-if="buttonFlag" v-debounce="()=>sendInput()"
					:src="require('@/assets/icons/png/send'+(temp==''?'':'_active')+'.png')" width="30" height="30" />
				<img v-else v-debounce="()=>stop()" src="@/assets/icons/png/stop.png" width="27" height="27" />
			</div>
			<!-- 底部文本 -->
			<div class="footer_text">该对话助手基于科大讯飞旗下的星火认知大模型Spark Max版本进行开发</div>
		</div>
		<!-- 选择活动弹窗 -->
		<el-dialog title="选择活动" :visible.sync="dialogTableVisible" @close="handleCloseChooseActivity" width="900px">
			<!-- 搜索条件 -->
			<el-form ref="queryForm" :model="queryParams" :inline="true">
				<el-row>
					<el-form-item label="活动名称" prop="name">
						<el-input v-model="queryParams.name" placeholder="请输入活动名称" clearable size="small"
							@keyup.enter.native="handleQuery" />
					</el-form-item>
					<el-form-item label="活动分类" prop="category">
						<el-select v-model="queryParams.categoryId" placeholder="请选择" @click.native="listAllCategory()" size="small"
							clearable>
							<el-option v-for="item in categoryOptions" :key="item.id" :label="item.name" :value="item.id" />
						</el-select>
					</el-form-item>
					<el-form-item style="margin-left: 20px;">
						<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
					</el-form-item>
				</el-row>
			</el-form>
			<!-- 活动表格 -->
			<el-table v-loading="loading" :data="activityList" row-key="id" max-height="300px" ref="chooseActivityTable"
				style="height: 300px;" @selection-change="handleChooseActivity">
				<el-table-column width="30">
					<template slot-scope="scope">
						<el-radio class="radio" :label="scope.row.id" v-model="radio"
							@change.native="handleChooseActivity(scope.row)"></el-radio>
					</template>
				</el-table-column>
				<el-table-column label="活动名称" prop="name" align="center" :show-overflow-tooltip="true" />
				<el-table-column label="活动分类" align="center" prop="category" :show-overflow-tooltip="true" />
				<el-table-column label="活动主题" align="center" prop="theme" :show-overflow-tooltip="true"></el-table-column>
				<el-table-column label="活动日期" align="center" :show-overflow-tooltip="true">
					<template slot-scope="scope">
						<span>{{scope.row.beginDate}} 至 {{scope.row.endDate}}</span>
					</template>
				</el-table-column>
			</el-table>
			<el-row style="margin-top:20px">
				<el-col :span="18">
					<!-- 底部分页 -->
					<el-pagination :page-size.sync="queryParams.pageSize" layout="total, sizes, prev, pager, next, jumper"
						:total="total" :page-sizes="[10, 20, 30, 40]" :current-page.sync="queryParams.pageNum"
						@current-change="getActivityList" @size-change="getActivityList" />
				</el-col>
				<!-- 底部按钮 -->
				<el-col :span="2"><el-button size="small" :type="tempChooseActivity==null?'':'primary'"
						@click="handleSubmitChooseActivity">确认</el-button></el-col>
				<el-col :span="2"><el-button size="small" @click="handleCloseChooseActivity">取消</el-button></el-col>
			</el-row>
		</el-dialog>
	</div>
</template>

<script>
	import {
		getRandomQuestion,
		getSessionHistoryQA,
		getUserSessionList
	} from '@/api/content/chat.js'
	import {
		listAllCategory
	} from '@/api/utils/selectOptions'
	import {
		getChatActivityList
	} from '@/api/content/activity.js'
	import {
		marked
	} from 'marked';
	import hljs from "highlight.js"; // 引入 highlight.js
	import "highlight.js/styles/darcula.css" // 引入高亮样式 
	import {
		getToken
	} from '@/utils/auth'
	export default {
		data() {
			return {
				websock: null, //websocket
				answer: "",
				temp: "",
				questionList: [],
				shouldScroll: true,
				buttonFlag: true,
				randomQuestion: [],
				sessionId: 3,
				sessionList: [],
				dialogTableVisible: false,
				queryParams: {
					pageNum: 1,
					pageSize: 10,
					name: null,
					categoryId: undefined,
					status: undefined
				},
				loading: false,
				categoryOptions: [],
				// 总条数
				total: 0,
				// 活动数据
				activityList: [],
				// 弹出层标题
				title: '',
				tempChooseActivity: null,
				chooseActivity: null,
				radio: '',
				websocketUrl: process.env.VUE_APP_WEBSOCKET_URL,
				role: '',
			};
		},
		watch: {
			questionList: {
				handler() {
					this.$nextTick(() => {
						if (this.shouldScroll) {
							const container = this.$refs.container;
							if (container) {
								container.scrollTop = container.scrollHeight;
							}
						}
					});
				},
				deep: true,
			},
			sessionId(newData) {
				if (newData != null) {
					getSessionHistoryQA(this.sessionId).then((res) => {
						this.questionList = res;
						// 切换后置于底部
						const container = this.$refs.container;
						container.scrollTo({
							top: container.scrollHeight,
							behavior: 'smooth'
						});
					})
				}
			},
		},
		created() {
			getRandomQuestion().then((res) => {
				this.randomQuestion = res;
			});
			getUserSessionList().then((res) => {
				this.sessionList = res;
				this.sessionId = res[0].id;
			})
		},
		mounted() {
			const container = this.$refs.container;
			if (container != undefined) {
				container.addEventListener("scroll", this.handleScroll);
			}
			// this.initWebSocket()
			var rendererMD = new marked.Renderer();
			marked.setOptions({
				renderer: rendererMD,
				highlight: function(code) {
					return hljs.highlightAuto(code).value;
				},
				pedantic: false,
				gfm: true,
				tables: true,
				breaks: true,
				sanitize: false,
				smartLists: true,
				smartypants: false,
				xhtml: false
			});
		},
		beforeDestroy() {
			const container = this.$refs.container;
			if (container != undefined) {
				container.removeEventListener("scroll", this.handleScroll);
			}
		},
		methods: {
			bgImageStyle() {
				if (this.role == "") {
					return;
				} else {
					return {
						'background-image': `url(${this.role}_background.png)`,
						'background-size': 'cover',
						'background-position': 'center',
					};
				}
			},
			selectRole(role) {
				//取消角色
				if (this.role == role) {
					this.$modal.msgSuccess('取消角色' + this.role + "成功")
					this.role = ""
				}
				//切换角色
				else {
					this.role = role;
					this.$modal.msgSuccess('以切换至角色' + this.role)
				}
			},

			formatActivityName(str) {
				// 检查字符串长度是否大于15
				if (str.length > 12) {
					// 如果大于15，返回前15个字符并加上 "..."
					return str.substring(0, 12) + '...';
				} else {
					// 如果小于等于15，返回整个字符串
					return str;
				}
			},
			handleRemoveChooseActivity() {
				this.chooseActivity = null;
				this.radio = '';
			},
			handleSubmitChooseActivity() {
				if (this.tempChooseActivity == null) {
					this.$modal.msgError("还未选择活动")
				} else {
					this.$confirm('确认选择当前活动 [ ' + this.formatActivityName(this.tempChooseActivity.name) + ' ] ?', '提示', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning'
					}).then(() => {
						this.$message({
							type: 'success',
							message: '选择成功!'
						});
						this.dialogTableVisible = false;
						this.chooseActivity = this.tempChooseActivity;
						this.tempChooseActivity = null;
					}).catch(() => {});

				}
			},
			handleCloseChooseActivity() {
				this.dialogTableVisible = false;
				this.tempChooseActivity = null;
			},
			// 单选
			handleChooseActivity(val) {
				this.tempChooseActivity = val;
			},
			handleOpenChooseActivity() {
				this.tempChooseActivity = this.chooseActivity;
				this.dialogTableVisible = true;
				this.getActivityList()
			},
			/** 搜索按钮操作 */
			handleQuery() {
				this.queryParams.pageNum = 1
				this.getActivityList()
			},
			listAllCategory() {
				listAllCategory().then(res => {
					this.categoryOptions = res;
				})
			},
			getActivityList() {
				this.loading = true
				getChatActivityList(this.queryParams).then(res => {
					this.activityList = res.rows
					this.total = res.total
					this.loading = false
				})
			},
			addSession() {
				this.sessionId = null;
				this.questionList = [];
			},
			handleScroll() {
				const container = this.$refs.container;
				if (container) {
					const isAtBottom =
						container.scrollTop + container.clientHeight >= container.scrollHeight - 1;
					if (!isAtBottom) {
						this.shouldScroll = false;
					} else {
						this.shouldScroll = true;
					}
				}
			},
			renderContent(content) {
				return marked(content).replace(/<pre>/g, "<pre class='hljs'>")
			},
			// WebSocket
			initWebSocket() {
				//初始化weosocket
				//这里的wsuri是根据服务端的ip进行配置（开发环境），生产环境的话可以使用上面的Ip或者是nginx做代理
				const wsuri = this.websocketUrl; //协议标识符是ws（如果加密，则为wss），服务器网址就是 URL。 
				this.websock = new WebSocket(wsuri, [getToken(), 'WebSocket']);
				this.websock.onmessage = this.websocketonmessage;
				this.websock.onopen = this.websocketonopen;
				this.websock.onerror = this.websocketonerror;
				this.websock.onclose = this.websocketclose;
			},
			sendInput() {
				if (this.temp == '') {
					this.$modal.msgError('输入不能为空')
				} else {
					this.initWebSocket()
					if (this.role == 'editor') {
						let chooseActivity = this.chooseActivity;
						let locations = chooseActivity.locations.map(o => {
							return o.name;
						})
						let activityContent = "在时间[ " + chooseActivity.beginDate + " 至 " + chooseActivity.endDate + " ]举办活动名为[ " +
							chooseActivity.name +
							" ],主题为[ " + chooseActivity.theme + " ]的[ " + chooseActivity.category + " ]活动," +
							"举办的地点在[ " + locations + " ]。\n" + this.temp;
						this.websocketsend(activityContent)
					} else {
						this.websocketsend(this.temp);
					}
					this.temp = ''
				}
			},
			sendRandom(e) {
				this.initWebSocket()
				this.websocketsend(e.content)
			},
			stop() {
				this.websocketclose();
				this.buttonFlag = true;
			},
			websocketsend(text) { //数据发送
				if (this.websock.readyState === WebSocket.OPEN) {
					this.answer = "";
					let msg = JSON.stringify({
						"sessionId": this.sessionId,
						"text": text,
						"systemRole": this.role,
					});
					//向后端发送消息
					this.websock.send(msg);
					this.questionList.push({
						"role": "user",
						"content": text
					});
					this.questionList.push({
						"role": "assistant",
						"content": ""
					});

					this.buttonFlag = false; // 转变为暂停按钮
					// 发送消息后应该置于底部
					this.$nextTick(() => {
						const container = this.$refs.container;
						container.scrollTop = container.scrollHeight;
					})
				} else {
					const that = this;
					setTimeout(() => {
						this.websocketsend(text)
					}, 1000)
				}
			},
			websocketonopen() { //连接建立之后执行send方法发送数据
			},
			websocketonerror(error) { //连接建立失败重连
				// this.initWebSocket();
			},
			websocketonmessage(e) { //数据接收
				let data = JSON.parse(e.data)
				console.log(data)
				//消息接收完毕
				if (data.data == "[END]") {
					// 新聊天建立
					if (this.sessionId == null) {
						getUserSessionList().then((res) => {
							this.sessionList = res;
							this.sessionId = res[0].id;
						})
					}
					this.buttonFlag = true;
					this.websocketclose()
				} else {
					this.questionList[this.questionList.length - 1].content += data.data
					console.log(this.questionList[this.questionList.length - 1])
				}
			},
			websocketclose(e) { //关闭
				console.log("关闭了")
				this.websock.close()
			}
		}
	};
</script>
<style>
	.app {}

	.top {
		margin: 5px;
	}

	.select {
		display: flex;
	}

	.select>.el-select>.el-input--suffix>.el-input__inner,
	.el-input__suffix>.el-input__suffix-inner>.el-input__icon {
		border: none !important;
		font-size: 15px;
		color: #a6a6a6;
		font-weight: bold;
	}

	.select>.el-select>.el-input.el-input--suffix>.el-input__inner {
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.el-select-dropdown {
		max-width: 20%;
	}

	.add_session {
		color: #a0a0a0;
		font-size: 25px;
		display: flex;
		cursor: pointer;
		align-items: center;

	}

	.container {
		display: flex;
		overflow-y: auto;
		height: 615px;
	}

	.role_select {
		top: 38%;
		left: 6%;
		width: 6%;
		height: 12%;
		position: absolute;
	}

	.role {
		margin-top: 20%;
		height: 100%;
		width: 200%;
		display: inline-block;
		padding: 10px 20px;
		transition: transform 0.3s ease;
		border: none;
		border-top-left-radius: 20px;
		border-top-right-radius: 10px;
		border-bottom-right-radius: 15px;
		border-bottom-left-radius: 5px;
		text-align: center;
		cursor: pointer;
		text-decoration: none;
	}

	.test_green {
		background-color: #2ecc71;
		color: #fff;
		box-shadow: 0 5px 15px rgba(46, 204, 113, 0.4);
	}

	.test_green:hover {
		transform: translateY(-3px);
		box-shadow: 0 8px 20px rgba(46, 204, 113, 0.6);
	}

	.test_green_active {
		transform: translateY(-3px);
		box-shadow: 0 8px 20px rgba(46, 204, 113, 0.6);
	}

	.test_red {
		background-color: #e50516;
		/* 红色背景色 */
		color: #fff;
		box-shadow: 0 5px 15px rgba(229, 5, 22, 0.4);
	}

	.test_red:hover {
		background-color: #c4000f;
		/* 悬停时的红色 */
		transform: translateY(-3px);
		box-shadow: 0 8px 20px rgba(229, 5, 22, 0.6);
	}

	.test_red_active {
		transform: translateY(-3px);
		box-shadow: 0 8px 20px rgba(229, 5, 22, 0.6);
	}

	.role_background {
		z-index: -1;
		height: 55%;
		width: 32%;
		opacity: 0.6;
		position: absolute;
		background-size: cover;
		left: 65%;
		top: 40%;
	}

	.role_volunteer {
		background-image: url('../../assets/images/volunteer_background.png');
	}

	.role_editor {
		background-image: url('../../assets/images/editor_background.png');
	}

	.chat_container {
		width: 60%;
		margin-left: 20%;
		border-radius: 5px;
		box-shadow: 10px 5px 5px #white;
	}

	.empty_container {
		text-align: center;
		height: 100%;
		position: relative;
	}

	.empty_content {
		margin: 0;
		position: absolute;
		top: 40%;
		left: 50%;
		transform: translate(-50%, -50%);
	}

	.choose_content {
		display: flex;
		margin: 30px;
		width: fit-content;
	}

	.choose_content_item {
		margin-left: 20px;
		color: #5f5f5f;
		cursor: pointer;
		height: 100px;
		font-size: 13px;
		border-radius: 15px;
		padding: 15px 10px 10px 10px;
		box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
		width: 150px;
	}

	.assistant,
	.user {
		display: flex;
		margin-top: 10px;
	}

	.assistant {
		justify-content: left;
	}

	.user {
		justify-content: right;
	}

	.user>div {
		margin-right: 20px;
		background-color: #f6f6f6;
	}

	.assistant>div {
		margin-left: 20px;
	}

	.user>div,
	.assistant>div {
		max-width: 80%;
		text-align: left;
		padding: 0px 20px 0px 20px;
		border-radius: 5px;
	}

	.user>img,
	.assistant>img {
		margin-top: 10px;
	}

	.content_item {
		padding: 0px 20px 0px 20px;
	}

	.content_input {

		width: 52%;
		display: flex;
		justify-content: center;
		margin: 10px auto 0px auto;
		padding: 5px 10px 5px 10px;
		border-radius: 50px;
		background-color: #f6f6f6;
		align-items: center;
	}

	.content_input>.el-input>.el-input__inner {
		border: 0 !important;
		background-color: #f6f6f6;
	}

	.content_input>img {
		cursor: pointer;
	}

	.footer_text {
		width: fit-content;
		margin: auto;
		margin-top: 3px;
		font-size: 13px;
		color: #909090;
	}
</style>