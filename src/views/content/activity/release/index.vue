<template>
	<div class="app-container">
		<!--头部活动信息-->
		<el-form v-show="showSearch" ref="form" :model="form" :inline="true" :rules="formRules" label-width="168px">
			<el-row>
				<el-form-item label="活动名称" prop="name">
					<el-input v-bind:readonly="type == 'view'" v-model="form.name" placeholder="请输入活动名称" size="small" />
				</el-form-item>

				<el-form-item label="分类" prop="categoryId">
					<el-select v-model="form.categoryId" placeholder="请选择" v-bind:disabled="type == 'view'" size="small"
						clearable>
						<el-option v-for="item in categoryOptions" :key="item.id" :label="item.name" :value="item.id" />
					</el-select>
				</el-form-item>

				<el-form-item label="招募权限" prop="allowedDepartmentId">
					<el-select v-model="form.allowedDepartmentId" placeholder="请选择" size="small" v-bind:disabled="type == 'view'"
						clearable>
						<el-option v-for="item in departmentOptions" :key="item.id" :label="item.name" :value="item.id" />
					</el-select>
				</el-form-item>
			</el-row>

			<el-form-item label="活动主题" prop="theme">
				<el-input v-model="form.theme" v-bind:readonly="type == 'view'" placeholder="请输入活动主题" clearable size="small" />
			</el-form-item>

			<el-form-item label="开始时间" prop="beginDate">
				<div class="block">
					<el-date-picker v-bind:readonly="type == 'view'" v-model="form.beginDate" align="right" type="date"
						size="small" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 190px" @change="changeBeginDate"
						:picker-options="pickerOptionsStart">
					</el-date-picker>
				</div>
			</el-form-item>

			<el-form-item label="结束时间" prop="endDate">
				<div class="block">
					<el-date-picker :disabled="form.beginDate == undefined" v-bind:readonly="type == 'view'"
						v-model="form.endDate" align="right" type="date" size="small" value-format="yyyy-MM-dd" :placeholder="
              form.beginDate == undefined ? '请先选择开始日期' : '选择日期'
            " style="width: 190px" :picker-options="pickerOptionsEnd">
					</el-date-picker>
				</div>
			</el-form-item>
			<el-row :gutter="10" class="mb8">
				<el-col :span="1.5" style="
            font-family: Arial, Helvetica, sans-serif;
            font-weight: 600;
            margin-left: 70px;
            size: small;
          ">
					班次信息
				</el-col>
				<el-col :span="1.5">
					<el-tooltip placement="top" :disabled="
              !(form.beginDate == undefined || form.endDate == undefined)
            " content="请先选择活动时间">
						<el-button v-if="type !== 'view'" type="primary" plain :disabled="
                form.beginDate == undefined || form.endDate == undefined
              " icon="el-icon-plus" size="mini" @click="handleAddAssignment">添加班次</el-button>
					</el-tooltip>
				</el-col>
				<el-col :span="4" :push="12">
					<el-form-item>
						<el-button type="primary" v-if="type != 'view'" size="medium"
							@click="handleSubmit">{{ aId ? "更新" : "发布" }}</el-button>
					</el-form-item>
					<el-form-item>
						<el-button v-if="!aId" type="info" @click="handleSave">保存到草稿箱</el-button>
					</el-form-item>
				</el-col>
			</el-row>
		</el-form>
		<!--下方信息表格-->
		<!-- 一级菜单 班次信息 -->
		<el-table v-loading="loading" style="margin-top: 10px" :data="form.activityAssignmentList" :key="isUpdate"
			row-key="id" :row-class-name="oneLevelTableRowClassName">
			<el-table-column type="expand">
				<template slot-scope="activityAssignment">
					<!-- 二级菜单 岗位信息 -->
					<el-table class="table-in-table" :data="activityAssignment.row.postList" style="width: 90%; margin: auto"
						row-key="id" border :row-class-name="twoLevelTableRowClassName">
						<el-table-column type="expand">
							<template slot-scope="postAssignment">
								<!--  三级菜单 志愿者信息 -->
								<el-table class="table-in-table" :data="postAssignment.row.volunteerInfoBoList"
									style="width: 80%; margin: auto" row-key="id" border>
									<el-table-column prop="name" label="志愿者姓名" align="center"></el-table-column>
									<el-table-column prop="phoneNumber" label="电话" align="center"></el-table-column>
									<el-table-column prop="qq" label="QQ" align="center"></el-table-column>
									<el-table-column v-if="type !== 'view'" label="操作" align="center"
										class-name="small-padding fixed-width">
										<template slot-scope="scope">
											<el-button size="mini" type="text" icon="el-icon-delete"
												@click="handleVolunteerDelete(scope.row)">删除</el-button>
										</template>
									</el-table-column>
								</el-table>
							</template>
						</el-table-column>
						<el-table-column prop="postName" label="岗位名" width="132.2" align="center"></el-table-column>
						<el-table-column label="需求人数" width="180" align="center">
							<template slot-scope="scope">
								<span>{{
                      scope.row.nowPeople == undefined ? 0 : scope.row.nowPeople
                    }}
									/ {{ scope.row.reqPeople }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="allowedDepartmentId" label="允许部门" align="center">
							<template slot-scope="scope">
								<span>{{
                    allowedDepartmentOptions[
                      allowedDepartmentOptions.findIndex(
                        (item) => item.id == scope.row.allowedDepartmentId
                      )
                    ].name
                  }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="allowedTitleId" label="允许职称" align="center">
							<template slot-scope="scope">
								<span>{{
                    allowedTitleOptions[
                      allowedTitleOptions.findIndex(
                        (item) => item.id == scope.row.allowedTitleId
                      )
                    ].name
                  }}</span>
							</template>
						</el-table-column>
						<el-table-column v-if="type !== 'view'" label="操作" align="center" class-name="small-padding fixed-width">
							<template slot-scope="scope">
								<el-button size="mini" type="text" icon="el-icon-delete" @click="
                      handlePostAssignmentDelete(
                        activityAssignment.row,
                        scope.row
                      )
                    ">删除</el-button>
							</template>
						</el-table-column>
					</el-table>
				</template>
			</el-table-column>
			<el-table-column label="日期" align="center" prop="time" />

			<el-table-column label="时间段" align="center">
				<template slot-scope="scope">
					<span>{{ scope.row.beginTime }}-{{ scope.row.endTime }}</span>
				</template>
			</el-table-column>
			<el-table-column label="地点" align="center" prop="locationName" />
			<el-table-column label="是否招募" align="center" prop="status">
				<template slot-scope="scope">
					<span v-if="type === 'view'">{{
              scope.row.status == "0" ? "否" : "是"
            }}</span>
					<el-switch v-if="type !== 'view'" :value="scope.row.status" :active-value="'1'" :inactive-value="'0'"
						@change="handleStatusChange(scope.row)" />
				</template>
			</el-table-column>
			<el-table-column label="类型" align="center" prop="typeName" />
			<el-table-column label="工作群" align="center" prop="groupAccount" >
				<template slot-scope="scope">
				      {{scope.row.groupAccount}}
							 <el-tooltip class="item" effect="dark" content="点击查看二维码" placement="right">
							     <el-button size="mini"  icon="el-icon-zoom-in" type="text"
							     	@click="handlePictureCardPreview(scope.row)"></el-button>
							    </el-tooltip>
				</template>
				</el-table-column>
			<el-table-column v-if="type !== 'view'" label="操作" align="center" class-name="small-padding fixed-width">
				<template slot-scope="scope">
					<el-button size="mini" type="text" icon="el-icon-edit"
						@click="handleAssignmentUpdate(scope.row)">修改</el-button>
					<el-button size="mini" type="text" icon="el-icon-delete"
						@click="handleAssignmentDelete(scope.row)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>
		</el-form>
		<!-- 缩略图 -->
		<el-dialog title="工作群聊二维码" :visible.sync="dialogVisible" :append-to-body="true">
			<div style="text-align: center;">
			<img style="height:50%;width: 50%;" :src="dialogImageUrl" alt="">
			</div>
		</el-dialog>
		<!-- 添加或修改对话框 -->
		<el-dialog :title="title" :visible.sync="open" width="650px" append-to-body @close='handleCancle'>
			<div class="dialog-content">
				<el-form ref="assignmentForm" :model="assignmentForm" :rules="assignmentRules" label-width="80px">
					<el-row>
						<el-col :span="12">
							<el-form-item label="日期" prop="time">
								<el-date-picker v-model="assignmentForm.time" align="right" type="date" size="small"
									value-format="yyyy-MM-dd" placeholder="选择日期" :picker-options="assignmentPickerOption">
								</el-date-picker>
							</el-form-item>
						</el-col>
						<el-col :span="12">
							<el-form-item label="地点" prop="locationId">
								<el-select v-model="assignmentForm.locationId" placeholder="请选择" size="small" clearable>
									<el-option v-for="item in locationOptions" :key="item.id" :value="item.id" :label="item.name" />
								</el-select>
							</el-form-item>
						</el-col>
					</el-row>
					<el-row>
						<el-col :span="12">
							<el-form-item label="时间段" prop="timeSlotId">
								<el-select v-model="assignmentForm.timeSlotId" placeholder="请选择" size="small" clearable>
									<el-option v-for="item in timeSlotOptions" :key="item.id"
										:label="format(item.beginTime, item.endTime)" :value="item.id" />
								</el-select>
							</el-form-item>
						</el-col>
						<el-col :span="12">
							<el-form-item label="班次类型" prop="typeId">
								<el-select v-model="assignmentForm.typeId" placeholder="请选择" size="small" clearable>
									<el-option v-for="item in assignmentTypeOptions" :key="item.id" :label="item.name" :value="item.id" />
								</el-select>
							</el-form-item>
						</el-col>
					</el-row>
					<el-form-item label="工作群二维码" label-width="120px" prop="qrCodeUrl">
						<el-upload :file-list="fileList" action="#" list-type="picture-card" accept=".jpg,.png" :limit="1"
							:before-upload="beforeUpload" :http-request="handleUpload" :on-exceed="onExceed">
							<i slot="default" class="el-icon-plus"></i>
							<div slot="file" slot-scope="{file}">
								<img class="el-upload-list__item" :src="file.url" alt="">
								<span class="el-upload-list__item-actions">
									<span class="el-upload-list__item-preview" @click="handlePictureCardPreview(file)">
										<i class="el-icon-zoom-in"></i>
									</span>
									<span v-if="!disabled" class="el-upload-list__item-delete" @click="fileRemove(file)">
										<i class="el-icon-delete"></i>
									</span>
								</span>
							</div>
						</el-upload>
					</el-form-item>
					<el-form-item label="工作群号" prop="groupAccount">
						<el-input v-model="assignmentForm.groupAccount" style="width:40%" minlength="5" maxlength="10"
							onkeyup="value=value.replace(/^(0+)|[^\d]+/g,'')"></el-input>
					</el-form-item>
					<el-form-item label="岗位信息" prop="postList">
						<el-select v-model="assignmentForm.postList" value-key="postId"
							:placeholder="isSelect() ? '请先填写以上数据' : '选择岗位'" :disabled="isSelect()" @remove-tag="removeTag"
							size="small" :clearable="assignmentForm.id === undefined" v-default-select="[choosePostOptions]" multiple
							collapse-tags style="width: 250px">
							<el-option v-for="item in postOptions" :key="item.id" :disabled="
                !(
                  choosePostOptions.find((o) => o.postId == item.id) ==
                  undefined
                )
              " :label="item.name" :value="{
                id: '$' + new Date().getTime() + item.id,
                postId: item.id,
                postName: item.name,
                reqPeople: 1,
                allowedDepartmentId: '0',
                allowedTitleId: '0',
              }" />
						</el-select>
					</el-form-item>
					<el-table border :data="assignmentForm.postList" max-height="200" size="mini" key="id">
						<el-table-column prop="postName" label="岗位名" align="center" />
						<el-table-column prop="reqPeople" label="所需人数" align="center" width="200px" style="margin-top: 500px">
							<template slot-scope="scope">
								<el-form-item label-width="0px" style="margin-top: 20px"
									:prop="'postList.' + scope.$index + '.reqPeople'"
									:rules="{required: true,message:'所需人数不为空', trigger: 'blur' }">
									<el-input-number v-model="scope.row.reqPeople" size="mini" :min="
                    scope.row.nowPeople == undefined || scope.row.nowPeople == 0
                      ? 1
                      : parseInt(scope.row.nowPeople)
                  " :max="20" controls-position="right"></el-input-number>
								</el-form-item>
							</template>
						</el-table-column>
						<el-table-column prop="allowedDepartmentId" label="允许部门" align="center">
							<template slot-scope="scope">
								<el-form-item label-width="0px" style="margin-top: 20px"
									:prop="'postList.' + scope.$index + '.allowedDepartmentId'"
									:rules="{required: true,message: '允许部门不为空', trigger: 'blur' }">
									<el-select v-model="scope.row.allowedDepartmentId" placeholder="选择部门" size="mini" clearable>
										<el-option v-for="item in allowedDepartmentOptions" :key="item.id" :label="item.name"
											:value="item.id" />
									</el-select>
								</el-form-item>
							</template>
						</el-table-column>
						<el-table-column prop="allowedTitleId" label="允许职称" align="center">
							<template slot-scope="scope">
								<el-form-item label-width="0px" style="margin-top: 20px"
									:prop="'postList.' + scope.$index + '.allowedTitleId'"
									:rules="{required: true,message:'允许职称不为空', trigger: 'blur' },">
									<el-select v-model="scope.row.allowedTitleId" placeholder="选择职称" size="mini" clearable>
										<el-option v-for="item in allowedTitleOptions" :key="item.id" :label="item.name" :value="item.id" />
									</el-select>
								</el-form-item>
							</template>
						</el-table-column>
					</el-table>
				</el-form>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submitForm">确 定</el-button>
				<el-button @click="cancel">取 消</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import {
		getActivityDetail,
		addActivity,
		updateActivity,
	} from "@/api/content/activity";
	import {
		listAllTitle,
		listAllTimeSlot,
		listAllCategory,
		listAllDepartment,
		listAllLocation,
		listAllAssignmentType,
		listAllPost,
	} from "@/api/utils/selectOptions";
	import {
		uploadImg
	} from "@/api/utils/upload";
	export default {
		name: "location",
		data() {
			var validateQrCodeUrl = (rule, value, callback) => {
				if (this.fileList.length === 0) {
					console.log(this.fileList)
					callback(new Error('工作群二维码不能为空'))
				} else {
					callback()
				}
			};
			var validateGroupAccount = (rule, value, callback) => {
				if (this.assignmentForm.groupAccount == undefined) {
					callback(new Error('工作群号不能为空'))
				} else if (this.assignmentForm.groupAccount.length < 5 || this.assignmentForm.groupAccount.length > 10) {
					callback(new Error('工作群号长度有误'))
				} else {
					callback()
				}
			};
			return {
				fileList: [],
				//预览图片路径
				dialogImageUrl: '',
				//是否预览图片
				dialogVisible: false,
				disabled: false,
				// 遮罩层
				loading: true,
				dialogLoading: null,
				// 显示搜索条件
				showSearch: true,
				reflush: true,
				// 总条数
				total: 0,
				// 活动数据
				activityList: null,
				// 弹出层标题
				title: "",
				// 是否显示弹出层
				open: false,
				categoryParams: {
					pageNum: 1,
					pageSize: 40,
				},
				//是否更新班次信息
				isUpdate: false,
				timeSlotOptions: [],
				locationOptions: [],
				postOptions: [],
				categoryOptions: [],
				assignmentTypeOptions: [],
				// 选择的岗位信息
				choosePostOptions: [],
				addPostAssignemntForm: {},
				// 判断是否有重复班次
				assignmentCheck: [],
				allowedTitleOptions: [{
					id: "0",
					name: "任何职称",
				}, ],
				allowedDepartmentOptions: [{
					id: "0",
					name: "任何部门",
				}, ],
				// 0为红会内部所有部门 -1 表示全体志愿者
				departmentOptions: [{
						id: "-1",
						name: "内外全体成员",
					},
					{
						id: "0",
						name: "内部全体成员",
					},
				],
				//活动表单参数
				form: {
					activityAssignmentList: [],
				},
				formRules: {
					name: [{
						required: true,
						message: "活动名称不为空",
						trigger: "blur"
					}],
					categoryId: [{
						required: true,
						message: "活动分类不为空",
						trigger: "blur"
					}, ],
					allowedDepartmentId: [{
						required: true,
						message: "招募权限不为空",
						trigger: "blur"
					}, ],
					theme: [{
						required: true,
						message: "主题不为空",
						trigger: "blur"
					}],
					beginDate: [{
						required: true,
						message: "开始日期不为空",
						trigger: "blur"
					}, ],
					endDate: [{
						required: true,
						message: "结束日期不为空",
						trigger: "blur"
					}, ],
				},
				//班次信息的表单
				assignmentForm: {},
				// 表单校验
				assignmentRules: {
					time: [{
						required: true,
						message: "班次时间不为空",
						trigger: "blur"
					}],
					locationId: [{
						required: true,
						message: "地点不为空",
						trigger: "blur"
					}, ],
					timeSlotId: [{
						required: true,
						message: "时间段不为空",
						trigger: "blur"
					}, ],
					typeId: [{
						required: true,
						message: "班次类型不为空",
						trigger: "blur"
					}, ],
					postList: [{
						required: true,
						message: "岗位信息不为空",
						trigger: "blur"
					}, ],
					qrCodeUrl: [{
						required: true,
						validator: validateQrCodeUrl,
						trigger: 'blur'
					}],
					groupAccount: [{
						required: true,
						validator: validateGroupAccount,
						trigger: 'blur'
					}],
				},
				// 该页面类型
				type: "add",
				aId: -1,
				assignmentPickerOption: {
					disabledDate: (time) => {
						return this.disabledTime(time);
					},
				},
				pickerOptionsStart: {
					disabledDate: (time) => {
						return this.disabledStart(time);
					},
				},
				pickerOptionsEnd: {
					disabledDate: (time) => {
						return this.disabledEnd(time);
					},
				},
			};
		},
		watch: {
			$route: {
				handler: function(route) {
					this.resetForm();
					this.aId = route.query && route.query.id;
					this.type = route.query && route.query.type;
				},
				deep: true,
				immediate: true,
			},
		},
		created() {
			this.getInformation();
			if (this.type !== "add" && this.aId) {
				this.getActivityDetail();
			} else {
				let tempActivity = JSON.parse(localStorage.getItem('tempActivity'));
				console.log(tempActivity)
				if (tempActivity != null) {
					const that = this;
					this.$modal
						.confirm('草稿箱内有上一次填写内容，是否继续填写？')
						.then(function() {
							//确认通过执行逻辑
							that.form = tempActivity;
						})
				}
				localStorage.removeItem('tempActivity')
			}
			this.loading = false;
		},
		methods: {
			beforeUpload(file) {
				// 在上传之前的钩子，返回 false 可以取消上传
				if (file.type !== "image/png" && file.type !== "image/jpeg" && file.type !== "image/jpg") {
					this.$message.error("只能上传png或jpg文件");
					return false;
				}
				return true;
			},
			handlePictureCardPreview(file) {
				let url = file.url || file.qrCodeUrl;
				this.dialogImageUrl = url;
				this.dialogVisible = true;
			},
			handleUpload(file) {
				var reader = new FileReader();
				let tempFile = file.file;
				reader.readAsDataURL(tempFile);
				const that = this;
				reader.onload = function(e) {
					tempFile.url = reader.result
					that.fileList.push(tempFile)
				};

			},
			onExceed() {
				this.$message.error("只能上传一个文件");
			},
			fileRemove() {
				this.fileList.pop();
			},
			//班次是否招募
			handleStatusChange(row) {

				const that = this;
				const text = row.status === "0" ? "发布招募" : "停止招募";
				this.$modal.confirm("确认要" + text + "该班次吗？").then(function() {
					row.status = row.status === "0" ? "1" : "0";
					that.isUpdate = !that.isUpdate;
					that.$modal.msgSuccess(text + "成功");
				});
			},
			changeBeginDate(e) {
				if (e == null) {
					this.form.endDate = null;
				}
			},
			format(beginTime, endTime) {
				return beginTime + "-" + endTime;
			},
			removeTag(val) {},
			getActivityDetail() {
				const that = this;
				const loading = that.$loading({
					lock: true,
					text: "加载中...请稍后...",
					spinner: "el-icon-loading",
					background: "rgba(0, 0, 0, 0.7)",
				});
				getActivityDetail(this.aId).then((response) => {
					this.form = response;
					this.assignmentCheck = this.form.activityAssignmentList.map((o) => {
						return {
							id: o.id,
							check: o.time + o.timeSlotId + o.locationId + o.typeId,
						};
					});
					loading.close();
				});
			},
			oneLevelTableRowClassName(row) {
				return this.tableRowClassName(row) + ' ' + (row.row.postList.length === 0 ? "row-expand-cover" : "");
			},
			twoLevelTableRowClassName(row) {
				return this.tableRowClassName(row) + ' ' + (row.row.volunteerInfoBoList === undefined ? "row-expand-cover" : (row
					.row.volunteerInfoBoList.length === 0 ? "row-expand-cover" : ""));
			},
			tableRowClassName(row) {
				if (row.row.id.includes("-") && !row.row.id.includes("$")) {
					return "disabled-row";
				}
				return "";
			},
			listAllDepartment() {
				listAllDepartment().then((response) => {
					this.departmentOptions = [...this.departmentOptions, ...response];
					this.allowedDepartmentOptions = [
						...this.allowedDepartmentOptions,
						...response,
					];
				});
			},
			listAllLocation() {
				listAllLocation().then((response) => {
					this.locationOptions = response;
				});
			},
			listAllCategory() {
				listAllCategory().then((response) => {
					this.categoryOptions = response;
				});
			},
			listAllTimeSlot() {
				listAllTimeSlot().then((response) => {
					this.timeSlotOptions = response;
				});
			},
			listAllAssignmentType() {
				listAllAssignmentType().then((response) => {
					this.assignmentTypeOptions = response;
				});
			},
			listAllPost() {
				listAllPost(this.form.categoryId).then((response) => {
					this.postOptions = response;
				});
			},
			listAllTitle() {
				if (this.allowedTitleOptions.length == 1) {
					listAllTitle().then((response) => {
						this.allowedTitleOptions = [...this.allowedTitleOptions, ...response];
					});
				}
			},
			// 取消按钮
			cancel() {
				this.open = false;
			},
			// 表单重置
			resetForm() {
				this.form = {
					activityAssignmentList: [],
				};
			},
			resetAssignmentForm() {
				this.assignmentForm = {};
			},
			//去除无效数据
			removeInvalidData() {
				this.form.activityAssignmentList.forEach((item) => {
					if (item.id.includes("$")) {
						delete item.id;
					}
					item.postList.forEach((post) => {
						if (post.id.includes("$")) {
							delete post.id;
						}
					});
				});
			},
			//点击保存草稿
			handleSave() {
				this.form.status = "0";
				// this.removeInvalidData();
				localStorage.setItem('tempActivity', JSON.stringify(this.form))
				this.$modal.msgSuccess("活动保存为草稿成功");
				this.$router.push({
					path: "/content/activity"
				});
			},
			//点击发布/更更新
			handleSubmit() {
				this.$refs["form"].validate((valid) => {
					if (valid) {
						const loading = this.$loading({
							lock: true,
							text: "提交中...请稍后...",
							spinner: "el-icon-loading",
							background: "rgba(0, 0, 0, 0.7)",
						});
						this.removeInvalidData();
						if (!this.aId) {
							this.form.status = "1";
							addActivity(this.form).then((response) => {
								this.$modal.msgSuccess("活动发布成功");
								loading.close();
								this.$router.push({
									path: "/content/activity"
								});
							});
						} else {
							updateActivity(this.form).then((response) => {
								this.$modal.msgSuccess("活动更新成功");
								loading.close();
								this.$router.push({
									path: "/content/activity"
								});
							});
						}
					}
				});
			},
			/** 添加班次操作 */
			handleAddAssignment() {
				const that = this;
				this.listAllPost();
				this.resetAssignmentForm();
				if (this.$refs.assignmentForm !== undefined) {
					this.$refs.assignmentForm.resetFields();
				}
				this.choosePostOptions = [];
				that.open = true;
				that.title = "添加班次";
			},
			getInformation() {
				this.listAllDepartment();
				this.listAllCategory();
				this.listAllTitle();
				this.listAllLocation(),
				this.listAllTimeSlot(),
				this.listAllAssignmentType(),
				this.resetAssignmentForm()
			},
			/** 修改班次按钮操作 */
			async handleAssignmentUpdate(row) {
				this.listAllPost();
				this.assignmentForm = JSON.parse(
					JSON.stringify(
						this.form.activityAssignmentList.find((o) => o.id === row.id)
					)
				);
				this.choosePostOptions = this.assignmentForm.postList;
				if (this.assignmentForm.qrCodeUrl != undefined) {
					this.fileList.push({
						url: this.assignmentForm.qrCodeUrl
					})
				}
				this.assignmentCheck.forEach((o) => {
					if (o.id === row.id) {
						o.flag = true;
					}
				});
				this.title = "修改班次";
				this.open = true;
				this.$nextTick(() => {
					this.$refs['assignmentForm'].clearValidate();
				})
			},
			isSelect() {
				return (
					this.assignmentForm.time == undefined ||
					this.assignmentForm.timeSlotId == undefined ||
					this.assignmentForm.locationId == undefined ||
					this.assignmentForm.typeId == undefined
				);
			},
			checkAssignment(checkFlag, index) {
				let ans = false;
				this.assignmentCheck.forEach((o) => {
					if (o.check == checkFlag && o.flag === undefined) {
						//此时说明修改到了重复的
						ans = true;
					}
					if (o.id === index) {
						delete o.flag;
					}
				});
				return ans;
			},
			getName() {
				this.assignmentForm.locationName = this.locationOptions.find(
					(o) => o.id == this.assignmentForm.locationId
				).name;
				this.assignmentForm.typeName = this.assignmentTypeOptions.find(
					(o) => o.id == this.assignmentForm.typeId
				).name;
				this.assignmentForm.beginTime = this.timeSlotOptions.find(
					(o) => o.id == this.assignmentForm.timeSlotId
				).beginTime;
				this.assignmentForm.endTime = this.timeSlotOptions.find(
					(o) => o.id == this.assignmentForm.timeSlotId
				).endTime;
			},
			/** 提交班次按钮 */
			submitForm() {
				this.$refs["assignmentForm"].validate((valid) => {
					if (valid) {
						//判断一下有没有更新图片
						let url = this.fileList[0].url;
						this.dialogLoading = this.$loading({
							lock: true,
							text: "获取数据中...请稍后...",
							spinner: "el-icon-loading",
							background: "rgba(0, 0, 0, 0.7)",
							target: '.el-dialog'
						});
						if (url.substring(0, 4) === 'data') {
							//先上传一下图片
							uploadImg(this.fileList[0], 'qrCode').then(res => {
								//再提交班次
								this.sumbitAssignmentForm(res)
							}).catch(err => {
								this.dialogLoading.close();
								delete this.dialogLoading;
							})
						} else {
							//直接提交班次
							this.sumbitAssignmentForm(url)
						}
					}
				});
			},
			/**提交班次的逻辑 url是二维码的路径*/
			sumbitAssignmentForm(url) {
				if (this.assignmentForm.id !== undefined) {
					let checkFlag =
						this.assignmentForm.time +
						this.assignmentForm.timeSlotId +
						this.assignmentForm.locationId +
						this.assignmentForm.typeId;
					if (this.checkAssignment(checkFlag, this.assignmentForm.id)) {
						this.$modal.msgError("有相同班次存在");
					} else {
						let index = this.form.activityAssignmentList.findIndex(
							(o) => o.id === this.assignmentForm.id
						);
						this.getName();
						this.form.activityAssignmentList[index] = {
							...this.form.activityAssignmentList[index],
							...this.assignmentForm,
							qrCodeUrl: url,
						};
						this.fileList = [];
						this.isUpdate = !this.isUpdate;
						this.open = false;
						this.dialogLoading.close();
					}
				} else {
					let checkFlag =
						this.assignmentForm.time +
						this.assignmentForm.timeSlotId +
						this.assignmentForm.locationId +
						this.assignmentForm.typeId;
					if (this.checkAssignment(checkFlag, undefined)) {
						this.$modal.msgError("有相同班次存在");
					} else {
						this.getName();
						this.assignmentForm.id =
							"$" +
							this.assignmentForm.locationId +
							this.assignmentForm.typeId +
							this.assignmentForm.timeSlotId +
							this.assignmentForm.time;
						this.assignmentForm.status = "1";
						this.assignmentForm.qrCodeUrl = url,
							this.form.activityAssignmentList =
							this.form.activityAssignmentList == undefined ? [JSON.parse(JSON.stringify(this.assignmentForm))] : [...
								this.form.activityAssignmentList, JSON.parse(JSON.stringify(this.assignmentForm))
							];
						this.open = false;
						this.dialogLoading.close();
					}
				}
				delete this.dialogLoading;
				this.$modal.msgSuccess("修改班次成功");
			},
			// 删除岗位信息按钮操作
			handlePostAssignmentDelete(parentRow, row) {
				this.$modal
					.confirm("是否确认删除该岗位？")
					.then(function() {
						return true;
					})
					.then(() => {
						//找到是在哪个班次下
						let index = this.form.activityAssignmentList.findIndex(
							(o) => o.id == parentRow.id
						);
						if (row.id.includes("$")) {
							this.form.activityAssignmentList[index].postList =
								this.form.activityAssignmentList[index].postList.filter(
									(o) => !(o.id == row.id)
								);
						} else {
							this.form.activityAssignmentList[index].postList.forEach((o) => {
								if (o.id == row.id) {
									o.id = "-" + o.id;
									o.volunteerInfoBoList = [];
								}
							});
						}
						this.$modal.msgSuccess("删除成功");
					})
					.catch(() => {});
			},
			/**
			 * 关闭dialog弹窗事件
			 */
			handleCancle() {
				this.fileList = [];
			},
			/** 删除班次按钮操作 */
			handleAssignmentDelete(row) {
				this.$modal
					.confirm("是否确认删除该班次？")
					.then(function() {
						return true;
					})
					.then(() => {
						if (row.id.includes("$")) {
							this.form.activityAssignmentList =
								this.form.activityAssignmentList.filter((o) => !(o.id == row.id));
						} else {
							this.form.activityAssignmentList.forEach((o) => {
								if (o.id == row.id) {
									o.id = "-" + o.id;
									o.postList = [];
								}
							});
						}
						this.$modal.msgSuccess("删除成功");
					})
					.catch(() => {});
			},
			disabledStart(time) {
				return time.getTime() < Date.now() - 8.64e7;
			},
			disabledEnd(time) {
				return (
					time.getTime() < new Date(this.form.beginDate).getTime() - 8.64e7 //结束时间大于等于开始时间
				);
			},
			disabledTime(time) {
				return !(
					new Date(this.form.beginDate).getTime() - 2.88e7 <= time.getTime() &&
					time.getTime() <= new Date(this.form.endDate).getTime() - 2.88e7
				);
			},
		},
	};
</script>
<style lang="scss" scoped>
	.app-container {
		::v-deep {
			.el-table th {
				background: #f6f6f6;
				color: black;
			}

			.el-table .disabled-row {
				display: none !important;
			}

			.el-table__expanded-cell {
				border-bottom: 0px;
				border-right: 0px;
				padding: 0px 0px 0px 47px;
			}

			.row-expand-cover .cell .el-table__expand-icon {
				display: none;
			}
		}

		.table-in-table {
			border-top: 0px;
		}

	}

	.dialog-content {
		height: 58vh;
		overflow: auto;
	}
</style>
<style lang="less">
	.captcha-img.el-switch.is-disabled {
		opacity: 1;

		.el-switch__core {
			cursor: pointer;
		}
	}
</style>