<template>
  <div class="app-container">
    <el-form
      v-show="showSearch"
      ref="queryForm"
      :model="queryParams"
      :inline="true"
    >
      <el-form-item label="活动名称" prop="activityName">
        <el-input
          v-model="queryParams.activityName"
          placeholder="请输入活动名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="地点" prop="locationId">
        <el-select
          v-model="queryParams.locationId"
          placeholder="请选择地点"
          @click.native="getlocation()"
          size="small"
          clearable
        >
          <el-option
            v-for="item in locationOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="时间段" prop="timeSlotId">
        <el-select
          v-model="queryParams.timeSlotId"
          placeholder="请选择时间段"
          @click.native="gettime()"
          size="small"
          clearable
        >
          <el-option
            v-for="item in timeOptions"
            :key="item.id"
            :label="format(item.beginTime, item.endTime)"
            :value="item.id"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="是否完成所有签到" prop="flag">
        <el-select
          v-model="queryParams.flag"
          placeholder="请选择"
          clearable
          size="small"
          style="width: 100px"
        >
          <el-option :key="1" label="是" :value="1" />
          <el-option :key="0" label="否" :value="0" />
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          style="margin-left: 20px"
          >搜索</el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="exportSignInUserRecord"
          >导出</el-button
        >
      </el-col>
    </el-row>

    <el-table
      id="oIncomTable"
      v-loading="loading"
      :data="attendList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        label="活动名"
        align="center"
        prop="activityName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="班次地点"
        align="center"
        prop="locationName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="班次时间"
        align="center"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <span
            >{{ scope.row.time }} {{ scope.row.timeSlotBegin }}-{{
              scope.row.timeSlotEnd
            }}</span
          >
        </template>
      </el-table-column>
      <el-table-column
        label="签到人"
        align="center"
        prop="userName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="签到时间"
        align="center"
        prop="signInTime"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.signInTime ? scope.row.signInTime : "无" }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="考勤打卡时间"
        align="center"
        prop="midTime"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.midTime ? scope.row.midTime : "无" }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="签退时间"
        align="center"
        prop="signOutTime"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <span>{{
            scope.row.signOutTime ? scope.row.signOutTime : "无"
          }}</span>
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
import { list, exportSignInUser } from "@/api/content/signIn";
import { listLocation } from "@/api/content/location";
import { listTime } from "@/api/content/timeSlot";
import FileSaver from "file-saver";
import XLSX from "xlsx";

export default {
  name: "Category",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 签到记录数据
      attendList: null,
      // 地点信息
      locationOptions: [],
      // 时间信息
      timeOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      opens: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        activityName: null,
        locationId: null,
        timeSlotId: null,
        flag: null,
      },
      XXXParams: {
        pageNum: 1,
        pageSize: 10,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    format(beginTime, endTime) {
      return beginTime + "-" + endTime;
    },
    /** 查询分类列表 */
    getList() {
      this.loading = true;
      list(this.queryParams).then((response) => {
        this.attendList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    getlocation() {
      listLocation(this.XXXParams).then((response) => {
        this.locationOptions = response.rows;
      });
    },
    gettime() {
      listTime(this.XXXParams).then((res) => {
        this.timeOptions = res.rows;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.opens = false;
      this.reset();
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },

    /*导出数据*/
    // Export(){
    //   const that = this;
    //   this.$modal
    //   .confirm('确认导出已查询的签到记录?')
    //   .then(function(){
    //     const XLSX = require('xlsx')
    // 	  // 使用 this.$nextTick 是在dom元素都渲染完成之后再执行
    // 	  return that.$nextTick(function () {
    // 		// 设置导出的内容是否只做解析，不进行格式转换     false：要解析， true:不解析
    // 		const xlsxParam = { raw: true }
    // 		const wb = XLSX.utils.table_to_book(document.querySelector('#oIncomTable'), xlsxParam)
    // 		// 导出excel文件名
    // 		let fileName = '签到记录' + new Date().getTime() + '.xlsx'

    // 		const wbout = XLSX.write(wb, { bookType: 'xlsx', bookSST: true, type: 'array' })
    // 		try {
    // 		  // 下载保存文件
    // 		  FileSaver.saveAs(new Blob([wbout], { type: 'application/octet-stream' }), fileName)
    // 		} catch (e) {
    // 		  if (typeof console !== 'undefined') {
    // 			console.log(e, wbout)
    // 		  }
    // 		}
    // 		return wbout
    // 	  })
    //   })
    //   .then(()=>{
    //     this.$modal.msgSuccess('导出成功')
    //   })
    //   .catch(()=>{
    //     console.log(1);
    //   })
    // },
    exportSignInUserRecord() {
      const that = this;
      this.$modal
        .confirm(
          '是否导出选中签到记录?<p style="color: red;">(如需计算服务工时请查询时选择完成所有签到)</p>'
        )
        .then(function () {
          const loading = that.$loading({
            lock: true,
            text: "导出中...请稍后...",
            spinner: "el-icon-loading",
            background: "rgba(0, 0, 0, 0.7)",
          });
          exportSignInUser(that.queryParams)
            .then(() => {
              loading.close();
              that.$modal.msgSuccess("导出成功");
            })
            .catch((error) => {
              loading.close();
              that.$modal.msgError("导出失败");
            });
        });
    },
    submitupdate() {
      updateCategory(this.form).then((response) => {
        this.$modal.msgSuccess("修改成功");
        this.opens = false;
        this.getList();
      });
    },
  },
};
</script>
