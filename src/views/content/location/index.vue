<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
      <el-form-item label="地点" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入活动地点"
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

    <el-table v-loading="loading" :data="locationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" :show-overflow-tooltip="true"/>
      <el-table-column label="id" align="center" prop="id" :show-overflow-tooltip="true"/>
      <el-table-column label="地点" align="center" prop="name" :show-overflow-tooltip="true"/>
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
				
				
        <el-form-item label="地点" prop="name">
          <el-input v-model="form.name" placeholder="请输入活动地点" />
        </el-form-item>
			<el-form-item label="经纬度" prop="address">
			  <div style="height:250px;width:100%;">
			  <el-amap ref="map" vid="amapDemo" :events="events" :center="center" :zoom="zoom">
			    <el-amap-marker :position="center" key="100"></el-amap-marker>
			  </el-amap>
			  </div>
			</el-form-item>
			<el-form-item label="经度" prop="longitude">
				<el-input v-model="form.longitude" placeholder="经度" maxlength="30" @change="refresh" onkeyup="value=value.replace(/[^\d.]/g,'')" />
			</el-form-item>
			<el-form-item label="纬度" prop="latitude">
				<el-input v-model="form.latitude" placeholder="纬度" maxlength="30" @change="refresh" onkeyup="value=value.replace(/[^\d.]/g,'')" />
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
import {  getLocation, delLocation, addLocation, updateLocation, listLocation,changeLocationStatus } from '@/api/content/location'

export default {
  name: 'location',
  data() {
		const _this = this;
    return {
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 选中数组
      ids: [],
      locationNames:[],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 地点数据
      locationList: null,
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
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
      },
			zoom: 18,
			center: [106.42395973205566, 29.82068913858382],
			events: {
			  click(e) {
			    _this.center = [e.lnglat.lng, e.lnglat.lat];
			    _this.form.longitude = e.lnglat.lng;
			    _this.form.latitude = e.lnglat.lat;
			  },
			},
    }
  },
  created() {
    this.getList()
  },
  methods: {
		refresh() {
		  this.center = [this.form.longitude, this.form.latitude];
		},
    /** 查询分类列表 */
    getList() {
      this.loading = true
      listLocation(this.queryParams).then(response => {
        this.locationList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
		// 地点状态修改
		handleStatusChange(row) {
		  const text = row.status === '0' ? '允许使用' : '禁止使用'
		  this.$modal
		    .confirm('确认要' + text + '活动地点"' + row.name + '"吗？')
		    .then(function() {
		      row.status = row.status === '0'?'1':'0';
		      return changeLocationStatus(row.id, row.status)
		    })
		    .then(() => {
		      this.$modal.msgSuccess(text + '成功')
		    })
		},
    // 取消按钮
    cancel() {
      this.open = false
			this.opens = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
				id: null,
        name: null,
      }
			this.zoom = 18,
			this.center = [106.42395973205566, 29.82068913858382],
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
      this.locationNames = selection.map(item => item.name)
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加地点'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getLocation(id).then(response => {
        this.form = response
				this.center = [response.longitude,response.latitude]
        this.open = true
        this.title = '修改地点'
      })
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
      let locationNames = row.name || this.locationNames;
      const ids = row.id || this.ids;
      locationNames = ids.length > 2 ? locationNames[0] + "," + locationNames[1] + "等..." : locationNames;
      this.$modal
        .confirm('是否确认删除地点:"' + locationNames+'"')
        .then(function () {
          return delLocation(ids);
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
<style lang="less">
.captcha-img.el-switch.is-disabled {
  opacity: 1;
  .el-switch__core {
    cursor: pointer;
  }
}
</style>