<template>
<div style="width: 100%;height:93.5vh">
	<el-row :gutter="20" style="background-color: #f4f4f4;height:100%">
    <el-col :span="8">
		<!-- 首页user信息 -->
			<el-row :gutter="30">
				<el-col :span="24">
					<el-card shadow= 'hover' style="height: 200px;margin:0 0 0 15px;">
														
							<div class="block">
								<el-avatar :size="80" :src="userInfo.avatar" style="margin-left: 20px;margin-top: 25px"></el-avatar>
								<div class="userInfo">
									<span class="important-font">{{userInfo.name}}</span>
									<p class="secondary-font">{{userInfo.department}}</p>
									<p class="third-font">{{userInfo.role}}</p>
								</div>
							</div>
							<div style="margin-top:5%;">
								<span >欢迎进入红会志愿管理系统后台~</span>
							</div>
					</el-card>
				</el-col>
			</el-row>
        <!-- 首页表格 -->
			<el-row>
				<el-col :span="24">
					<el-card shadow= 'hover' class="tableInfo" >
						<div slot="header">
							<span class="important-font">干事信息</span>
							<el-select v-model="queryParams.departmentId"   placeholder="请选择" 
							style="width:30%"
							size="mini"
							clearable>
							  <el-option
							    v-for="item in slectOptions"
							    :key="item.id"
							    :label="item.name"
							    :value="item.id"
							  />
							</el-select>
						</div>
						<div>
							<el-table id="oIncomTable" :data="infoList" size="mini" max-height="100%">
												<el-table-column label="姓名" align="center" prop="userName" />
												<el-table-column label="部门" align="center" prop="departmentName" />
												<el-table-column label="参加活动次数" align="center" prop="userParticipateInActivityNum" />
												<el-table-column label="活动总时长" align="center" prop="userVolunteerTimes" />
											</el-table>
						</div> 
					</el-card>
				</el-col >
			</el-row>
    </el-col>
		
    <el-col :span="16">
			<el-row type="flex" justify="start">
				<!-- 三个活动信息 -->
				<el-col :span="8">
					<div class="num">
						<el-card shadow= 'hover' v-for="item in countData" :key="item.name" :body-style="{ display: 'flex',padding: 0 }" class="OrderCard">
							<i :style="{ background: item.color}">
							   <svg-icon :icon-class="item.icon" />
							</i>
							<div>
								<p class="important-font">{{item.value}}</p>
								<p class="secondary-font">{{item.str}}</p>
							</div>
						</el-card>
					</div>
				</el-col>
				<el-col :span="15">
				<div class="swiper">
					<el-carousel>
								<el-carousel-item v-for="swiper in swiperList" :key="swiper">
									<img :src="baseUrl+swiper" class="swiperItem"></img>
								</el-carousel-item>
							</el-carousel>
				</div>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="24">
				<!-- 柱状图 -->
				<el-card style="height: 300px;width:800px">
					<div  ref="barEcharts" style="height:300px;width: 800px;">{{initBarEcharts()}}</div>
				</el-card>
				</el-col>
			</el-row>
    </el-col>
	</el-row>
</div>
</template>
 
<script>
import { mapGetters } from 'vuex'
import * as echarts from 'echarts'
import {getRankingByActivity,getTotalVolunteerTimes,getVolunteerNums,getActivityNums,getVariousActivitiesNum} from '@/api/dashboard'

export default {
  name: "Index",
  data() {
    return {
			circleUrl:"https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
			imgurl:'',
			dataarry:[],
			namearry:[],
			infoList:null,
			userInfo:{
				name:'',
				avatar:'',
				department:'',
				role:''
			},
			queryParams:{
				departmentId:'',
				sortType:''
			},
			imglist:[
				
				'../../assets/icons/png/T.png',
				'../../assets/icons/png/A.png',
				'../../assets/icons/png/V.png'
			],
			activityInfo:[],
			baseUrl:'http://redcross.heping.fun/images/swiper/',
			swiperList:[
				'image01.jpg',
				'image02.jpg',
				'image03.jpg'
			],
      value: new Date(),
      tableData: [{
          
      }],
			slectOptions:[
				{}
			],
      countData:
        [
					{
						str:'已注册志愿者人数',
						value:'',
						color:'red',
						icon:'volunteerNums'
					},
					{
						str:'已开展活动数',
						value:'',
						color:'rgb(255,184,74)',
						icon:'activityNums'
					},
					{
						str:'总志愿时长',
						value:'',
						color:'rgb(39,169,227)',
						icon:'volunteerTotalTime'
					}
        ],
    }
  },
	created(){
		this.getinformation()
	},
	methods:{
		getinformation(){
			this.userInfo.role = this.$store.getters.role
			this.userInfo.name = this.$store.getters.name
			this.userInfo.department = this.$store.getters.department
			this.userInfo.avatar = this.$store.getters.avatar
			getRankingByActivity().then(response=>{
				this.infoList = response
			}),

			getVolunteerNums().then(response=>{
				this.countData[0].value = response
			}),
			getActivityNums().then(response=>{
				this.countData[1].value = response
			}),
			getTotalVolunteerTimes().then(response=>{
				this.countData[2].value = response
			})

		},
    //柱状图
  initBarEcharts () {
    // 新建一个promise对象
      let p = new Promise(async(resolve) => {
				await getVariousActivitiesNum().then(response=>{
					this.activityInfo = response
					console.log(this.activityInfo)
					let i = 0;
					this.activityInfo.forEach(o=>{
						this.dataarry[i] =  o.nums;
						this.namearry[i] = o.categoryName
						i++;
					})

				}),
        resolve()
      })
      //然后异步执行echarts的初始化函数
      p.then(() => {
        //	此dom为echarts图标展示dom
        let myChart = echarts.init(this.$refs.barEcharts)
        let option = {
          title: {
            text: '活动详情'
          },
          tooltip: {},
          legend: {
            data: ['活动次数']
          },
          xAxis: {
						axisLabel: {
						interval: 0, //强制文字产生间隔
						rotate: 0,
						textStyle: { //文字样式
						color: "black",
						fontSize: 12,
						// fontFamily: 'Microsoft YaHei'
						}
						},
            data: this.namearry,
						
          },
          yAxis: {
						interval:1
					},
          series: [
            {
              name: '活动次数',
              type: 'bar',
              data: this.dataarry,
							barWidth:'20%'
            },
            
          ],
        }
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
      })
    },
    
  }
}
</script>
 
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="less" scoped>
.el-card__body {
    // padding: 10px;
}
.block{
	display:flex;
}
.userCard{
  height: 120px;
  display: flex;
  border-bottom: 2px solid #DCDFE6;
  border-radius: 2px;
}
.userInfo{
	margin-left:20%;
	margin-top:10%;
	text-align: center;
}
.important-font{
    font-weight: 700;
    font-size: 20px;
		text-align: center;
}
.secondary-font{
  color: #909399;
	text-align: center;
	font-size: 15px;
}
.third-font{
	color: #909399;
	font-size: 13px;
}
.login-info{
  height: 40px;
  text-align: left;
  color: #909399;
}
.tableInfo{
	height:495px;
	margin:10px 0 0 15px;
}
.swiper{
	margin-left:10px;
	margin-top:10%;
}
.swiperItem{
	width: 100%;
	height: inherit;
}
.OrderCard{
   margin-bottom: 15px;
   border: #909399 1px solid;
   border-radius: 10px;
	 height: 120px;
	 width:90%;
   i{
     width: 40%;
     height: 120px;
     font-size: 48px;
     color:#fff;
		 display:flex;
		 justify-content:center;
		 align-items:center;
   }
   div{
		 margin-top:10px;
     width: 150px;
   }
}
.el-card{
  border: none;
}
.num{
	 display: flex;
	 flex-wrap: wrap;
}
.el-carousel{
	height:300px;
}
.el-carousel__item h3 {
	    color: #475669;
	    font-size: 14px;
	    opacity: 0.75;
	    line-height: 150px;
	    margin: 0;
	  }
	.el-carousel__item:nth-child(2n) {
		 background-color: #99a9bf;
	}
	
	.el-carousel__item:nth-child(2n+1) {
		 background-color: #d3dce6;
	}
.el-col {
	 margin-top: 10px;
 }
</style>
<style>
	
</style>