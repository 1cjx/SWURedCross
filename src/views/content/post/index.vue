<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
			
			<el-form-item label="所属活动分类" prop="categoryName" label-width="120px">
			  <el-select v-model="queryParams.categoryId"  placeholder="请选择"
				size="small"
				clearable>
			    <el-option
			      v-for="item in categoryOptions"
			      :key="item.id"
			      :label="item.name"
			      :value="item.id"
						
			    />
			  </el-select>
			</el-form-item>
			
      <el-form-item label="岗位名称" prop="postName">
        <el-input
          v-model="queryParams.postName"
          placeholder="请输入岗位名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
			
			<el-form-item label="状态" prop="status">
			  <el-select
			    v-model="queryParams.status"
			    placeholder="岗位状态"
			    clearable
			    size="small"
			    style="width: 100px"
			  >
			    <el-option :key="1" label="是" :value="1" />
			    <el-option :key="0" label="否" :value="0" />
			  </el-select>
			</el-form-item>
			
      
			
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
			
      
    </el-row>

    <el-table v-loading="loading" :data="PostList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"  :show-overflow-tooltip="true" />
      <el-table-column label="id" align="center" prop="id" :show-overflow-tooltip="true" />
      <el-table-column label="岗位名称" align="center" prop="name"  :show-overflow-tooltip="true" />
			<el-table-column label="所属活动分类" align="center" prop="categoryName"  :show-overflow-tooltip="true" />
      <el-table-column label="岗位状态" align="center" prop="status" >
			<template slot-scope="scope">
					<span v-if="scope.row.status == '1'">已启用</span>
			    <span v-if="scope.row.status == '0'">未启用</span>
			</template>
      </el-table-column>
      <el-table-column label="岗位描述" align="center" prop="description"  :show-overflow-tooltip="true" />
			
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      :page-size.sync="queryParams.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      :page-sizes="[10, 20, 30, 40]"
      :current-page.sync="queryParams.pageNum"
      @current-change="getList"
      @size-change="getList"
    />

    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
				<el-form-item label="名称" prop="name">
				  <el-input v-model="form.name" placeholder="请输入岗位名" />
				</el-form-item>
        <el-form-item label="所属活动分类" prop="categoryId">
          <el-select
            v-model="form.categoryId"
            placeholder="请选择"
            clearable
          >
            <el-option
              v-for="item in categoryOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
				<el-form-item  label="描述" prop="description" >
				  <el-input type="textarea" v-model="form.description" autosize placeholder="请输入描述" />
				</el-form-item>
        
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
		
  </div>
</template>

<script>
import {  getPost, delPost, addPost, updatePost, listPost} from '@/api/content/post'
import { listAllCategory } from '@/api/utils/selectOptions'
export default {
  name: 'POST',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 选中数组
      ids: [],
      postNames:[],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 岗位数据
      PostList: null,
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
			opens:false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        categoryName: null,
        postName: null,
        status: undefined
      },
			categoryOptions:[
        {"id":"0",
        "name":"活动通用岗位"}
      ],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [{ required: true, message: "岗位名不为空", trigger: "blur" }],
        categoryId: [
          { required: true, message: "所属活动分类不为空", trigger: "blur" },
        ],
      }
    }
  },
  created() {
    this.getList()
    this.listAllCategory();
  },
  methods: {
    /** 查询分类列表 */
    getList() {
      this.loading = true
      listPost(this.queryParams).then(response => {
        this.PostList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
		cancels(){
			this.opens = false
			this.reset()
		},
    // 表单重置
    reset() {
      this.form = {
				categoryId: "0",
        name: null,
        status: null,
				description:undefined
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.postNames = selection.map(item => item.name)
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    listAllCategory(){
      listAllCategory().then(response=>{
        this.categoryOptions = [...this.categoryOptions,...response];
        console.log(this.categoryOptions)
      })
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加岗位'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getPost(id).then(response => {
        this.form = response
        this.open = true
        this.title = '修改岗位'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePost(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addPost(this.form).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
		submitupdate(){
			updatePost(this.form).then(response =>{
				this.$modal.msgSuccess(response.msg)
				this.open = false
				this.getList()
			})
		},
    /** 删除按钮操作 */
    handleDelete(row) {
      let postNames = row.name || this.postNames;
      const ids = row.id || this.ids;
      postNames = ids.length > 2 ? postNames[0] + "," + postNames[1] + "等..." : postNames;
      this.$modal
        .confirm('是否确认删除岗位:"' + postNames+'"')
        .then(function () {
          return delPost(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch((e)=>{if(e=='error')this.getList();})
    },
    
  }
}
</script>
