<template>
  <div class>
    <el-row style="margin-top: 20px">
      <el-col>
        <el-input style="width: 200px" v-model="value"></el-input>
      </el-col>
    </el-row>
    <el-row style="width:300px;margin-top: 30px">
      <el-autocomplete
        placeholder="带输入建议"
        :fetch-suggestions="querySearch"
        :tigger-on-focus="false"
        v-model="autocompleteValue"
      >
      </el-autocomplete>
    </el-row>

    <el-row style="width:300px;margin-top: 30px">
      <el-select v-model="value" placeholder="请选择" filterable @change="changeSelect">
        <el-option
          v-for="item in fruits"
          :key="item.id"
          :label="item.name"
          :value="item.name">
        </el-option>
      </el-select>
    </el-row>
    <el-row style="width:300px;margin-top: 30px">
      <el-radio-group v-model="radio" @change="changeRadio">
        <el-radio label="男"></el-radio>
        <el-radio label="女"></el-radio>
      </el-radio-group>
    </el-row>

    <el-row style="width:300px;margin-top: 30px">
      <el-checkbox-group v-model="checkList" @change="changeChecks">
        <el-checkbox label="复选1"></el-checkbox>
        <el-checkbox label="复选2"></el-checkbox>
      </el-checkbox-group>
    </el-row>
    <el-row style="width:300px;margin-top: 30px">
      <el-date-picker v-model="date" type="date" placeholder="选择日期" value-format="yyyy-MM-dd"
                      @change="changeDate"></el-date-picker>
      <el-date-picker v-model="datetime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"
                      @change="changeDateTime"></el-date-picker>
      <el-date-picker v-model="dateRange"
                      type="daterange"
                      start-placeholder="开始日期"
                      end-placeholder="结束日期"
                      value-format="yyyy-MM-dd"
                      @change="changeDateRange"
      ></el-date-picker>
    </el-row>
    <el-row style="width:800px;margin-top: 30px">
      <el-table
        :data="tableData"
        border
        :header-cell-style="{background:'aliceblue',fontSize:'20px'}">
        <el-table-column label="姓名" prop="name" align="center"></el-table-column>
        <el-table-column label="id" prop="id" align="center"></el-table-column>
        <el-table-column label="地址" prop="address" align="center"></el-table-column>
        <el-table-column label="操作" align="center" >
          <template v-slot="scope">
            <el-button type="primary" size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
            <el-button type="danger" size="mini" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'Element',
  data () {
    return {
      value: '',
      autocompleteValue: '',
      radio: '',
      date: '',
      datetime: '',
      dateRange: '',
      coffees: [{ value: '1星巴克咖啡' }, { value: '2瑞幸咖啡' }, { value: '3德克士咖啡' }],
      fruits: [
        {
          name: '苹果',
          id: 1
        },
        {
          name: '香蕉',
          id: 2
        },
        {
          name: '橙子',
          id: 3
        },
        {
          name: '橘子',
          id: 4
        }
      ],
      checkList: [],
      tableData: [
        {
          name: '苹果',
          id: 1,
          address: '北京'
        },
        {
          name: '香蕉',
          id: 2,
          address: '上海'
        },
        {
          name: '橙子',
          id: 3,
          address: '广州'
        },
        {
          name: '橘子',
          id: 4,
          address: '深圳'
        }
      ]
    }
  },
  methods: {
    querySearch (queryString, cb) {
      let results = queryString ? this.coffees.filter(item => item.value.includes(queryString)) : this.coffees
      //调用 callback返回建议列表
      cb(results)
    },
    changeSelect () {
      console.log(this.value)
    },
    changeRadio () {
      console.log(this.radio)
    },
    changeChecks () {
      console.log(this.checkList)
    },
    changeDate () {
      console.log(this.date)
    },
    changeDateTime () {
      console.log(this.datetime)
    },
    changeDateRange () {
      console.log(this.dateRange)
    },
    handleEdit (index, row) {
      console.log(index, row)
      this.$confirm('确认删除么','提示',{
        type: 'warning'
      }).then(() => {
        this.$message({
          type: 'success',
          message: '删除成功!'
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    handleDelete (index, row) {
      console.log(index, row)
    }
  }
}
</script>
