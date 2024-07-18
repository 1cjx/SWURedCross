<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
      <el-form-item label="部门" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入部门名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
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

    <el-table v-loading="loading" :data="departmentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" :show-overflow-tooltip="true"/>
      <el-table-column label="id" align="center" prop="id" :show-overflow-tooltip="true"/>
      <el-table-column label="地点" align="center" prop="name" :show-overflow-tooltip="true"/>
      
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" :show-overflow-tooltip="true">
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

    <!-- 添加或修改分类对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
				
				
        <el-form-item label="部门" prop="name">
          <el-input v-model="form.name" placeholder="请输入部门名称" />
        </el-form-item>
				

        
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
		
		<el-dialog :title="title" :visible.sync="opens" width="500px" append-to-body>
		  <el-form ref="form" :model="form" :rules="rules" label-width="80px">
				
				
		    <el-form-item label="部门" prop="name">
		      <el-input v-model="form.name" placeholder="请输入活动地点" />
		    </el-form-item>
				
		
		    
		  </el-form>
		  <div slot="footer" class="dialog-footer">
		    <el-button type="primary" @click="submitupdate">确 定</el-button>
		    <el-button @click="cancels">取 消</el-button>
		  </div>
		</el-dialog>
		
  </div>
</template>

<script>
import {  getDepartment, delDepartment, addDepartment, updateDepartment, listDepartment } from '@/api/content/department'

export default {
  name: 'department',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 选中数组
      ids: [],
      departmentNames:[],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 部门数据
      departmentList: null,
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
			opens:false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        
        status: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询分类列表 */
    getList() {
      this.loading = true
      listDepartment(this.queryParams).then(response => {
        this.departmentList = response.rows
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
				id: null,
        name: null,
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
      this.departmentNames = selection.map(item=>item.name)
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加时间'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getDepartment(id).then(response => {
        this.form = response
        this.opens = true
        this.title = '修改时间'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDepartment(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addDepartment(this.form).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
		submitupdate(){
			updateDepartment(this.form).then(response =>{
				this.$modal.msgSuccess('修改成功')
				this.opens = false
				this.getList()
			})
		},
    /** 删除按钮操作 */
    handleDelete(row) {

      let departmentNames = row.name || this.departmentNames;
      const ids = row.id || this.ids;
      departmentNames = ids.length > 2 ? departmentNames[0] + "," + departmentNames[1] + "等..." : departmentNames;
      this.$modal
        .confirm('是否确认删除分类:"' + departmentNames+'"')
        .then(function () {
          return delDepartment(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch((e)=>{if(e=='error')this.getList();});
    },
    
  }
}
</script>
