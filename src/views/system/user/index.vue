<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="24" :xs="24">
        <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
          <div>
            <el-form-item label="部门" prop="college">
              <el-select v-model="queryParams.departmentId" placeholder="请选择"
                size="small" clearable>
                <el-option v-for="item in departmentOptions" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
            <el-form-item label="职称" prop="title">
                <el-select v-model="queryParams.titleId" placeholder="请选择" size="small"
                  clearable>
                  <el-option v-for="item in titleOptions" :key="item.id" :label="item.name" :value="item.id"
                    :disabled="item.status == 0" />
                </el-select>
              </el-form-item>
            <el-form-item label="学号" prop="id">
              <el-input v-model="queryParams.id" placeholder="请输入用户学号" clearable size="small"
                @keyup.enter.native="handleQuery" />
            </el-form-item>

            <el-form-item label="姓名" prop="name">
              <el-input v-model="queryParams.name" placeholder="请输入姓名" clearable size="small"
                @keyup.enter.native="handleQuery" />
            </el-form-item>
            <!-- select -->


          </div>
          <el-collapse-transition>
            <div v-show="showMoreQuery">
              <!-- select -->
              <el-form-item label="电话" prop="phoneNumber">
                <el-input v-model="queryParams.phoneNumber" placeholder="请输入用户电话" clearable size="small"
                  @keyup.enter.native="handleQuery" />
              </el-form-item>

              <el-form-item label="QQ" prop="qq">
                <el-input v-model="queryParams.qq" placeholder="请输入QQ号码" clearable size="small"
                  @keyup.enter.native="handleQuery" />
              </el-form-item>
              <el-form-item label="学院" prop="college">
              <el-select v-model="queryParams.collegeId" placeholder="请选择"  size="small"
                clearable>
                <el-option v-for="item in collegeOptions" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
              


              <el-form-item label="是否绑定微信" prop="isBind" label-width="120px">
                <el-select v-model="queryParams.isBind" placeholder="用户状态" clearable size="small">
                  <el-option :key="1" label="是" :value="1" />
                  <el-option :key="0" label="否" :value="0" />
                </el-select>
              </el-form-item>
            </div>
          </el-collapse-transition>
        </el-form>


        <el-row :gutter="10">
          <el-col :span="1.5">
            <el-popover placement="bottom" width="260" trigger="click">
              <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="openAddUsers">批量导入</el-button>
              <el-button type="primary" plain icon="el-icon-view" size="mini"
                @click="handleImportRecordDetail">查看导入记录</el-button>
              <el-button slot="reference" type="primary" plain icon="el-icon-plus" size="mini">用户导入</el-button>
            </el-popover>

          </el-col>

          <el-col :span="1.5">
            <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
              @click="handleDelete">删除</el-button>
          </el-col>

          <el-col :span="1.5" style="float: right;">
            <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">搜索</el-button>
          </el-col>
          <el-col :span="1.5" style="float: right;">
            <el-button type="primary" :icon="showMoreQuery === true ? 'el-icon-caret-top' : 'el-icon-caret-bottom'" size="small"
              @click="handleExpand">{{ showMoreQuery === true ? '条件收起' : '条件展开' }}</el-button>
          </el-col>
          <!-- <right-toolbar
            :show-search.sync="showSearch"
            :columns="columns"
            @queryTable="getList"
          /> -->
        </el-row>

        <el-table :data="userList" style="width: 100%" @selection-change="handleSelectionChange" v-loading="loading"
          element-loading-text="数据正在加载中...">
          <el-table-column type="selection" width="55"  fixed/>
          <el-table-column prop="department" label="部门" align="center" :show-overflow-tooltip="true" />
          <el-table-column prop="title" label="职称" align="center" :show-overflow-tooltip="true" />
          <el-table-column prop="name" label="姓名" align="center" :show-overflow-tooltip="true"  />
          <el-table-column prop="id" label="学号" align="center" :show-overflow-tooltip="true"  />

          <el-table-column prop="sex" label="性别" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.sex == '1'">男</span>
              <span v-if="scope.row.sex == '2'">女</span>
            </template>
          </el-table-column>
          <el-table-column prop="college" label="学院" align="center" :show-overflow-tooltip="true" />

          <el-table-column prop="phoneNumber" label="电话" align="center" :show-overflow-tooltip="true" />
          <el-table-column prop="qq" label="QQ" align="center" :show-overflow-tooltip="true" />
          <el-table-column prop="email" label="邮箱" align="center" :show-overflow-tooltip="true" />
          <el-table-column prop="isBind" label="是否绑定微信" align="center" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <span v-if="scope.row.isBind == '1'">已绑定</span>
              <span v-if="scope.row.isBind == '0'">未绑定</span>
            </template>
          </el-table-column>

          <el-table-column label="操作" align="center" width="160" class-name="small-padding fixed-width">
            <template v-if="scope.row.id !== 1" slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
    <el-pagination :page-size.sync="queryParams.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total"
      :page-sizes="[10, 20, 30, 40]" :current-page.sync="queryParams.pageNum" @current-change="getList"
      @size-change="getList" />
    <!-- 添加或修改参数配置对话框 -->


    <!-- 修改 -->
    <el-dialog title="修改用户" :visible.sync="open" width="600px" >
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input readonly v-model="form.name" placeholder="请输入用户姓名" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学号" prop="id">
              <el-input readonly v-model="form.id" placeholder="请输入学号" maxlength="17" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="学院" prop="college">
              <el-select v-model="form.collegeId" placeholder="请选择" style="width: 100%;" clearable>
                <el-option v-for="item in collegeOptions" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱地址" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="电话" prop="phoneNumber">
              <el-input v-model="form.phoneNumber" placeholder="请输入用户电话" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="QQ" prop="qq">
              <el-input v-model="form.qq" placeholder="请输入用户QQ" maxlength="20" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户性别">
              <el-select v-model="form.sex" placeholder="请选择">
                <el-option :key="'1'" label="男" :value="'1'" />
                <el-option :key="'2'" label="女" :value="'2'" />
                <el-option :key="'0'" label="未知" :value="'0'" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门">
              <el-select v-model="form.departmentId" placeholder="请选择">
                <el-option v-for="item in departmentOptions" :key="item.id" :label="item.name" :value="item.id"
                  :disabled="item.status == 0" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="职称">
              <el-select v-model="form.titleId" placeholder="请选择职称">
                <el-option v-for="item in titleOptions" :key="item.id" :label="item.name" :value="item.id"
                  :disabled="item.status == 0" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="登录后台">
              <el-select v-model="form.type" placeholder="请选择">
                <el-option :key="1" label="是" :value="'1'" />
                <el-option :key="0" label="否" :value="'0'" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 批量导入 -->
    <el-dialog title="批量导入" :visible.sync="openAdd" width="600px" append-to-body>
      <el-row>
        <el-col :span="4" :offset="1">
          <el-button type="primary" icon="el-icon-download" size="small" @click="handletemplateDownLoad">模板下载
          </el-button>
        </el-col>
      </el-row>
      </el-col>
      <el-row>
        <el-col :span="24">
          <div class="uploadFile">
            <el-upload :file-list="fileList" class="upload-demo" list-type="text" accept=".xlsx, .xls" drag
              action="upload" :on-remove="fileRemove" :limit="1" :http-request="handleUpload" :on-exceed="onExceed">
              <i class="el-icon-upload" />
              <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
              <div slot="tip" class="el-upload__tip">只能上传xlsx/xls文件</div>
            </el-upload>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="4" :offset="16">
          <el-button type="primary" size="small" @click="handleAddUsers">确认上传<i
              class="el-icon-upload el-icon--right"></i></el-button>
        </el-col>
      </el-row>
    </el-dialog>

    <el-dialog title="导入记录" :visible.sync="openImportRecordDetail" width="900px" append-to-body>
      <el-row>
        <el-col>
          <el-table :data="importRecordList" style="width:100%;height:450px" max-height="450px">
            <el-table-column prop="createTime" label="时间" align="center" />
            <el-table-column prop="allNum" label="导入记录数" align="center" />
            <el-table-column prop="succeedNum" label="成功数" align="center" />
            <el-table-column prop="failNum" label="失败数" align="center" />
            <el-table-column label="操作" align="center" width="160" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="el-icon-edit" v-if="scope.row.failNum !== '0'"
                  @click="getImportDetail(scope.row)">查看详情</el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination :page-size.sync="recordQueryParams.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="recordTotal"
      :page-sizes="[10, 20, 30, 40]" :current-page.sync="recordQueryParams.pageNum" @current-change="getImportRecordDetail"
      @size-change="getImportRecordDetail" />
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
// import { getToken } from '@/utils/auth'
import {
  listUser,
  getUser,
  delUser,
  addUser,
  updateUser,
  changeUserStatus,
  templateDownLoad,
  excelAddUsers,
  userImportRecordList,
  failUploadDownload,
} from "@/api/system/user";
import {
  listAllTitle,
  listAllCollege,
  listAllCategory,
  listAllDepartment,
} from "@/api/utils/selectOptions";

export default {
  name: "User",
  data() {
    return {
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        phoneNumber: undefined,
        status: undefined,
      },
      showMoreQuery: false,
      multiple: true,
      // 是否显示弹出层
      open: false,
      // 是否弹出批量插入
      openAdd: false,
      openImportRecordDetail: false,
      //上传文件列表
      fileList: [],
      //上传文件
      file: null,
      recordQueryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      recordTotal: 0,
      // 表单校验
      rules: {
        userName: [
          { required: true, message: "用户名称不能为空", trigger: "blur" },
          {
            min: 2,
            max: 20,
            message: "用户名称长度必须介于 2 和 20 之间",
            trigger: "blur",
          },
        ],
        nickName: [
          { required: true, message: "用户昵称不能为空", trigger: "blur" },
        ],
        password: [
          { required: true, message: "用户密码不能为空", trigger: "blur" },
          {
            min: 5,
            max: 20,
            message: "用户密码长度必须介于 5 和 20 之间",
            trigger: "blur",
          },
        ],
        email: [
          {
            type: "email",
            message: "'请输入正确的邮箱地址",
            trigger: ["blur", "change"],
          },
        ],
        phoneNumber: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur",
          },
        ],
      },
      importRecordList: [],
      // 角色选项（修改）
      titleOptions: [],
      // 部门选项
      departmentOptions: [],
      // 学院选项
      collegeOptions: [],
      categoryOptions: [],
      // 显示搜索条件
      showSearch: true,
      // 遮罩层
      loading: true,
      // 用户表格数据
      userList: null,
      // 总条数
      total: 0,
      // 选中数组
      ids: [],
      // 表单参数
      form: {},
    };
  },
  watch: {},
  created() {
		this.listAllCollege();
		this.listAllTitle();
		this.listAllDepartment();
    this.getList();
  },
  methods: {
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listUser(this.queryParams).then((response) => {
        this.userList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids; /*学号*/
      getUser(id).then((response) => {
        this.form = response;
        this.open = true;
      });
    },
    // 表单重置
    reset() {
      this.form = {
        college: undefined,
        department: undefined,
        id: undefined,
        // isBind: '0',
        name: undefined,
        phoneNumber: undefined,
        qq: undefined,
        sex: "0",
        email: undefined,
        status: "0",
        title: undefined,
        titleId: undefined,
        departmentId: undefined,
      };
      this.resetForm("form");
    },
    handleExpand() {
      this.showMoreQuery = !this.showMoreQuery;
    },
    //打开新增用户窗口
    openAddUsers() {
      this.file = null;
      this.fileList = [];
      this.openAdd = true;
    },
    handleAddUsers() {
      if (this.file === null) {
        this.$modal.msgError("还未选择文件");
      } else {
        const that = this;
        this.$modal
          .confirm(
            '是否导入当前文件?<p style="color: red;">(请确认使用模板文件)</p>'
          )
          .then(function () {
            const loading = that.$loading({
              lock: true,
              text: "导入中...请稍后...",
              spinner: "el-icon-loading",
              background: "rgba(0, 0, 0, 0.7)",
            });
            excelAddUsers(that.file)
              .then((res) => {
                that.$modal.msgSuccess("导入成功");
                loading.close();
                that.getList();
              })
              .catch((error) => {
                loading.close();
                that.getList();
              });
            that.openAdd = false;
          });
      }
    },
    /** 批量新增用户模板下载 */
    handletemplateDownLoad() {
      templateDownLoad();
    },
    handleImportRecordDetail() {
      this.recordQueryParams = {
        pageNum: 1,
        pageSize: 10,
      },
      this.getImportRecordDetail();
    },
    getImportRecordDetail() {
      userImportRecordList(this.recordQueryParams).then((response) => {
        this.importRecordList = response.rows;
        this.recordTotal = response.total;
        this.openImportRecordDetail = true;
      });
    },
    getImportDetail(row) {
      const that = this;
      const loading = that.$loading({
        lock: true,
        text: "导出中...请稍后...",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
      failUploadDownload(row.id)
        .then((response) => {
          that.$modal.msgSuccess("导出成功");
          loading.close();
        })
        .catch((error) => {
          that.$modal.msgError("导出失败");
          loading.close();
        });
    },
    listAllCollege() {
      listAllCollege().then((response) => {
        this.collegeOptions = response;
      });
    },
    listAllDepartment() {
      listAllDepartment().then((response) => {
        this.departmentOptions = response;
      });
    },
    listAllTitle() {
      listAllTitle().then((response) => {
        this.titleOptions = response;
      });
    },
    listAllCategory() {
      listAllCategory().then((response) => {
        this.categoryOptions = response;
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      const tempIds = ids.length > 2 ? ids[0] + "," + ids[1] + "等..." : ids;
      this.$modal
        .confirm('是否确认删除用户编号为"' + tempIds + '"的数据项？')
        .then(function () {
          return delUser(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch((e) => {
          if (e == "error") this.getList();
        });
    },
    handleUpload(file) {
      this.file = file.file;
    },
    onExceed() {
      this.$message.error("只能上传一个文件");
    },
    fileRemove(file, fileList) {
      this.fileList.pop();
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
					const loading = this.$loading({
						lock: true,
						text: "提交中...请稍后...",
						spinner: "el-icon-loading",
						target:'.el-dialog',
						background: "rgba(0, 0, 0, 0.7)"
					});
            updateUser(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
							loading.close();
              this.open = false;
              this.getList();
            });
        }
      });
    },
  },
};
</script>
<style lang="less" scoped>
.uploadFile {
  margin-top: 25px;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>