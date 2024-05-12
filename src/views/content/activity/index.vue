<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" >
      <el-row>
			<el-form-item label="活动名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入活动名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
			
			<el-form-item label="活动分类" prop="category">
			  <el-select v-model="queryParams.categoryId"   placeholder="请选择" @click.native="listAllCategory()"
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
			<el-form-item label="状态" prop="status">
			  <el-select
			    v-model="queryParams.status"
			    placeholder="是否发布"
			    clearable
			    size="small"
			    style="width: 100px"
			  >
			    <el-option :key="1" label="是" :value="1" />
			    <el-option :key="0" label="否" :value="0" />
			  </el-select>
			</el-form-item>
			<el-form-item style="margin-left: 20px;">
			  <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
			</el-form-item>
			
			</el-row>
    </el-form>

    <el-row :gutter="10" class="mb8">
			<el-col :span="1.5" style="font-family: Arial, Helvetica, sans-serif;font-weight:600;margin-left: 100px;
			size: small;">
			</el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleChoose('add',null)"
        >新增</el-button>
      </el-col>

			
      
    </el-row>

    <el-table v-loading="loading" :data="activityList" row-key="id">
      <el-table-column label="活动名称" align="center" prop="name" :show-overflow-tooltip="true"/>
      <el-table-column label="活动分类" align="center" prop="category" :show-overflow-tooltip="true"/>
			<el-table-column label="是否发布" align="center" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="1"
            inactive-value="0"
            disabled
            
      class="captcha-img"
            @click.native="handleStatusChange(scope.row)"
          />
        </template>
			</el-table-column>
			<el-table-column label="活动日期" align="center" :show-overflow-tooltip="true">
				<template slot-scope="scope">
					<span>{{scope.row.beginDate}} 至 {{scope.row.endDate}}</span>
				</template>
			</el-table-column>
			<el-table-column label="是否外招" align="center" :show-overflow-tooltip="true">
			 <template slot-scope="scope">
			 		<span v-if="scope.row.allowedDepartmentId == '-1'">是</span>
			    <span v-else>否</span>
			 </template>
			 </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleChoose('view',scope.row)"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleChoose('update',scope.row)"
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
  </div>
</template>

<script>
import {listActivity,changeActivityStatus,deleteActivity} from '@/api/content/activity'
import {listAllCategory} from '@/api/utils/selectOptions'
export default {
  name: 'location',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 显示搜索条件
      showSearch: true,
			timeOptions:null,
      // 总条数
      total: 0,
      // 活动数据
      activityList: [],
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
        categoryId:undefined,
        status: undefined
      },
			categoryParams:{
				pageNum: 1,
				pageSize: 40
			},
			categoryOptions:[],
    }
  },
  created() {
    this.getList()
  },
  methods: {
    // 活动状态修改
    handleStatusChange(row) {
      const text = row.status === '0' ? '发布招募' : '停止招募'
      this.$modal
        .confirm('确认要' + text + '活动"' + row.name + '"吗？')
        .then(function() {
          row.status = row.status === '0'?'1':'0';
          return changeActivityStatus(row.id, row.status)
        })
        .then(() => {
          this.$modal.msgSuccess(text + '成功')
        })
    },
    /** 查询分类列表 */
    getList() {
      this.loading = true
      listActivity(this.queryParams).then(response => {
        this.activityList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
		listAllCategory(){
				listAllCategory().then((response) => {
					this.categoryOptions = response
				})
		},
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 新增按钮操作 */
		handleChoose(type,row){
			if(type==='add'){
				this.$router.push('/release?type='+type)
			}
			else if(type==='view'){
				this.$router.push('/release?id=' + row.id+'&&type='+type)
			}
			else if(type=='update'){
				this.$router.push('/release?id=' + row.id+'&&type='+type)
			}
		},
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.$router.push('/release?id=' + row.id)
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateLocation(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addLocation(this.form).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
		submitupdate(){
			updateLocation(this.form).then(response =>{
				this.$modal.msgSuccess('修改成功')
				this.opens = false
				this.getList()
			})
		},
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$modal.confirm('是否确认删除活动"' + row.name+'"?<span style="color: red;">(一旦删除会印象已报名志愿者相关数据，请谨慎删除！)</span>').then(function() {
				return deleteActivity(row.id)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    
  }
}
</script>

<style lang="less">
.captcha-img.el-switch.is-disabled {
  opacity: 1;
  .el-switch__core {
    cursor: pointer;
  }
}
</style>